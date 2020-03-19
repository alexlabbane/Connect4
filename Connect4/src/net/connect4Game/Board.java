package net.connect4Game;

import java.util.ArrayList;
import java.util.Stack;

public class Board {
	
	private ArrayList<ArrayList<Piece>> board;
	private Stack<Integer> lastRow;
	private Stack<Integer> lastColumn;
	private Stack<Integer> lastColor;
	public int moveCount; //42 max
	//Transpose of board is stored
	
	public Board(Board board) {
		//this.lastRow = board.getLastRow();
		//this.lastColumn = board.getLastCol();
		//this.lastColor = board.getLastColor();
		this.moveCount = board.moveCount;
		
		ArrayList<ArrayList<Piece>> tmp = board.getBoard();
		this.board = new ArrayList<ArrayList<Piece>>();
		for(int i = 0; i < 7; i++) {
			this.board.add(new ArrayList<Piece>());
			for(int j = 0; j < 6; j++) {
				Piece newPiece = new Piece(tmp.get(i).get(j).getColor());
				this.board.get(i).add(newPiece);
			}
		}
	}
	
	
	public Board() {
		this.lastRow = new Stack<Integer>();
		this.lastColumn = new Stack<Integer>();
		this.lastColor = new Stack<Integer>();
		this.lastRow.push(0);
		this.lastColumn.push(0);
		this.lastColor.push(0);
		
		this.moveCount = 0;
		
		board = new ArrayList<ArrayList<Piece>>();
		for(int i = 0; i < 7; i++) {
			board.add(new ArrayList<Piece>());
			for(int j = 0; j < 6; j++) {
				board.get(i).add(new Piece(0));
			}
		}
	}
	
	public int getLastRow() { return this.lastRow.peek(); }
	public int getLastCol() { return this.lastColumn.peek(); }
	public int getLastColor() { return this.lastColor.peek(); }
	public ArrayList<ArrayList<Piece>> getBoard() { return this.board; }
	
	public boolean executeMove(int color, int col) {
		//Executes move and returns true if valid; false if not valid
		ArrayList<Piece> column = board.get(col);
		for(int i = column.size() - 1; i >= 0; i--) {
			if(column.get(i).getColor() == 0) {
				column.get(i).setColor(color);
				this.lastRow.push(i);
				this.lastColumn.push(col);
				this.lastColor.push(color);
				this.moveCount++;
				//System.out.println("COLOR " + color);
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
				this.lastColor.pop();
				this.lastColumn.pop();
				this.lastRow.pop();
				this.moveCount--;
				return true;
			}
		}
		
		
		return false;
	}
	
	public int checkWin() {
		//0 - No win, 1 - Blue win, 2 - Red win, -1 - Draw
		int count = 0;
		
		//Count horizontal
		for(int i = -3; i <= 3; i++) {
			int current = this.lastColumn.peek() + i;
			if(current >= 0 && current < 7) {
				if(board.get(current).get(this.lastRow.peek()).getColor() == this.lastColor.peek()) count++;
				else count = 0;
				
				if(count >= 4) return this.lastColor.peek();
			}
		}
		
		if(count >= 4) return this.lastColor.peek();
		count = 0;
		
		//Count vertical
		for(int i = -3; i <= 3; i++) {
			int current = this.lastRow.peek() + i;
			if(current >= 0 && current < 6) {
				if(board.get(this.lastColumn.peek()).get(current).getColor() == this.lastColor.peek()) count++;
				else count = 0;
				
				if(count >= 4) return this.lastColor.peek();
			}
		}
		if(count >= 4) return this.lastColor.peek();
		count = 0;
		
		//Check diagonal (top-left to bottom-right)
		for(int i = -3; i <= 3; i++) {
			int currentRow = this.lastRow.peek() + i;
			int currentCol = this.lastColumn.peek() + i;
			if(currentRow >= 0 && currentRow < 6 && currentCol >= 0 && currentCol < 7) {
				if(board.get(currentCol).get(currentRow).getColor() == this.lastColor.peek()) count++;
				else count = 0;
				
				if(count >= 4) return this.lastColor.peek();
			}
		}
		if(count >= 4) return this.lastColor.peek();
		count = 0;
		
		//Check diagonal (bottom-left to top-right)
		for(int i = -3; i <= 3; i++) {
			int currentRow = this.lastRow.peek() - i;
			int currentCol = this.lastColumn.peek() + i;
			if(currentRow >= 0 && currentRow < 6 && currentCol >= 0 && currentCol < 7) {
				if(board.get(currentCol).get(currentRow).getColor() == this.lastColor.peek()) count++;
				else count = 0;
				
				if(count >= 4) return this.lastColor.peek();
			}
		}
		if(count >= 4) return this.lastColor.peek();

		
		if(this.moveCount >= 42) return -1;
		return 0;
	}
	
