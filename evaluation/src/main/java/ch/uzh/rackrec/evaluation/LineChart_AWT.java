package ch.uzh.rackrec.evaluation;

import org.jfree.chart.ChartPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;

import ch.uzh.rackrec.eval.MetricTable;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChart_AWT extends ApplicationFrame {

   public LineChart_AWT( String applicationTitle , String chartTitle, MetricTable values ) {
      super(applicationTitle);
      JFreeChart lineChart = ChartFactory.createLineChart(
         chartTitle,
         "k","Value",
         createDataset(values),
         PlotOrientation.VERTICAL,
         true,true,false);
      
      ChartPanel chartPanel = new ChartPanel( lineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      setContentPane( chartPanel );
   }

   private DefaultCategoryDataset createDataset(MetricTable values) {
      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
      for (int i = 0; i < values.get(0).getRight().size(); i++) {
          dataset.addValue( values.get(0).getRight().get(i) , values.get(0).getLeft() , Integer.toString(i + 1) );
          dataset.addValue( values.get(1).getRight().get(i) , values.get(1).getLeft() , Integer.toString(i + 1) );
          dataset.addValue( values.get(2).getRight().get(i) , values.get(2).getLeft() , Integer.toString(i + 1) );
          dataset.addValue( values.get(3).getRight().get(i) , values.get(3).getLeft() , Integer.toString(i + 1) );
	}
      return dataset;
   }
}