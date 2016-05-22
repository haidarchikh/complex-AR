package se.ltu.thesis.haidar.plotter;

import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.json.JSONObject;

import se.ltu.thesis.haidar.agent.CloudWorld;
import se.ltu.thesis.haidar.datagenerator.StateUpdater;

@SuppressWarnings("serial")
public class FileDataPlotter extends ApplicationFrame {

	private Map<Integer, JSONObject> mData; 
	
	public FileDataPlotter(final String title) {

		super(title);
		
		mData = getFile(StateUpdater.FILE_PATH_DATA2);
		
		//XYDataset dataset	= plotThroughput();
		//JFreeChart chart 	= throughputChart(dataset);
		
		XYDataset dataset	= plotDelay();
		JFreeChart chart 	= delayChart(dataset);
		
		final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
		
	}
	
	private JFreeChart delayChart(XYDataset dataset){
		JFreeChart chart = ChartFactory.createXYLineChart("Delay",
				"Time t", "Delay (ms)", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}
	
	private JFreeChart throughputChart(XYDataset dataset){
		JFreeChart chart = ChartFactory.createXYLineChart("Throughput",
				"Time t", "Throughput (Mbps)", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}
	private XYDataset plotDelay() {
		
		final XYSeries D_N1_C1 = getTimeXY(CloudWorld.D_N1_C1);
		final XYSeries D_N1_C2 = getTimeXY(CloudWorld.D_N1_C2);
		
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(D_N1_C1);
		dataset.addSeries(D_N1_C2);
		return dataset;
	}
	private XYDataset plotThroughput() {
		
		final XYSeries T_N1_C1 = getTimeXY(CloudWorld.T_N1_C1);
		final XYSeries T_N1_C2 = getTimeXY(CloudWorld.T_N1_C2);
		
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(T_N1_C1);
		dataset.addSeries(T_N1_C2);
		return dataset;
	}
	
	private Map<Integer, JSONObject> getFile(String path){
		StateUpdater mUpdater = new StateUpdater();	
		mUpdater.loadDataFromFile(path);
		Map<Integer, JSONObject> mData = mUpdater.loadDataFromFile(path);
		return mData;	
	}
	
	private XYSeries getTimeXY(String mName){
		final XYSeries mXY = new XYSeries(mName);
		
		JSONObject mState = null;
		for(int i =0 ; i< mData.size(); i++){
			mState = mData.get(i);
			int value = (int) mState.get(mName);
			mXY.add(i, value);
		}
		return mXY;
	}
	
	public static void main(final String[] args) {

		final FileDataPlotter demo = new FileDataPlotter("XY Series Demo");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

	}

}
