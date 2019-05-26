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
    	
    	
    	List<CompletionEventData> eventData = CompletionEventsNew.readAllEvents();
    	
        //set up properties object, see the readme for available properties depending on the module type
        Properties props = new Properties();
        props.setProperty("database-file", Example.class.getResource("/").getPath() + "DefaultModel-Mscorlib");
        props.setProperty("base-path", Example.class.getResource("/").getPath());
        props.setProperty("context-path", "context/");
        props.setProperty("delta", "5");
        props.setProperty("lambda", "0");
        

        //choose your module, see the readme for available modules
        KaveContextModule module = new KaveContextModule(props);

        //create your Recommender, see the readme for available recommenders
        DefaultRecommender rec = new DefaultRecommender(module);
        
        //feed  the context into the reccommender
        Integer k = 10;

		MetricCollection metricCollection1 = new MetricCollection(k);

        for (int i = 0; i < eventData.size(); i++) {
            Set<Pair<IMemberName, Double>> reccommenderResult = rec.query(eventData.get(i).getEventContext());

    		QueryMetrics metrics = new QueryMetrics(reccommenderResult, eventData.get(i).getEventResult(), k);
    		metrics.calculateMetricTable();
    		metrics.print();

    		metricCollection1.add(metrics);

		}        
           
		metricCollection1.calculateMeanMetrics();
		System.out.println(metricCollection1.size());
		metricCollection1.printMeanMetrics();
		
	    LineChart_AWT chart = new LineChart_AWT("RACK Evaluation Results" , "Performance Metrics", metricCollection1.get());
	    chart.pack( );
	    RefineryUtilities.centerFrameOnScreen( chart );
	    chart.setVisible( true );
    }
}
