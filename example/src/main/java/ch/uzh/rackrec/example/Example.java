package ch.uzh.rackrec.example;

import ch.uzh.rackrec.rec.DefaultRecommender;
import ch.uzh.rackrec.rec.config.KaveContextModule;

import java.util.Properties;

public class Example {

    public static void main(String[] args) {

        //set up properties object, see the readme for available properties depending on the module type
        Properties props = new Properties();
        props.setProperty("test", "test");

        //choose your module, see the readme for available modules
        KaveContextModule module = new KaveContextModule(props);

        //create your Recommender, see the readme for available recommenders
        DefaultRecommender rec = new DefaultRecommender(module);
    }
}
