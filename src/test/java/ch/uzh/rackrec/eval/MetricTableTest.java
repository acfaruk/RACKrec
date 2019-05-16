package ch.uzh.rackrec.eval;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class MetricTableTest {
	
	private MetricTable testMetricTable;
	private ArrayList<Double> testArrayList;
	private Pair<String, ArrayList<Double>> testMetric;
	
    @Before
    public void initialize(){
    	Integer testK = 10;
    	testMetricTable = new MetricTable(testK);
    	
    	testArrayList = new ArrayList<>();
    	testArrayList.add(1.0);
    	testMetric = new ImmutablePair<>("Test Metric", testArrayList);
    	this.testMetricTable.add(testMetric);
    	
    }
    
    @Test
    public void testGetMetricTable() {
    	
    	//assertEquals(testMetricTable.getMetricTable(), this.testMetricTable);
    }
    
    @Test
    public void testAdd() {
    	
    	assertEquals(testMetricTable.getMetricTable().size(), 1);
    }
    
    @Test
    public void testGet() {

    	assertEquals(this.testMetricTable.get(0), this.testMetric);
    }

}
