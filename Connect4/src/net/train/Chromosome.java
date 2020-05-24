package net.train;

import java.util.Random;

/**
 * 
 * @author scien
 * Stores data for each chromosome
 * 
 */
public class Chromosome implements Comparable<Chromosome> {
	private String[] genes;
	private Random rand;
	private int score; //+1 for wins, -1 for losses, 0 for draws
	
	/**
	 * Generate a random chromosome
	 */
	public Chromosome() {
		rand = new Random();
		genes = new String[] {"", "", "", "", "", ""};
		score = 0;
		
		for(int i = 0; i < 6; i++) {
			int newGene = rand.nextInt();
			
			genes[i] = Integer.toBinaryString(newGene);
			int padSize = 32 - genes[i].length();
			String padding = "";
			for(int j = 0; j < padSize; j++) padding += "0";
			genes[i] = padding + genes[i];
		}
	}
	
	public Chromosome(int oneSequence, int twoSequence, int threeSequence, int oneSequenceThreat, int twoSequenceThreat, int threeSequenceThreat) {
		rand = new Random();
		genes = new String[] {"", "", "", "", "", ""};
		score = 0;
		
		int[] vals = new int[] {oneSequence, twoSequence, threeSequence, oneSequenceThreat, twoSequenceThreat, threeSequenceThreat};
		
		for(int i = 0; i < 6; i++) {
			int newGene = vals[i];
			
			genes[i] = Integer.toBinaryString(newGene);
			int padSize = 32 - genes[i].length();
			String padding = "";
			for(int j = 0; j < padSize; j++) padding += "0";
			genes[i] = padding + genes[i];
		}
	}
	
	public Chromosome(int[] weights) {
		rand = new Random();
		genes = new String[] {"", "", "", "", "", ""};
		score = 0;
		
		for(int i = 0; i < 6; i++) {
			int newGene =  weights[i];
			
			genes[i] = Integer.toBinaryString(newGene);
			int padSize = 32 - genes[i].length();
			String padding = "";
			for(int j = 0; j < padSize; j++) padding += "0";
			genes[i] = padding + genes[i];
		}
	}
	
	public Chromosome(String[] genes) {
		rand = new Random();
		score = 0;
		this.genes = genes;
	}
	
	
	public void addWin() { this.score++; }
	public void addLoss() { this.score--; }
	
	public int getScore() { return this.score; }
	public String[] getGenes() { return this.genes; };
	
	public void mutate() {
		for(int i = 0; i < genes.length; i++) {
			for(int j = 0; j < genes[i].length(); j++) {
				if(rand.nextInt(300) == 299) {
					//System.out.print("Mutation occurred ");
					
					char newBit = '1';
					if(genes[i].charAt(j) == '1') newBit = '0';
					
					genes[i] = genes[i].substring(0, j) + newBit + genes[i].substring(j + 1);
				}
			}
		}
	}
	
	public int[] getWeights() {
		int[] weights = new int[] {0, 0, 0, 0, 0, 0};
		
		for(int i = 0; i < genes.length; i++) {
			weights[i] = Math.abs(Integer.parseUnsignedInt(genes[i], 2) % 100);
		}
		
		return weights;
	}
	
	public String toString() {
		String toReturn = "";
		for(int i = 0; i < genes.length; i++) {
			toReturn += genes[i] + " ";
		}
		//toReturn += "\n";
		for(int i = 0; i < genes.length; i++) {
			try {
				toReturn += Math.abs(Integer.parseUnsignedInt(genes[i], 2) % 100) + "\t";
			} catch(Exception e) {
				System.out.println(e.toString());
			}
		}
		
		return toReturn;
	}

	@Override
	public int compareTo(Chromosome o) {
		if(this.getScore() < o.getScore()) return -1;
		else if(this.getScore() > o.getScore()) return 1;
		return 0;
	}
}
