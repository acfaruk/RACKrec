package ch.uzh.rackrec.model.provider;

import org.junit.Test;
import static org.junit.Assert.*;

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
        String sqlStatement = factory.storeAPIs();
        String expected = "INSERT OR IGNORE INTO apis (API) VALUES (?)";

        assertEquals(sqlStatement, expected);
    }
}