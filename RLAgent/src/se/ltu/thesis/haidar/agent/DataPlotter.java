package se.ltu.thesis.haidar.agent;

import java.awt.BasicStroke;
import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demo showing a dataset created using the {@link XYSeriesCollection}
 * class.
 *
 */
public class DataPlotter extends ApplicationFrame {

	/**
	 * A demonstration application showing an XY series containing a null value.
	 *
	 * @param title
	 *            the frame title.
	 */
	
	public DataPlotter(final String title) {

		super(title);
		final JFreeChart chart = ChartFactory.createXYLineChart("Throughput",
				"Time epoch", "Throughput (MB)", createDatasetThroughput(),
				PlotOrientation.VERTICAL, true, true, false);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
		/*
		 * super(title); String chartTitle = "OOOOO"; JFreeChart xylineChart =
		 * ChartFactory.createXYLineChart( chartTitle , "Category" , "Score" ,
		 * createDataset() , PlotOrientation.VERTICAL , true , true , false);
		 * 
		 * ChartPanel chartPanel = new ChartPanel( xylineChart );
		 * chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
		 * final XYPlot plot = xylineChart.getXYPlot( ); XYLineAndShapeRenderer
		 * renderer = new XYLineAndShapeRenderer( ); renderer.setSeriesPaint( 0
		 * , Color.RED ); renderer.setSeriesPaint( 1 , Color.GREEN );
		 * //renderer.setSeriesPaint( 2 , Color.YELLOW );
		 * renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
		 * renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
		 * //renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
		 * plot.setRenderer( renderer ); setContentPane( chartPanel );
		 */
	}

	private XYDataset createDatasetThroughput() {
		final XYSeries N1_C1 = new XYSeries("N1C1");
		// series.add(1.0, 500.2);
		DataSetGenerator mG_N1_C1 = new DataSetGenerator(true);
		mG_N1_C1.addDataPlan(40, 20, 10, 10);
		mG_N1_C1.addDataPlan(60, 50, 10, 10);

		// mG.addDataPlan(200, 300, 20, 15);
		// mG.addDataPlan(100, 200, 2 , 1 );
		int counter = 0;
		while (true) {
			int res = mG_N1_C1.getSample();
			// System.out.println(res);
			if (res == -1) {
				break;
			}
			counter++;
			N1_C1.add(counter, res);
		}
		final XYSeries N1_C2 = new XYSeries("N1C2");
		DataSetGenerator mG_N1_C2 = new DataSetGenerator(true);
		mG_N1_C2.addDataPlan(50, 50, 10, 10);
		mG_N1_C2.addDataPlan(50, 20, 10, 10);

		// mG.addDataPlan(200, 300, 20, 15);
		// mG.addDataPlan(100, 200, 2 , 1 );
		int counter2 = 0;
		while (true) {
			int res = mG_N1_C2.getSample();
			// System.out.println(res);
			if (res == -1) {
				break;
			}
			counter2++;
			N1_C2.add(counter2, res);
		}

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(N1_C1);
		dataset.addSeries(N1_C2);
		return dataset;
	}

	private XYDataset createDatasetDelay() {
		final XYSeries N1_C1 = new XYSeries("N1C1");
		// series.add(1.0, 500.2);
		DataSetGenerator mG_N1_C1 = new DataSetGenerator(true);
		mG_N1_C1.addDataPlan(40 , 40, 10 , 10);
		mG_N1_C1.addDataPlan(60, 300, 10, 10);

		int counter = 0;
		while (true) {
			int res = mG_N1_C1.getSample();
			// System.out.println(res);
			if (res == -1) {
				break;
			}
			counter++;
			N1_C1.add(counter, res);
		}
		final XYSeries N1_C2 = new XYSeries("N1C2");
		DataSetGenerator mG_N1_C2 = new DataSetGenerator(true);
		mG_N1_C2.addDataPlan(50, 200, 10, 10);
		mG_N1_C2.addDataPlan(50, 50 , 10, 10);

		int counter2 = 0;
		while (true) {
			int res = mG_N1_C2.getSample();
			// System.out.println(res);
			if (res == -1) {
				break;
			}
			counter2++;
			N1_C2.add(counter2, res);
		}
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(N1_C1);
		dataset.addSeries(N1_C2);
		return dataset;
	}

	// ****************************************************************************
	// * JFREECHART DEVELOPER GUIDE *
	// * The JFreeChart Developer Guide, written by David Gilbert, is available
	// *
	// * to purchase from Object Refinery Limited: *
	// * *
	// * http://www.object-refinery.com/jfreechart/guide.html *
	// * *
	// * Sales are used to provide funding for the JFreeChart project - please *
	// * support us so that we can continue developing free software. *
	// ****************************************************************************

	/**
	 * Starting point for the demonstration application.
	 *
	 * @param args
	 *            ignored.
	 */
	public static void main(final String[] args) {

		final DataPlotter demo = new DataPlotter("XY Series Demo");
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

	}

}
