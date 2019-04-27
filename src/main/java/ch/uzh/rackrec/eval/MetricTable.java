package ch.uzh.rackrec.eval;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

public class MetricTable {
	
	public ArrayList<Pair<String, ArrayList<Double>>> metricTable;

		
	public MetricTable(Integer k) {
		metricTable = new ArrayList<>();
	}
	
	public void printTable() {
		System.out.println("this is a test print statement");
	}
}