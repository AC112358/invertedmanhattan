package library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import jdk.nashorn.internal.parser.Token;

public class SearchRef {
	//should make a new file to process with rsID, p-val, & location
	private File refFile;
	private File rsIDFile;
	private HashMap<String, String> lines;
	private Set<String> realLines;
	
	public SearchRef(String rs) throws FileNotFoundException{
		refFile = new File("../../REF.txt");
		rsIDFile = new File(rs);
		lines = new HashMap<String, String>();
		realLines = new TreeSet<String>();
	}
	public SearchRef(String ref, String rs) throws FileNotFoundException{
		this(rs);
		refFile = new File(ref);
	}
	
	protected void makeIntermediateTextFile(boolean verbose, File writeTo) throws IOException{
		System.out.println("making lines");
		makeLines(verbose);
		System.out.println("making real lines");
		makeRealLines(verbose);
		System.out.println("writing to file");
		writeToFile(writeTo);
		lines.clear();
		realLines.clear();
	}
	
	private void makeRealLines(boolean verbose) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(rsIDFile));
		String line = in.readLine();
		int index = 1;
		while (line != null){
			//chr bp rsID pVal is what we want to output
			//rsID a1 a2 freq.hapmap.ceu beta se.2gc p.2gc n is what we have
			String[] tokens = line.split("\\s+");
			try{
				String s = lines.get(tokens[0]);
				if (s != null){
					realLines.add(s + " " + tokens[0] + " " + tokens[tokens.length-2]);
				}else{
					if (verbose){
						System.out.println("No information on rsID " + tokens[0]);
					}
				}
			}catch(Exception e){
				if (verbose){
					System.out.println(lineRsInvalidMsg(index));
				}
			}
			line = in.readLine();
			index++;
		}
		in.close();
	}
	
	
	private void makeLines(boolean verbose) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(refFile));
		in.readLine();
		String line = in.readLine();
		//System.out.println(line);
		String[] tokens;
		int index = 2;
		while (line != null){
			tokens = line.split("\\s+");
			/*if (index < 25){
				System.out.print(line + ": ");
				for (String s : tokens){
					System.out.print(s + ",");
				}
				System.out.println();
			}*/
			try{
				String location = tokens[tokens.length-2] + " " + tokens[tokens.length-1];
				lines.put(tokens[1], location);
			}catch(Exception e){
				if (verbose){
					System.out.println(lineInvalidMsg(index));	
				}
			}
			line = in.readLine();
			index++;
		}
		in.close();
	}
	
	private void writeToFile(File writeTo){
		try{
			PrintWriter writer = new PrintWriter(writeTo, "UTF-8");
			for (String ln : realLines){
				writer.println(ln);
			}
			writer.close();
		}catch(IOException e){
			return;
		}
	}
	
	private static String lineInvalidMsg(int index){
		return "Line " + index + " invalid formatting! (2nd col rsID, 2nd to last col chromosome, last col base pair)";
	}
	
	private static String lineRsInvalidMsg(int index){
		return "Line " + index + " invalid formatting! (1st col rsID, 2nd to last col p val)";
	}
	
	
	public static void main(String[] args) throws IOException{
		SearchRef sF = new SearchRef("../WHRadjMen.txt");
		sF.makeIntermediateTextFile(true, new File("INTERMEDIATE_TEXT_FILE1.txt"));
	}
}
