package ch.uzh.rackrec.rec;

import cc.kave.commons.model.events.completionevents.Context;
import ch.uzh.rackrec.data.ContextKaveDataSet;
import ch.uzh.rackrec.data.KaveDataSet;
import ch.uzh.rackrec.rec.config.KaveContextModule;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;

public class DefaultRecommenderTest {

    private Properties properties;
    private Logger mockedLogger;
    private KaveDataSet dataSet;

    @Before
    public void initialize(){
        properties = new Properties();
        URL resource = DefaultRecommender.class.getResource("/");
        properties.setProperty("base-path", resource.getPath());
        properties.setProperty("context-path", "context/");
        properties.setProperty("database-file", resource.getPath()+"DefaultModel-Mscorlib.data");
        properties.setProperty("delta", "5");
        properties.setProperty("apis", "mscorlib");
        properties.setProperty("lambda", "0");
        properties.setProperty("use-KKC","true");
        mockedLogger = mock(Logger.class);
        dataSet = new ContextKaveDataSet(properties,mockedLogger);
    }

    @Test
    public void defaultRecommenderCreation(){
        KaveContextModule module = new KaveContextModule(properties);
        DefaultRecommender sut = new DefaultRecommender(module);
        Iterator<Context> contextItrator = dataSet.getContextData().iterator();
        sut.query(contextItrator.next());

    }
}
