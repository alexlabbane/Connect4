package net.connect4Game;

import java.util.Scanner;

import net.AI.AIAgent;

public class Player {
	//Can be either an AI player or a human player
	private boolean isHuman;
	private int color;
	private GUI gui;
	private Scanner humanReader;
	private AIAgent ai;
	private Board gameBoard;
	
	public Player(boolean human, int color, GUI gui, Board board) {
		this.isHuman = human;
		this.color = color;
		this.gui = gui;
		humanReader = new Scanner(System.in);
		ai = new AIAgent(board, 15, color);
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
			System.out.println("Score: " + gameBoard.getScore(this.color));
			return true;
		} else {
			System.out.println("AI PLAY");
			return this.ai.playMove();
		}		
	}
}
