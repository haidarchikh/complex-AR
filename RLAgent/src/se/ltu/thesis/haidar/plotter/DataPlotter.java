package se.ltu.thesis.haidar.plotter;



import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.math3.distribution.ParetoDistribution;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import cern.jet.random.Normal;
import se.ltu.thesis.haidar.datagenerator.DataGenerator;
import se.ltu.thesis.haidar.datagenerator.DataGenerator.GaussianGenerator;
import se.ltu.thesis.haidar.datagenerator.DataGenerator.ParetoGenerator;

/**
 * A simple demo showing a dataset created using the {@link XYSeriesCollection}
 * class.
 *
 */
@SuppressWarnings("serial")
public class DataPlotter extends ApplicationFrame {

	/**
	 * A demonstration application showing an XY series containing a null value.
	 *
	 * @param title
	 *            the frame title.
	 */
	
	public DataPlotter(final String title) {

		super(title);
		
		XYDataset dataset = plotFreqBoth();
		JFreeChart chart = freqChart(dataset);
		
		final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
		

        GradientPaint gp1 = new GradientPaint(1.0f, 2.0f, Color.black, 3.0f,
                4.0f, Color.BLACK);
		
        XYPlot mPlot = chart.getXYPlot();
        mPlot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

        XYTextAnnotation annotation1 = new XYTextAnnotation(" μ = 3, σ\u00B2  = 1.5 ", 2.0, 0.6);
        annotation1.setFont(new Font("SansSerif", Font.PLAIN, 11));
        annotation1.setBackgroundPaint(new Color(255 ,120 ,120));
        annotation1.setPaint(gp1);
        mPlot.addAnnotation(annotation1);
        
        /*
        XYTextAnnotation annotation2 = new XYTextAnnotation(" \u03B1 = 4, Scale = 50 ", 70, 0.035);
        annotation2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        annotation2.setBackgroundPaint(new Color(130 ,130 ,255));
        annotation2.setPaint(gp1);
        mPlot.addAnnotation(annotation2);
		*/
		
		
		
		/*
		
		
		XYPlot plot = (XYPlot) chart.getPlot();
        final IntervalMarker target = new IntervalMarker(500.0, 270.0);
        target.setLabel("Target Range");
        target.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));
        target.setLabelAnchor(RectangleAnchor.LEFT);
        target.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
        target.setPaint(new Color(222, 222, 255, 128));
        plot.addRangeMarker(target, Layer.BACKGROUND);
		*/
	}

	// To create a delay chart, you can change the distribution used
	private JFreeChart delayChart(XYDataset dataset){
		JFreeChart chart = ChartFactory.createXYLineChart("Pareto distributed delay",
				"Time epoch", "Delay (ms)", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}
	// To create a throughput chart, you can change the distribution used
	private JFreeChart throughputChart(XYDataset dataset){
		JFreeChart chart = ChartFactory.createXYLineChart("Throughput",
				"Time epoch", "Throughput (Mbps)", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}
	// To create a frequency domain chart, you can change the distribution used
	private JFreeChart freqChart(XYDataset dataset){
		JFreeChart chart = ChartFactory.createXYLineChart("Cumulative Distribution Function (CDF)",
				"x", "CDF", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}
	
	
	// Add a time domain data
	private XYDataset plotTimeGaussian() {
		DataGenerator mG_N1_C1 = new DataGenerator();
		mG_N1_C1.addGaussianPlan(400 , 50, 5 , 1);
		mG_N1_C1.addGaussianPlan(600, 20, 5, 1);
		final XYSeries N1_C1 = getTimeXY("N1C1", mG_N1_C1);
		
		
		DataGenerator mG_N1_C2 = new DataGenerator();
		mG_N1_C2.addGaussianPlan(600, 20, 5, 1);
		mG_N1_C2.addGaussianPlan(400, 50 , 5, 1);
		final XYSeries N1_C2 = getTimeXY("N1C2", mG_N1_C2);
		
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(N1_C1);
		dataset.addSeries(N1_C2);
		return dataset;
	}
	// Add a time domain data
	private XYDataset plotTimePareto() {
		
		DataGenerator mG_N1_C1 = new DataGenerator();
		mG_N1_C1.addParetoPlan(400 , 40, 5 , 10);
		mG_N1_C1.addParetoPlan(600, 300, 5, 10);
		final XYSeries N1_C1 = getTimeXY("N1C1",mG_N1_C1);
		
		DataGenerator mG_N1_C2 = new DataGenerator();
		mG_N1_C2.addParetoPlan(500, 200, 5, 10);
		mG_N1_C2.addParetoPlan(500, 50 , 5, 10);
		final XYSeries N1_C2 = getTimeXY("N1C2",mG_N1_C2);
		
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(N1_C1);
		dataset.addSeries(N1_C2);
		return dataset;
	}
	
	private XYDataset plotFreqBoth() {
		
		int sampleCount_G = 1000000;
		double mMean 	= 50;
		double variance = 11;
		double roundTo_G = 1;
		DataGenerator mG_G = new DataGenerator();
		mG_G.addGaussianPlan(sampleCount_G , mMean, variance , roundTo_G);
		
		final XYSeries mXY_G = getFreqXY("Gaussian CDF", mG_G);
		
		
		int sampleCount_P = 1000000;
		double mScale 	= 50;
		double mShape	= 4;
		double roundTo_P 	= 1;
		DataGenerator mG_P = new DataGenerator();
		mG_P.addParetoPlan(sampleCount_P , mScale, mShape, roundTo_P);
		
		final XYSeries mXY_P = getFreqXY("Pareto distribution", mG_P);
		
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(mXY_G);
		dataset.addSeries(mXY_P);
		return dataset;
	}
	
	private XYSeries getTimeXY(String mName,DataGenerator mG ){
		final XYSeries mXY = new XYSeries(mName);
		double[] mData = mG.getData();
		for(int i =0 ; i< mData.length; i++){
			mXY.add(i, mData[i]);
		}
		return mXY;
	}
	private XYSeries getFreqXY(String mName,DataGenerator mG){
		final XYSeries mXY = new XYSeries(mName);
		TreeMap<Double, Integer> mStatistics  = mG.getStatistics();
		Iterator<Entry<Double, Integer>> mIterator = mStatistics.entrySet()
				.iterator();
		while (mIterator.hasNext()) {
			Entry<Double, Integer> mEntry = mIterator.next();
			double sample = mEntry.getKey();
			
			// each generator has a different method to get PDF
			if(mG.getGenerator() instanceof ParetoGenerator){
				ParetoGenerator m = (ParetoGenerator) mG.getGenerator();
				ParetoDistribution mPareto = m.getGenerator();
				double pro = mPareto.density(sample);
				if(sample< 1000)
				mXY.add(sample, pro);	
			}
			if(mG.getGenerator() instanceof GaussianGenerator){
				GaussianGenerator m = (GaussianGenerator) mG.getGenerator();
				Normal mNormal = m.getGenerator();
				double pro = mNormal.pdf(sample);
				if(sample< 110)
				mXY.add(sample, pro);
			}
		}
		return mXY;
	}

	public static void main(final String[] args) {

		final DataPlotter demo = new DataPlotter("XY Series Demo");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

	}
}