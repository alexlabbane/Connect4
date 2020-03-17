package net.AI;

import net.connect4Game.Board;

public class AIAgent {
	private int depth;
	private Board gameBoard;
	
	public AIAgent(Board gameBoard, int depth) {
		this.depth = depth;
		this.gameBoard = gameBoard;
	}
	
	
}
