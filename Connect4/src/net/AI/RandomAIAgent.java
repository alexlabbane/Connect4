package net.AI;

import java.util.Random;

import net.connect4Game.Board;
import net.connect4Game.bitBoard;

public class RandomAIAgent {
	
	private bitBoard gameBoard;
	private Random moveGenerator;
	private int color;
	private int opponentColor;
	private int numIterations;
	
	public RandomAIAgent(int color, bitBoard gameBoard, int numIterations) {
		this.gameBoard = gameBoard;
		this.color = color;
		this.moveGenerator = new Random();
		this.numIterations = numIterations;
		
		if(this.color == 1) this.opponentColor = 2;
		else this.opponentColor = 1;
	}
	
	private int simulateGames(int numGames, int firstMove) {
		int numLosses = 0;
		
		for(int i = 0; i < this.numIterations; i++) {
			numLosses += this.simulateGame(firstMove);
		}
		
		return numLosses;
	}
	
	private int simulateGame(int firstMove) {
		int movesMade = 1;
		int nextMove = this.moveGenerator.nextInt(Integer.MAX_VALUE - 1000) % 7;
		boolean AITurn = false;
		
		if(this.gameBoard.executeMove(this.color, firstMove)) {
			while(this.gameBoard.checkWin(this.color) == 0) {
				if(AITurn) {
					if(this.gameBoard.executeMove(this.color, nextMove)) {
						movesMade++;
						AITurn = !AITurn;
					}
				} else {
					if(this.gameBoard.executeMove(this.opponentColor, nextMove)) {
						movesMade++;
						AITurn = !AITurn;
					}
				}
				
				nextMove = this.moveGenerator.nextInt(Integer.MAX_VALUE - 1000) % 7;
			}
			
			int val = 0;
			if(this.gameBoard.checkWin(this.color) == this.opponentColor) val = (42 - movesMade); //Loss
			else if(this.gameBoard.checkWin(this.color) != this.color) val = 0; //Draw
			else val = 0; //Win
			
			while(movesMade > 0) {
				this.gameBoard.removeLastPiece();
				movesMade--;
			}
			
			return val;
		
			
		} else {
			//Invalid move
			return 1000000;
		}
		
	}
	
	public int getScore() {
		//Gets score instead of playing move
		int bestRow = 3;
		int bestVal = Integer.MAX_VALUE; //Minimize value
		
		for(int i = 0; i < 7; i++) {
			int val = this.simulateGames(this.numIterations, i);
			//System.out.println(val);
			if(val < bestVal) {
				bestVal = val;
				bestRow = i;
			}
		}
		
		return bestVal;
	}
	
	public boolean playMove() {
		int bestRow = 3;
		int bestVal = Integer.MAX_VALUE; //Minimize value
		
		for(int i = 0; i < 7; i++) {
			int val = this.simulateGames(this.numIterations, i);
			if(val < bestVal) {
				bestVal = val;
				bestRow = i;
			}
		}
		
		System.out.println("AI plays in row " + bestRow);
		return this.gameBoard.executeMove(this.color, bestRow);
	}
}
