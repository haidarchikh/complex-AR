package se.ltu.d7031e.CAR;

public class CAFactory {
	private static ComplexActivity brushingTeeth 	= null; 
	private static ComplexActivity preparingCoffee 	= null;
	private static Situation atHome = null;
	
	private CAFactory(){}
	
	public static ComplexActivity brushingTeeth(){
		if(brushingTeeth == null){
			brushingTeeth = new ComplexActivity(Consts.CA_BRUSHING_TEETH);
			/*
			 * Add all activities to mActivities and mContext then add again to 
			 * special cases (core, start , end)
			 * */

			AtomicActivity standing             = new AtomicActivity(Consts.A_STANDING,                  0.1);
			AtomicActivity openBathroomCupboard = new AtomicActivity(Consts.A_OPEN_BATHROOM_CUPBOARD,    0.1);
			AtomicActivity takeToothbrush       = new AtomicActivity(Consts.A_TAKE_TOOTHBRUSH,           0.1);
			AtomicActivity takeToothpaste       = new AtomicActivity(Consts.A_TAKE_TOOTHPASTE,           0.1);
			AtomicActivity brushingHandMove     = new AtomicActivity(Consts.A_TOOTH_BRUSHING_HAND_MOVE,  0.6);
			
			brushingTeeth.addActivity        (standing             );
			brushingTeeth.addActivity        (openBathroomCupboard );
			brushingTeeth.addActivity        (takeToothbrush       );
			brushingTeeth.addActivity        (takeToothpaste       );
			brushingTeeth.addActivity        (brushingHandMove     );
			brushingTeeth.addStartActivity   (standing             );
			brushingTeeth.addEndActivity     (brushingHandMove     );
			brushingTeeth.addCoreActivity    (standing             );
			brushingTeeth.addActivity        (brushingHandMove     );
			
			ContextAttribute atBathroom      = new ContextAttribute(Consts.C_AT_BATHROOM,        0.6);
			ContextAttribute bathroomLightON = new ContextAttribute(Consts.C_BATHROOM_LIGHT_ON,	 0.4);		
			
			brushingTeeth.addContext         (atBathroom           );
			brushingTeeth.addContext         (bathroomLightON      );
			brushingTeeth.addCoreContext     (atBathroom           );
	
			brushingTeeth.setmThreshold(0.6);
			brushingTeeth.setmLifespan(2);
			
			return brushingTeeth;
		}
		return brushingTeeth;
	}
	public static ComplexActivity preparingCoffee(){
		if(preparingCoffee == null){

			preparingCoffee                 = new ComplexActivity(Consts.CA_PREPARING_COFFEE   );			
			
			AtomicActivity standing         = new AtomicActivity(Consts.A_STANDING,        0.25);
			AtomicActivity walking          = new AtomicActivity(Consts.A_WALKING,         0.25);
			AtomicActivity coffeMug         = new AtomicActivity(Consts.A_COFFEE_MUG,      0.25);
			AtomicActivity coffeeMachine    = new AtomicActivity(Consts.A_COFFEE_MACHINE,  0.25);
			
			preparingCoffee.addActivity         (standing      );
			preparingCoffee.addActivity         (walking       );
			preparingCoffee.addActivity         (coffeMug      );
			preparingCoffee.addActivity         (coffeeMachine );
			preparingCoffee.addStartActivity    (standing      );
			preparingCoffee.addEndActivity      (coffeeMachine );
			preparingCoffee.addCoreActivity     (standing      );
			//preparingCoffee.addCoreActivity     (walking       );
			preparingCoffee.addCoreActivity     (coffeMug      );
			
			ContextAttribute atKitchen      = new ContextAttribute(Consts.C_AT_KITCHEN,			0.6);
			ContextAttribute kitchenLightON = new ContextAttribute(Consts.C_KITCHEN_LIGHT_ON,	0.4);
			
			preparingCoffee.addContext         (kitchenLightON );
			preparingCoffee.addContext         (atKitchen      );
			preparingCoffee.addCoreContext     (atKitchen      );
			preparingCoffee.setmThreshold(0.6);
			preparingCoffee.setmLifespan(300);
						
			return preparingCoffee;
		}
		return preparingCoffee;
	}
	public static ComplexActivity gettingOut(){
		return null;
	}
	public static Situation atHome(){
		if(atHome == null){
			atHome = new Situation(Consts.S_AT_HOME);
			atHome.addComplexActivity(CAFactory.brushingTeeth());
			atHome.addComplexActivity(CAFactory.preparingCoffee());
			return atHome;
		}
		return atHome;
	}
}