package library;

import java.io.File;

import uk.ac.roslin.ensembl.config.DBConnection.DataSource;
import uk.ac.roslin.ensembl.dao.database.DBRegistry;
import uk.ac.roslin.ensembl.dao.database.DBSpecies;
import uk.ac.roslin.ensembl.datasourceaware.core.DAChromosome;
import uk.ac.roslin.ensembl.datasourceaware.core.DAGene;
import uk.ac.roslin.ensembl.exception.ConfigurationException;
import uk.ac.roslin.ensembl.exception.DAOException;
import uk.ac.roslin.ensembl.exception.NonUniqueException;
import uk.ac.roslin.ensembl.model.Coordinate;
import uk.ac.roslin.ensembl.model.core.Chromosome;

public class GetLocations {
	private File file;
	private static DBSpecies human;
	private static DAGene gene;
	private static Coordinate coord;
	private static Chromosome chromosome;
	private static float[] chrLengths = null;
	private static boolean setUp = false;
	
	private static final float DIV_BY = (float)Math.pow(10, 8);

	
	protected static void setUpChrLengths(){
		DAChromosome temp = new DAChromosome();
		chrLengths = new float[25];
		chrLengths[0] = 0;
		for (int i = 1; i <= 22; i++){
			temp.setChromosomeName(""+i);
			chrLengths[i] = temp.getLength()/DIV_BY + chrLengths[i];
		}
		temp.setChromosomeName("X");
		chrLengths[23] = chrLengths[22] + temp.getLength()/DIV_BY;
		temp.setChromosomeName("Y");
		chrLengths[24] = chrLengths[23] + temp.getLength()/DIV_BY;
	}
	
	public GetLocations() throws NonUniqueException, ConfigurationException, DAOException{
		if (!setUp){
			DBRegistry eReg = DBRegistry.createRegistryForDataSource(DataSource.ENSEMBLDB);
			System.out.println("why");
			human = eReg.getSpeciesByAlias("human");
			setUpChrLengths();
			setUp = true;
		}
	}
	
	public GetLocations(File theFile) throws NonUniqueException, ConfigurationException, DAOException{
		super();
		file = theFile;		
	}
	private float getLocation(String name) throws NonUniqueException, DAOException{
		System.out.println("hello 1");
		gene = human.getGeneByStableID(name);
		System.out.println("hello 2");
		System.out.println(gene);
		System.out.println(gene.getDescription());
		System.out.println("hello 2.5");
		chromosome = (Chromosome) gene.getChromosomeMapping().getTarget();
		System.out.println("hello 3");
		coord = gene.getChromosomeMapping().getTargetCoordinates();
		System.out.println("hello 4");
		int chrNum = getChrNum(chromosome.getName());
		System.out.println("hello 5");
		return chrLengths[chrNum] + (coord.getStart() + coord.getEnd())/DIV_BY;
	}
	
	private int getChrNum(String s){
		try{
			return Integer.parseInt(s);
		}
		catch(NumberFormatException e){
			if (s.equals("X")){
				return 23;
			}
		}
		return 24;
	}
	
	/*public static void main(String[] args) throws NonUniqueException, ConfigurationException, DAOException{
		System.out.println("hi");
		setUpChrLengths();
		System.out.println("hi again");
		GetLocations gL = new GetLocations();
		System.out.println("hello");
		String[] genes = {"ENSG00000153551"};//, "699"};//, "harry potter < aang"};
		for (String s : genes){
			try{
				System.out.println(gL.getLocation(s));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}*/
}
