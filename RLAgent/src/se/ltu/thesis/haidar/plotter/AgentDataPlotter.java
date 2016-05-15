package se.ltu.thesis.haidar.plotter;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import se.ltu.thesis.haidar.database.SqlStatistics;
import se.ltu.thesis.haidar.database.SqlStatistics.Reward;
import se.ltu.thesis.haidar.database.SqlStatistics.Tuple;



public class AgentDataPlotter extends ApplicationFrame {
	
	private SqlStatistics mS;
	
	public AgentDataPlotter(final String title) {
		super(title);
		mS = new SqlStatistics();
		mS.connect();
		
		// get the data set
		XYDataset dataset = reward();
		
		// put the data set on a chart
		JFreeChart chart = episodeChart(dataset);
		
		
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
		setContentPane(chartPanel);
		mS.discounect();
	}
	private XYSeriesCollection reward(){

		final XYSeriesCollection dataset = new XYSeriesCollection();
		
		dataset.addSeries(getRewardXYSeries(7, 1, 0.6, 0.7));
		dataset.addSeries(getRewardXYSeries(7, 0.1, 0.3, 0.3));
		dataset.addSeries(getRewardXYSeries(7, 0.5, 0.8, 0.3));
		dataset.addSeries(getRewardXYSeries(7, 0.7, 0.4, 0.8));
		
		return dataset;
		
		
	}
	// this method take a tuple and return a XYSeries with the reward
	private XYSeries getRewardXYSeries(int test_id, double epsilon, double learning, double discount){
		// Get the tuple from the database
		Tuple mTuple 		= mS.getTuple(test_id, epsilon, learning, discount);
		int tuple_id 		= mTuple.getID();
		String mTupleName = mTuple.getName();
		
		// get the reward from the database
		Reward mReward = mS.getReward(test_id, tuple_id);
		double[] reward = mReward.getArray();
		// add the reward to the XYSeries
		final XYSeries mRewardXY = new XYSeries(mTupleName);
		for(int i = 0 ; i < reward.length ; i++){
			mRewardXY.add(i+1, reward[i]);
		}
		return mRewardXY;
	}

	// To create a episode/reward chart
	private JFreeChart episodeChart(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYLineChart("Episode",
				"Episode number", "Reward", dataset, PlotOrientation.VERTICAL,
				true, true, false);
		return chart;
	}

	public static void main(final String[] args) {

		final AgentDataPlotter demo = new AgentDataPlotter("XY Series Demo");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}
}
