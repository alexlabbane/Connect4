package net.AI;

import net.connect4Game.Board;

public class AIAgent {
	private int depth;
	private int color;
	private int opposingColor;
	private Board gameBoard;
	
	public AIAgent(Board gameBoard, int depth, int color) {
		this.depth = depth;
		this.color = color;
		if(this.color == 1) this.opposingColor = 2;
		else if(this.color == 2) this.opposingColor = 1;
		this.gameBoard = gameBoard;
	}
	
	private int[] miniMax(boolean maximizingPlayer, int remainingDepth, int alpha, int beta) {
		//Returns optimal next move based on the results of minimax
		//Scores each gameboard as we go, utilizes backtracking
		
		if(maximizingPlayer) {
			int maxEval = Integer.MIN_VALUE;
			int maxCol = -1;
			for(int col = 0; col < 7; col++) {
				//Try all columns in minimax
				if(gameBoard.executeMove(this.color, col)) {
					int boardScore = miniMax(!maximizingPlayer, remainingDepth - 1, alpha, beta)[0];
					maxEval = Integer.max(maxEval, boardScore);
					alpha = Integer.max(alpha, boardScore);
					gameBoard.removePiece(col); //Backtracking
					
					if(beta <= alpha) break;
				}
				else { 
					int boardScore = gameBoard.getScore(this.color); //TODO: Write function to score the board
					return new int[] {boardScore, col};
				}
			}
			
			return new int[]{maxEval, maxCol};
		} else { //Minimizing player
			int minEval = Integer.MAX_VALUE;
			int minCol = -1;
			for(int col = 0; col < 7; col++) {
				//Try all columns in minimax
				if(gameBoard.executeMove(this.opposingColor, col)) {
					int boardScore = miniMax(!maximizingPlayer, remainingDepth - 1, alpha, beta)[0];
					minEval = Integer.min(minEval, boardScore);
					beta = Integer.min(alpha,  boardScore);
					gameBoard.removePiece(col); //Backtracking
					
					if(beta <= alpha) break;
				}
				else { 
					int boardScore = gameBoard.getScore(this.color); //TODO: Write function to score the board
					return new int[] {boardScore, col};
				}
			}
			
			return new int[] {minEval, minCol};
		}

	}
	
	public void playMove() {
		int nextMove = miniMax(true, depth, Integer.MIN_VALUE, Integer.MAX_VALUE)[1];
		
		gameBoard.executeMove(this.color, nextMove);
	}
	
}
