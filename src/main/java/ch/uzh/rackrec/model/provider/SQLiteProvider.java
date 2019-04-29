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

    @Inject
    public SQLiteProvider(Properties properties, Logger logger) {
        dbLocation = properties.getProperty("database-file");

        try {
            // db parameters
            String url = "jdbc:sqlite:" + dbLocation;
            // create a connection to the database
             conn = DriverManager.getConnection(url);
             logger.log(null, "Connection to SQLite has been established.");
        } catch (SQLException e) {
             logger.log(null, "Connection to SQLite has been established.");
        }
    }

    public boolean tableExists(String tableName) throws SQLException {
        String tableExistsQuery = ""
           + "SELECT name "
           + "FROM sqlite_master "
           + "WHERE type='table' "
           + "AND name='" + tableName + "';";
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(tableExistsQuery);

        return rs.next();
    }

    @Override
    public void prepareSchemas() throws SQLException {
        createTokenTable();
        createAPITable();
        createContextTable();
        createTokenReferenceTable();
        createAPIReferenceTable();
    } 
  
    private void createContextTable() throws SQLException {
        boolean contextTableExistsAlready = tableExists("contexts");
        if(contextTableExistsAlready) {
            return;
        }

        String createContextSchema = ""
            + "CREATE TABLE contexts("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "Context varchar(255) NOT NULL"
            + ")";
        Statement statement = conn.createStatement();
        statement.execute(createContextSchema);
    }

    private void createAPITable() throws SQLException {
        boolean apiTableExists = tableExists("apis");
        if(apiTableExists) {
            return;
        }

        String createAPISchema = ""
            + "CREATE TABLE apis("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "API varchar(255) NOT NULL UNIQUE"
            + ")";
        Statement statement = conn.createStatement();
        statement.execute(createAPISchema);
    }

    private void createTokenTable() throws SQLException {
        boolean tokenTableExists = tableExists("tokens");
        if(tokenTableExists) {
            return;
        }

        String createTokenSchema = ""
                      + "CREATE TABLE tokens("
                      + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                      + "Token varchar(255) NOT NULL UNIQUE"
                      + ")";
        Statement statement = conn.createStatement();
        statement.execute(createTokenSchema);
      }

    private void createTokenReferenceTable() throws SQLException {
        boolean tokenRefTableExists = tableExists("TokenReferences");
        if(tokenRefTableExists) {
            return;
        }
        String createTokenReferenceSchema =  ""
            + "CREATE TABLE TokenReferences("
            + "Token int,"
            + "Context int,"
            + "Count int,"
            + "FOREIGN KEY (Token) REFERENCES tokens(ID),"
            + "FOREIGN KEY (Context) REFERENCES contexts(ID)"
            + ")";
        Statement statement = conn.createStatement();
        statement.execute(createTokenReferenceSchema);
    }

    private void createAPIReferenceTable() throws SQLException {
        boolean apiRefTableExists = tableExists("APIReferences");
        if(apiRefTableExists) {
            return;
        }
        String createAPIReferenceSchema =  ""
           + "CREATE TABLE APIReferences("
           + "API int,"
           + "Context int,"
           + "Count int,"
           + "FOREIGN KEY (API) REFERENCES apis(ID),"
           + "FOREIGN KEY (Context) REFERENCES contexts(ID)"
           + ")";
        Statement statement = conn.createStatement();
        statement.execute(createAPIReferenceSchema);
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
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

    private void createNewTokenContextReference(String token, ITypeName context) throws SQLException {
        String storeReference = ""
            + "INSERT INTO TokenReferences (Token, Context, Count) VALUES ("
            + "(SELECT ID FROM tokens WHERE Token=\""+ token +"\"), "
            + "(SELECT ID FROM contexts WHERE Context=\""+ context +"\"),"
            + " 1)";

        Statement stmt = conn.createStatement();
        stmt.execute(storeReference);
    }

    private void updateExistingTokenContextReference(String token, ITypeName context) throws SQLException {
        String updateReference = ""
            + "UPDATE TokenReferences "
            + "SET Count = Count + 1 "
            + "WHERE Token=(SELECT ID FROM tokens WHERE Token=\""+ token +"\")"
            + "AND Context=(SELECT ID FROM contexts WHERE Context=\""+ context +"\")";

        Statement stmtn = conn.createStatement();
        stmtn.execute(updateReference);
    }

    private boolean tokenContextReferenceExists(String token, ITypeName context) throws SQLException {
        boolean foundReference = false;
        String query = ""
           + "SELECT * FROM TokenReferences WHERE Token=(SELECT ID FROM tokens WHERE Token=\""+ token +"\")"
           + "AND Context=(SELECT ID FROM contexts WHERE Context=\""+ context +"\")";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        foundReference = rs.next();

        return foundReference;
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

    private void updateExistingApiContextReference(String api, ITypeName context) throws SQLException {
        String updateReference = ""
            + "UPDATE APIReferences "
            + "SET Count = Count + 1 "
            + "WHERE API=(SELECT ID FROM apis WHERE API=\""+ api +"\")"
            + "AND Context=(SELECT ID FROM contexts WHERE Context=\""+ context +"\")";

        Statement stmtn = conn.createStatement();
        stmtn.execute(updateReference);
    }

    private void createNewApiContextReference (String api, ITypeName context) throws SQLException {
        String storeReference = ""
            + "INSERT INTO APIReferences "
            + "(API, Context, Count) VALUES ("
				+ "(SELECT ID FROM apis WHERE API=\""+api+"\"), "
				+ "(SELECT ID FROM contexts WHERE CONTEXT=\""+ context +"\"),"
				+ "1"
			+ ")";

        Statement stmt = conn.createStatement();
        stmt.execute(storeReference);
    }

    protected boolean apiContextReferenceExists(String api, ITypeName context) throws SQLException {
        boolean foundReference = false;
        String query = ""
           + "SELECT * FROM APIReferences WHERE API=(SELECT ID FROM apis WHERE API=\""+ api +"\")"
           + "AND Context=(SELECT ID FROM contexts WHERE Context=\""+ context +"\")";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        foundReference = rs.next();

        return foundReference;
    }

    public List<String> getApisForContext(String context) throws SQLException {
        String getApisQuery = "SELECT API FROM APIReferences WHERE Context=(SELECT ID FROM contexts WHERE Context=\""+ context +"\")";
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(getApisQuery);
        List<String> apis = new ArrayList<String>();
        while(rs.next()) {
            apis.add(rs.getString("API"));
        }
        return apis;
    }

    public int getApiCountForContext(String context, String api) throws SQLException {
        String getApisQuery = ""
            + "SELECT Count "
            + "FROM APIReferences "
            + "WHERE Context=(SELECT ID FROM contexts WHERE Context=\""+ context +"\")"
            + "AND "
            + "API=(SELECT ID FROM apis WHERE API=\""+ api +"\")";

        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(getApisQuery);
        int count = 0;
        count = rs.getInt("Count");
        return count;
    }

    @Override
    public List<String> getTokensForAPI(String api) throws SQLException {
    	List<String> tokens = new ArrayList<String>();

        String query = ""
          + "WITH ContextsWithApi AS (SELECT Context FROM APIReferences WHERE API=(SELECT ID FROM apis WHERE API=\"" + api + "\")),"
          + "     TokenReferencesWithApi AS (SELECT Token FROM TokenReferences WHERE Context IN ContextsWithApi)"
          + "SELECT Token From tokens WHERE ID IN TokenReferencesWithApi";
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
        	tokens.add(rs.getString("Token"));
        }

        return tokens;
    }

    @Override
    public KAC getTopKAPIForToken(int k, String keyword) throws SQLException {
        String query = ""
          + "WITH ContextsWithKeyword AS (SELECT Context FROM TokenReferences WHERE Token=(SELECT ID FROM tokens WHERE Token=\"" + keyword + "\")),"
          + "     APIReferencesWithKeyword AS (SELECT API as ReferencedAPI, Count FROM APIReferences WHERE Context IN ContextsWithKeyword)"
          + "SELECT API, SUM(COUNT) From apis JOIN APIReferencesWithKeyword on apis.ID=APIReferencesWithKeyword.ReferencedAPI GROUP BY API ORDER BY SUM(COUNT) DESC";
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        int counter = 0;
        Map<Integer, MethodName> kacMap = new HashMap<Integer, MethodName>();

        while(rs.next() && counter < k) {
        	kacMap.put(rs.getInt("SUM(COUNT)"), new MethodName(rs.getString("API")));
      
        	counter++;
        }
        KAC kac = new KAC(keyword, kacMap);
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

    protected void storeContext(ITypeName name) throws SQLException {
        // TODO Auto-generated method stub
        String insertContext = "" 
           + "INSERT OR IGNORE INTO Contexts (Context) VALUES (\"" + name + "\")";

        Statement statement = conn.createStatement();
        statement.execute(insertContext);
    }

    protected void storeTokens(List<String> tokens) throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute(prepareStoreTokens(tokens));
    }
    protected String prepareStoreTokens (List<String>tokens) {
        String values = tokens.stream()
          .map(token -> "(\"" + token + "\"),")
          .collect(Collectors.joining("\n"));

        values = terminateSqlStatement(values);
        String insertTokens = ""
            + "INSERT OR IGNORE INTO tokens (Token) VALUES "
            + values;

        return insertTokens;
    }

    protected String prepareStoreAPI(List<String> apis) {
        String values = apis.stream()
          .map(token -> "(\"" + token + "\"),")
          .collect(Collectors.joining("\n"));

        values = terminateSqlStatement(values);
        String insertAPIs = ""
            + "INSERT OR IGNORE INTO apis (API) VALUES "
            + values;

        return insertAPIs;
    }

    private String terminateSqlStatement(String statement) {
        return replaceLastChar(statement, ';');
    }

    private String replaceLastChar(String target, Character newChar) {
        return target.substring(0, target.length()-1) + newChar;

    }

    private void storeTokenContextReference() {
        // TODO Auto-generated method stub
    }


    protected void storeAPIS(List<String> apis) throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute(prepareStoreAPI(apis));
    }

    protected List<String> getAPIs() throws SQLException {
        String getAPIsQuery = ""
                + "SELECT API from apis";

        List<String> apis = new ArrayList<String>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(getAPIsQuery);
        while(rs.next()) {
            apis.add(rs.getString("API"));
        }
        return apis;
    }

    protected List<String> getContexts() throws SQLException {
        String getContextsQuery = ""
                + "SELECT Context from Contexts";

        List<String> contexts = new ArrayList<String>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(getContextsQuery);
        while(rs.next()) {
            contexts.add(rs.getString("Context"));
        }
        return contexts;
    }

    protected List<String> getTokens() throws SQLException {
        String getTokensQuery = ""
                + "SELECT Token from tokens";

        List<String> tokens = new ArrayList<String>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(getTokensQuery);
        while(rs.next()) {
            tokens.add(rs.getString("Token"));
        }
        return tokens;
    }

    public List<String> getTokensForContext(String context) throws SQLException {
        String getTokensQuery = "SELECT Token FROM TokenReferences WHERE Context=(SELECT ID FROM contexts WHERE Context=\""+ context +"\")";
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(getTokensQuery);
        List<String> tokens = new ArrayList<String>();
        while(rs.next()) {
            tokens.add(rs.getString("Token"));
        }
        return tokens;
    }

    private String prepareGetContextQuery(String token) {
		String getContextsQuery = ""
			+ "select token, sum(count) "
			+ "from tokenreferences "
			+ "where context in("
				+ "select context "
				+ "from tokenreferences "
				+ "where token=("
					+ "select id "
					+ "from tokens "
					+ "where token=\""+ token +"\""
				+ ")"
			+ ") group by token";
		return getContextsQuery;
    	
    }
    public KKC getKKCForKeywords(Map.Entry<String, String> keywordPair) throws SQLException {
        Double score = this.getKKCScore(keywordPair);
        List<MethodName> correspondingAPIs = this.getAPIsForKeywords(keywordPair);

        return new KKC(keywordPair, correspondingAPIs, score);
    }

    private List<MethodName> getAPIsForKeywords(Map.Entry<String, String> keywordPair) throws SQLException {
        String getAPIQuery = ""
            + "WITH RelevantContexts AS("
                + "SELECT Context "
                + "FROM TokenReferences "
                + "Where Token=("
                    + "Select ID "
                    + "from tokens "
                    + "where token=\""+ keywordPair.getKey() + "\" OR "
                    + "token=\"" + keywordPair.getValue() + "\""
                + ")"
            + "), RelevantAPIS AS("
                + "SELECT API FROM APIReferences WHERE Context IN RelevantContexts"
            + ")"
            + "SELECT API From apis Where ID IN RelevantAPIS";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(getAPIQuery);
        List<MethodName> apis = new ArrayList<MethodName>();
        while(rs.next()) {
            apis.add(new MethodName(rs.getString("API")));
        }
        return apis;
    }

    @Override
	public double getKKCScore(Map.Entry<String, String> keyWordPair) throws SQLException {
		String getContextsQueryForToken1 = prepareGetContextQuery(keyWordPair.getKey());
		String getContextsQueryForToken2 = prepareGetContextQuery(keyWordPair.getValue());
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
		
		return computeCosineSimilarity(c1, c2);
	}
	private void parseRow(ResultSet rs, HashMap<String, Double> target) throws SQLException {
			target.put(""+rs.getInt("Token"), (double) rs.getInt("SUM(Count)"));
	}

	protected double computeCosineSimilarity(HashMap<String, Double> vector1, HashMap<String, Double> vector2) {
		  Set<String> sharedKeys = new HashSet<>(vector1.keySet());
		  Set<String> keysVector1 = vector1.keySet();
		  Set<String> keysVector2 = vector2.keySet();

		  sharedKeys.retainAll(vector2.keySet());
		  double sumproduct = 0; 
		  double A = 0;
		  double B = 0;
		  sumproduct = sharedKeys.stream()
			               		 .mapToDouble(key -> vector1.get(key) * vector2.get(key))
			               		 .sum();
		  
		  A = keysVector1.stream()
				  		 .mapToDouble(key -> vector1.get(key) * vector1.get(key))
				  		 .sum();

		  B = keysVector2.stream()
				  		 .mapToDouble(key -> vector2.get(key) * vector2.get(key))
				  		 .sum();

		  return sumproduct / Math.sqrt(A * B);
	}
}
