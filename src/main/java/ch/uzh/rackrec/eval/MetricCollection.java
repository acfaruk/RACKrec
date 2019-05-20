package ch.uzh.rackrec.eval;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class MetricCollection {
	
	private ArrayList<QueryMetrics> metricCollection;
	private MetricTable meanMetrics;
	private Integer k;
	
	public MetricCollection(Integer k) {
		this.k = k;
		this.metricCollection = new ArrayList<QueryMetrics>();
	}

	public void add(QueryMetrics queryMetrics) {
		this.metricCollection.add(queryMetrics);
	}
	
	public void printCollection() {
		
	}
	
	public MetricTable get() {
		return this.meanMetrics;
	}
	
	public void printMeanMetrics() {
		this.meanMetrics.printTable();
	}
	
	public void calculateMeanMetrics() {
		this.meanMetrics = new MetricTable(this.k);
		ArrayList<Double> meanAccuracy = new ArrayList<Double>();
		ArrayList<Double> meanReciprocalRank = new ArrayList<Double>();
		ArrayList<Double> meanPrecision = new ArrayList<Double>();
		ArrayList<Double> meanRecall = new ArrayList<Double>();

		
		for (int i = 0; i < this.k; i++) {
			meanAccuracy.add(0.0);
			meanReciprocalRank.add(0.0);
			meanPrecision.add(0.0);
			meanRecall.add(0.0);
		}
				
		for (int i = 0; i < metricCollection.size(); i++) {
			Pair<String, ArrayList<Double>> accuracy = metricCollection.get(i).getMetricTable().get(0);
			Pair<String, ArrayList<Double>> reciprocalRank = metricCollection.get(i).getMetricTable().get(1);
			Pair<String, ArrayList<Double>> precision = metricCollection.get(i).getMetricTable().get(2);
			Pair<String, ArrayList<Double>> recall = metricCollection.get(i).getMetricTable().get(3);

			for (int j = 0; j < precision.getRight().size(); j++) {
				// if accuracy.getRight.get(j) not null
				if (accuracy.getRight().get(j) != null) {
					meanAccuracy.set(j, meanAccuracy.get(j) + accuracy.getRight().get(j));
					meanReciprocalRank.set(j, meanReciprocalRank.get(j) + reciprocalRank.getRight().get(j));
					meanPrecision.set(j, meanPrecision.get(j) + precision.getRight().get(j));
					meanRecall.set(j, meanRecall.get(j) + recall.getRight().get(j));
				}
			}
		}
		
		for (int i = 0; i < meanPrecision.size(); i++) {
			//check for 0
			meanAccuracy.set(i, (double) Math.round(meanAccuracy.get(i) / (double) metricCollection.size() * 1000) / 1000);
			meanReciprocalRank.set(i, (double) Math.round(meanReciprocalRank.get(i) / (double) metricCollection.size() * 1000) / 1000);
			meanPrecision.set(i, (double) Math.round(meanPrecision.get(i) / (double) metricCollection.size() * 1000) / 1000);
			meanRecall.set(i, (double) Math.round(meanRecall.get(i) / (double) metricCollection.size() * 1000) / 1000);

		}
		Pair<String, ArrayList<Double>> meanAccuracyPair = new ImmutablePair<>("Top-K Accuracy", meanAccuracy);
		Pair<String, ArrayList<Double>> meanReciprocalRankPair = new ImmutablePair<>("Mean Reciprocal Rank@K", meanReciprocalRank);
		Pair<String, ArrayList<Double>> meanPrecisionPair = new ImmutablePair<>("Mean Precision@K", meanPrecision);
		Pair<String, ArrayList<Double>> meanRecallPair = new ImmutablePair<>("Mean Recall@K", meanRecall);
		
		this.meanMetrics.add(meanAccuracyPair);
		this.meanMetrics.add(meanReciprocalRankPair);
		this.meanMetrics.add(meanPrecisionPair);
		this.meanMetrics.add(meanRecallPair);
	}

	public Integer size() {
		return this.metricCollection.size();
	}
}
