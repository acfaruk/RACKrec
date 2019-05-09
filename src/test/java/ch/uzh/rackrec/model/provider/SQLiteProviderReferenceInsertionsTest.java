package ch.uzh.rackrec.model.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cc.kave.commons.model.naming.impl.v0.types.TypeName;

public class SQLiteProviderReferenceInsertionsTest {
    String testingDB = "./testdb.db";
    SQLiteProvider provider;
    TypeName testContext = mock(TypeName.class);
    String[] apis = {"someLib.someMethod", "otherLib.otherMethod"};
    String[] tokens = {"compu", "MD5", "compu"};
    Logger logger = mock(Logger.class);

    @Before
    public void initialize() throws SQLException {
        Properties settings = new Properties();
        settings.put("database-file", testingDB);
        provider = new SQLiteProvider(settings, logger);
        provider.prepareSchemas();
        assertTrue(provider.tableExists("tokens"));
        assertTrue(provider.tableExists("apis"));
        when(testContext.toString()).thenReturn("someClientClass.someMethod");
    }
    
    @After
    public void deleteDB() {
        provider.closeConnection();
        File file = new File(testingDB); 
        file.delete();
    }

    @Test
    public void testStoreNewApiReference() throws SQLException {
        provider.storeAPIS(Arrays.asList(apis));
        provider.storeContext(testContext);
        provider.saveApiContextReference("someLib.someMethod", testContext);
        List<String> storedReferences = provider.getApisForContext(testContext.toString());
        int actualNumOfReferences = storedReferences.size();
        int expectedNumOfReferences = 1;
        assertEquals(expectedNumOfReferences, actualNumOfReferences);

        assertTrue(provider.apiContextReferenceExists("someLib.someMethod", testContext));
    }

    @Test
    public void testStoreNewTokenReference() throws SQLException {
        provider.storeTokens(Arrays.asList(tokens));
        provider.storeContext(testContext);
        provider.saveTokenContextReference(Arrays.asList(tokens), testContext);

        List<String> storedReferences = provider.getTokensForContext(testContext.toString());
        int actualNumOfReferences = storedReferences.size();
        int expectedNumOfReferences = 2;
        assertEquals(expectedNumOfReferences, actualNumOfReferences);
    }

    @Test
    public void testStoreExistingApiReference() throws SQLException {
        provider.storeAPIS(Arrays.asList(apis));
        provider.storeContext(testContext);
        provider.saveApiContextReference("someLib.someMethod", testContext);
        List<String> storedReferences = provider.getApisForContext(testContext.toString());

        int actualNumOfReferences = storedReferences.size();
        int expectedNumOfReferences = 1;

        assertEquals(expectedNumOfReferences, actualNumOfReferences);
        assertTrue(provider.apiContextReferenceExists("someLib.someMethod", testContext));
        assertEquals(1, provider.getApiCountForContext(testContext.toString(),"someLib.someMethod" ));

        provider.saveApiContextReference("someLib.someMethod", testContext);
        storedReferences = provider.getApisForContext(testContext.toString());
        actualNumOfReferences = storedReferences.size();
        assertEquals(expectedNumOfReferences, actualNumOfReferences);
        assertEquals(2, provider.getApiCountForContext(testContext.toString(),"someLib.someMethod" ));
        
        String[] multipleApis = {"someLib.someMethod", "someLib.someMethod"};
        provider.saveApiContextReference(Arrays.asList(multipleApis), testContext);
        assertEquals(4, provider.getApiCountForContext(testContext.toString(),"someLib.someMethod" ));
    }

    @Test
    public void testStoreMultipleExistingApiReferences() throws SQLException {
        provider.storeAPIS(Arrays.asList(apis));
        provider.storeContext(testContext);
        provider.saveApiContextReference("someLib.someMethod", testContext);

        String[] multipleApis = {"someLib.someMethod", "someLib.someMethod"};
        provider.saveApiContextReference(Arrays.asList(multipleApis), testContext);
        assertEquals(3, provider.getApiCountForContext(testContext.toString(),"someLib.someMethod" ));
    }

    @Test
    public void testStoreModelEntry() throws Exception {
        ModelEntry entry = new ModelEntry(Arrays.asList(tokens), Arrays.asList(apis), testContext);

        provider.saveMinedContext(entry);
    
        List<String> tokenRows = provider.getTokens();
        int nrOfTokens = tokenRows.size();
        int expectedNrOfTokens = tokens.length - 1;
        assertEquals(expectedNrOfTokens, nrOfTokens);

        List<String> apiRows = provider.getAPIs();
        int nrOfAPIs = apiRows.size();
        int expectedNrOfApis = 2;
        assertEquals(expectedNrOfApis, nrOfAPIs);

        List<String> contextRows = provider.getContexts();
        int nrOfContexts = contextRows.size();
        int expectedNrOfContexts = 1;
        assertEquals(expectedNrOfContexts, nrOfContexts);

        List<String> contextForApiRows = provider.getApisForContext(testContext.toString());
        int nrOfApisForCtx = contextForApiRows.size();
        int expectedNrOfApisForCtx = 2;
        assertEquals(expectedNrOfApisForCtx, nrOfApisForCtx);
    }
}
