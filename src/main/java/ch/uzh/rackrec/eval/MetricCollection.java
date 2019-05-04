package ch.uzh.rackrec.eval;

import java.util.ArrayList;
import java.util.Iterator;

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
	
	public void printMeanMetrics() {
		this.meanMetrics.printTable();
	}
	
	public void calculateMeanMetrics() {
		this.meanMetrics = new MetricTable(this.k);
		ArrayList<Double> meanPrecision = new ArrayList<Double>();
		ArrayList<Double> meanRecall = new ArrayList<Double>();

		
		for (int i = 0; i < this.k; i++) {
			meanPrecision.add(0.0);
			meanRecall.add(0.0);
		}
				
		for (int i = 0; i < metricCollection.size(); i++) {
			Pair<String, ArrayList<Double>> precision = metricCollection.get(i).getMetricTable().get(0);
			Pair<String, ArrayList<Double>> recall = metricCollection.get(i).getMetricTable().get(1);

			for (int j = 0; j < precision.getRight().size(); j++) {
				meanPrecision.set(j, meanPrecision.get(j) + precision.getRight().get(j));
				meanRecall.set(j, meanRecall.get(j) + recall.getRight().get(j));
			}
		}
		
		for (int i = 0; i < meanPrecision.size(); i++) {
			//check for 0
			
			meanPrecision.set(i, (double) Math.round(meanPrecision.get(i) / (double) metricCollection.size() * 1000) / 1000);
			meanRecall.set(i, (double) Math.round(meanRecall.get(i) / (double) metricCollection.size() * 1000) / 1000);

		}
		Pair<String, ArrayList<Double>> meanPrecisionPair = new ImmutablePair<>("Mean Precision@K", meanPrecision);
		Pair<String, ArrayList<Double>> meanRecallPair = new ImmutablePair<>("Mean Recall@K", meanRecall);
		
		this.meanMetrics.add(meanPrecisionPair);
		this.meanMetrics.add(meanRecallPair);
	}
}
