package se.ltu.d7013e.rabbitMQ;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.json.JSONObject;

public class Mockup extends Thread {
	
	private JSONObject lying                  = new JSONObject();
	private JSONObject running                = new JSONObject();
	private JSONObject sitting                = new JSONObject();
	private JSONObject standing               = new JSONObject();
	private JSONObject walking                = new JSONObject();
	private JSONObject toothBrushHandMove     = new JSONObject();
	private JSONObject openBathRoomCupboard   = new JSONObject();
	private JSONObject takeToothbrush         = new JSONObject();
	private JSONObject takeToothpaste         = new JSONObject();
	private JSONObject coffeeMug              = new JSONObject();
	private JSONObject coffeeMachine          = new JSONObject();
	
	private JSONObject atBathroom             = new JSONObject();
	private JSONObject bathroomLightOn        = new JSONObject();
	private JSONObject atKitchen              = new JSONObject();
	private JSONObject kitchenLightON         = new JSONObject();
	
	private BlockingQueue<JSONObject> mOutQ = new ArrayBlockingQueue<>(100);
	
	public Mockup(){
		lying                 .put(Consts.ACTIVITY, Consts.A_LYING);
		running               .put(Consts.ACTIVITY, Consts.A_RUNNING);
		sitting               .put(Consts.ACTIVITY, Consts.A_SITTING);
		standing              .put(Consts.ACTIVITY, Consts.A_STANDING);
		walking               .put(Consts.ACTIVITY, Consts.A_WALKING);
		toothBrushHandMove    .put(Consts.ACTIVITY, Consts.A_TOOTH_BRUSHING_HAND_MOVE);
		openBathRoomCupboard  .put(Consts.ACTIVITY, Consts.A_OPEN_BATHROOM_CUPBOARD);
		takeToothbrush        .put(Consts.ACTIVITY, Consts.A_TAKE_TOOTHBRUSH);
		takeToothpaste        .put(Consts.ACTIVITY, Consts.A_TAKE_TOOTHPASTE);
		coffeeMug             .put(Consts.ACTIVITY, Consts.A_COFFEE_MUG);
		coffeeMachine         .put(Consts.ACTIVITY, Consts.A_COFFEE_MACHINE);
		atBathroom            .put(Consts.CONTEXT , Consts.C_AT_BATHROOM);
		bathroomLightOn       .put(Consts.CONTEXT , Consts.C_BATHROOM_LIGHT_ON);
		atKitchen             .put(Consts.CONTEXT , Consts.C_AT_KITCHEN);
		kitchenLightON        .put(Consts.CONTEXT , Consts.C_KITCHEN_LIGHT_ON);
	}
	
	private boolean runningb = false;
	@Override
	public void run() {
		while(runningb){
			SendBrushingTeeth();
			SendPreparingCoffee();
			runningb = false;
		}
	}
	private void SendPreparingCoffee(){
		mOutQ.add(standing);
		//mOutQ.add(walking);
		mOutQ.add(coffeeMug);
		mOutQ.add(coffeeMachine);
		mOutQ.add(atKitchen);
		mOutQ.add(kitchenLightON);
	}
	private void SendBrushingTeeth(){
		mOutQ.add(standing);
		
		mOutQ.add(takeToothbrush);
		mOutQ.add(bathroomLightOn);
		mOutQ.add(takeToothpaste);
		mOutQ.add(openBathRoomCupboard);
		mOutQ.add(atBathroom);
		mOutQ.add(toothBrushHandMove);
	}
	public void setRunning(boolean running){
		this.runningb = running;
	}
	public BlockingQueue<JSONObject> getmOutQ() {
		return mOutQ;
	}
}