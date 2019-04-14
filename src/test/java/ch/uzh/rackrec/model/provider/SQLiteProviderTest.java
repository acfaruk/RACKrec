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
import static org.mockito.Mockito.mock;

import cc.kave.commons.model.naming.impl.v0.types.TypeName;

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
		String sqlStatement = provider.prepareStoreTokens(Arrays.asList(tokens));
		String expected = "INSERT OR IGNORE INTO tokens (Token) VALUES (\"comput\"),\n(\"MD5\"),\n(\"Hash\");";
	
		assertEquals(sqlStatement, expected);
	}

	@Test
	public void testPrepareStoreAPI() {
		String[] apis = {"someclass.somemethod", "otherclass.othermethod"};
		List<String> testApis = new ArrayList<String>(Arrays.asList(apis));
		String sqlStatement = provider.prepareStoreAPI(testApis);
		String expected = "INSERT OR IGNORE INTO apis (API) VALUES (\"someclass.somemethod\"),\n(\"otherclass.othermethod\");";
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
		String[] apis = {"someclass.somemethod", "otherclass.othermethod", "otherclass.othermethod"};
		provider.storeAPIS(Arrays.asList(apis));
		provider.storeAPIS(Arrays.asList(apis));

		List<String> apiRows = provider.getAPIs();
		int actualLength = apiRows.size();
		int expectedLength = 2;
		assertEquals(expectedLength, actualLength);
	}

	@Test
	public void testStoreTokens() throws SQLException {
		provider.prepareSchemas();
		assertTrue(provider.tableExists("tokens"));
		String[] tokens = {"compu", "md5", "hash"};
		String[] duplicateTokens = {"hash"};
		provider.storeTokens(Arrays.asList(tokens));
		provider.storeTokens(Arrays.asList(duplicateTokens));
		
		List<String> tokenRows = provider.getTokens();
		int actualLength = tokenRows.size();
		int expectedLength = 3;
		assertEquals(expectedLength, actualLength);
	}

	@Test
	public void testStoreModelEntry() throws Exception {
		provider.prepareSchemas();

		String[] tokens = {"compu", "md5", "hash"};
		String[] apis = {"someclass.somemethod", "otherclass.othermethod", "otherclass.othermethod"};
		TypeName typeName = mock(TypeName.class);
		
		ModelEntry entry = new ModelEntry(Arrays.asList(tokens), Arrays.asList(apis), typeName);

		provider.saveMinedContext(entry);
	
		List<String> tokenRows = provider.getTokens();
		int nrOfTokens = tokenRows.size();
		int expectedNrOfTokens = 3;
		assertEquals(expectedNrOfTokens, nrOfTokens);

		List<String> apiRows = provider.getAPIs();
		int nrOfAPIs = apiRows.size();
		int expectedNrOfApis = 2;
		assertEquals(expectedNrOfApis, nrOfAPIs);
	}
}
