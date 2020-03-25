package net.AI;

import net.connect4Game.Board;

public class HybridAIAgent {

	private AIAgent ai;
	private RandomAIAgent rai;
	
	public HybridAIAgent(Board gameBoard, int depth, int color) {
		ai = new AIAgent(gameBoard, depth, color, 1);
		rai = new RandomAIAgent(color, gameBoard, 10000);
	}
	
	public boolean playMove() {
		int randomScore = rai.getScore();
		if(randomScore < 100000) {
			return rai.playMove();
		} else {
			return ai.playMove();
		}
	}
	
}
