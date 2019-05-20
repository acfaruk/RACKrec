package ch.uzh.rackrec.eval;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;

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
		ArrayList<Double> reciprocalRank = new ArrayList<Double>();
		ArrayList<Double> precision = new ArrayList<Double>();
		ArrayList<Double> recall = new ArrayList<Double>();

		for (int i = 0; i < maxK; i++) {		
			// if i > resultRack else set null
			//System.out.println(i);
			//System.out.println(this.resultRACK.size());
			if (i < this.resultRACK.size()) {
				ArrayList<Double> precisionRecallReciprocalRank = calculatePrecisionRecallReciprocalRank(i);
				reciprocalRank.add(precisionRecallReciprocalRank.get(0));
				precision.add(precisionRecallReciprocalRank.get(1));
				recall.add(precisionRecallReciprocalRank.get(2));
				if (precisionRecallReciprocalRank.get(1) > 0.0) {
					accuracy.add(1.0);
				} else {
					accuracy.add(0.0);
				}
			} else {
				reciprocalRank.add(null);
				precision.add(null);
				recall.add(null);
				accuracy.add(null);
			}
		}
				
		Pair<String, ArrayList<Double>> accuracyMetrics = new ImmutablePair<>("Accuracy@K", accuracy);
		Pair<String, ArrayList<Double>> rrMetrics = new ImmutablePair<>("Reciprocal Rank@K", reciprocalRank);
		Pair<String, ArrayList<Double>> precisionMetrics = new ImmutablePair<>("Precision@K", precision);
		Pair<String, ArrayList<Double>> recallMetrics = new ImmutablePair<>("Recall@K", recall);
				
		this.myMetricTable.add(accuracyMetrics);
		this.myMetricTable.add(rrMetrics);
		this.myMetricTable.add(precisionMetrics);
		this.myMetricTable.add(recallMetrics);
	}
	
	private ArrayList<Double> calculatePrecisionRecallReciprocalRank(Integer k) {
		Integer rackCounter = 0;
		Integer truePositives = 0;
		double reciprocalRank = 0.0;
		double precision;
		double recall;

	    Iterator<Pair<IMemberName, Double>> goldIterator = this.resultGold.iterator();
	    Iterator<Pair<IMemberName, Double>> rackIterator = this.resultRACK.iterator();
	    
	    while(rackIterator.hasNext() && rackCounter < k + 1) {
	    	Pair<IMemberName, Double> rackPair = rackIterator.next();
			Boolean foundInGold = false;
	    	while (goldIterator.hasNext() && foundInGold == false) {
				Pair<IMemberName, Double> goldPair = goldIterator.next();
				//System.out.print(rackPair.getLeft().toString() + " ");
		        IMemberName name1 = new MethodName(goldPair.getLeft().getFullName());
		        //System.out.print(name1.toString() + " ");
		        //System.out.println(rackPair.getLeft().toString().equals(name1.toString()) );
			
				if (rackPair.getLeft().toString().equals(name1.toString())) {
					foundInGold = true;
					truePositives += 1;
					if (reciprocalRank == 0.0) {
						reciprocalRank = 1.0 / ((double) rackCounter + 1.0);
					}
					break;
				}
			}
			goldIterator = this.resultGold.iterator();
			foundInGold = false;
	    	rackCounter += 1;
	    }
	    if (this.resultRACK.size() == 0 || this.resultGold.size() == 0) {
	    	precision = 0;
			recall = 0;
		} else {
		    precision = (double) truePositives / (double) k;
		    recall = (double) truePositives / (double) this.resultGold.size();
		}	
		ArrayList<Double> returnValues = new ArrayList<Double>();
		returnValues.add(reciprocalRank);
		returnValues.add((double) Math.round(precision * 1000) / 1000);
		returnValues.add((double) Math.round(recall * 1000) / 1000);
		
		return returnValues;

	}
}
