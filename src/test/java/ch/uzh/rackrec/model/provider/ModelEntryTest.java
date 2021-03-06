package ch.uzh.rackrec.model.provider;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import cc.kave.commons.model.naming.types.ITypeName;

public class ModelEntryTest {
    ModelEntry entry;
    List<String> validTokens;
    List<String> invalidTokens;
    List<String> validAPIs;
    List<String> invalidAPIs;
    
    ITypeName validTypeName;
    ITypeName invalidTypeName;

    @Before
    public void initialize(){
        String[] someTokens = { "compu", "MD5", "has" };
        String[] someAPIs = { "Object.hashCode", "MessageDigest.digest" };
        String[] emptyList = {};
        validTokens = Arrays.asList(someTokens);
        validAPIs = Arrays.asList(someAPIs);
        invalidAPIs = Arrays.asList(emptyList);
        invalidTokens = Arrays.asList(emptyList);

        validTypeName = mock(TypeName.class);
    }

    @After
    public void reset() {
        entry = null;
    }

    @Test(expected=InvalidModelEntryException.class)
    public void invalidAPITest() throws Exception{
        entry = new ModelEntry(validTokens, invalidAPIs, validTypeName);
    }

    @Test(expected=InvalidModelEntryException.class)
    public void invalidTokenTest() throws Exception{
        entry = new ModelEntry(invalidTokens, validAPIs, validTypeName);
    }

    @Test(expected=InvalidModelEntryException.class)
    public void invalidTypeTest() throws Exception{
        entry = new ModelEntry(validTokens, validAPIs, invalidTypeName);
    }

    @Test
    public void validTest(){
        try {
            entry = new ModelEntry(validTokens, validAPIs, validTypeName);
        } catch (Exception e) {
            fail("ModelEntry instantiation should not fail with valid parameters");
        }
    }
}
