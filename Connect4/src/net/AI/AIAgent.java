package net.AI;

import net.connect4Game.Board;

public class AIAgent {
	private int depth;
	private int color;
	private int opposingColor;
	private Board gameBoard;
	
	public AIAgent(Board gameBoard, int depth, int color) {
		this.depth = 9;
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
			//System.out.println("WINNING BOARD");
			//tmpBoard.printBoard();
			//System.out.println("BASE1");
			return new int[] {remainingDepth * 100000, lastCol};
		}
		if(tmpBoard.checkWin() == this.opposingColor) {
			//System.out.println("BASE2");
			return new int[] {remainingDepth * -100000, lastCol};
		}
		if(tmpBoard.checkWin() == -1 || tmpBoard.moveCount >= 42) {
			//System.out.println("BASE3");
			return new int[] {0, lastCol};
		}
		if(remainingDepth <= 0) {
			//System.out.println("Depth 0: " + (gameBoard.getScore(this.color) - gameBoard.getScore(this.opposingColor)));
			//System.out.println("BASE4");
			return new int[] {tmpBoard.getScore(this.color) - tmpBoard.getScore(this.opposingColor), lastCol}; 
		}
		
		int[] moveOrder = new int[]{3, 2, 4, 1, 5, 0, 6};
		if(maximizingPlayer) {
			int maxEval = Integer.MIN_VALUE;
			int maxCol = -1;
			for(int col : moveOrder) {
				//Try all columns in minimax
				//Board nextBoard = new Board(tmpBoard);
				if(tmpBoard.executeMove(this.color, col)) {
					//if(gameBoard.checkWin() != 0) return new int[]{Integer.MAX_VALUE - 1, col};
					int boardScore = miniMax(false, remainingDepth - 1, alpha, beta, col, tmpBoard)[0];
					//System.out.println("MAX OF " + maxEval + " AND " + boardScore);
					if(boardScore > maxEval) maxCol = col;
					maxEval = Integer.max(maxEval, boardScore);
					alpha = Integer.max(alpha, boardScore);
					tmpBoard.removePiece(col); //Backtracking
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
			for(int col : moveOrder) {
				//Try all columns in minimax
				//Board nextBoard = new Board(tmpBoard);
				if(tmpBoard.executeMove(this.opposingColor, col)) {
					//if(gameBoard.checkWin() != 0) return new int[]{Integer.MIN_VALUE + 1, col};
					int boardScore = miniMax(true, remainingDepth - 1, alpha, beta, col, tmpBoard)[0];

					//System.out.println("MIN OF " + minEval + " AND " + boardScore);
					if(boardScore < minEval) minCol = col;
					minEval = Integer.min(minEval, boardScore);
					beta = Integer.min(beta,  boardScore);
					tmpBoard.removePiece(col); //Backtracking
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
		//Board newBoard = new Board(this.gameBoard);
		int[] nextMove = miniMax(true, this.depth, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, this.gameBoard);
		System.out.println("MINIMAX: " + nextMove[0] + " " + nextMove[1]);

		return gameBoard.executeMove(this.color, nextMove[1]);
	}
	
}
