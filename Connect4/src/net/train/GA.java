package net.train;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import net.connect4Game.Board;
import net.connect4Game.GUI;
import net.connect4Game.Game;
import net.connect4Game.Player;

/**
 * 
 * @author
 * Genetic Algorithm class used to tune weights in evaluation function
 */
public class GA {
	private int populationSize;
	private int maxGenerations; //0 to run infinitely
	private int currentGeneration;
	private Chromosome[] population;
	private ArrayList<String> generationData; //Stores data to write for the current generation
	private long timeID; //Stores time in ms as unique id for training session
	
	public GA() {
		this.populationSize = 100;
		this.maxGenerations = 0;
		this.currentGeneration = 0;
		this.generationData = new ArrayList<String>();
		this.timeID = new Date().getTime();
		
		initPopulation();
	}
	
	public GA(int populationSize, int maxGenerations) {
		this.populationSize = Math.max(5, populationSize); //Population size cannot be less than 5
		this.maxGenerations = maxGenerations;
		this.currentGeneration = 0;
		this.generationData = new ArrayList<String>();
		this.timeID = new Date().getTime();
		
		initPopulation();
	}
	
	public void train() {
		while(this.currentGeneration < this.maxGenerations || this.maxGenerations == 0) {
			simulateGeneration();
		}
	}
	
	/**
	 * Initialized first generation randomly
	 */
	private void initPopulation() {
		population = new Chromosome[this.populationSize];
		
		for(int i = 0; i < this.populationSize; i++) {
			Chromosome newChromosome = new Chromosome();
			population[i] = newChromosome;
		}
	}
	
	private void simulateGeneration() {
		this.generationData.clear();
		
		System.out.println("Generation " + (this.currentGeneration + 1));
		this.generationData.add("Generation" + (this.currentGeneration + 1));
		recordGenerationData();
		evaluatePopulationFitness();	
		
		int best = Integer.MIN_VALUE;
		int[] bestWeights = new int[1];
		int bestMember = 0;
		for(int i = 0; i < this.populationSize; i++) {
			Chromosome c = this.population[i];
			
			if(c.getScore() > best) {
				best = Integer.max(best, c.getScore());
				bestWeights = c.getWeights();
				bestMember = (i+1);
			}
		}
		
		this.generationData.add("\nBest Performer");
		String desc = bestMember + ":\t";
		for(int weight : bestWeights) {
			desc += weight + "\t";
		}
		this.generationData.add(desc);
		writeGenerationData();
		
		createNewPopulation();
		mutatePopulation();
		
		this.currentGeneration++;
	}
	
	void createNewPopulation() {
		Random rand = new Random();
		
		//Sort population by score
		Arrays.sort(this.population, Collections.reverseOrder());
		Chromosome[] newPopulation = new Chromosome[this.populationSize];
		
		for(Chromosome c : this.population) {
			System.out.print(c.getScore() + " ");
		}
		System.out.println();
		for(Chromosome c : this.population) {
			for(int weight : c.getWeights()) {
				System.out.print(weight + " ");
			}
			System.out.println();
		}
		
		//Keep top 25% of population
		int cutOff = this.populationSize / 4;
		int leftToPick = this.populationSize - cutOff;
		for(int i = 0; i < cutOff; i++) {
			newPopulation[i] = new Chromosome(this.population[i].getWeights());
		}
		
		//Select 5% of remaining population to randomly survive
		int toChoose = Math.max(1, leftToPick / 20);
		int[] bag = new int[leftToPick];
		for(int i = cutOff; i < this.populationSize; i++) bag[i - cutOff] = i; //Get all possible indices to select in the bag
		for(int i = 0; i < toChoose; i++) {
			int selectedIndex = rand.nextInt(leftToPick);
			newPopulation[cutOff + i] = new Chromosome(this.population[bag[selectedIndex]].getWeights());
			
			//Replace selected index with end of list and decrease leftToPick by one
			bag[selectedIndex] = bag[bag.length - i - 1];
			leftToPick--;
		}
		
		//Remainder of population from random breeding with the best performers
		for(int i = 0; i < leftToPick; i++) {
			Chromosome firstParent = this.population[i];
			Chromosome secondParent = this.population[rand.nextInt(cutOff + toChoose)]; //Pick second parent from anything (could even be the same parent; not worried about that yet)
			
			newPopulation[cutOff + toChoose + i] = this.cross(firstParent, secondParent);
		}
		
		this.population = newPopulation;
	}
	
