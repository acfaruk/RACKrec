package ch.uzh.rackrec.eval;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;

public class MetricCollectionTest {
	
	QueryMetrics queryMetrics1;
	QueryMetrics queryMetrics2;
	
	MetricTable metricTable1;
	MetricTable metricTable2;
	
	ArrayList<Double> accuracy1 = new ArrayList<Double>(Arrays.asList(0.0, 1.0, 1.0, null));
	ArrayList<Double> reciprocalRank1 = new ArrayList<Double>(Arrays.asList(0.0, 0.5, 0.5, null));
	ArrayList<Double> precision1 = new ArrayList<Double>(Arrays.asList(0.0, 0.5, 0.667, null));
	ArrayList<Double> recall1 = new ArrayList<Double>(Arrays.asList(0.0, 0.5, 1.0, null));		

	ArrayList<Double> accuracy2 = new ArrayList<Double>(Arrays.asList(1.0, 1.0, 1.0, 1.0));
	ArrayList<Double> reciprocalRank2 = new ArrayList<Double>(Arrays.asList(1.0, 1.0, 1.0, 1.0));
	ArrayList<Double> precision2 = new ArrayList<Double>(Arrays.asList(1.0, 1.0, 1.0, 1.0));
	ArrayList<Double> recall2 = new ArrayList<Double>(Arrays.asList(1.0, 1.0, 1.0, 1.0));	
	
	Integer k;
	MetricCollection testMetricCollection;
	
    @Before
    public void initialize() {
    	k = 4;
    	
    	Pair<String, ArrayList<Double>> accuracyMetrics1 = new ImmutablePair<>("Accuracy@K", accuracy1);
    	Pair<String, ArrayList<Double>> rrMetrics1 = new ImmutablePair<>("Reciprocal Rank@K", reciprocalRank1);
    	Pair<String, ArrayList<Double>> precisionMetrics1 = new ImmutablePair<>("Precision@K", precision1);
    	Pair<String, ArrayList<Double>> recallMetrics1 = new ImmutablePair<>("Recall@K", recall1);
    	
    	Pair<String, ArrayList<Double>> accuracyMetrics2 = new ImmutablePair<>("Accuracy@K", accuracy2);
    	Pair<String, ArrayList<Double>> rrMetrics2 = new ImmutablePair<>("Reciprocal Rank@K", reciprocalRank2);
    	Pair<String, ArrayList<Double>> precisionMetrics2 = new ImmutablePair<>("Precision@K", precision2);
    	Pair<String, ArrayList<Double>> recallMetrics2 = new ImmutablePair<>("Recall@K", recall2);
    	
    	metricTable1 = new MetricTable(k);
    	
    	metricTable1.add(accuracyMetrics1);
    	metricTable1.add(rrMetrics1);
    	metricTable1.add(precisionMetrics1);
    	metricTable1.add(recallMetrics1);
    	
    	metricTable2 = new MetricTable(k);
    	
    	metricTable2.add(accuracyMetrics2);
    	metricTable2.add(rrMetrics2);
    	metricTable2.add(precisionMetrics2);
    	metricTable2.add(recallMetrics2);
    	
    	queryMetrics1 = new QueryMetrics(metricTable1);
    	queryMetrics2 = new QueryMetrics(metricTable2);
    	
    	testMetricCollection = new MetricCollection(k);
    }
    
    @Test
    public void testEmptySize() {
    	Integer size = testMetricCollection.size();
    	Integer expected = 0;
    	assertEquals(expected, size);
    }
    
    @Test
    public void testGetMeanMetricsEmpty() {
    	Integer size = testMetricCollection.size();
    	String expected = "[]";
    	MetricTable meanMetrics = testMetricCollection.getMeanMetrics();
    	assertEquals(expected, meanMetrics.getMetricTable().toString());
    }
    
    @Test
    public void testAddMetricTable() {
    	testMetricCollection.add(queryMetrics1);
    	
    	Integer size = testMetricCollection.size();
    	Integer expected = 1;
    	assertEquals(expected, size);    
    }
    
    @Test
    public void testCalculateMeanMetrics() {
    	testMetricCollection.add(queryMetrics1);
    	testMetricCollection.add(queryMetrics2);
    	
    	testMetricCollection.calculateMeanMetrics();
    	String expected = "[(Top-K Accuracy,[0.5, 1.0, 1.0, 1.0]), (Mean Reciprocal Rank@K,[0.5, 0.75, 0.75, 1.0]), (Mean Precision@K,[0.5, 0.75, 0.834, 1.0]), (Mean Recall@K,[0.5, 0.75, 1.0, 1.0])]";
    	MetricTable meanMetrics = testMetricCollection.getMeanMetrics();
    	assertEquals(expected, meanMetrics.getMetricTable().toString());
    }
}
