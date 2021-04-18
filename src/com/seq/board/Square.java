package com.seq.board;

import java.util.*;

import com.seq.*;
import com.seq.cards.*;

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
		return square != null && !square.color.isEmpty() && !square.color.equals( SeqBot.get().getMyTokenColor() );
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
	
	public Map<Integer, Set<Square>> getMyAxisRanges() {
		return myAxisRanges;
	}

	public Integer getPositionNumber() {
		return positionNumber;
	}

	public void setPositionNumber(Integer positionNumber) {
		this.positionNumber = positionNumber;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public int compareTo( Square o ) {
		return o.positionNumber.compareTo( this.positionNumber );
	}
	
	@Override
	public String toString() {
		return "[" + getCard().toString() + "=" + this.color + "]";
	}
	
	private Integer positionNumber;
	private int row;
	private int col;
	private String color;
	private Map<Integer, Set<Square>> myAxisRanges = new HashMap<>();
	private Set<Square> myRange = new TreeSet<>();
	private Set<Square> opponentRange = new TreeSet<>();
}
