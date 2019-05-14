package ch.uzh.rackrec.model.gen;

import ch.uzh.rackrec.data.ContextKaveDataSet;
import ch.uzh.rackrec.data.ContextKaveDataSetTest;
import ch.uzh.rackrec.model.gen.nlp.IdentifierLemmatizer;
import ch.uzh.rackrec.model.gen.nlp.ModifiedStopwordProvider;
import ch.uzh.rackrec.model.provider.ModelEntry;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static junit.framework.TestCase.assertTrue;

public class DefaultModelGeneratorTest {

    private Properties properties;
    private Logger mockedLogger;
    private ContextKaveDataSet kaveDataSet;
    private IdentifierLemmatizer lemmatizer;

    @Before
    public void initialize(){
        properties = new Properties();
        URL resource = ContextKaveDataSetTest.class.getResource("/");
        properties.setProperty("base-path", resource.getPath());
        properties.setProperty("context-path", "context/");
        mockedLogger = mock(Logger.class);

        kaveDataSet = new ContextKaveDataSet(properties, mockedLogger);
        lemmatizer = new IdentifierLemmatizer(new ModifiedStopwordProvider(), mockedLogger);
    }

    @Test
    public void modelEntriesGenerated(){
        DefaultModelGenerator sut = new DefaultModelGenerator(lemmatizer, properties, kaveDataSet, mockedLogger);

        int count = 0;
        for (ModelEntry entry : sut.getModelEntries()) {
            System.out.println(entry);
            count++;
        }

        assertTrue(count != 0);
    }
}
