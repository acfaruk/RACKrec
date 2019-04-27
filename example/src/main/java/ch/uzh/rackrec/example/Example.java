package ch.uzh.rackrec.example;

import ch.uzh.rackrec.eval.Metrics;
import ch.uzh.rackrec.rec.DefaultRecommender;
import ch.uzh.rackrec.rec.config.KaveContextModule;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMemberName;

public class Example {

    public static void main(String[] args) {

        //set up properties object, see the readme for available properties depending on the module type
        Properties props = new Properties();
        props.setProperty("base-path", "/home/luc/Documents/RACK");
        props.setProperty("model-path", "/model");
        props.setProperty("context-path", "/Contexts-170503");

        //choose your module, see the readme for available modules
        //KaveContextModule module = new KaveContextModule(props);

        //create your Recommender, see the readme for available recommenders
        //DefaultRecommender rec = new DefaultRecommender(module);
        
        //create dummy context for now
        Context ctx = new Context();
        Set<Pair<IMemberName, Double>> eventResult = Collections.emptySet();
        Set<Pair<IMemberName, Double>> reccommenderResult = Collections.emptySet();
        
        //feed  the context into the reccommender
        //Set<Pair<IMemberName, Double>> reccommenderResult = rec.query(ctx);
        
		Metrics metrics = new Metrics(reccommenderResult, eventResult, 10);
		
		System.out.println(metrics.calculateMetricTable());
    }
}
