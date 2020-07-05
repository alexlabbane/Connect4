package net.connect4Game;

import java.util.Scanner;

import net.AI.AIAgent;
import net.AI.HybridAIAgent;
import net.AI.RandomAIAgent;

public class Player {
	//Can be either an AI player or a human player
	private boolean isHuman;
	private int color;
	private int opposingColor;
	private GUI gui;
	private Scanner humanReader;
	private AIAgent ai;
	private RandomAIAgent rai;
	private HybridAIAgent hai;
	private int AIType;
	private int[] parameterWeights; //Used for genetic algorithm AI
	
	private bitBoard gameBoard;
	
	public Player(boolean human, int color, GUI gui, bitBoard board, int AIType, int tmpType, int depth) {
		this.isHuman = human;
		this.color = color;
		if(this.color == 1) this.opposingColor = 2;
		else this.opposingColor = 1;
		
		this.gui = gui;
		humanReader = new Scanner(System.in);
		
		this.AIType = AIType;
		this.rai = new RandomAIAgent(color, board, 10000);
		this.ai = new AIAgent(board, depth, color, tmpType); //Uses evaluation function tmpType
		this.hai = new HybridAIAgent(board, depth, color);
		
		this.gameBoard = board;
		this.parameterWeights = null;
	}
	
	public Player(boolean human, int color, GUI gui, bitBoard board, int AIType, int[] parameterWeights, int depth) {
		this.isHuman = human;
		this.color = color;
		if(this.color == 1) this.opposingColor = 2;
		else this.opposingColor = 1;
		
		this.gui = gui;
		humanReader = new Scanner(System.in);
		
		this.AIType = 1;
		this.rai = null;
		this.hai = null;
		this.ai = new AIAgent(board, depth, color, parameterWeights); //Uses evaluation function tmpType
		
		this.gameBoard = board;
		this.parameterWeights = parameterWeights;
	}
	
	
	public void setGUI(GUI gui) { this.gui = gui; }
	
	public void setBoard(bitBoard gameBoard) { this.gameBoard = gameBoard; this.ai.setBoard(gameBoard); }
	
	public boolean isHuman() { return this.isHuman; }
	
	public int getColor() { return this.color; }
	
	public boolean play(bitBoard gameBoard) {
		if(this.isHuman) {
			//Listen for inputs from player
			while(gui.inputIsEnabled()) {
				try { 
					//Checks 20 times/second
					Thread.sleep(50); 
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
			return true;
		} else {
			//System.out.println("The AI is thinking.");
			boolean movePlayed =  this.ai.playMove();
			
			this.gui.setAiInfo(this.ai.getNodesEvaluated(), this.ai.getNodesPruned());
			
			return movePlayed;
		}		
	}
}
