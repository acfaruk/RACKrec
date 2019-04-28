package ch.uzh.rackrec.model.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;
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
import ch.uzh.rackrec.model.view.KAC;

public class SQLiteProviderAccessorsTest {
    String testingDB = "./testdb.db";
    SQLiteProvider provider;
    String[] tokens = {"compu", "md5", "hash"};
    String[] apis = { "otherclass.othermethod","someclass.somemethod", "otherclass.othermethod"};
    TypeName typeName = mock(TypeName.class);
    Logger logger = mock(Logger.class);
    ModelEntry entry;

    @Before
    public void initialize() throws Exception {
        Properties settings = new Properties();
        settings.put("database-file", testingDB);
        provider = new SQLiteProvider(settings, logger);
        provider.prepareSchemas();
        entry = new ModelEntry(Arrays.asList(tokens), Arrays.asList(apis), typeName);
        provider.saveMinedContext(entry);
    }
    
    @After
    public void deleteDB() {
        provider.closeConnection();
        File file = new File(testingDB); 
        file.delete();
    }

    @Test
    public void testgetTopKEntries() throws Exception {
        String[] tokensCTX2 = tokens;
        String[] apisCTX2 = { "otherclass.othermethod", "someclass.somemethod"};
        TypeName typeNameCTX2 = mock(TypeName.class);
        when(typeNameCTX2.toString()).thenReturn("client2");
        
        ModelEntry entryCTX2 = new ModelEntry(Arrays.asList(tokensCTX2), Arrays.asList(apisCTX2), typeNameCTX2);
		provider.saveMinedContext(entryCTX2);

        String[] tokensCTX3 = tokens;
        String[] apisCTX3 = {"someclass.somemethod", "someclass.somemethod", "someclass.somemethod", "someclass.somemethod", "thirdAPI.thridmeth", "fourthAPI.fourthmeth", "fifthAPI.fifthMeth" };
        TypeName typeNameCTX3 = mock(TypeName.class);
        when(typeNameCTX3.toString()).thenReturn("client3");
        
        ModelEntry entryCTX3 = new ModelEntry(Arrays.asList(tokensCTX3), Arrays.asList(apisCTX3), typeNameCTX3);
        provider.saveMinedContext(entryCTX3);

        String[] tokensCTX4 = {"bla"};
        String[] apisCTX4 = {"unrelatedclass.somemethod" };
        TypeName typeNameCTX4 = mock(TypeName.class);
        when(typeNameCTX4.toString()).thenReturn("client4");
        
        ModelEntry entryCTX4 = new ModelEntry(Arrays.asList(tokensCTX4), Arrays.asList(apisCTX4), typeNameCTX4);
        provider.saveMinedContext(entryCTX4);
    
        List<String> tokenRows = provider.getTokens();
        int nrOfTokens = tokenRows.size();
        int expectedNrOfTokens = 4;
        assertEquals(expectedNrOfTokens, nrOfTokens);

        List<String> apiRows = provider.getAPIs();
        int nrOfAPIs = apiRows.size();
        int expectedNrOfApis = 6;
        assertEquals(expectedNrOfApis, nrOfAPIs);

        List<String> contextRows = provider.getContexts();
        int nrOfContexts = contextRows.size();
        int expectedNrOfContexts = 4;
        assertEquals(expectedNrOfContexts, nrOfContexts);

        List<String> contextForApiRows = provider.getApisForContext(typeName.toString());
        int nrOfApisForCtx = contextForApiRows.size();
        int expectedNrOfApisForCtx = 2;
        assertEquals(expectedNrOfApisForCtx, nrOfApisForCtx);
        
        int k = 2;
        KAC topKKAC = provider.getTopKAPIForToken(k, "compu");
        assertEquals(k, topKKAC.getFreqToApi().size());
        assertEquals("someclass.somemethod", topKKAC.getFreqToApi().get(6));
        assertEquals("otherclass.othermethod", topKKAC.getFreqToApi().get(3));
    }
    @Test
    public void testGetTokensForAPI() throws Exception {
        List<String> tokenRows = provider.getTokens();
        int nrOfTokens = tokenRows.size();
        int expectedNrOfTokens = tokens.length;
        assertEquals(expectedNrOfTokens, nrOfTokens);

        List<String> apiRows = provider.getAPIs();
        int nrOfAPIs = apiRows.size();
        int expectedNrOfApis = 2;
        assertEquals(expectedNrOfApis, nrOfAPIs);

        List<String> contextRows = provider.getContexts();
        int nrOfContexts = contextRows.size();
        int expectedNrOfContexts = 1;
        assertEquals(expectedNrOfContexts, nrOfContexts);

        List<String> contextForApiRows = provider.getApisForContext(typeName.toString());
        int nrOfApisForCtx = contextForApiRows.size();
        int expectedNrOfApisForCtx = 2;
        assertEquals(expectedNrOfApisForCtx, nrOfApisForCtx);
        
        
        List<String> tokensForAPI = provider.getTokensForAPI(apis[0]);
        assertEquals(tokensForAPI.size(), tokens.length);
    }
}
