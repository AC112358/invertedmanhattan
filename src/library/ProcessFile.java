package library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ProcessFile{
	private File file;
	protected String name;
	private ArrayList<Integer> chrs;
	private ArrayList<Integer> posns;
	private ArrayList<String> rsIds;
	private ArrayList<Double> pVals;
	private int maxPosn;
	private int index;
	private int tenPow;
	private int minPValIndex;
	protected static double[] chromosomeLengths = {0, 248956422, 242193529, 198295559, 190214555, 
    		181538259, 170805979, 159345973, 145138636, 138394717, 
    		133797422, 135086622, 133275309, 114364328, 107043718, 
    		101991189, 90338345, 83257441, 80373285, 58617616, 64444167,
    		46709983, 50818468, 156040895, 57227415};// for X & Y
	protected static float[] fullLengths;
	private static final float DIV_BY = (float)Math.pow(10, 8);
	
	
  public ProcessFile(){
	    index = 0;
	    minPValIndex = 0;
	    maxPosn = 0;
	    chrs = new ArrayList<Integer>();
	    posns = new ArrayList<Integer>();
	    rsIds = new ArrayList<String>();
	    pVals = new ArrayList<Double>();
	    file = null;
	    fullLengths = new float[25]; //23 if no X & Y
	    fullLengths[0] = 0;
	    for (int i = 1; i < fullLengths.length; i++){
	    	fullLengths[i] = (float) (fullLengths[i-1] + chromosomeLengths[i]/DIV_BY);
	    }
}
 
	public ProcessFile(String fileName, float reject, float prob) throws IOException{
		this();
		name = fileName;
		file = new File(fileName);
		extractInfo(reject, prob);
	}
	
	public ProcessFile(String fileName) throws IOException{
		this(fileName, 0, 0);
	}

	private boolean rejectP(double pVal, float reject, float prob){
		//reject < 0 ==> test if p val < |reject|
		if (prob < 0){
			prob = 0;
		}
		if (prob > 1){
			prob = 1;
		}
		if (reject == 0){
			return false;
		}
		else if (reject > 0){
			if (pVal > reject){
				return Math.random() < prob;
			}else{
				return false;
			}
		}
		else{
			reject *= -1;
			if (pVal < reject){
				return Math.random() < prob;	
			}else{
				return false;	
			}
		}
	}
	
	public void extractInfo(float reject, float prob) throws IOException{
		//System.out.println("extracting info");
		boolean addToLists = true;
		String[] tokens;
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = in.readLine();
		int numRejected = 0;
		while (line != null){
			addToLists = true;
			tokens = line.split(" ");
			if (tokens.length < 4){
				addToLists = false;
			}
			if (addToLists){
				for (int i = 0; i < 2; i++){
					try{
						Integer.parseInt(tokens[i]);
					}catch(NumberFormatException e){
						addToLists = false;
					}
				}
				try{
					Double.parseDouble(tokens[3]);
				}catch(NumberFormatException e){
					addToLists = false;
				}
				
				double pVal = Double.parseDouble(tokens[3]);
				if (addToLists){
					addToLists = !rejectP(pVal, reject, prob);
					if (!addToLists){
						numRejected++;
					}
							
				}
				
				if (addToLists){
					chrs.add(Integer.parseInt(tokens[0]));
					posns.add(Integer.parseInt(tokens[1]));
					pVals.add(pVal);
					rsIds.add(tokens[2]);
					if (posns.get(posns.size()-1) > maxPosn){
						maxPosn = posns.get(posns.size()-1);
					}
					if (pVals.get(pVals.size()-1) < pVals.get(minPValIndex)){
						minPValIndex = posns.size()-1;
					}
				}
			}
			line = in.readLine();
		}
		//System.out.println(numRejected + " rejected");
		in.close();
		tenPow = (int)(Math.ceil(Math.log(maxPosn)/Math.log(10)));
	}
	
	
	public boolean hasNext(){
		return index < rsIds.size();
	}
	
	public void advanceIndex(){
		index++;
	}
	
	public String getRsId(){
		return rsIds.get(index);
	}
	
	public double getPVal(){
		return pVals.get(index);
	}
	
	public int getChromosome(){
		return chrs.get(index);
	}
	
	public int getPosition(){
		return posns.get(index);
	}


  private float logP(int i){
    return (float)(-Math.log(pVals.get(i))/Math.log(10));
  }
  public float getLogP(){
    return logP(index);
  }
  
  
  public float getXPosn(){
    return (float)fullLengths[getChromosome() - 1] + getPosition()/DIV_BY;//(float)((getChromosome() * Math.pow(10, tenPow) + getPosition())/Math.pow(10, tenPow));
  }
  
  public int getSize(){
    return posns.size();
  }
  
  public float maxYCor(){
    return logP(minPValIndex);
  }
}