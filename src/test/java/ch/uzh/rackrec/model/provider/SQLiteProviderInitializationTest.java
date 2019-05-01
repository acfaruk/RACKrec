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
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class SQLiteProviderInitializationTest {
    String testingDB = "./testdb.db";
    SQLiteProvider provider;
    String inexistentDatabase = "thisDBDoesNotExist";
    String contextDBName = "contexts";
    String apiDBName = "apis";
    Logger logger = mock(Logger.class);

    @Before
    public void initialize() {
        Properties settings = new Properties();
        settings.put("database-file", testingDB);
        provider = new SQLiteProvider(settings, logger);
    }
    
    @After
    public void deleteDB() {
        provider.closeConnection();
        File file = new File(testingDB); 
        file.delete();
    }

    @Test
    public void testCheckTableExists() throws SQLException {
        boolean tableDoesNotExist = !provider.tableExists(inexistentDatabase);
        
        assertTrue(tableDoesNotExist);
    }
    
    @Test
    public void testCreateSchemas() {
        boolean tableDoesExist = false;
        boolean apiTableDoesExist = false;

        try {
            provider.prepareSchemas();
            tableDoesExist = provider.tableExists(contextDBName);
            apiTableDoesExist = provider.tableExists(apiDBName);
        } catch (SQLException e) {
        	fail(e.getMessage());
            e.printStackTrace();
        }

        assertTrue(apiTableDoesExist);
        assertTrue(tableDoesExist);
    }
    
    @Test
    public void testMultipleCreation() throws SQLException {
        provider.prepareSchemas();
        boolean tableDoesExist = provider.tableExists(contextDBName);
        assertTrue(tableDoesExist);
        try { 
            provider.prepareSchemas();
            assertTrue(tableDoesExist);
        } catch(Exception e) {
            fail("Creation methods should check if the table to be created exists");
            e.printStackTrace();
        }
    }
}