package ch.uzh.rackrec.rec;

import ch.uzh.rackrec.rec.config.DefaultModule;
import org.junit.Before;
import org.junit.Test;

public class DefaultRecommenderTest {

    @Before
    public void initialize(){

    }

    @Test
    public void defaultRecommenderCreation(){
        DefaultModule module = DefaultModule.create();
        DefaultRecommender defaultRecommender = new DefaultRecommender(module);
    }
}
