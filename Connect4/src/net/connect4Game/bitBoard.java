package net.connect4Game;

import java.util.Stack;

public class bitBoard {
	private long current;
	private long mask;
	private int currentColor;
	private Stack<Long> lastMask;
	private Stack<Long> lastCurrent;
	private Stack<Integer> lastColor;
	
	private final static int HEIGHT = 6;
	private final static int WIDTH = 7;
	private final static long ONE = 1;
	private final static long FIRST_SEVEN_BITS = 255;
	private final static long TOP_MASK_ZERO = Long.parseLong("-283691315109953");
	
	//Color 1 = blue; color 2 = red
	public bitBoard() {
		this.lastMask = new Stack<Long>();
		this.lastCurrent = new Stack<Long>();
		this.lastColor = new Stack<Integer>();
		this.current = 0;
		this.mask = 0;
		this.currentColor = 1;
	}
	
	public Long getLastMask() { return this.lastMask.peek(); }
	public Long getLastCurrent() { return this.lastCurrent.peek(); }
	public int getLastColor() { return this.lastColor.peek(); }
	
	public boolean executeMove(int color, int col) {
		this.lastMask.push(this.mask);
		this.lastCurrent.push(this.current);
		this.lastColor.push(this.currentColor);
				
		//Keep current as opponents board so that when we add piece to mask, it is effectively being added to the player
		if(this.currentColor == color)
			this.current = this.current ^ this.mask;
		
		if((this.mask & topMask(col)) != 0)
			return false;
		
		this.mask |= mask + bottomMask(col);
		
		if(color == 1) this.currentColor = 2;
		else this.currentColor = 1;
		
		return true;
	}
	
	public boolean removeLastPiece() {
		if(this.lastMask.empty())
			return false;

		this.mask = getLastMask();
		this.current = getLastCurrent();
		this.currentColor = getLastColor();
		
		this.lastMask.pop();
		this.lastCurrent.pop();
		this.lastColor.pop();
		return true;
	}
	
	public int countThreeSeq(int color) {
		if(this.currentColor != color)
			this.current = this.current ^ this.mask;
		
		int result = 0;
		
		//Horizontal consecutive (i.e. 1110)
		long h1 = this.current;
		h1 &= h1 >> (HEIGHT+1);
		h1 &= h1 >> (HEIGHT+1);
		h1 = h1 >> (HEIGHT+1);
		
		//Horizontal consecutive (i.e. 0111)
		long h2 = this.current;
		h2 &= h2 << (HEIGHT+1);
		h2 &= h2 << (HEIGHT+1);
		h2 = h2 << (HEIGHT+1);
		
		//Horizontal gap left (i.e. 1011)
		long h3 = this.current;
		h3 &= this.current << 3*(HEIGHT+1);
		h3 &= this.current << 2*(HEIGHT+1);
		h3 = h3 >> (HEIGHT+1);
		
		//Horizontal gap right (i.e. 1101)
		long h4 = this.current;
		h4 &= this.current >> 3*(HEIGHT+1);
		h4 &= this.current >> 2*(HEIGHT+1);
		h4 = h4 << (HEIGHT+1);
		
		//Diagonal 1 consecutive (i.e. 1110)
		long d11 = this.current;
		d11 &= d11 >> (HEIGHT+2);
		d11 &= d11 >> (HEIGHT+2);
		d11 = d11 >> (HEIGHT+2);
		
		//Diagonal 1 consecutive (i.e. 0111)
		long d12 = this.current;
		d12 &= d12 << (HEIGHT+2);
		d12 &= d12 << (HEIGHT+2);
		d12 = d12 << (HEIGHT+2);
		
		//Diagonal 1 gap left (i.e. 1011)
		long d13 = this.current;
		d13 &= this.current << 3*(HEIGHT+2);
		d13 &= this.current << 2*(HEIGHT+2);
		d13 = d13 >> (HEIGHT+2);
		
		//Diagonal 1 gap right (i.e. 1101)
		long d14 = this.current;
		d14 &= this.current >> 3*(HEIGHT+2);
		d14 &= this.current >> 2*(HEIGHT+2);
		d14 = d14 << (HEIGHT+2);
		
		//Diagonal 2 consecutive (i.e. 1110)
		long d21 = this.current;
		d21 &= d21 >> (HEIGHT);
		d21 &= d21 >> (HEIGHT);
		d21 = d21 >> (HEIGHT);
		
		//Diagonal 2 consecutive (i.e. 0111)
		long d22 = this.current;
		d22 &= d22 << (HEIGHT);
		d22 &= d22 << (HEIGHT);
		d22 = d22 << (HEIGHT);
		
		//Diagonal 2 gap left (i.e. 1011)
		long d23 = this.current;
		d23 &= this.current << 3*(HEIGHT);
		d23 &= this.current << 2*(HEIGHT);
		d23 = d23 >> (HEIGHT);
		
		//Diagonal 2 gap right (i.e. 1101)
		long d24 = this.current;
		d24 &= this.current >> 3*(HEIGHT);
		d24 &= this.current >> 2*(HEIGHT);
		d24 = d24 << (HEIGHT);
		
		//Vertical (only consecutive can occur because gravity)
		long v = this.current;
		v &= v << 1;
		v &= v << 1;
		v = v << 1;
		
		long threatBoard = TOP_MASK_ZERO & (h1|h2|h3|h4|d11|d12|d13|d14|d21|d22|d23|d24|v);
		result += Long.bitCount(TOP_MASK_ZERO & (h1|h2|h3|h4|d11|d12|d13|d14|d21|d22|d23|d24|v));
		System.out.println(longToBinaryString(TOP_MASK_ZERO & (h1|h2|h3|h4|d11|d12|d13|d14|d21|d22|d23|d24|v)));
		System.out.println(longToBinaryString((h1|h2|h3|h4|d11|d12|d13|d14|d21|d22|d23|d24|v)));

		//TODO: Or threatboard with even/odd boards to return even/odd threat counts
		
		return result;
	}
	
	@Override
	public String toString() {
		String boardString = Long.toBinaryString(this.mask);
		while(boardString.length() < 49) {
			boardString = "0" + boardString;
		}
		
		String finalString = "";
		int count = 0;
		for(int i = 0; i < boardString.length(); i++) {
			finalString += boardString.charAt(i);
			if((count + 1) % 7 == 0) finalString += " ";
			count++;
		}
		
		return finalString;
	}
	
	public String longToBinaryString(long l) {
		String boardString = Long.toBinaryString(l);
		while(boardString.length() < 49) {
			boardString = "0" + boardString;
		}
		
		String finalString = "";
		int count = 0;
		for(int i = 0; i < boardString.length(); i++) {
			finalString += boardString.charAt(i);
			if((count + 1) % 7 == 0) finalString += " ";
			count++;
		}
		
		return finalString;
	}
	
	public static long topMask(int col) {
		return ONE << (HEIGHT - 1) << col*(HEIGHT + 1);
	}
	
	public static long bottomMask(int col) {
		return ONE << col*(HEIGHT+1);
	}
	
	public static long fullMask(int col) {
		return FIRST_SEVEN_BITS << (7*col);
	}
}
