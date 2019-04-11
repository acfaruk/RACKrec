package ch.uzh.rackrec.model.provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import cc.kave.commons.model.naming.types.ITypeName;

public class SQLiteProvider implements IDatabaseProvider{
    private Connection conn = null;
    private Properties properties;
    private String dbLocation;

    @Inject
    public SQLiteProvider(Properties properties) {
    	dbLocation = properties.getProperty("database-file");

        try {
            // db parameters
            String url = "jdbc:sqlite:" + dbLocation;
            // create a connection to the database
             conn = DriverManager.getConnection(url);
             System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
        if (conn != null) {
           //conn.close();
         }
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
        String createAPISchema = ""
            + "CREATE TABLE apis("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "API varchar(255) NOT NULL UNIQUE"
            + ")";
        Statement statement = conn.createStatement();
        statement.execute(createAPISchema);
    }

  private void createTokenTable() throws SQLException {
    String createTokenSchema = ""
                  + "CREATE TABLE tokens("
                  + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                  + "Token varchar(255) NOT NULL UNIQUE"
                  + ")";
    Statement statement = conn.createStatement();
    statement.execute(createTokenSchema);
  }
  
  private void createTokenReferenceTable() throws SQLException {
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

/*
  public static void main(String[] args) {
    
    Properties props = new Properties();
    SQLiteProvider provider = new SQLiteProvider(props);
    try {
      System.out.println(provider.tableExists("tokens"));
      System.out.println(provider.tableExists("butter"));
      System.out.println(provider.getTokensForAPI("Digest"));
      provider.closeConnection();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  */

  @Override
  public List<String> getTokensForAPI(String api) throws SQLException {
    Statement stmt = conn.createStatement();
    String exists = "SELECT * FROM tokens";
    ResultSet rs = stmt.executeQuery(exists);
    List<String> tokens = new ArrayList<String>();
    while(rs.next())
      tokens.add(rs.getString("token"));
    return tokens;
  }

  @Override
  public List<String> getTopKAPIForToken(int k, String keyword) {
    // TODO Auto-generated method stub
    return null;
  }

@Override
public boolean saveMinedContext(ModelEntry modelEntry) throws SQLException {
	List<String> apis = modelEntry.apiReferences;
	ITypeName name = modelEntry.enclosingContextType;
	List<String> tokens = modelEntry.tokens;
	
	storeAPIS(apis);
	storeTokens(tokens);
	storeApiContextReference();
	storeTokenContextReference();
	return false;
}

private void storeTokens(List<?> tokens) {
	String sql = prepareStoreTokens(tokens);
}
public String prepareStoreTokens (List<?>tokens) {
	String values = tokens.stream()
		  .map(token -> "(\"" + token + "\"),")
		  .collect(Collectors.joining("\n"));

	values = terminateSqlStatement(values);
	String insertTokens = ""
			+ "INSERT INTO tokens (Token) VALUES "
			+ values;

	return insertTokens;
}

public String prepareStoreAPI(String api) {
	String insertAPIs = "INSERT INTO apis (API) VALUES " + "(\"" + api + "\")";

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

private void storeApiContextReference() {
	// TODO Auto-generated method stub
	
}

public void storeAPIS(List<String> apis) throws SQLException {
	for(String api: apis) {
		try {
		Statement statement = conn.createStatement();
		statement.execute(prepareStoreAPI(api));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

public List<String> getAPIs() throws SQLException {
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

}
