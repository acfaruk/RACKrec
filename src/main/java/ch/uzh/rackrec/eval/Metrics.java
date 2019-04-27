package ch.uzh.rackrec.eval;

import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
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
	
	public ArrayList<Pair<String, ArrayList<Double>>> calculateMetricTable() {
		
		ArrayList<Double> accuracy = new ArrayList<Double>();
		ArrayList<Double> mrr = new ArrayList<Double>();
		ArrayList<Double> precision = new ArrayList<Double>();
		ArrayList<Double> recall = new ArrayList<Double>();

		for (int i = 0; i < maxK; i++) {
			accuracy.add(calculateTopKAccuracy(i));
			mrr.add(calculateMeanReciprocalRank(i));
			precision.add(calculateMeanAveragePrecision(i));
			recall.add(calculateMeanRecall(i));
		}
				
		Pair<String, ArrayList<Double>> accuracyMetrics = new ImmutablePair<>("Top-K Accuracy", accuracy);
		Pair<String, ArrayList<Double>> mrrMetrics = new ImmutablePair<>("Mean Reciprocal Rank@K", mrr);
		Pair<String, ArrayList<Double>> precisionMetrics = new ImmutablePair<>("Mean Average Precision@K", precision);
		Pair<String, ArrayList<Double>> recallMetrics = new ImmutablePair<>("Mean Recall@K", recall);

		
		ArrayList<Pair<String, ArrayList<Double>>> metricTable = new ArrayList<>();
		
		metricTable.add(accuracyMetrics);
		metricTable.add(mrrMetrics);
		metricTable.add(precisionMetrics);
		metricTable.add(recallMetrics);

		return metricTable;
	}
	
	
	private double calculateTopKAccuracy(Integer k) {
		return 0.0;
	}
	
	private double calculateMeanReciprocalRank(Integer k) {
		return 0.0;
	}

	private double calculateMeanAveragePrecision(Integer k) {
		return 0.0;
	}
	
	private double calculateMeanRecall(Integer k) {
		return 0.0;
	}
}
