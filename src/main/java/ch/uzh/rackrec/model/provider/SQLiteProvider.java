package ch.uzh.rackrec.model.provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import ch.uzh.rackrec.model.view.KKC;
import com.google.inject.Inject;

import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import ch.uzh.rackrec.model.view.KAC;

public class SQLiteProvider implements IDatabaseProvider{
    private Connection conn = null;
    private String dbLocation;
    private SQLQueryFactory queryFactory;
    private Logger logger;
    private final String TOKEN_COLUMN = "Token";
    private final int BATCH_SIZE = 100;

    @Inject
    public SQLiteProvider(Properties properties, Logger logger) throws SQLException {
        dbLocation = properties.getProperty("database-file");
        queryFactory = new SQLQueryFactory();
        this.logger = logger;

        String url = "jdbc:sqlite:" + dbLocation;
        conn = DriverManager.getConnection(url);
    }

    public boolean tableExists(String tableName) throws SQLException {
        String tableExistsQuery = queryFactory.getSQLiteTable();

        try(PreparedStatement stmt = conn.prepareStatement(tableExistsQuery)) {
            stmt.setString(1, tableName);
            try(ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.log(null, e.getLocalizedMessage());
            }
        }
    }

    public void saveTokenContextReference(String token, ITypeName context) throws SQLException {
        boolean referenceExistsAlready = tokenContextReferenceExists(token, context);

        if(referenceExistsAlready) {
            updateExistingTokenContextReference(token, context);
        } else {
            createNewTokenContextReference(token, context);
        }
    }

    @Override
    public void prepareSchemas() throws SQLException {
        createTokenTable();
        createAPITable();
        createContextTable();
        createTokenReferenceTable();
        createAPIReferenceTable();
    }

    public void saveTokenContextReference(List<String> tokens, ITypeName context) throws SQLException {
        for(String token: tokens) {
            saveTokenContextReference(token, context);
        }
    }

    public void saveApiContextReference(String api, ITypeName context) throws SQLException {
        boolean referenceExistsAlready = apiContextReferenceExists(api, context);

        if(referenceExistsAlready) {
            updateExistingApiContextReference(api, context);
        } else {
            createNewApiContextReference(api, context);
        }
    }

    public void saveApiContextReference(List<String> apis, ITypeName context) throws SQLException {
        for(String api: apis) {
            saveApiContextReference(api, context);
        }
    }

    public List<String> getApisForContext(String context) throws SQLException {
        String getApisQuery = queryFactory.getApisForContext();
        try(PreparedStatement stmt = conn.prepareStatement(getApisQuery)) {
            stmt.setString(1, context);

            try (ResultSet rs = stmt.executeQuery()) {
                List<String> apis = new ArrayList<>();
                while (rs.next()) {
                    apis.add(rs.getString("API"));
                }

                return apis;
            }
        }
    }

    public int getApiCountForContext(String context, String api) throws SQLException {
        String getApisQuery = queryFactory.getAPICountForContext();
        try(PreparedStatement stmt = conn.prepareStatement(getApisQuery)) {
            stmt.setString(1, context);
            stmt.setString(2, api);

            try(ResultSet rs = stmt.executeQuery()) {
                int count = 0;
                count = rs.getInt("Count");
                return count;
            }
        }
    }

