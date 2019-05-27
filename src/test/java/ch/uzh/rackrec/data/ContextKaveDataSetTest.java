package ch.uzh.rackrec.data;

import cc.kave.commons.model.events.completionevents.Context;
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
        properties.setProperty("generate-model", "true");
        mockedLogger = mock(Logger.class);
    }

    @Test
    public void createKontextCaveDataSet(){
        URL resource = ContextKaveDataSetTest.class.getResource("/");
        properties.setProperty("context-path", resource.getPath());

        ContextKaveDataSet sut = new ContextKaveDataSet(properties, mockedLogger);

        int counter = 0;
        for(Context c : sut.getContextData()){
            counter++;
        }
        assertEquals(29, counter);
    }

    @Test
    public void testWrongContextFolder(){
        properties.setProperty("context-path", "/something-weird");

        boolean runtimeExceptionThrown = false;

        try{
            ContextKaveDataSet sut = new ContextKaveDataSet(properties, mockedLogger);
        }catch (RuntimeException e){
            runtimeExceptionThrown = true;

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
