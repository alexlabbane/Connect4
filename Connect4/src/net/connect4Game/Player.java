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
	
	private Board gameBoard;
	
	public Player(boolean human, int color, GUI gui, Board board, int AIType, int tmpType) {
		this.isHuman = human;
		this.color = color;
		if(this.color == 1) this.opposingColor = 2;
		else this.opposingColor = 1;
		
		this.gui = gui;
		humanReader = new Scanner(System.in);
		
		this.AIType = AIType;
		this.rai = new RandomAIAgent(color, board, 10000);
		this.ai = new AIAgent(board, 8, color, tmpType); //Uses evaluation function 1
		this.hai = new HybridAIAgent(board, 8, color);
		
		this.gameBoard = board;
	}
	
	public boolean isHuman() { return this.isHuman; }
	
	public boolean play(Board gameBoard) {
		if(this.isHuman) {
			//Listen for inputs from player
			while(gui.inputIsEnabled()) {
				try { 
					//Checks 20 times/second
					Thread.sleep(50); 
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
			System.out.println("Players Score: " + (gameBoard.getScore(this.color) - gameBoard.getScore(this.opposingColor)));
			return true;
		} else {
			System.out.println("The AI is thinking.");
			if(this.AIType == Game.AITypes.MINIMAX.getType()) return this.ai.playMove();
			else if(this.AIType == Game.AITypes.RANDOM.getType()) return this.rai.playMove();
			else if(this.AIType == Game.AITypes.HYBRID.getType()) return hai.playMove();
			else return false;
		}		
	}
}
