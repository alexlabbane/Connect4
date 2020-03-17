package net.connect4Game;

import java.util.ArrayList;

public class Board {
	
	private ArrayList<ArrayList<Piece>> board;
	private int lastRow;
	private int lastColumn;
	private int lastColor;
	private int moveCount; //42 max
	//Transpose of board is stored
	
	public Board() {
		this.lastRow = 0;
		this.lastColumn = 0;
		this.lastColor = 0;
		this.moveCount = 0;
		
		board = new ArrayList<ArrayList<Piece>>();
		for(int i = 0; i < 7; i++) {
			board.add(new ArrayList<Piece>());
			for(int j = 0; j < 6; j++) {
				board.get(i).add(new Piece(0));
			}
		}
	}
	
	public int getLastRow() { return this.lastRow; }
	public int getLastCol() { return this.lastColumn; }
	public int getLastColor() { return this.lastColor; }
	public ArrayList<ArrayList<Piece>> getBoard() { return this.board; }
	
	public boolean executeMove(int color, int col) {
		//Executes move and returns true if valid; false if not valid
		ArrayList<Piece> column = board.get(col);
		for(int i = column.size() - 1; i >= 0; i--) {
			if(column.get(i).getColor() == 0) {
				column.get(i).setColor(color);
				this.lastRow = i;
				this.lastColumn = col;
				this.lastColor = color;
				this.moveCount++;
				System.out.println("COLOR " + color);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean removePiece(int col) {
		ArrayList<Piece> column = board.get(col);
		for(int i = 0; i < column.size(); i++) {
			if(column.get(i).getColor() != 0) {
				column.get(i).setColor(0);
				this.moveCount--;
			}
		}
		
		
		return false;
	}
	
	public int checkWin() {
		//0 - No win, 1 - Blue win, 2 - Red win, -1 - Draw
		int count = 0;
		
		//Count horizontal
		for(int i = -3; i <= 3; i++) {
			int current = this.lastColumn + i;
			if(current >= 0 && current < 7) {
				if(board.get(current).get(this.lastRow).getColor() == this.lastColor) count++;
			}
		}
		
		if(count >= 4) return this.lastColor;
		count = 0;
		
		//Count vertical
		for(int i = -3; i <= 3; i++) {
			int current = this.lastRow + i;
			if(current >= 0 && current < 6) {
				if(board.get(this.lastColumn).get(current).getColor() == this.lastColor) count++;
			}
		}
		if(count >= 4) return this.lastColor;
		count = 0;
		
		//Check diagonal (top-left to bottom-right)
		for(int i = -3; i <= 3; i++) {
			int currentRow = this.lastRow + i;
			int currentCol = this.lastColumn + i;
			if(currentRow >= 0 && currentRow < 6 && currentCol >= 0 && currentCol < 7) {
				if(board.get(currentCol).get(currentRow).getColor() == this.lastColor) count++;
			}
		}
		if(count >= 4) return this.lastColor;
		count = 0;
		
		//Check diagonal (top-left to bottom-right)
		for(int i = -3; i <= 3; i++) {
			int currentRow = this.lastRow - i;
			int currentCol = this.lastColumn + i;
			if(currentRow >= 0 && currentRow < 6 && currentCol >= 0 && currentCol < 7) {
				if(board.get(currentCol).get(currentRow).getColor() == this.lastColor) count++;
			}
		}
		if(count >= 4) return this.lastColor;

		
		if(this.moveCount >= 42) return -1;
		return 0;
	}
	
	public int getScore(int color) {
		return 1;
	}
	
	void printBoard() {
		//Print board (transpose of the arraylist)
		System.out.println();
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 7; j++) {
				System.out.print(board.get(j).get(i).toString() + " ");
			}
			System.out.println();
		}
		
		System.out.println();
	}
	
}
