package ch.uzh.rackrec.model;

import ch.uzh.rackrec.data.ContextKaveDataSet;
import ch.uzh.rackrec.data.KaveDataSet;
import ch.uzh.rackrec.model.gen.DefaultModelGenerator;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;

public class DefaultModelTest {
    DefaultModel sut;
    Logger mockedLogger ;

    @Before
    public void initialize(){
        mockedLogger = mock(Logger .class);
        KaveDataSet dataset = new ContextKaveDataSet(new Properties(),mockedLogger);
        DefaultModelGenerator ModelGenerator = new DefaultModelGenerator(new Properties(), dataset,mockedLogger );
        sut = new DefaultModel(new Properties(), ModelGenerator, mockedLogger );
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
