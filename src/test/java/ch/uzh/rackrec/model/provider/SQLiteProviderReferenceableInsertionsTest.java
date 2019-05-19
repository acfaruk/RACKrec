package ch.uzh.rackrec.model.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

import static org.mockito.Mockito.mock;

public class SQLiteProviderReferenceableInsertionsTest {
    String testingDB = "./testdb.db";
    SQLiteProvider provider;
    String[] apis = {"someclass.somemethod", "otherclass.othermethod", "otherclass.othermethod"};
	String[] tokens = {"compu", "md5", "hash"};
	String[] tokensWithDuplicates = {"hash", "set"};
	Logger logger = mock(Logger.class);

    @Before
    public void initialize() throws SQLException {
        Properties settings = new Properties();
        settings.put("database-file", testingDB);
        provider = new SQLiteProvider(settings, logger);
        provider.prepareSchemas();
        assertTrue(provider.tableExists("tokens"));
    }
    
    @After
    public void deleteDB() {
        provider.closeConnection();
        File file = new File(testingDB); 
        file.delete();
    }

    @Test
    public void testStoreApis() throws SQLException {
        provider.storeAPIS(Arrays.asList(apis));

        List<String> apiRows = provider.getAPIs();
        int actualLength = apiRows.size();
        int expectedLength = 2;
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testTryStoreApisMultipleTimes() throws SQLException {
        provider.storeAPIS(Arrays.asList(apis));
        provider.storeAPIS(Arrays.asList(apis));

        List<String> apiRows = provider.getAPIs();
        int actualLength = apiRows.size();
        int expectedLength = 2;
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testStoreTokens() throws SQLException {
        provider.storeTokens(Arrays.asList(tokens));
        
        List<String> tokenRows = provider.getTokens();
        int actualLength = tokenRows.size();
        int expectedLength = 3;
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testStoreTokensWithDuplicates() throws SQLException {
        provider.storeTokens(Arrays.asList(tokens));
        provider.storeTokens(Arrays.asList(tokensWithDuplicates));
        
        List<String> tokenRows = provider.getTokens();
        int actualLength = tokenRows.size();
        int expectedLength = 4;
        assertEquals(expectedLength, actualLength);
    }
}
