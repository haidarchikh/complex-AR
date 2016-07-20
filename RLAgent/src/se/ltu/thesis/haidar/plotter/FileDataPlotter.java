package se.ltu.thesis.haidar.plotter;

import java.awt.Color;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
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
		
		mData = getFile(StateUpdater.FILE_PATH_DATA3);
		
		//XYDataset dataset	= plotThroughput();
		//JFreeChart chart 	= throughputChart(dataset);
		
		//XYDataset dataset	= plotDelay();
		//JFreeChart chart 	= delayChart(dataset);
		
		XYDataset datasetDN1C1	= plotDelayDN1C1();
	    final XYItemRenderer renderer1 = new StandardXYItemRenderer();
	    renderer1.setSeriesPaint(0,new Color(51, 102, 204));
        final NumberAxis rangeAxis1 = new NumberAxis("");
        final XYPlot subplot1 = new XYPlot(datasetDN1C1, null, rangeAxis1, renderer1);
        subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		
		
		XYDataset datasetDN1C2	= plotDelayDN1C2();
		final XYItemRenderer renderer2 = new StandardXYItemRenderer();
		renderer2.setSeriesPaint(0,new Color(220, 57, 18));
        final NumberAxis rangeAxis2 = new NumberAxis("");
        rangeAxis2.setAutoRangeIncludesZero(false);
        final XYPlot subplot2 = new XYPlot(datasetDN1C2, null, rangeAxis2, renderer2);
        subplot2.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        
        XYDataset datasetDN2C1	= plotDelayDN2C1();
    	final XYItemRenderer renderer3 = new StandardXYItemRenderer();
    	renderer3.setSeriesPaint(0,new Color(255, 153, 0));
        final NumberAxis rangeAxis3 = new NumberAxis("                Throughput (Mbps)");
        rangeAxis2.setAutoRangeIncludesZero(false);
        final XYPlot subplot3 = new XYPlot(datasetDN2C1, null, rangeAxis3, renderer3);
        subplot2.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        
		XYDataset datasetDN2C2	= plotDelayDN2C2();
    	final XYItemRenderer renderer4 = new StandardXYItemRenderer();
    	renderer4.setSeriesPaint(0,new Color(16, 150, 24));
        final NumberAxis rangeAxis4 = new NumberAxis("");
        rangeAxis2.setAutoRangeIncludesZero(false);
        final XYPlot subplot4 = new XYPlot(datasetDN2C2, null, rangeAxis4, renderer4);
        subplot2.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        
     // parent plot...
        final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new NumberAxis("Time t"));
        plot.setGap(10.0);
        
        // add the subplots...
        plot.add(subplot1, 1);
        plot.add(subplot2, 1);
        plot.add(subplot3, 1);
        plot.add(subplot4, 1);
        plot.setOrientation(PlotOrientation.VERTICAL);

        // return a new chart containing the overlaid plot...
        final ChartPanel chartPanel = new ChartPanel( new JFreeChart("Delay",
                              JFreeChart.DEFAULT_TITLE_FONT, plot, true));
        
		
		
		//XYPlot plot = (XYPlot) chart.getPlot();
		XYItemRenderer renderer = plot.getRenderer();
		
		//renderer.setSeriesPaint(0,Color.black);
		
		//renderer.setSeriesPaint(0,new Color(51, 102, 204));
		//renderer.setSeriesPaint(1,new Color(220, 57, 18));
		//renderer.setSeriesPaint(3,new Color(255, 153, 0));
		//renderer.setSeriesPaint(2,new Color(16, 150, 24));
		
		//final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        
		setContentPane(chartPanel);
		
	}
	
	private JFreeChart delayChart(XYDataset dataset){
		JFreeChart chart = ChartFactory.createXYLineChart("",
				"Time t", "Delay (ms)", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}
	
	private JFreeChart throughputChart(XYDataset dataset){
		JFreeChart chart = ChartFactory.createXYLineChart("",
				"Time t", "Throughput (Mbps)", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}
	private XYDataset plotDelay() {
		
		final XYSeries D_N1_C1 = getTimeXY(CloudWorld.D_N1_C1);
		final XYSeries D_N1_C2 = getTimeXY(CloudWorld.D_N1_C2);
		
		final XYSeries D_N2_C1 = getTimeXY(CloudWorld.D_N2_C1);
		final XYSeries D_N2_C2 = getTimeXY(CloudWorld.D_N2_C2);
		
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(D_N1_C1);
		dataset.addSeries(D_N1_C2);
		
		dataset.addSeries(D_N2_C1);
		dataset.addSeries(D_N2_C2);
		
		return dataset;
	}
	private XYDataset plotDelayDN1C1() {
		final XYSeries D_N1_C1 = getTimeXY(CloudWorld.T_N1_C1);
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(D_N1_C1);
		return dataset;
	}
	private XYDataset plotDelayDN1C2() {
		final XYSeries D_N1_C2 = getTimeXY(CloudWorld.T_N1_C2);
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(D_N1_C2);
		return dataset;
	}
	private XYDataset plotDelayDN2C1() {
		final XYSeries D_N2_C1 = getTimeXY(CloudWorld.T_N2_C1);
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(D_N2_C1);
		return dataset;
	}
	private XYDataset plotDelayDN2C2() {
		final XYSeries D_N2_C2 = getTimeXY(CloudWorld.T_N2_C2);
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(D_N2_C2);
		return dataset;
	}
	private XYDataset plotThroughput() {
		
		final XYSeries T_N1_C1 = getTimeXY(CloudWorld.T_N1_C1);
		final XYSeries T_N1_C2 = getTimeXY(CloudWorld.T_N1_C2);
		
		final XYSeries T_N2_C1 = getTimeXY(CloudWorld.T_N2_C1);
		final XYSeries T_N2_C2 = getTimeXY(CloudWorld.T_N2_C2);
		
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(T_N1_C1);
		dataset.addSeries(T_N1_C2);
		
		dataset.addSeries(T_N2_C1);
		dataset.addSeries(T_N2_C2);
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
			if(value<1250)
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
