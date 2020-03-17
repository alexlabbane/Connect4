package net.connect4Game;

public class Piece {
	private int color; //0 is null, 1 is blue, 2 is red
	
	public Piece(int col) {
		this.color = col;
	}
	
	public int getColor() {
		//0 - null, 1 - blue, 2 - red
		return this.color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public String toString() {
		if(this.color == 0) { 
			return "Null";
		} else if(this.color == 1){
			return "Blue";
		}
		
		return "Red ";
	}
}