    @Override
    public List<String> getTokensForAPI(String api) throws SQLException {
        String query = queryFactory.getTokensForAPI();
        try(PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, api);
            try(ResultSet rs = stmt.executeQuery()) {

                List<String> tokens = new ArrayList<>();
                while (rs.next()) {
                    tokens.add(rs.getString(TOKEN_COLUMN));
                }

                return tokens;
            }
        }
    }

    @Override
    public KAC getTopKAPIForToken(int k, String keyword) throws SQLException {
        String query = queryFactory.getTopKCountedAPIsForKeyword();

        try(PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, keyword);
            try(ResultSet rs = stmt.executeQuery()) {
                int counter = 0;
                Map<Integer, MethodName> kacMap = new HashMap<>();

                while (rs.next() && counter < k) {
                    kacMap.put(rs.getInt("SUM(COUNT)"), new MethodName(rs.getString("API")));

                    counter++;
                }
                return new KAC(keyword, kacMap);
            }
        }
    }

    @Override
    public boolean saveMinedContext(ModelEntry modelEntry) throws SQLException {
        List<String> apis = modelEntry.apiReferences;
        ITypeName name = modelEntry.enclosingContextType;
        List<String> tokens = modelEntry.tokens;

        storeAPIS(apis);
        storeTokens(tokens);
        storeContext(name);
        saveApiContextReference(apis, name);
        saveTokenContextReference(tokens, name);
        return false;
    }

    public KKC getKKCForKeywords(Map.Entry<String, String> keywordPair) throws SQLException {
        Double score = this.getKKCScore(keywordPair);
        List<MethodName> correspondingAPIs = this.getAPIsForKeywords(keywordPair);

        return new KKC(keywordPair, correspondingAPIs, score);
    }

    @Override
    public double getKKCScore(Map.Entry<String, String> keyWordPair) throws SQLException {
        String keyword1 = keyWordPair.getKey();
        String keyword2 = keyWordPair.getValue();

        String getContextsQueryForToken = queryFactory.getCountedNeighborTokens();

        try(PreparedStatement stmt = conn.prepareStatement(getContextsQueryForToken)) {
            stmt.setString(1, keyword1);

            HashMap<String, Double> c1 = new HashMap<>();
            HashMap<String, Double> c2 = new HashMap<>();

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    parseRow(rs, c1);
                }
            }

            stmt.setString(1, keyword2);

            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    parseRow(rs, c2);
                }
            }

            return computeCosineSimilarity(c1, c2);
        }
    }

    public List<String> getTokensForContext(String context) throws SQLException {
        String getTokensQuery = queryFactory.getTokensFromContext();
        try(PreparedStatement stmt = conn.prepareStatement(getTokensQuery)) {
            stmt.setString(1, context);
            try(ResultSet rs = stmt.executeQuery()) {

                List<String> tokens = new ArrayList<>();
                while (rs.next()) {
                    tokens.add(rs.getString(TOKEN_COLUMN));
                }
                return tokens;
            }
        }
    }


    protected boolean apiContextReferenceExists(String api, ITypeName context) throws SQLException {
        boolean foundReference = false;
        String query = queryFactory.getAPIReferences();
        try(PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, api);
            stmt.setString(2, context.toString());
            try(ResultSet rs = stmt.executeQuery()) {
                foundReference = rs.next();

                return foundReference;
            }
        }
    }

    protected void storeContext(ITypeName name) throws SQLException {
        String insertContext = queryFactory.storeContext();

        try(PreparedStatement stmt = conn.prepareStatement(insertContext)) {
            stmt.setString(1, name.toString());
            stmt.execute();
        }
    }

    protected void storeTokens(List<String> tokens) throws SQLException {
        try(PreparedStatement statement = conn.prepareStatement(queryFactory.storeTokens())) {
            int batchCounter = 0;
            for(String token: tokens) {
                statement.setString(1, token);
                statement.addBatch();
                batchCounter++;

                boolean executeBatch = shouldExecuteBatch(batchCounter, tokens.size());
                if(executeBatch) {
                   statement.executeBatch();
                }
            }
        }
    }

    protected boolean shouldExecuteBatch(int batchCounter, int nrOfItems) {
        boolean batchSizeReached = batchCounter % BATCH_SIZE == 0;
        boolean itemsDepleated = batchCounter == nrOfItems;
        return batchSizeReached || itemsDepleated;
    }

    protected void storeAPIS(List<String> apis) throws SQLException {
        String sqlStatement = queryFactory.storeAPIs();
        try(PreparedStatement statement = conn.prepareStatement(sqlStatement)) {
            int batchCounter = 0;
            for(String api: apis) {
                statement.setString(1, api);	
                statement.addBatch();
                batchCounter++;

                boolean executeBatch = shouldExecuteBatch(batchCounter, apis.size());
                if(executeBatch) {
                   statement.executeBatch();
                }
            }
        }
    }

    protected List<String> getAPIs() throws SQLException {
        String getAPIsQuery = queryFactory.getAllAPIs();

        List<String> apis = new ArrayList<>();
        try(PreparedStatement statement = conn.prepareStatement(getAPIsQuery)) {
            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    apis.add(rs.getString("API"));
                }

                return apis;
            }
        }
    }

    protected List<String> getContexts() throws SQLException {
        String getContextsQuery = queryFactory.getAllContexts();

        List<String> contexts = new ArrayList<>();
        try(PreparedStatement statement = conn.prepareStatement(getContextsQuery)) {
            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    contexts.add(rs.getString("Context"));
                }
                return contexts;
            }
        }
    }

    protected List<String> getTokens() throws SQLException {
        String getTokensQuery = queryFactory.getAllTokens();

        List<String> tokens = new ArrayList<>();
        try(PreparedStatement statement = conn.prepareStatement(getTokensQuery)) {
            try(ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    tokens.add(rs.getString(TOKEN_COLUMN));
                }
                return tokens;
            }
        }
    }

    protected double computeCosineSimilarity(HashMap<String, Double> vector1, HashMap<String, Double> vector2) {
        return CosineSimilarity.compute(vector1, vector2);
    }

    private List<MethodName> getAPIsForKeywords(Map.Entry<String, String> keywordPair) throws SQLException {
        String firstKeyword = keywordPair.getKey();
        String secondKeyword = keywordPair.getValue();
        String getAPIQuery = queryFactory.getApisFromKeywordPairQuery();

        try(PreparedStatement stmt = conn.prepareStatement(getAPIQuery)) {
            stmt.setString(1, firstKeyword);
            stmt.setString(2, secondKeyword);
            try(ResultSet rs = stmt.executeQuery()) {

                List<MethodName> apis = new ArrayList<>();
                while (rs.next()) {
                    apis.add(new MethodName(rs.getString("API")));
                }
                return apis;
            }
        }
    }

    private void createContextTable() throws SQLException {
        String createContextSchema = queryFactory.createContextTable();

        boolean contextTableExistsAlready = tableExists("contexts");
        if(contextTableExistsAlready) {
            return;
        }

        try(PreparedStatement statement = conn.prepareStatement(createContextSchema)) {
            statement.execute();
        }
    }

    private void createAPITable() throws SQLException {
        String createAPISchema = queryFactory.createAPITable();

        boolean apiTableExists = tableExists("apis");
        if(apiTableExists) {
            return;
        }

        try(PreparedStatement statement = conn.prepareStatement(createAPISchema)) {
            statement.execute();
        }
    }

    private void createTokenTable() throws SQLException {
        String createTokenSchema = queryFactory.createTokenTable();

        boolean tokenTableExists = tableExists("tokens");
        if(tokenTableExists) {
            return;
        }
        try(PreparedStatement statement = conn.prepareStatement(createTokenSchema)) {
            statement.execute();
        }
    }

    private void createTokenReferenceTable() throws SQLException {
        String createTokenReferenceSchema = queryFactory.createTokenReferenceTable();

        boolean tokenRefTableExists = tableExists("TokenReferences");
        if(tokenRefTableExists) {
            return;
        }
        try(PreparedStatement statement = conn.prepareStatement(createTokenReferenceSchema)) {
            statement.execute();
        }
    }

    private void createAPIReferenceTable() throws SQLException {
        String createAPIReferenceSchema = queryFactory.createAPIReferenceTable();

        boolean apiRefTableExists = tableExists("APIReferences");
        if(apiRefTableExists) {
            return;
        }
        try(PreparedStatement statement = conn.prepareStatement(createAPIReferenceSchema)) {
            statement.execute();
        }
    }

    private void createNewTokenContextReference(String token, ITypeName context) throws SQLException {
        String storeReference = queryFactory.storeNewTokenContextReference();

        try(PreparedStatement stmt = conn.prepareStatement(storeReference)) {
            stmt.setString(1, token);
            stmt.setString(2, context.toString());
            stmt.execute();
        }
    }

    private void updateExistingTokenContextReference(String token, ITypeName context) throws SQLException {
        String updateReference = queryFactory.incrementCounterOfTokenRefernce();

        try(PreparedStatement stmt = conn.prepareStatement(updateReference)) {
            stmt.setString(1, token);
            stmt.setString(2, context.toString());
            stmt.execute();
        }
    }

    private boolean tokenContextReferenceExists(String token, ITypeName context) throws SQLException {
        String query = queryFactory.getTokenReferences();

        boolean foundReference = false;
        try(PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, token);
            stmt.setString(2, context.toString());
            try(ResultSet rs = stmt.executeQuery()) {
                foundReference = rs.next();

                return foundReference;
            }
        }
    }

    private void updateExistingApiContextReference(String api, ITypeName context) throws SQLException {
        String updateReference = queryFactory.incrementCounterOfAPIReference();

        try(PreparedStatement stmt = conn.prepareStatement(updateReference)) {
            stmt.setString(1, api);
            stmt.setString(2, context.toString());
            stmt.execute();
        }
    }

    private void createNewApiContextReference (String api, ITypeName context) throws SQLException {
        String storeReference = queryFactory.storeNewAPIContextReference();

        try(PreparedStatement stmt = conn.prepareStatement(storeReference)) {
            stmt.setString(1, api);
            stmt.setString(2, context.toString());
            stmt.execute();
        }
    }

    private void parseRow(ResultSet rs, HashMap<String, Double> target) throws SQLException {
        target.put(""+rs.getInt(TOKEN_COLUMN), (double) rs.getInt("SUM(Count)"));
    }
}
