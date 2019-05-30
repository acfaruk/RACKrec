package ch.uzh.rackrec.eval;

import static org.junit.Assert.assertEquals;

import java.util.*;


import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;

public class QueryMetricsTest {
	
	Set<Pair<IMemberName, Double>> recommenderResult;
	Set<Pair<IMemberName, Double>> eventResult;
	Integer maxK = 4;
	MethodName mockMember1;
	MethodName mockMember2;
	MethodName mockMember3;
	Pair<IMemberName, Double> pair1;
	Pair<IMemberName, Double> pair2;
	Pair<IMemberName, Double> pair3;

	
	QueryMetrics testMetrics;
    
    @Before
    public void initialize() {
        mockMember1 = mock(MethodName.class);   
        when(mockMember1.getName()).thenReturn("methodname1");
        when(mockMember1.getFullName()).thenReturn("methodname1");
        when(mockMember1.toString()).thenReturn("MethodName(methodname1)");
        
        mockMember2 = mock(MethodName.class);   
        when(mockMember2.getName()).thenReturn("methodname2");
        when(mockMember2.getFullName()).thenReturn("methodname2");
        when(mockMember2.toString()).thenReturn("MethodName(methodname2)");
        
        mockMember3 = mock(MethodName.class);   
        when(mockMember3.getName()).thenReturn("methodname3");
        when(mockMember3.getFullName()).thenReturn("methodname3");
        when(mockMember3.toString()).thenReturn("MethodName(methodname3)");
        
        recommenderResult = new LinkedHashSet<Pair<IMemberName,Double>>(3);
        eventResult = new LinkedHashSet<Pair<IMemberName,Double>>(3);
        
        pair1 = Pair.of(mockMember1, 1.0);
        pair2 = Pair.of(mockMember2, 0.9);
        pair3 = Pair.of(mockMember3, 0.8);
        
    }
    
    @Test
    public void testCalculatemetricTable() {
        recommenderResult.add(pair1);
        recommenderResult.add(pair2);
        recommenderResult.add(pair3);
        
        eventResult.add(pair2);
        eventResult.add(pair3);
        
    	testMetrics = new QueryMetrics(recommenderResult, eventResult, maxK, "simple");
    	
    	testMetrics.calculateMetricTable();
    	testMetrics.print();
    	
    	MetricTable result = testMetrics.getMetricTable();
    	String expected = "[(Accuracy@K,[0.0, 1.0, 1.0, null]), (Reciprocal Rank@K,[0.0, 0.5, 0.5, null]), (Precision@K,[0.0, 0.5, 0.667, null]), (Recall@K,[0.0, 0.5, 1.0, null])]";
    	assertEquals(expected, result.getMetricTable().toString());
    }
    
    @Test
    public void testCalculateMetricTableEmptyEvent() {
        
        eventResult.add(pair2);
        
    	testMetrics = new QueryMetrics(recommenderResult, eventResult, maxK, "simple");
    	testMetrics.calculateMetricTable();
    	testMetrics.print();
    	
    	MetricTable result = testMetrics.getMetricTable();
    	String expected = "[(Accuracy@K,[null, null, null, null]), (Reciprocal Rank@K,[null, null, null, null]), (Precision@K,[null, null, null, null]), (Recall@K,[null, null, null, null])]";

    	assertEquals(expected, result.getMetricTable().toString());
    	
    }
    
    @Test
    public void testCalculateMetricTableEmptyRecommender() {
        
        recommenderResult.add(pair2);
        
    	testMetrics = new QueryMetrics(recommenderResult, eventResult, maxK, "simple");
    	testMetrics.calculateMetricTable();
    	testMetrics.print();
    	
    	MetricTable result = testMetrics.getMetricTable();
    	String expected = "[(Accuracy@K,[0.0, null, null, null]), (Reciprocal Rank@K,[0.0, null, null, null]), (Precision@K,[0.0, null, null, null]), (Recall@K,[0.0, null, null, null])]";
    	assertEquals(expected, result.getMetricTable().toString());
    	
    }
    
    @Test
    public void testCalculateMetricTableEmptyBoth() {
                
    	testMetrics = new QueryMetrics(recommenderResult, eventResult, maxK, "simple");
    	testMetrics.calculateMetricTable();
    	testMetrics.print();
    	
    	MetricTable result = testMetrics.getMetricTable();
    	String expected = "[(Accuracy@K,[null, null, null, null]), (Reciprocal Rank@K,[null, null, null, null]), (Precision@K,[null, null, null, null]), (Recall@K,[null, null, null, null])]";
    	assertEquals(expected, result.getMetricTable().toString());
    	
    }
    
    @Test
    public void testExtended() {
                
    	testMetrics = new QueryMetrics(recommenderResult, eventResult, maxK, "extended");
    	testMetrics.calculateMetricTable();
    	testMetrics.print();
    	
    	MetricTable result = testMetrics.getMetricTable();
    	String expected = "[(Accuracy@K,[null, null, null, null]), (Reciprocal Rank@K,[null, null, null, null]), (Precision@K,[null, null, null, null]), (Recall@K,[null, null, null, null])]";
    	assertEquals(expected, result.getMetricTable().toString());
    	
    }
    
}
