package ch.uzh.rackrec.model.provider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SQLQueryFactoryTest {
    SQLQueryFactory factory = new SQLQueryFactory();

    @Test
    public void testPrepareStoreTokens() {
        String sqlStatement = factory.storeTokens();
        String expected = "INSERT OR IGNORE INTO tokens (Token) VALUES (?)";
    
        assertEquals(expected, sqlStatement);
    }

    @Test
    public void testPrepareStoreAPI() {
        String[] apis = {"someclass.somemethod", "otherclass.othermethod"};

        List<String> testApis = new ArrayList<String>(Arrays.asList(apis));

        String sqlStatement = factory.storeAPIs(testApis);
        String expected = "INSERT OR IGNORE INTO apis (API) VALUES (\"someclass.somemethod\"),\n(\"otherclass.othermethod\");";

        assertEquals(sqlStatement, expected);
    }
}