package net.AI;

import net.connect4Game.Board;

public class AIAgent {
	private int depth;
	private int color;
	private int opposingColor;
	private Board gameBoard;
	
	public AIAgent(Board gameBoard, int depth, int color) {
		this.depth = 42;
		this.color = color;
		if(this.color == 1) this.opposingColor = 2;
		else if(this.color == 2) this.opposingColor = 1;
		this.gameBoard = gameBoard;
	}
	
	private int[] miniMax(boolean maximizingPlayer, int remainingDepth, int alpha, int beta, int lastCol, Board tmpBoard) {
		//Returns optimal next move based on the results of minimax
		//Scores each gameboard as we go, utilizes backtracking
		//System.out.println("Call to minimax " + remainingDepth);
		if(tmpBoard.checkWin() == this.color) {
			return new int[] {100000, lastCol};
		}
		if(tmpBoard.checkWin() == this.opposingColor) {
			return new int[] {-100000, lastCol};
		}
		if(tmpBoard.checkWin() == -1) {
			return new int[] {0, lastCol};
		}
		if(remainingDepth <= 0) {
			//System.out.println("Depth 0: " + (gameBoard.getScore(this.color) - gameBoard.getScore(this.opposingColor)));
			return new int[] {tmpBoard.getScore(this.color)- tmpBoard.getScore(this.opposingColor), lastCol}; 
		}
		
		
		if(maximizingPlayer) {
			int maxEval = Integer.MIN_VALUE;
			int maxCol = -1;
			for(int col = 0; col < 7; col++) {
				//Try all columns in minimax
				if(tmpBoard.executeMove(this.color, col)) {
					//if(gameBoard.checkWin() != 0) return new int[]{Integer.MAX_VALUE - 1, col};
					int boardScore = miniMax(false, remainingDepth - 1, alpha, beta, col, new Board(tmpBoard))[0];
					//System.out.println("MAX OF " + maxEval + " AND " + boardScore);
					if(boardScore > maxEval) maxCol = col;
					maxEval = Integer.max(maxEval, boardScore);
					alpha = Integer.max(alpha, boardScore);
					//gameBoard.removePiece(col); //Backtracking
					//gameBoard.printBoard();
					if(beta <= alpha) break;
				} else {
					continue;
				}
			}
			//System.out.println("DEPTH " + remainingDepth + ": " + maxEval);
			return new int[]{maxEval, maxCol};
		} else { //Minimizing player
			int minEval = Integer.MAX_VALUE;
			int minCol = -1;
			for(int col = 0; col < 7; col++) {
				//Try all columns in minimax
				if(tmpBoard.executeMove(this.opposingColor, col)) {
					//if(gameBoard.checkWin() != 0) return new int[]{Integer.MIN_VALUE + 1, col};
					int boardScore = miniMax(true, remainingDepth - 1, alpha, beta, col, new Board(tmpBoard))[0];
					//System.out.println("MIN OF " + minEval + " AND " + boardScore);
					if(boardScore < minEval) minCol = col;
					minEval = Integer.min(minEval, boardScore);
					beta = Integer.min(beta,  boardScore);
					//gameBoard.removePiece(col); //Backtracking
					//gameBoard.printBoard();
					if(beta <= alpha) break;
				} else {
					continue;
				}
			}
			//System.out.println("Depth " + remainingDepth + ": " + minEval);
			return new int[] {minEval, minCol};
		}

	}
	
	public boolean playMove() {
		Board newBoard = new Board(this.gameBoard);
		int[] nextMove = miniMax(true, this.depth, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, newBoard);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("MINIMAX: " + nextMove[0] + " " + nextMove[1]);
		return gameBoard.executeMove(this.color, nextMove[1]);
	}
	
}
