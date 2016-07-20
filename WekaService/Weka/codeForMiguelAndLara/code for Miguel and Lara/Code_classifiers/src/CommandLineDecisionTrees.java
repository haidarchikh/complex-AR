import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;


public class CommandLineDecisionTrees {

	static final String fname = "C:\\Andrey\\Tools\\Weka-3-7\\allResult.bat";
	
	static final String cmdStart = "java -cp \"weka.jar\" ";
	
	static String classifiers[] = {"weka.classifiers.trees.J48"};
	
	static String parameters[][] = {{"-C 0.25 -M 2"}};
	
	static String inputnames[] = 
		{"C:\\Users\\andboy\\Desktop\\ActRec\\ChestFeaturesNoTimestamp.arff"};
	
	static final String SEP = "\t";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try{
			
			
			
			//Step 1. Compose the bat file
		    FileWriter fstream = new FileWriter(fname, false);
		    BufferedWriter out = new BufferedWriter(fstream);
		    out.write("cd C:\\Andrey\\Tools\\Weka-3-7\\\n"); //Just in case
		    //Now composing Weka parameters.
		    for (int k=0;k<inputnames.length;k++){
			    for (int i=0;i<classifiers.length;i++){
				    for (int j=0;j<parameters[i].length;j++){
					    out.write(cmdStart + classifiers[i]+" "+parameters[i][j]+
					    		" -t "+inputnames[k]+"\n");
				    }
			    }		    	
		    }
		    out.close();
			
			//Step 2. Run it.
			
			String command = "cmd /c "+fname;
			Process tr = Runtime.getRuntime().exec(command);
			BufferedReader rd = new BufferedReader( new InputStreamReader( tr.getInputStream() ) );
			String s = rd.readLine();
			/*while (null!=s){
			  System.out.println( s );				
			  s = rd.readLine();
			}*/
						
		    for (int k=0;k<inputnames.length;k++){
			    for (int i=0;i<classifiers.length;i++){
				    for (int j=0;j<parameters[i].length;j++){
				    	while (!s.contains("Correctly Classified Instances")){
				    		s = rd.readLine();
				    	}
			    		s = rd.readLine();				    	
				    	//Skip first - that was for training set
				    	while (!s.contains("Correctly Classified Instances")){
				    		s = rd.readLine();
				    	}				    		    	
				    	String[] spl = s.split("\\s");				    	
				    	s = spl[spl.length-2];
						System.out.println(inputnames[k]+SEP+
								classifiers[i]+SEP+parameters[j][i]+SEP+s);				    	
				    }
			    }
		    }
					    
		}catch(Throwable e){
			e.printStackTrace();
		}
		
	}

}
