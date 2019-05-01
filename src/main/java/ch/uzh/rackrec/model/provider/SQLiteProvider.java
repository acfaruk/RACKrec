package ch.uzh.rackrec.model.provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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


    @Inject
    public SQLiteProvider(Properties properties, Logger logger) {
        dbLocation = properties.getProperty("database-file");
        queryFactory = new SQLQueryFactory();
        this.logger = logger;

        try {
            String url = "jdbc:sqlite:" + dbLocation;
             conn = DriverManager.getConnection(url);
             logger.log(null, "Connection to SQLite has been established.");
        } catch (SQLException e) {
             logger.log(null, e.getLocalizedMessage());
        }
    }

    public boolean tableExists(String tableName) throws SQLException {
        String tableExistsQuery = queryFactory.getSQLiteTable(tableName);

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(tableExistsQuery);
        boolean tableExists = rs.next();

        statement.close();
        return tableExists;
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
        String getApisQuery = queryFactory.getApisForContext(context);
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(getApisQuery);
        List<String> apis = new ArrayList<String>();
        while(rs.next()) {
            apis.add(rs.getString("API"));
        }

        stmt.close();
        return apis;
    }

    public int getApiCountForContext(String context, String api) throws SQLException {
        String getApisQuery = queryFactory.getAPICountForContext(context, api);
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(getApisQuery);
        int count = 0;
        count = rs.getInt("Count");
        stmt.close();
        return count;
    }

    @Override
    public List<String> getTokensForAPI(String api) throws SQLException {
        String query = queryFactory.getTokensForAPI(api);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        List<String> tokens = new ArrayList<String>();
        while(rs.next()) {
            tokens.add(rs.getString("Token"));
        }

        stmt.close();
        return tokens;
    }

    @Override
    public KAC getTopKAPIForToken(int k, String keyword) throws SQLException {
        String query = queryFactory.getTopKCountedAPIsForKeyword(keyword);

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        int counter = 0;
        Map<Integer, MethodName> kacMap = new HashMap<Integer, MethodName>();

        while(rs.next() && counter < k) {
            kacMap.put(rs.getInt("SUM(COUNT)"), new MethodName(rs.getString("API")));

            counter++;
        }
        KAC kac = new KAC(keyword, kacMap);
        stmt.close();
        return kac;
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

        String getContextsQueryForToken1 = queryFactory.getCountedNeighborTokens(keyword1);
        String getContextsQueryForToken2 = queryFactory.getCountedNeighborTokens(keyword2);
        Statement stmt = conn.createStatement();

        HashMap<String, Double> c1 = new HashMap<String, Double>();
        HashMap<String, Double> c2 = new HashMap<String, Double>();

        ResultSet rs = stmt.executeQuery(getContextsQueryForToken1);
        while(rs.next()) {
            parseRow(rs, c1);
        }

        rs = stmt.executeQuery(getContextsQueryForToken2);
        while(rs.next()) {
            parseRow(rs, c2);
        }

        stmt.close();
        return computeCosineSimilarity(c1, c2);
    }

    public List<String> getTokensForContext(String context) throws SQLException {
        String getTokensQuery = queryFactory.getTokensFromContext(context);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(getTokensQuery);

        List<String> tokens = new ArrayList<String>();
        while(rs.next()) {
            tokens.add(rs.getString("Token"));
        }
        stmt.close();
        return tokens;
    }


    protected boolean apiContextReferenceExists(String api, ITypeName context) throws SQLException {
        boolean foundReference = false;
        String query = queryFactory.getAPIReferences(api, context);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        foundReference = rs.next();

        stmt.close();
        return foundReference;
    }

    protected void storeContext(ITypeName name) throws SQLException {
        String insertContext = queryFactory.storeContext(name);

        Statement statement = conn.createStatement();
        statement.execute(insertContext);
        statement.close();
    }

    protected void storeTokens(List<String> tokens) throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute(queryFactory.storeTokens(tokens));
        statement.close();
    }

    protected void storeAPIS(List<String> apis) throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute(queryFactory.storeAPIs(apis));
        statement.close();
    }

    protected List<String> getAPIs() throws SQLException {
        String getAPIsQuery = queryFactory.getAllAPIs();

        List<String> apis = new ArrayList<String>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(getAPIsQuery);
        while(rs.next()) {
            apis.add(rs.getString("API"));
        }
        statement.close();
        return apis;
    }

    protected List<String> getContexts() throws SQLException {
        String getContextsQuery = queryFactory.getAllContexts();

        List<String> contexts = new ArrayList<String>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(getContextsQuery);
        while(rs.next()) {
            contexts.add(rs.getString("Context"));
        }
        statement.close();
        return contexts;
    }

    protected List<String> getTokens() throws SQLException {
        String getTokensQuery = queryFactory.getAllTokens();

        List<String> tokens = new ArrayList<String>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(getTokensQuery);
        while(rs.next()) {
            tokens.add(rs.getString("Token"));
        }
        statement.close();
        return tokens;
    }

	protected double computeCosineSimilarity(HashMap<String, Double> vector1, HashMap<String, Double> vector2) {
        return CosineSimilarity.compute(vector1, vector2);
	}

    private List<MethodName> getAPIsForKeywords(Map.Entry<String, String> keywordPair) throws SQLException {
        String getAPIQuery = queryFactory.getApisFromKeywordPairQuery(keywordPair.getKey(), keywordPair.getValue());

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(getAPIQuery);

        List<MethodName> apis = new ArrayList<MethodName>();
        while(rs.next()) {
            apis.add(new MethodName(rs.getString("API")));
        }
        stmt.close();
        return apis;
    }

    private void createContextTable() throws SQLException {
        String createContextSchema = queryFactory.createContextTable();

        boolean contextTableExistsAlready = tableExists("contexts");
        if(contextTableExistsAlready) {
            return;
        }

        Statement statement = conn.createStatement();
        statement.execute(createContextSchema);
        statement.close();
    }

    private void createAPITable() throws SQLException {
        String createAPISchema = queryFactory.createAPITable();

        boolean apiTableExists = tableExists("apis");
        if(apiTableExists) {
            return;
        }

        Statement statement = conn.createStatement();
        statement.execute(createAPISchema);
        statement.close();
    }

    private void createTokenTable() throws SQLException {
        String createTokenSchema = queryFactory.createTokenTable();

        boolean tokenTableExists = tableExists("tokens");
        if(tokenTableExists) {
            return;
        }
        Statement statement = conn.createStatement();
        statement.execute(createTokenSchema);
        statement.close();
    }

    private void createTokenReferenceTable() throws SQLException {
        String createTokenReferenceSchema = queryFactory.createTokenReferenceTable();

        boolean tokenRefTableExists = tableExists("TokenReferences");
        if(tokenRefTableExists) {
            return;
        }
        Statement statement = conn.createStatement();
        statement.execute(createTokenReferenceSchema);
        statement.close();
    }

    private void createAPIReferenceTable() throws SQLException {
        String createAPIReferenceSchema = queryFactory.createAPIReferenceTable();

        boolean apiRefTableExists = tableExists("APIReferences");
        if(apiRefTableExists) {
            return;
        }
        Statement statement = conn.createStatement();
        statement.execute(createAPIReferenceSchema);
        statement.close();
    }

    private void createNewTokenContextReference(String token, ITypeName context) throws SQLException {
        String storeReference = queryFactory.storeNewTokenContextReference(token, context);

        Statement stmt = conn.createStatement();
        stmt.execute(storeReference);
        stmt.close();
    }

    private void updateExistingTokenContextReference(String token, ITypeName context) throws SQLException {
        String updateReference = queryFactory.incrementCounterOfTokenRefernce(token, context);

        Statement stmt = conn.createStatement();
        stmt.execute(updateReference);
        stmt.close();
    }

    private boolean tokenContextReferenceExists(String token, ITypeName context) throws SQLException {
        String query = queryFactory.getTokenReferences(token, context);

        boolean foundReference = false;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        foundReference = rs.next();

        stmt.close();
        return foundReference;
    }

    private void updateExistingApiContextReference(String api, ITypeName context) throws SQLException {
        String updateReference = queryFactory.incrementCounterOfAPIReference(api, context);

        Statement stmt = conn.createStatement();
        stmt.execute(updateReference);
        stmt.close();
    }

    private void createNewApiContextReference (String api, ITypeName context) throws SQLException {
        String storeReference = queryFactory.storeNewAPIContextReference(api, context);

        Statement stmt = conn.createStatement();
        stmt.execute(storeReference);
        stmt.close();
    }

    private void parseRow(ResultSet rs, HashMap<String, Double> target) throws SQLException {
        target.put(""+rs.getInt("Token"), (double) rs.getInt("SUM(Count)"));
    }
}
