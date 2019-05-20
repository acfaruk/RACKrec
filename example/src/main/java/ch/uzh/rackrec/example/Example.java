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
            //System.out.println(reccommenderResult);

    		QueryMetrics metrics = new QueryMetrics(reccommenderResult, eventData.get(i).getEventResult(), k);
    		metrics.print();

    		metricCollection1.add(metrics);

		}        
        
        /*
        Set<Pair<IMemberName, Double>> reccommenderResult = new LinkedHashSet<Pair<IMemberName,Double>>(6);
        
        IMemberName name1 = new MethodName("method1");
        IMemberName name2 = new MethodName("method2");
        IMemberName name3 = new MethodName("method3");
        IMemberName name4 = new MethodName("method4");
        IMemberName name5 = new MethodName("method5");
        IMemberName name6 = new MethodName("method6");
        
        Pair<IMemberName, Double> pair1 = Pair.of(name1, 0.9);
        Pair<IMemberName, Double> pair2 = Pair.of(name2, 0.8);
        Pair<IMemberName, Double> pair3 = Pair.of(name3, 0.7);
        Pair<IMemberName, Double> pair4 = Pair.of(name4, 0.6);
        Pair<IMemberName, Double> pair5 = Pair.of(name5, 0.5);
        Pair<IMemberName, Double> pair6 = Pair.of(name6, 0.4);
        
        reccommenderResult.add(pair1);
        reccommenderResult.add(pair2);
        reccommenderResult.add(pair3);
        reccommenderResult.add(pair4);
        reccommenderResult.add(pair5);
        reccommenderResult.add(pair6);
        */
        
		metricCollection1.calculateMeanMetrics();
		System.out.println(metricCollection1.size());
		metricCollection1.printMeanMetrics();
		
	    LineChart_AWT chart = new LineChart_AWT("RACK Evaluation Results" , "Performance Metrics", metricCollection1.get());
	    chart.pack( );
	    RefineryUtilities.centerFrameOnScreen( chart );
	    chart.setVisible( true );

                
        //create dummy context for now
        //Context ctx = new Context();
        /*
        Set<Pair<IMemberName, Double>> eventResult = new LinkedHashSet<Pair<IMemberName,Double>>(3);
        Set<Pair<IMemberName, Double>> reccommenderResult = new LinkedHashSet<Pair<IMemberName,Double>>(6);
        Set<Pair<IMemberName, Double>> reccommenderResult2 = new LinkedHashSet<Pair<IMemberName,Double>>(6);

        
        IMemberName name1 = new MethodName("method1");
        IMemberName name2 = new MethodName("method2");
        IMemberName name3 = new MethodName("method3");
        IMemberName name4 = new MethodName("method4");
        IMemberName name5 = new MethodName("method5");
        IMemberName name6 = new MethodName("method6");
        IMemberName name7 = new MethodName("method7");
        IMemberName name8 = new MethodName("method8");
        IMemberName name9 = new MethodName("method9");
        IMemberName name10 = new MethodName("method10");
        IMemberName name11 = new MethodName("method11");
        IMemberName name12 = new MethodName("method12");
        IMemberName name13 = new MethodName("method13");
        IMemberName name14 = new MethodName("method14");
        IMemberName name15 = new MethodName("method15");
        IMemberName name16 = new MethodName("method16");
        IMemberName name17 = new MethodName("method17");
        IMemberName name18 = new MethodName("method18");
        IMemberName name19 = new MethodName("method19");
        IMemberName name20 = new MethodName("method20");
        

        Pair<IMemberName, Double> pair1 = Pair.of(name1, 0.9);
        Pair<IMemberName, Double> pair2 = Pair.of(name2, 0.8);
        Pair<IMemberName, Double> pair3 = Pair.of(name3, 0.7);
        Pair<IMemberName, Double> pair4 = Pair.of(name4, 0.6);
        Pair<IMemberName, Double> pair5 = Pair.of(name5, 0.5);
        Pair<IMemberName, Double> pair6 = Pair.of(name6, 0.4);
        Pair<IMemberName, Double> pair7 = Pair.of(name7, 0.3);
        Pair<IMemberName, Double> pair8 = Pair.of(name8, 0.2);
        Pair<IMemberName, Double> pair9 = Pair.of(name9, 0.1);
        Pair<IMemberName, Double> pair10 = Pair.of(name10, 0.0);   
        Pair<IMemberName, Double> pair11 = Pair.of(name11, 0.0);   
        Pair<IMemberName, Double> pair12 = Pair.of(name12, 0.0);   
        Pair<IMemberName, Double> pair13 = Pair.of(name13, 0.0);   
        Pair<IMemberName, Double> pair14 = Pair.of(name14, 0.0);   
        Pair<IMemberName, Double> pair15 = Pair.of(name15, 0.0);   
        Pair<IMemberName, Double> pair16 = Pair.of(name16, 0.0);   
        Pair<IMemberName, Double> pair17 = Pair.of(name17, 0.0);   
        Pair<IMemberName, Double> pair18 = Pair.of(name18, 0.0);   
        Pair<IMemberName, Double> pair19 = Pair.of(name19, 0.0);   
        Pair<IMemberName, Double> pair20 = Pair.of(name20, 0.0);   
        
        eventResult.add(pair1);
        eventResult.add(pair2);
        eventResult.add(pair3);
        
        reccommenderResult.add(pair6);
        reccommenderResult.add(pair1);
        reccommenderResult.add(pair9);
        reccommenderResult.add(pair11);
        reccommenderResult.add(pair16);
        reccommenderResult.add(pair18);
        
        reccommenderResult2.add(pair2);
        reccommenderResult2.add(pair5);
        reccommenderResult2.add(pair6);
        reccommenderResult2.add(pair8);
        reccommenderResult2.add(pair1);
        reccommenderResult2.add(pair12);
                                
        //For a real evaluation we need a large amount of rack results and the corresponding gold sets, the paper used 150
        //So for every query we calculate the individual metrics and the consolidate these into a metrics object. as of now
        //a metrics object only has one result.
        //collection of metrics has function merge or smth that calculates the mean of all queries
        
        
		QueryMetrics metrics1 = new QueryMetrics(reccommenderResult, eventResult, k);
		QueryMetrics metrics2 = new QueryMetrics(reccommenderResult2, eventResult, k);
		//QueryMetrics metrics3 = new QueryMetrics(reccommenderResult, eventResult, k);
		//QueryMetrics metrics4 = new QueryMetrics(reccommenderResult, eventResult, k);
		
		metrics1.print();
		metrics2.print();
		//metrics3.print();
		//metrics4.print();
		System.out.println("---------------------------------------------------------------------------------------------");

		MetricCollection metricCollection1 = new MetricCollection(k);
		
		metricCollection1.add(metrics1);
		metricCollection1.add(metrics2);
		//metricCollection1.add(metrics3);
		//metricCollection1.add(metrics4);
		
		metricCollection1.calculateMeanMetrics();
		metricCollection1.printMeanMetrics();
		
	    LineChart_AWT chart = new LineChart_AWT("RACK Evaluation Results" , "Performance Metrics", metricCollection1.get());
	    chart.pack( );
	    RefineryUtilities.centerFrameOnScreen( chart );
	    chart.setVisible( true );
	    
	    */
    }
}
