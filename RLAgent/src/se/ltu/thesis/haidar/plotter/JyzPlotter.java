package se.ltu.thesis.haidar.plotter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.ChartScene;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapHotCold;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.colors.colormaps.ColorMapWhiteRed;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.Cylinder;
import org.jzy3d.plot3d.primitives.HistogramBar;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.Tube;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.ILegend;
import org.jzy3d.plot3d.rendering.lights.Light;
import org.jzy3d.plot3d.rendering.view.View;

import se.ltu.thesis.haidar.database.SqlStatistics;
import se.ltu.thesis.haidar.database.SqlStatistics.Reward;
import se.ltu.thesis.haidar.database.SqlStatistics.Tuple;


public class JyzPlotter extends AbstractAnalysis {
	
	private SqlStatistics mS;
	
	public JyzPlotter(){
		super();
		mS = new SqlStatistics();
		mS.connect();
	}
	
	public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new JyzPlotter());
    }

    @Override
    public void init() {

        // Define range and precision for the function to plot
        Range range = new Range(100, 200);
        int steps = 80;
        
        
        float radius = 0.05f;
        int slices = 50;
        Color mColor = new Color(0.1f,0.2f, 0.4f, 1.0f);
        List<AbstractDrawable> mL = new LinkedList<>();
        
        AbstractDrawable mDrawable;
        Cylinder mCylinder;
        HistogramBar mHistogramBar; 
        Tube mTube;
        Coord3d mCoord;
        
        //IColorMap map =  new ColorMapWhiteRed();
        IColorMap map = new ColorMapHotCold();
        ColorMapper mMap  = new ColorMapper(map, 100, 200);
        
        int test_id = 10;
        double epsilon = 0.9;
        BigDecimal mStep = new BigDecimal("0.5");
        
        Random r = new Random();
        
        for (BigDecimal mLearningRate 	= BigDecimal.ZERO;
				mLearningRate.compareTo(BigDecimal.ONE) <= 0;
				mLearningRate = mLearningRate.add(mStep)) {
			
			for (BigDecimal mDiscountFactor 	= BigDecimal.ZERO; 
					mDiscountFactor.compareTo(BigDecimal.ONE)<= 0;
					mDiscountFactor = mDiscountFactor.add(mStep)) {
				double[] reward = getReward(test_id, epsilon, mLearningRate.doubleValue(), mDiscountFactor.doubleValue());
				
				mCoord = new Coord3d(mLearningRate.doubleValue(), mDiscountFactor.doubleValue(), 0);
				//mColor = mMap.getColor(reward[0]);
					
				float x = r.nextFloat() - 0.5f;
		        float y = r.nextFloat() - 0.5f;
		        float z = r.nextFloat() - 0.5f;
		        float a = 1.0f;
		        //mColor = new Color(x, y, z, a);
		        
				
				
				
				//mDrawable = new Cylinder();
				//((Cylinder) mDrawable).setData(mCoord, (float) reward[0], radius, slices, 1, mColor);
				
				
				//mDrawable = new HistogramBar(4);
				//((HistogramBar) mDrawable).setData(mCoord, (float) reward[0], radius, mColor);
				
				
				mDrawable = new Tube(mCoord, radius, (float) reward[0], 4, 10,  new Color(x, y, z));
				//((Tube)mDrawable).setFaceDisplayed(true);
				((Tube)mDrawable).setWireframeDisplayed(true);
				((Tube)mDrawable).setWireframeColor(Color.WHITE);
				((Tube)mDrawable).setLegendDisplayed(false);
				((Tube)mDrawable).setFaceDisplayed(true);
				
				//((Tube)mDrawable).dispose();
				mL.add(mDrawable);
				}
			}
        
        
        
        /*
        final Cylinder cylinder2 = new Cylinder();
        Coord3d position2 = new Coord3d(2,2,2);
        Color color2 = new Color(0.6f,0.2f, 0.4f, 0.5f);
        cylinder2.setData(position2, 0.4f, 0.8f, 100, 1,  color2); 
        
        
        
        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(true);
        */
        // Create a chart
        mS.disconnect();
        Light mLight = new Light();
        mLight.setEnabled(true);
        mLight.setAmbiantColor(new Color(0.3f, 0.5f, 0.3f));
        mLight.setPosition(new Coord3d(1, 1, 100));
        
        
        AWTChartComponentFactory.SCREENSHOT_FOLDER = "./";
        
        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
        
        chart.getAxeLayout().setMainColor(Color.WHITE);
        chart.getView().setBackgroundColor(Color.BLACK);
        
        
        ChartScene mChart =  chart.getScene();
        //mChart.add(mLight);
        mChart.add(mL);
        //chart.setAxeDisplayed(true);
    }
	private double[] getReward(int test_id, double epsilon, double learning, double discount){
		Tuple mTuple 		= mS.getTuple(test_id, epsilon, learning, discount);
		int tuple_id 		= mTuple.getID();
		System.out.println(mTuple.getName());
		Reward mReward 		= mS.getReward(test_id, tuple_id);
		double[] reward 	= mReward.getArray();
		System.out.println(mReward.toString());
		return reward;
	}
}
