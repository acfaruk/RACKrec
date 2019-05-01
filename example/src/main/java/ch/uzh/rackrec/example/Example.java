package ch.uzh.rackrec.example;

import ch.uzh.rackrec.eval.MetricCollection;
import ch.uzh.rackrec.eval.QueryMetrics;
import ch.uzh.rackrec.rec.DefaultRecommender;
import ch.uzh.rackrec.rec.config.KaveContextModule;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.types.ITypeName;

public class Example {

    public static void main(String[] args) {

        //set up properties object, see the readme for available properties depending on the module type
        Properties props = new Properties();
        props.setProperty("base-path", "/home/luc/Documents/RACK");
        props.setProperty("model-path", "/model");
        props.setProperty("context-path", "/Contexts-170503");
        
        Integer k = 10;

        //choose your module, see the readme for available modules
        //KaveContextModule module = new KaveContextModule(props);

        //create your Recommender, see the readme for available recommenders
        //DefaultRecommender rec = new DefaultRecommender(module);
        
        //create dummy context for now
        //Context ctx = new Context();
        Set<Pair<IMemberName, Double>> eventResult = Collections.emptySet();
        Set<Pair<IMemberName, Double>> reccommenderResult = Collections.emptySet();
                        
        //For a real evaluation we need a large amount of rack results and the corresponding gold sets, the paper used 150
        //So for every query we calculate the individual metrics and the consolidate these into a metrics object. as of now
        //a metrics object only has one result.
        //collection of metrics has function merge or smth that calculates the mean of all queries
        
        //feed  the context into the reccommender
        //Set<Pair<IMemberName, Double>> reccommenderResult = rec.query(ctx);
        
		QueryMetrics metrics1 = new QueryMetrics(reccommenderResult, eventResult, k);
		QueryMetrics metrics2 = new QueryMetrics(reccommenderResult, eventResult, k);
		QueryMetrics metrics3 = new QueryMetrics(reccommenderResult, eventResult, k);
		QueryMetrics metrics4 = new QueryMetrics(reccommenderResult, eventResult, k);
		
		metrics1.print();
		metrics2.print();
		metrics3.print();
		metrics4.print();
		System.out.println("---------------------------------------------------------------------------------------------");

		MetricCollection metricCollection1 = new MetricCollection(k);
		
		metricCollection1.add(metrics1);
		metricCollection1.add(metrics2);
		metricCollection1.add(metrics3);
		metricCollection1.add(metrics4);
		
		metricCollection1.calculateMeanMetrics();
		metricCollection1.printMeanMetrics();
    }
}
