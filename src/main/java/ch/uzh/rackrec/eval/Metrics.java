package ch.uzh.rackrec.eval;

import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import cc.kave.commons.model.naming.codeelements.IMemberName;

public class Metrics {
	
	private Set<Pair<IMemberName, Double>> resultRACK;
	private Set<Pair<IMemberName, Double>> resultGold;
	private Integer maxK;
	
	public MetricTable metricTable = new MetricTable(maxK);
	
	public Metrics(Set<Pair<IMemberName, Double>> resultRACK, Set<Pair<IMemberName, Double>> resultGold, Integer maxK) {
		this.resultRACK = resultRACK;
		this.resultGold = resultGold;
		this.maxK = maxK;
	}
	
	public MetricTable calculateMetricTable() {
		
		return metricTable;
	}
	
	
	private double calculateTopKAccuracy() {
		return 0.0;
	}
	
	private double calculateMeanReciprocalRank() {
		return 0.0;
	}

	private double calculateMeanAveragePrecision() {
		return 0.0;
	}
	
	private double calculateMeanRecall() {
		return 0.0;
	}
}
