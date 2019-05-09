package ch.uzh.rackrec.data;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ContextKaveDataSetTest {

    private Properties properties;
    private Logger mockedLogger;

    @Before
    public void initialize(){
        properties = new Properties();
        URL resource = ContextKaveDataSetTest.class.getResource("/");
        properties.setProperty("base-path", resource.getPath());
        mockedLogger = mock(Logger.class);
    }

    @Test
    public void createKontextCaveDataSet(){
        properties.setProperty("context-path", "context/");

        ContextKaveDataSet sut = new ContextKaveDataSet(properties, mockedLogger);

        assertEquals(29,sut.getContextData().size());
    }

    @Test
    public void testWrongContextFolder(){
        properties.setProperty("context-path", "wrong/");

        boolean runtimeExceptionThrown = false;

        try{
            ContextKaveDataSet sut = new ContextKaveDataSet(properties, mockedLogger);
        }catch (RuntimeException e){
            runtimeExceptionThrown = true;

            Path path = Paths.get(URI.create("file://" + properties.getProperty("base-path") + properties.getProperty("context-path")));
            assertEquals("The context data folder was not found in: " + path + " please update the folder name.", e.getMessage());
        }

        assertTrue(runtimeExceptionThrown);
    }

    @Test
    public void testNoPropertySet(){
        //properties.setProperty("context-path", "wrong/");

        boolean runtimeExceptionThrown = false;

        try{
            ContextKaveDataSet sut = new ContextKaveDataSet(properties, mockedLogger);
        }catch (RuntimeException e){
            runtimeExceptionThrown = true;

            assertEquals("The context path was not found in the properties object. The data can't be loaded!", e.getMessage());
        }

        assertTrue(runtimeExceptionThrown);
    }
}
