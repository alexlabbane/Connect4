package net.AI;

import java.util.HashMap;

public class TranspositionTable {
	//A transposition table class for AI Agents to use
	HashMap<Long, TranspositionEntry> table;
	
	
	public TranspositionTable () {
		table = new HashMap<Long, TranspositionEntry>();
	}
	
	public void put(TranspositionEntry entry) {
		table.put(entry.zobrist, entry);
	}
	
	public boolean has(long zobrist) {
		return this.table.containsKey(zobrist);
	}
}
