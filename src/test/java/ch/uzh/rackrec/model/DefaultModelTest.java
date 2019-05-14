package ch.uzh.rackrec.model;

import ch.uzh.rackrec.data.ContextKaveDataSet;
import ch.uzh.rackrec.data.KaveDataSet;
import ch.uzh.rackrec.model.gen.DefaultModelGenerator;

import ch.uzh.rackrec.model.gen.ModelGenerator;
import ch.uzh.rackrec.model.provider.SQLiteProvider;
import ch.uzh.rackrec.model.provider.SQLiteProviderInitializationTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultModelTest {
    DefaultModel sut;
    Properties properties;
    ModelGenerator generatorMock;
    Logger loggerMock;
    SQLiteProvider sqliteMock;

    @Before
    public void initialize(){
        properties = new Properties();
        generatorMock = mock(ModelGenerator.class);
        loggerMock = mock(Logger.class);
        sqliteMock = mock(SQLiteProvider.class);
        sut = new DefaultModel(properties, generatorMock, loggerMock, sqliteMock);
    }

    @Test
    public void testgetKAC(){
        List<String> keywords = new ArrayList<>();
        keywords.add("Key1");
        keywords.add("Test");
        boolean typecorecct = sut.getKAC(keywords,5) instanceof List;
        assertTrue(typecorecct);
    }
    @Test
    public void testgetKKC(){
        boolean typecorecct = sut.getKKC(new ArrayList<>(),0.0) instanceof List;
        assertTrue(typecorecct);
    }
    @Test
    public void testgetContext(){
        boolean typecorecct = sut.getContext(new String()) instanceof List;
        assertTrue(typecorecct);
    }

}
