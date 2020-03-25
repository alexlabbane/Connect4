package net.AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import net.connect4Game.Board;
import util.Pair;

public class AIAgent {
	private int depth;
	private int color;
	private int opposingColor;
	private Board gameBoard;
	private int type;
	private TranspositionTable transpositionTable;
	private int[] moveOrder;
	
	public AIAgent(Board gameBoard, int depth, int color, int type) {
		this.depth = depth;
		this.color = color;
		if(this.color == 1) this.opposingColor = 2;
		else if(this.color == 2) this.opposingColor = 1;
		this.gameBoard = gameBoard;
		this.type = type;
		this.transpositionTable = new TranspositionTable();
		this.moveOrder = new int[]{3, 1, 4, 2, 5, 0, 6};
		
	}
	/* Doesn't work yet :/
	private List<Pair<Integer, Integer>> miniMaxTransposition(boolean maximizingPlayer, int remainingDepth, int alpha, int beta, int lastCol, Board tmpBoard) {
		//Returns optimal next move based on the results of minimax
		//Scores each gameboard as we go, utilizes backtracking
		//System.out.println("Call to minimax " + remainingDepth);
		if(tmpBoard.checkWin() == this.color) {
			//System.out.println("WINNING BOARD");
			//tmpBoard.printBoard();
			//System.out.println("BASE1");

			return Arrays.asList(new Pair<Integer, Integer>(remainingDepth * 100000, lastCol));
		}
		if(tmpBoard.checkWin() == this.opposingColor) {
			//System.out.println("BASE2");
			
			return Arrays.asList(new Pair<Integer, Integer>(remainingDepth * -100000, lastCol));
		}
		if(tmpBoard.checkWin() == -1 || tmpBoard.moveCount >= 42) {
			//System.out.println("BASE3");
			return Arrays.asList(new Pair<Integer, Integer>(0, lastCol));
		}
		if(remainingDepth <= 0) {
			//System.out.println("Depth 0: " + (gameBoard.getScore(this.color) - gameBoard.getScore(this.opposingColor)));
			//System.out.println("BASE4");
			switch(this.type) {
			case 1:
				return Arrays.asList(new Pair<Integer, Integer>(tmpBoard.getScore(color) - tmpBoard.getScore(this.opposingColor), lastCol)); 
			case 2:
				return Arrays.asList(new Pair<Integer, Integer>(tmpBoard.getScore2(color) - tmpBoard.getScore2(this.opposingColor), lastCol)); 
			case 3:
				return Arrays.asList(new Pair<Integer, Integer>(tmpBoard.getScore3(color) - tmpBoard.getScore3(this.opposingColor), lastCol)); 
			case 4:
				return Arrays.asList(new Pair<Integer, Integer>(tmpBoard.getScore4(color) - tmpBoard.getScore4(this.opposingColor), lastCol)); 
			}
			return Arrays.asList(new Pair<Integer, Integer>(tmpBoard.getScore(this.color) - tmpBoard.getScore(this.opposingColor), lastCol)); 
		}
		
		int[] moveOrder = new int[] {0, 0, 0, 0, 0, 0, 0};
		int index = 0;
		for(int column : this.moveOrder) {
			moveOrder[index] = column;
			index++;
		}
		
		boolean replacementFlag = true;
		if(maximizingPlayer) {
			List<Pair<Integer, Integer>> toReturn = new ArrayList<Pair<Integer, Integer>>();
			
			int maxEval = Integer.MIN_VALUE;
			int maxCol = -1;
			for(int col : moveOrder) {
				//Try all columns in minimax
				//Board nextBoard = new Board(tmpBoard);
				if(tmpBoard.executeMove(this.color, col)) {
					//if(gameBoard.checkWin() != 0) return new int[]{Integer.MAX_VALUE - 1, col};
					int boardScore;
					if(this.transpositionTable.has(tmpBoard.currentZobrist)) {
						TranspositionEntry entry = this.transpositionTable.table.get(tmpBoard.currentZobrist);
						if(entry.depth >= remainingDepth) {
							boardScore = entry.eval;
							System.out.println("Transposition " + tmpBoard.currentZobrist);
							//System.out.println(entry.depth);
							//System.out.println();
							replacementFlag = false;
						} else {
							index = 0;
							for(Pair<Integer, Integer> p : entry.evalOrder) {
								this.moveOrder[index] = p.second;
								index++;
							}
							
							boardScore = miniMaxTransposition(false, remainingDepth - 1, alpha, beta, col, tmpBoard).get(0).first;
						}
					} else {
					
						List<Pair<Integer, Integer>> eval = miniMaxTransposition(true, remainingDepth - 1, alpha, beta, col, tmpBoard);
						boardScore = eval.get(0).first;
						
					}
					//System.out.println("MAX OF " + maxEval + " AND " + boardScore);
					toReturn.add(new Pair<Integer, Integer>(boardScore, col));
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
			toReturn.sort(Collections.reverseOrder());
			//Collections.sort(toReturn);
			
			if(replacementFlag) this.transpositionTable.put(new TranspositionEntry(
					tmpBoard.currentZobrist,
					remainingDepth,
					0, //Not sure what to do with flag yet
					maxEval,
					0, //Not sure what to do with ancient yet
					maxCol,
					toReturn
				));
			
			return toReturn;
		} else { //Minimizing player
			List<Pair<Integer, Integer>> toReturn = new ArrayList<Pair<Integer, Integer>>();
			int minEval = Integer.MAX_VALUE;
			int minCol = -1;
			for(int col : moveOrder) {
				//Try all columns in minimax
				//Board nextBoard = new Board(tmpBoard);
				if(tmpBoard.executeMove(this.opposingColor, col)) {
					//if(gameBoard.checkWin() != 0) return new int[]{Integer.MIN_VALUE + 1, col};
					int boardScore;
					if(this.transpositionTable.has(tmpBoard.currentZobrist)) {
						TranspositionEntry entry = this.transpositionTable.table.get(tmpBoard.currentZobrist);
						//System.out.println("Transposition " + tmpBoard.currentZobrist);
						if(entry.depth >= remainingDepth) {
							boardScore = entry.eval;
							replacementFlag = false;
							System.out.println("Transposition " + tmpBoard.currentZobrist);
						} else {
							index = 0;
							for(Pair<Integer, Integer> p : entry.evalOrder) {
								this.moveOrder[index] = p.second;
								index++;
							}
							
							boardScore = miniMaxTransposition(true, remainingDepth - 1, alpha, beta, col, tmpBoard).get(0).first;
						}
					} else {
					
						List<Pair<Integer, Integer>> eval = miniMaxTransposition(true, remainingDepth - 1, alpha, beta, col, tmpBoard);
						boardScore = eval.get(0).first;
					}
					toReturn.add(new Pair<Integer, Integer>(boardScore, col));

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
			Collections.sort(toReturn);
			//toReturn.sort(Collections.reverseOrder());
			
			if(replacementFlag) this.transpositionTable.put(new TranspositionEntry(
					tmpBoard.currentZobrist,
					remainingDepth,
					0, //Not sure what to do with flag yet
					minEval,
					0, //Not sure what to do with ancient yet
					minCol,
					toReturn
				));
			

			return toReturn;
		}
		
	}
	*/
	