	/**
	 * Breeds two chromosomes together
	 * @param c1 The first parent
	 * @param c2 The second parent
	 * @return The child of c1 and c2
	 */
	Chromosome cross(Chromosome c1, Chromosome c2) {
		String[] weights1 = c1.getGenes();
		String[] weights2 = c2.getGenes();
		String[] newGenes = new String[weights1.length];
		Random rand = new Random();
		
		for(int i = 0; i < weights1.length; i++) {
			String gene = "";
			for(int j = 0; j < weights1[i].length(); j++) {
				int choice = rand.nextInt(2);
				if(choice == 0) gene += weights1[i].charAt(j);
				else gene += weights2[i].charAt(j);
			}
			newGenes[i] = gene;
		}
		
		return new Chromosome(newGenes);
	}
	
	void mutatePopulation() {
		for(Chromosome c : this.population) {
			c.mutate();
		}
	}
	
	private void evaluatePopulationFitness() {
		this.generationData.add("\nMatch results");
		
		int count = 1;
		for(int i = 0; i < this.populationSize; i++) {
			for(int j = i + 1; j < this.populationSize; j++) {
				//System.out.println("Game " + count);
				Player player1 = new Player(false, 1, null, null, 1, this.population[i].getWeights()); //Create player with weights from population[i]
				Player player2 = new Player(false, 2, null, null, 1, this.population[j].getWeights()); //Create player with weights from population[j]
				
				Game game = new Game(player1, player2, false);
				int winner = game.execute();

				String matchDesc = (i+1) + " vs " + (j+1) + ":\t";
				if(winner == 1) { this.population[i].addWin(); this.population[j].addLoss(); matchDesc += (i+1); }
				else if(winner == 2) { this.population[i].addLoss(); this.population[j].addWin(); matchDesc += (j+1); }
				else { matchDesc += "DRAW"; }
				this.generationData.add(matchDesc);
				
				
				player1 = new Player(false, 2, null, null, 1, this.population[i].getWeights()); //Swap colors
				player2 = new Player(false, 1, null, null, 1, this.population[j].getWeights()); //Swap colors
				
				game = new Game(player2, player1, false); //Change who goes first
				winner = game.execute();

				matchDesc = (j+1) + " vs " + (i+1) + ":\t";
				if(winner == 1) { this.population[j].addWin(); this.population[i].addLoss(); matchDesc += (j+1); }
				else if(winner == 2) { this.population[j].addLoss(); this.population[i].addWin(); matchDesc += (i+1); }
				else { matchDesc += "DRAW"; }
				this.generationData.add(matchDesc);
				
				count++;
				if(count % 1000 == 0) System.out.println("Game " + count);
			}
		}
	}
	
	private void recordGenerationData() {
		this.generationData.add("\nPopulation");
		for(int i = 0; i < this.populationSize; i++) {
			Chromosome c = this.population[i];
			String desc = (i+1) + ":\t";
			for(int weight : c.getWeights()) {
				desc += weight + "\t";
			}
			this.generationData.add(desc);
		}
	}

	private void writeGenerationData() {
		File dataDirectory = new File("./trainingData" + this.timeID);
		if(!dataDirectory.exists()) dataDirectory.mkdir();
		
		BufferedWriter generationWriter;
		try {
			generationWriter = new BufferedWriter(new FileWriter("./trainingData" + this.timeID + "/" + "Gen" + (1+this.currentGeneration) + ".txt"));
		} catch (IOException e) {
			System.out.println("Couldn't write data for generation " + this.currentGeneration);
			return;
		}
		
		for(String line : this.generationData) {
			try {
				generationWriter.write(line);
				generationWriter.newLine();
			} catch(IOException e) {
				System.out.println("Couldn't write line for generation " + this.currentGeneration);
				System.out.println("Continuing to try next line.");
			}
		}
		
		try {
			generationWriter.close();
		} catch (IOException e) {
			System.out.println("Couldn't close generation writer");
		}
	}
	
	
	
}
