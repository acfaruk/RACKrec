package ch.uzh.rackrec.model.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

import ch.uzh.rackrec.model.view.KAC;
import ch.uzh.rackrec.model.view.KKC;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cc.kave.commons.model.naming.impl.v0.types.TypeName;

public class SQLiteProviderAccessorsTest {
    String testingDB = "./testdb.db";
    SQLiteProvider provider;
    String[] tokens = {"compu", "md5", "hash"};
    String[] apis = { "a.type2, project1.M1","a.type1, project1.M1", "a.type2, project1.M1"};
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
        String[] apisCTX2 = { "a.type2, project1.M1", "a.type1, project1.M1"};
        TypeName typeNameCTX2 = mock(TypeName.class);
        when(typeNameCTX2.toString()).thenReturn("client2");
        
        ModelEntry entryCTX2 = new ModelEntry(Arrays.asList(tokensCTX2), Arrays.asList(apisCTX2), typeNameCTX2);
		provider.saveMinedContext(entryCTX2);

        String[] tokensCTX3 = tokens;
        String[] apisCTX3 = {"a.type1, project1.M1", "a.type1, project1.M1", "a.type1, project1.M1", "a.type1, project1.M1", "thirdAPI.thridmeth", "fourthAPI.fourthmeth", "fifthAPI.fifthMeth" };
        TypeName typeNameCTX3 = mock(TypeName.class);
        when(typeNameCTX3.toString()).thenReturn("client3");
        
        ModelEntry entryCTX3 = new ModelEntry(Arrays.asList(tokensCTX3), Arrays.asList(apisCTX3), typeNameCTX3);
        provider.saveMinedContext(entryCTX3);

