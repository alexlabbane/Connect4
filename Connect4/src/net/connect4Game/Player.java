package net.connect4Game;

import java.util.Scanner;

public class Player {
	//Can be either an AI player or a human player
	private boolean isHuman;
	private int color;
	private GUI gui;
	private Scanner humanReader;
	
	public Player(boolean human, int color, GUI gui) {
		this.isHuman = human;
		this.color = color;
		this.gui = gui;
		humanReader = new Scanner(System.in);
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
			System.out.println("Listener achieved.");
			return true;
		} else {
			//TODO: AI Player
		}
		
		return false;
	}
}
