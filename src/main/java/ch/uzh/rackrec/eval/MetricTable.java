package ch.uzh.rackrec.eval;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

public class MetricTable {
	
	private ArrayList<Pair<String, ArrayList<Double>>> metricTable;

		
	public MetricTable(Integer k) {
		this.metricTable = new ArrayList<>();
	}
	
	public void printTable() {
		System.out.println(this.metricTable);
	}
	
	public void add(Pair<String, ArrayList<Double>> metric) {
		this.metricTable.add(metric);
	}

	public Pair<String, ArrayList<Double>> get(int i) {
		return metricTable.get(i);
	}
	
	public ArrayList<Pair<String, ArrayList<Double>>> getMetricTable() {
		return this.metricTable;
	}
}