        String[] tokensCTX4 = {"bla"};
        String[] apisCTX4 = {"a.type3, project1.M1" };
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
        assertEquals("MethodName(a.type1, project1.M1)", topKKAC.getFreqToApi().get(6).toString());
        assertEquals("MethodName(a.type2, project1.M1)", topKKAC.getFreqToApi().get(3).toString());
    }
    @Test
    public void testGetTokensForAPI() throws Exception {
        List<String> tokenRows = provider.getTokens();
        int nrOfTokens = tokenRows.size();
        int expectedNrOfTokens = 3;
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

    @Test
    public void testGetKKCScoreOfKeywords() throws Exception {
        String[] tokensCTX2 = tokens;
        String[] apisCTX2 = { "a.type2, project1.M1", "a.type1, project1.M1"};
        TypeName typeNameCTX2 = mock(TypeName.class);
        when(typeNameCTX2.toString()).thenReturn("client2");
        
        ModelEntry entryCTX2 = new ModelEntry(Arrays.asList(tokensCTX2), Arrays.asList(apisCTX2), typeNameCTX2);
		provider.saveMinedContext(entryCTX2);

        String[] tokensCTX3 = tokens;
        String[] apisCTX3 = {"a.type1, project1.M1", "a.type1, project1.M1", "a.type1, project1.M1", "a.type1, project1.M1", "thirdAPI.thridmeth", "fourthAPI.fourthmeth", "fifthAPI.fifthMeth" };
        TypeName typeNameCTX3 = mock(TypeName.class);
        when(typeNameCTX3.toString()).thenReturn("client3");
        
        ModelEntry entryCTX3 = new ModelEntry(Arrays.asList(tokensCTX3), Arrays.asList(apisCTX3), typeNameCTX3);
        provider.saveMinedContext(entryCTX3);

        String[] tokensCTX4 = {"bla", "compu", "test"};
        String[] apisCTX4 = {"a.type3, project1.M1" };
        TypeName typeNameCTX4 = mock(TypeName.class);
        when(typeNameCTX4.toString()).thenReturn("client4");
        
        ModelEntry entryCTX4 = new ModelEntry(Arrays.asList(tokensCTX4), Arrays.asList(apisCTX4), typeNameCTX4);
        provider.saveMinedContext(entryCTX4);
    
        List<String> tokenRows = provider.getTokens();
        int nrOfTokens = tokenRows.size();
        int expectedNrOfTokens = 5;
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
        

        // "compu" and "hash" are almost always mentioned in contexts with the same
        // keywords. Thus, a cosine similarity close to 1 is expected!
        double kkcScoreRelatedKeywords = provider.getKKCScore(new AbstractMap.SimpleEntry<String, String>("compu", "hash"));
        // "compu" is only mentioned once in a context where also "bla" is mentioned
        double kkcScoreWeaklyRelatedKeywords = provider.getKKCScore(new AbstractMap.SimpleEntry<String, String>("compu", "bla"));
        // "md5" is never mentioned in the same context as "bla", we are expecting a score
        // close to 0.
        double kkcScoreUnrelatedKeywords = provider.getKKCScore(new AbstractMap.SimpleEntry<String, String>("md5", "bla"));
        
        boolean kkcScoreRelatedGreaterNull = kkcScoreRelatedKeywords > 0;
        boolean relatedOverWeaklyRelated = kkcScoreRelatedKeywords > kkcScoreWeaklyRelatedKeywords;
        boolean weaklyRelatedOverUnrelated = kkcScoreWeaklyRelatedKeywords > kkcScoreUnrelatedKeywords;

        System.out.println(kkcScoreRelatedKeywords);
        System.out.println(kkcScoreWeaklyRelatedKeywords);
        System.out.println(kkcScoreUnrelatedKeywords);
        assertTrue(kkcScoreRelatedGreaterNull);
        assertTrue(relatedOverWeaklyRelated);
        assertTrue(weaklyRelatedOverUnrelated);
    }
    @Test
    public void testGetKKC() throws Exception {
        String[] tokensCTX2 = tokens;
        String[] apisCTX2 = { "a.type2, project1.M1", "a.type1, project1.M1"};
        TypeName typeNameCTX2 = mock(TypeName.class);
        when(typeNameCTX2.toString()).thenReturn("client2");

        ModelEntry entryCTX2 = new ModelEntry(Arrays.asList(tokensCTX2), Arrays.asList(apisCTX2), typeNameCTX2);
        provider.saveMinedContext(entryCTX2);

        String[] tokensCTX3 = { "code" };
        String[] apisCTX3 = {"a.type1, project1.M1", "fourthAPI.fourthmeth", "fifthAPI.fifthMeth" };
        TypeName typeNameCTX3 = mock(TypeName.class);
        when(typeNameCTX3.toString()).thenReturn("client3");

        ModelEntry entryCTX3 = new ModelEntry(Arrays.asList(tokensCTX3), Arrays.asList(apisCTX3), typeNameCTX3);
        provider.saveMinedContext(entryCTX3);

        String[] tokensCTX4 = {"bla", "compu", "test"};
        String[] apisCTX4 = {"a.type3, project1.M1" };
        TypeName typeNameCTX4 = mock(TypeName.class);
        when(typeNameCTX4.toString()).thenReturn("client4");

        ModelEntry entryCTX4 = new ModelEntry(Arrays.asList(tokensCTX4), Arrays.asList(apisCTX4), typeNameCTX4);
        provider.saveMinedContext(entryCTX4);

        List<String> tokenRows = provider.getTokens();
        int nrOfTokens = tokenRows.size();
        int expectedNrOfTokens = 6;
        assertEquals(expectedNrOfTokens, nrOfTokens);

        List<String> apiRows = provider.getAPIs();
        int nrOfAPIs = apiRows.size();
        int expectedNrOfApis = 5;
        assertEquals(expectedNrOfApis, nrOfAPIs);

        List<String> contextRows = provider.getContexts();
        int nrOfContexts = contextRows.size();
        int expectedNrOfContexts = 4;
        assertEquals(expectedNrOfContexts, nrOfContexts);

        List<String> contextForApiRows = provider.getApisForContext(typeName.toString());
        int nrOfApisForCtx = contextForApiRows.size();
        int expectedNrOfApisForCtx = 2;
        assertEquals(expectedNrOfApisForCtx, nrOfApisForCtx);


        // "compu" and "hash" are almost always mentioned in contexts with the same
        // keywords. Thus, a cosine similarity close to 1 is expected!
        KKC kkcRelatedKeywords = provider.getKKCForKeywords(new AbstractMap.SimpleEntry<String, String>("compu", "hash"));
        assertEquals(3, kkcRelatedKeywords.getApis().size());
        // "compu" is only mentioned once in a context where also "bla" is mentioned
        KKC kkcWeaklyRelatedKeywords = provider.getKKCForKeywords(new AbstractMap.SimpleEntry<String, String>("code", "bla"));
        assertEquals(4, kkcWeaklyRelatedKeywords.getApis().size() );
        // "md5" is never mentioned in the same context as "bla", we are expecting a score
        // close to 0.
        KKC kkcUnrelatedKeywords = provider.getKKCForKeywords(new AbstractMap.SimpleEntry<String, String>("md5", "bla"));
        assertEquals(3, kkcUnrelatedKeywords.getApis().size());
    }
}
