package ch.uzh.rackrec.eval;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import cc.kave.commons.model.naming.codeelements.IMemberName;

public class QueryMetrics {
	
	private Set<Pair<IMemberName, Double>> resultRACK;
	private Set<Pair<IMemberName, Double>> resultGold;
	private Integer maxK;
	
	private MetricTable myMetricTable;
		
	public QueryMetrics(Set<Pair<IMemberName, Double>> resultRACK, Set<Pair<IMemberName, Double>> resultGold, Integer maxK) {
		this.resultRACK = resultRACK;
		this.resultGold = resultGold;
		this.maxK = maxK;
		
		this.myMetricTable = new MetricTable(this.maxK);
		
		calculateMetricTable();
	}
	
	public void print() {
		this.myMetricTable.printTable();
	}
	
	public MetricTable getMetricTable() {
		return this.myMetricTable;
	}
	
	private void calculateMetricTable() {
		
		ArrayList<Double> accuracy = new ArrayList<Double>();
		ArrayList<Double> mrr = new ArrayList<Double>();
		ArrayList<Double> precision = new ArrayList<Double>();
		ArrayList<Double> recall = new ArrayList<Double>();

		for (int i = 0; i < maxK; i++) {
			//accuracy.add(calculateTopKAccuracy(i));
			//mrr.add(calculateMeanReciprocalRank(i));
			//precision.add(calculateMeanAveragePrecision(i));
			//recall.add(calculateMeanRecall(i));
			
			Pair<Double, Double> precisionRecall = calculatePrecisionAndRecall(i);
			precision.add(precisionRecall.getLeft());
			recall.add(precisionRecall.getRight());
			if (precisionRecall.getLeft() > 0.0) {
				accuracy.add(1.0);
			} else {
				accuracy.add(0.0);
			}
		}
				
		Pair<String, ArrayList<Double>> accuracyMetrics = new ImmutablePair<>("Accuracy@K", accuracy);
		Pair<String, ArrayList<Double>> mrrMetrics = new ImmutablePair<>("Reciprocal Rank@K", mrr);
		Pair<String, ArrayList<Double>> precisionMetrics = new ImmutablePair<>("Precision@K", precision);
		Pair<String, ArrayList<Double>> recallMetrics = new ImmutablePair<>("Recall@K", recall);
				
		this.myMetricTable.add(accuracyMetrics);
		this.myMetricTable.add(mrrMetrics);
		this.myMetricTable.add(precisionMetrics);
		this.myMetricTable.add(recallMetrics);
	}
	
	//does a value in the rack result appear in the gold set in the first k results?
	private double isAccurate(Integer k) {
		Integer goldCounter = 0;
		Integer rackCounter = 0;

	    Iterator<Pair<IMemberName, Double>> goldIterator = this.resultGold.iterator();
	    Iterator<Pair<IMemberName, Double>> rackIterator = this.resultRACK.iterator();

	    while(goldIterator.hasNext() && goldCounter < k) {
	    	Pair<IMemberName, Double> goldPair = goldIterator.next();
	    	while (rackIterator.hasNext() && rackCounter < k) {
				Pair<IMemberName, Double> rackPair = rackIterator.next();
				if (goldPair.getLeft() == rackPair.getLeft()) {
					return 1.0;
				}
				rackCounter += 1;
			}
	    	goldCounter += 1;
	    }
		return 0.0;
	}
	
	private Pair<Double, Double> calculatePrecisionAndRecall(Integer k) {
		Integer rackCounter = 0;
		Integer truePositives = 0;
		Integer falsePositives = 0;
		double precision;
		double recall;

	    Iterator<Pair<IMemberName, Double>> goldIterator = this.resultGold.iterator();
	    Iterator<Pair<IMemberName, Double>> rackIterator = this.resultRACK.iterator();
	    
	    while(rackIterator.hasNext() && rackCounter < k + 1) {
	    	Pair<IMemberName, Double> rackPair = rackIterator.next();
			Integer goldCounter = 0;
	    	while (goldIterator.hasNext()) {
				Pair<IMemberName, Double> goldPair = goldIterator.next();
				if (rackPair.getLeft() == goldPair.getLeft()) {
					goldCounter += 1;
					break;
				}
			}
	    	if (goldCounter == 0) {
				falsePositives += 1;
			} else {
				truePositives += 1;
			}
			goldIterator = this.resultGold.iterator();
	    	rackCounter += 1;
	    }
	    if (this.resultRACK.size() == 0 || this.resultGold.size() == 0) {
	    	precision = 0;
			recall = 0;
		} else {
		    precision = (double) truePositives / (double) this.resultRACK.size();
		    recall = (double) truePositives / (double) this.resultGold.size();
		}	
		return Pair.of((double) Math.round(precision * 1000) / 1000, (double) Math.round(recall * 1000) / 1000);
	}
}