	public int getScore(int color) {
		//TODO: Produce better evaluation function
		//Count spaces that would complete a connect 4
		//The lower the row, the higher the score
		
		int evenOdd = 0; //1 if odd; 2 if even
		int score = 0;
		int count = 0;
		
		for(int i = 0; i < 6; i++) { //row
			if(i % 2 == 0) evenOdd = 2;
			else evenOdd = 1;
			
			for(int j = 0; j < 7; j++) { //col
				if(this.board.get(j).get(i).getColor() != 0) continue; //Space already played
				boolean connect4Space = false;
				
				//Horizontal
				count = 0;
				for(int k = -3; k <= 3; k++) {
					int currentCol = j + k;
					if(currentCol < 0 || currentCol > 6) continue;
					
					if(k == 0) count++;
					else if(this.board.get(currentCol).get(i).getColor() == color) count++;
					else count = 0;
					
					if(count >= 4) {
						connect4Space = true;
						break;
					}
				}
				
				if(connect4Space) {
					score += (1) * 10;
					score += (i + 1); //Small bonus for lower rows
					if(color == evenOdd) score *= 2;
					continue;
				}
				
				//Vertical
				count = 0;
				for(int k = -3; k <= 3; k++) {
					int currentRow = i + k;
					if(currentRow < 0 || currentRow > 5) continue;
					
					if(k == 0) count++;
					else if(this.board.get(j).get(currentRow).getColor() == color) count++;
					else count = 0;
					
					if(count >= 4) {
						connect4Space = true;
						break;
					}
				}
				
				if(connect4Space) {
					score += (1) * 10;
					score += (i + 1); //Small bonus for lower rows
					if(color == evenOdd) score *= 2;
					continue;
				}
				
				//Diagonal (top-left to bottom-right)
				count = 0;
				for(int k = -3; k <= 3; k++) {
					int currentRow = i + k;
					int currentCol = j + k;
					if(currentRow < 0 || currentRow > 5) continue;
					if(currentCol < 0 || currentCol > 6) continue;
					
					if(k == 0) count++;
					else if(this.board.get(currentCol).get(currentRow).getColor() == color) count++;
					else count = 0;
					
					if(count >= 4) {
						connect4Space = true;
						break;
					}
				}
				
				if(connect4Space) {
					score += (1) * 10;
					score += (i + 1); //Small bonus for lower rows
					if(color == evenOdd) score *= 2;
					continue;
				}
				
				//Diagonal (top-right to bottom-left)
				count = 0;
				for(int k = -3; k <= 3; k++) {
					int currentRow = i - k;
					int currentCol = j + k;
					if(currentRow < 0 || currentRow > 5) continue;
					if(currentCol < 0 || currentCol > 6) continue;
					
					if(k == 0) count++;
					else if(this.board.get(currentCol).get(currentRow).getColor() == color) count++;
					else count = 0;
					
					if(count >= 4) {
						connect4Space = true;
						break;
					}
					
				}
				
				if(connect4Space) {
					score += (1) * 10;
					score += (i + 1); //Small bonus for lower rows
					if(color == evenOdd) score *= 2 * (i + 1); //Big bonus for evenOdd & lower rows
					continue;
				}
				
			}
		}
		
		return score;
	}
	
	public void printBoard() {
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
