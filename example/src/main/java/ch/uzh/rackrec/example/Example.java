package ch.uzh.rackrec.example;

import ch.uzh.rackrec.eval.MetricCollection;
import ch.uzh.rackrec.eval.QueryMetrics;
import ch.uzh.rackrec.rec.DefaultRecommender;
import ch.uzh.rackrec.rec.config.KaveContextModule;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jfree.ui.RefineryUtilities;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import cc.kave.commons.model.naming.types.ITypeName;

public class Example {

    public static void main(String[] args) {
        	
        //set up properties object, see the readme for available properties depending on the module type
        Properties props = new Properties();
        props.setProperty("database-file", Example.class.getResource("/").getPath() + "DefaultModel-Mscorlib-simple");
        props.setProperty("context-path", Example.class.getResource("/").getPath() + "context/");
        props.setProperty("delta", "5");
        props.setProperty("lambda", "0");
        props.setProperty("generate-model", "false");
        props.setProperty("use-KKC", "false");
        
        // Set the location for the event data set
        String eventDataLocation = Example.class.getResource("/").getPath() + "Events";
        
        // Set the type of the database, currently it's either extended or basic
        //String databaseType = "extended";
        String databaseType = "simple";

        // Retrieve relevant events from the data set
    	List<CompletionEventData> eventData = CompletionEventsNew.readAllEvents(eventDataLocation, databaseType);
 
        //choose your module, see the readme for available modules
        KaveContextModule module = new KaveContextModule(props);

        //create your Recommender, see the readme for available recommenders
        DefaultRecommender rec = new DefaultRecommender(module);
        
        // set the max K for the evaluation
        Integer k = 10;
        
        // create an empty collection of metrics
		MetricCollection metricCollection1 = new MetricCollection(k);

        for (int i = 0; i < eventData.size(); i++) {
    		// feed an event context into the reccommender and get the results
            Set<Pair<IMemberName, Double>> reccommenderResult = rec.query(eventData.get(i).getEventContext());
            
            // compare the results with the event proposals
    		QueryMetrics metrics = new QueryMetrics(reccommenderResult, eventData.get(i).getEventResult(), k, databaseType);
    		metrics.calculateMetricTable();
    		metrics.print();
    		
    		// add the results to the collection
    		metricCollection1.add(metrics);

		}        
        
        //Calculate and print the mean metrics for all queries in a collection
		metricCollection1.calculateMeanMetrics();
		System.out.println("Number of queries in this metric evaluation: " + metricCollection1.size());
		metricCollection1.printMeanMetrics();
		
		//Put the data in a chart and display it
	    LineChart_AWT chart = new LineChart_AWT("RACK Evaluation Results" , "Performance Metrics of " + metricCollection1.size() + " queries", metricCollection1.getMeanMetrics());
	    chart.pack( );
	    RefineryUtilities.centerFrameOnScreen( chart );
	    chart.setVisible( true );
    }
}
