package se.ltu.d7031e.CAR;

public class CAFactory {
	private static ComplexActivity brushingTeeth 	= null; 
	private static ComplexActivity preparingCoffee 	= null;
	private static Situation atHome = null;
	private ComplexActivity mCA;

	public static ComplexActivity brushingTeeth(){
		if(brushingTeeth == null){
			CAFactory mFactory = new CAFactory(Consts.CA_BRUSHING_TEETH, 2, 0.6);
			
			mFactory.addAtomicActivity(Consts.A_STANDING,                  0.1, true  , false , true  );
			mFactory.addAtomicActivity(Consts.A_OPEN_BATHROOM_CUPBOARD,    0.1, false , false , false );
			mFactory.addAtomicActivity(Consts.A_TAKE_TOOTHBRUSH,           0.1, false , false , false );
			mFactory.addAtomicActivity(Consts.A_TAKE_TOOTHPASTE,           0.1, false , false , false );
			mFactory.addAtomicActivity(Consts.A_TOOTH_BRUSHING_HAND_MOVE,  0.6, false , true  , true  );
			
			mFactory.addContextAttribute(Consts.C_AT_BATHROOM       , 0.6, false, false , false);
			mFactory.addContextAttribute(Consts.C_BATHROOM_LIGHT_ON , 0.6, false, false , true);
			
			brushingTeeth = mFactory.GetmCA();
			return brushingTeeth;
		}
		return brushingTeeth;
	}
	public static ComplexActivity preparingCoffee(){
		if(preparingCoffee == null){

			CAFactory mFactory = new CAFactory(Consts.CA_PREPARING_COFFEE, 300, 0.6);
			
			mFactory.addAtomicActivity(Consts.A_STANDING,        0.1, true  , false , true  );
			mFactory.addAtomicActivity(Consts.A_WALKING,         0.1, false , false , false );
			mFactory.addAtomicActivity(Consts.A_COFFEE_MUG,      0.1, false , false , true  );
			mFactory.addAtomicActivity(Consts.A_COFFEE_MACHINE,  0.1, false , false , false );
			
			mFactory.addContextAttribute(Consts.C_AT_KITCHEN       , 0.6, false, false , true);
			mFactory.addContextAttribute(Consts.C_KITCHEN_LIGHT_ON , 0.6, false, false , true);
			
			preparingCoffee = mFactory.GetmCA();
			return preparingCoffee;
		}
		return preparingCoffee;
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
	private CAFactory(String mName, int mLifespan , double mThreshold){
		mCA = new ComplexActivity(mName);
		mCA.setmLifespan(mLifespan);
		mCA.setmThreshold(mThreshold);
	}
	private void addAtomicActivity(String mName, double mWeight,
			boolean isStartAtomicActivity, boolean isEndAtomicActivity, boolean isCoreAtomicActivity){
		
		AtomicActivity mActivity = new AtomicActivity(mName, mWeight);
		mCA.addActivity(mActivity);
		
		if(isStartAtomicActivity){mCA.addStartActivity(mActivity);}
		if(isEndAtomicActivity  ){mCA.addEndActivity  (mActivity);}
		if(isCoreAtomicActivity) {mCA.addCoreActivity (mActivity);}
		if(isStartAtomicActivity&&isEndAtomicActivity){System.out.println("Can't be start and end at the same time");}
	}
	private void addContextAttribute(String mName, double mWeight,
			boolean isStartContextAttribute, boolean isEndContextAttribute, boolean isCoreContextAttribute){
		ContextAttribute mContext = new ContextAttribute(mName, mWeight);
		mCA.addContext(mContext);
		
		if(isStartContextAttribute){mCA.addStartContext(mContext);}
		if(isEndContextAttribute  ){mCA.addEndContext  (mContext);}
		if(isCoreContextAttribute) {mCA.addCoreContext (mContext);}
		if(isStartContextAttribute&&isEndContextAttribute){System.out.println("Can't be start and end at the same time");}
	}
	private ComplexActivity GetmCA(){
		return mCA;
	}
}