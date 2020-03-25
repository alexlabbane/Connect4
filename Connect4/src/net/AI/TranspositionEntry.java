package net.AI;

import java.util.ArrayList;
import java.util.List;

import util.Pair;

public class TranspositionEntry {

	public long zobrist;
	public int depth;
	public int flag;
	public int eval;
	public int ancient;
	public int move; //Next column to move based on this position
	public List<Pair<Integer, Integer>> evalOrder;
	
	public TranspositionEntry(long zobrist, int depth, int flag, int eval, int ancient, int move, List<Pair<Integer, Integer>> evalOrder) {
		this.zobrist = zobrist;
		this.depth = depth;
		this.flag = flag;
		this.eval = eval;
		this.ancient = ancient;
		this.move = move;
		this.evalOrder = evalOrder;
	}
	
	public TranspositionEntry(long zobrist, int depth, int flag, int eval, int ancient) {
		//If no best move to store
		this.zobrist = zobrist;
		this.depth = depth;
		this.flag = flag;
		this.eval = eval;
		this.ancient = ancient;
		this.move = -1;
	}
}
