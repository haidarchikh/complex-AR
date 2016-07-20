import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;


public class BaselsActRecToArff {

	public static final String dir = "/Users/baselkikhia/Desktop/Ian_ACC/New_collected_data/data_by_positions/";//Common directory
	
	public static final String files[] = {"lying.dat","running.dat","sitting.dat","stairs_down.dat","stairs_up.dat",
		"standing.dat","walking.dat"};//Input files
	
	public static final String distinctLabels[] = {
		"lying",//0
		"running",//1
		"sitting",//2
		"stairs_down",//3
		"stairs_up",//4
		"standing",//5
		"walking",//6
	};
	
	//public static final int labels[] = {0,0,2,1,5,1,7,1,7,6,2,6,2,4,3,4,3,4,2,4};
	
	public static final String labels[] = {
		"lying","running","sitting","stairs_down","stairs_up",
		"standing","walking"
	};
	
	public static final String positions[] = {"Chest","RightHip","RightWrist","RightThigh","RightFoot", "LowerBack"};  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try{
		
			BufferedWriter outFiles[] = new BufferedWriter[positions.length];
			
			for(int i=0;i<outFiles.length;i++){
				outFiles[i] = new BufferedWriter(new FileWriter(dir+positions[i]+".arff"));
				//Relation
				outFiles[i].write("@RELATION sensor"+positions[i]+"\n\n");
				outFiles[i].write("@ATTRIBUTE Timestamp NUMERIC\n");				
				outFiles[i].write("@ATTRIBUTE accX NUMERIC\n");				
				outFiles[i].write("@ATTRIBUTE accY NUMERIC\n");
				outFiles[i].write("@ATTRIBUTE accZ NUMERIC\n");
				outFiles[i].write("@ATTRIBUTE accTotal NUMERIC\n");				
				
				outFiles[i].write("@ATTRIBUTE label {"+distinctLabels[0]);
				for(int j=1;j<distinctLabels.length;j++){
					outFiles[i].write(","+distinctLabels[j]);
				}
				outFiles[i].write("}\n\n");
				
				outFiles[i].write("@DATA\n");				
			}			
			
			for (int i=0;i<files.length;i++){//For every input file
				//Open the file
				BufferedReader br = new BufferedReader(new FileReader(dir+files[i]));
				
				//Read line by line
				br.readLine();
				br.readLine();
				br.readLine();
				
				String s = br.readLine();
				while(s!=null){
					String elements[] = s.split("\\t");
					for (int j=0;j<positions.length;j++){
						double accX = Double.parseDouble(elements[4*j+1]);
						double accY = Double.parseDouble(elements[4*j+2]);
						double accZ = Double.parseDouble(elements[4*j+3]);
						
						double accTotal = Math.sqrt(accX*accX+accY*accY+accZ*accZ);
						
						outFiles[j].write(elements[4*j]+","+elements[4*j+1]+","+elements[4*j+2]+","+elements[4*j+3]+","+accTotal+","+labels[i]+"\n");
						outFiles[j].flush();						
					}
					
					//TODO For a single file - put it here
					
					s = br.readLine();
				}
			}
			
			for (int j=0;j<positions.length;j++){
				outFiles[j].close();
			}
			
		}catch(Throwable e){
			e.printStackTrace();
		}
				
	}

}
