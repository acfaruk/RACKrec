package ch.uzh.rackrec.model.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SQLiteProviderTest {
	String testingDB = "./testdb.db";
	SQLiteProvider provider;

	@Before
	public void initialize() {
		Properties settings = new Properties();
		settings.put("database-file", testingDB);
		provider = new SQLiteProvider(settings);
	}
	
	@After
	public void deleteDB() {
		provider.closeConnection();
        File file = new File(testingDB); 
        file.delete();
	}

	@Test
	public void testPrepareStoreTokens() {
		String[] tokens = {"comput", "MD5", "Hash"};
		List<String> testTokens = new ArrayList<String>(Arrays.asList(tokens));
		String sqlStatement = provider.prepareStoreTokens(testTokens);
		String expected = "INSERT INTO tokens (Token) VALUES (\"comput\"),\n(\"MD5\"),\n(\"Hash\");";
	
		assertEquals(sqlStatement, expected);
	}

	@Test
	public void testPrepareStoreAPI() {
		String[] apis = {"someclass.somemethod", "otherclass.othermethod"};
		String sqlStatement = provider.prepareStoreAPI(apis[0]);
		String expected = "INSERT INTO apis (API) VALUES (\"someclass.somemethod\")";
		System.out.println(sqlStatement);
	
		assertEquals(sqlStatement, expected);
	}
	@Test
	public void testCheckTableExists() throws SQLException {
		boolean tableDoesNotExist = !provider.tableExists("testTable123");
		
		assertTrue(tableDoesNotExist);
	}
	
	@Test
	public void testCreateSchemas() {
		boolean tableDoesExist = false;
		boolean apiTableDoesExist = false;
		try {
    		provider.prepareSchemas();
			tableDoesExist = provider.tableExists("contexts");
			apiTableDoesExist = provider.tableExists("apis");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(apiTableDoesExist);
		assertTrue(tableDoesExist);
	}
	
	@Test
	public void testMultipleCreation() throws SQLException {
		provider.prepareSchemas();
		boolean tableDoesExist = provider.tableExists("contexts");
		assertTrue(tableDoesExist);
		try { 
			provider.prepareSchemas();
			assertTrue(tableDoesExist);
		} catch(Exception e) {
			fail("Creation methods should check if the table to be created exists");
			e.printStackTrace();
		}
	}

	@Test
	public void testStoreApis() throws SQLException {
		provider.prepareSchemas();
		assertTrue(provider.tableExists("apis"));
		String[] apis = {"someclass.somemethod", "otherclass.othermethod"};
		provider.storeAPIS(Arrays.asList(apis));
		
		List<String> apiRows = provider.getAPIs();
		int actualLength = apiRows.size();
		int expectedLength = 2;
		assertEquals(expectedLength, actualLength);
	}
}
