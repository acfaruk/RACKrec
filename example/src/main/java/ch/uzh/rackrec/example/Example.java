package ch.uzh.rackrec.example;

import ch.uzh.rackrec.rec.DefaultRecommender;
import ch.uzh.rackrec.rec.config.DefaultModule;

public class Example {

    public static void main(String[] args) {
        System.out.println("Test");

        DefaultRecommender rec = new DefaultRecommender(DefaultModule.create());

        //rec.query()
    }
}
