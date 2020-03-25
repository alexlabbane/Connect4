package util;

import java.util.Comparator;

public class Pair<T1 extends Comparable<T1>, T2 extends Comparable<T2>> implements Comparable<Pair> {

	public T1 first;
	public T2 second;
	
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public int compareTo(Pair arg0) {
		// TODO Auto-generated method stub
		return this.first.compareTo((T1) arg0.first);
	}

}
