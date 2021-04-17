package com.seq;

import java.util.*;

public class Square implements Comparable<Square> {

	public Square( int positionNumber, String color ) {
		this.row = ( positionNumber - 1 ) / 10;
		this.col = ( positionNumber - 1 ) % 10;
		this.positionNumber = positionNumber;
		this.color = color == null ? "" : color;
	}
	
	public Card getCard() {
		return Board.get().getCardMatrix()[this.row][this.col];
	}
	
	public static boolean isOpponents( Square square ) {
		return square != null && !square.color.isEmpty() && !square.color.equals( SeqBot.get().myTokenColor );
	}
	
	public void addToMyRange( Collection<Square> squares ) {
		this.myRange.addAll( squares );
	}
	
	public void addToOpponentRange( Collection<Square> squares ) {
		this.opponentRange.addAll( squares );
	}
	
	public Set<Square> getMyRange() {
		return this.myRange;
	}

	public Set<Square> getOpponentRange() {
		return this.opponentRange;
	}

	@Override
	public int compareTo( Square o ) {
		return o.positionNumber.compareTo( this.positionNumber );
	}
	
	@Override
	public String toString() {
		return "[" + getCard().toString() + "=" + this.color + "]";
	}
	
	Map<Integer, Set<Square>> myAxisRanges = new HashMap<>();
	Integer positionNumber;
	int row;
	int col;
	String color;
	private Set<Square> myRange = new TreeSet<>();
	private Set<Square> opponentRange = new TreeSet<>();
}