	private List<Pair<Integer, Integer>> miniMax(boolean maximizingPlayer, int remainingDepth, int alpha, int beta, int lastCol, Board tmpBoard) {
		//Returns optimal next move based on the results of minimax
		//Scores each gameboard as we go, utilizes backtracking
		//System.out.println("Call to minimax " + remainingDepth);
		if(tmpBoard.checkWin() == this.color) {
			return Arrays.asList(new Pair<Integer, Integer>(remainingDepth * 10000000, lastCol));
		}
		if(tmpBoard.checkWin() == this.opposingColor) {
			return Arrays.asList(new Pair<Integer, Integer>(remainingDepth * -10000000, lastCol));
		}
		if(tmpBoard.checkWin() == -1 || tmpBoard.moveCount >= 42) {
			return Arrays.asList(new Pair<Integer, Integer>(0, lastCol));
		}
		if(remainingDepth <= 0) {
			switch(this.type) {
			case 1:
				return Arrays.asList(new Pair<Integer, Integer>(tmpBoard.getScore(color) - tmpBoard.getScore(this.opposingColor), lastCol)); 
			case 2:
				return Arrays.asList(new Pair<Integer, Integer>(tmpBoard.getScore2(color) - tmpBoard.getScore2(this.opposingColor), lastCol)); 
			case 3:
				return Arrays.asList(new Pair<Integer, Integer>(tmpBoard.getScore3(color) - tmpBoard.getScore3(this.opposingColor), lastCol)); 
			case 4:
				return Arrays.asList(new Pair<Integer, Integer>(tmpBoard.getScore4(color) - tmpBoard.getScore4(this.opposingColor), lastCol)); 
			}
			return Arrays.asList(new Pair<Integer, Integer>(tmpBoard.getScore(this.color) - tmpBoard.getScore(this.opposingColor), lastCol)); 
		}
		
		if(maximizingPlayer) {
			List<Pair<Integer, Integer>> toReturn = new ArrayList<Pair<Integer, Integer>>();
			
			int maxEval = Integer.MIN_VALUE;
			int maxCol = -1;
			for(int col : moveOrder) {
				//Try all columns in minimax
				if(tmpBoard.executeMove(this.color, col)) {
					int boardScore;
					List<Pair<Integer, Integer>> eval = miniMax(false, remainingDepth - 1, alpha, beta, col, tmpBoard);
					boardScore = eval.get(0).first;
					
					toReturn.add(new Pair<Integer, Integer>(boardScore, col));
					if(boardScore > maxEval) maxCol = col;
					maxEval = Integer.max(maxEval, boardScore);
					alpha = Integer.max(alpha, boardScore);
					tmpBoard.removePiece(col); //Backtracking

					if(beta <= alpha) {
						break;
					}
				} else {
					continue;
				}
			}
			toReturn.sort(Collections.reverseOrder());
			
			return toReturn;
			
		} else { //Minimizing player
			List<Pair<Integer, Integer>> toReturn = new ArrayList<Pair<Integer, Integer>>();
			int minEval = Integer.MAX_VALUE;
			int minCol = -1;
			for(int col : moveOrder) {
				//Try all columns in minimax
				if(tmpBoard.executeMove(this.opposingColor, col)) {
					int boardScore;
					
					List<Pair<Integer, Integer>> eval = miniMax(true, remainingDepth - 1, alpha, beta, col, tmpBoard);
					boardScore = eval.get(0).first;
					
					toReturn.add(new Pair<Integer, Integer>(boardScore, col));

					if(boardScore < minEval) minCol = col;
					minEval = Integer.min(minEval, boardScore);
					beta = Integer.min(beta,  boardScore);
					tmpBoard.removePiece(col); //Backtracking

					if(beta <= alpha) {
						break;
					}
				} else {
					continue;
				}
			}
			Collections.sort(toReturn);

			return toReturn;
		}

	}

	public boolean playMove() {
		//Use iterative deepening w/transposition table to improve move ordering and performance for previously seen board states
		//Board newBoard = new Board(this.gameBoard);
		List<Pair<Integer, Integer>> nextMoves = null;
		for(int i = 1; i <= this.depth; i++) {
			nextMoves = miniMax(true, i, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, this.gameBoard);
			nextMoves.sort(Collections.reverseOrder());
			
			int index = 0;
			for(Pair<Integer, Integer> p : nextMoves) {
				this.moveOrder[index] = p.second;
				index++;
			}
		}
		//Scanner wait = new Scanner(System.in);
		//int t = wait.nextInt();
		
		System.out.println("Move Preferences");
		for(Pair<Integer, Integer> p : nextMoves) {
			System.out.print(p.first + "\t");
		}
		System.out.println();
		for(Pair<Integer, Integer> p : nextMoves) {
			System.out.print(p.second + "\t");
		}
		System.out.println();
		
		System.out.println("Minimax Score: " + nextMoves.get(0).first);
		System.out.println("AI plays in row " + nextMoves.get(0).second);
		return gameBoard.executeMove(this.color, nextMoves.get(0).second);
	}
	
}
