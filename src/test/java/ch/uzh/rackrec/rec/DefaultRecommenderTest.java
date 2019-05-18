package ch.uzh.rackrec.rec;

import ch.uzh.rackrec.rec.config.KaveContextModule;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Properties;

public class DefaultRecommenderTest {

    private Properties properties;

    @Before
    public void initialize(){
        properties = new Properties();
        URL resource = DefaultRecommender.class.getResource("/");
        properties.setProperty("base-path", resource.getPath());
        properties.setProperty("context-path", "context/");
        properties.setProperty("database-file", resource.getPath()+"DefaultModel-Mscorlib");
        properties.setProperty("delta", "5");
        properties.setProperty("apis", "mscorlib");
        properties.setProperty("lambda", "0");
    }

    @Test
    public void defaultRecommenderCreation(){
        KaveContextModule module = new KaveContextModule(properties);
        DefaultRecommender sut = new DefaultRecommender(module);
    }
}
