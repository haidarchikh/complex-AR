package se.ltu.thesis.haidar.plotter;

import java.math.BigDecimal;
import java.util.Random;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import se.ltu.thesis.haidar.database.SqlStatistics;
import se.ltu.thesis.haidar.database.SqlStatistics.Reward;
import se.ltu.thesis.haidar.database.SqlStatistics.Tuple;


public class ScatterDemo extends AbstractAnalysis{
	private SqlStatistics mS;
	public static void main(String[] args) throws Exception {
		AnalysisLauncher.open(new ScatterDemo());
		
	}
	public ScatterDemo(){
		super();
		mS = new SqlStatistics();
		mS.connect();
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
	
	public void init(){
        int size = 9;
        float x;
        float y;
        float z;
        float a;
        
        
        Coord3d[] points = new Coord3d[size];
        Color[]   colors = new Color[size];
        
        int test_id = 9;
        double epsilon = 0.9;
        BigDecimal mStep = new BigDecimal("0.5");
        Random r = new Random();
        a = 0.25f;
        int pointNum = 0;
        for (BigDecimal mLearningRate 	= BigDecimal.ZERO;
				mLearningRate.compareTo(BigDecimal.ONE) <= 0;
				mLearningRate = mLearningRate.add(mStep)) {
			
			for (BigDecimal mDiscountFactor 	= BigDecimal.ZERO; 
					mDiscountFactor.compareTo(BigDecimal.ONE)<= 0;
					mDiscountFactor = mDiscountFactor.add(mStep)) {
				double[] reward = getReward(test_id, epsilon, mLearningRate.doubleValue(), mDiscountFactor.doubleValue());
				points[pointNum] = new Coord3d(mLearningRate.doubleValue(), mDiscountFactor.doubleValue(), reward[0]);
				
				
				x = r.nextFloat() - 0.5f;
	            y = r.nextFloat() - 0.5f;
	            z = r.nextFloat() - 0.5f;
	            colors[pointNum] = new Color(x, y, z, a);
	            System.out.println(pointNum);
	            pointNum++;
				}
			}
        
        
        /*
        r = new Random();
        r.setSeed(0);
        
        for(int i=0; i<size; i++){
            x = r.nextFloat() - 0.5f;
            y = r.nextFloat() - 0.5f;
            z = r.nextFloat() - 0.5f;
            points[i] = new Coord3d(x, y, z);
            a = 0.25f;
            colors[i] = new Color(x, y, z, a);
        }
        */
        mS.disconnect();
        Scatter scatter = new Scatter(points, colors,5.0f);
        chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
        chart.getScene().add(scatter);
        
    }
}