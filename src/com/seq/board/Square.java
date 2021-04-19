package com.seq.board;

import org.apache.commons.lang3.StringUtils;
import com.seq.*;
import com.seq.cards.*;

public class Square implements Comparable<Square> {

	public Square( int pos, String color ) {
		this.pos = pos;
		this.color = color == null ? "" : color;
	}

	public boolean isWild() {
		return Board.getSquare( pos ).getCard().equals( new Card(CardSuit.WILD, CardRank.WILD) );
	}
	
	public Card getCard() {
		return Board.getCardMatrix()[getRow()][getCol()];
	}
	
	public static boolean isOpponents( Square square ) {
		return square != null && StringUtils.isNotBlank( square.color ) && !square.color.equals( SeqBot.get().getMyTokenColor() );
	}
	
	public static boolean isMine( Square square ) {
		return square != null && StringUtils.isNotBlank( square.color ) && !square.color.equals( SeqBot.get().getOpponentTokenColor() );
	}
	
	public Integer getPos() {
		return pos;
	}

	public String getColor() {
		return color;
	}
	
	public void setColor( String color ) {
		this.color = color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( pos == null ) ? 0: pos.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if( this == obj ) return true;
		if( !( obj instanceof Square ) ) return false;
		Square other = (Square) obj;
		if( pos == null ) {
			if( other.pos != null ) return false;
		} else if( !pos.equals( other.pos ) ) return false;
		return true;
	}

	@Override
	public int compareTo( Square o ) {
		return o.pos.compareTo( pos );
	}
	
	@Override
	public String toString() {
		return getCard() + "@" + pos + (StringUtils.isNotBlank(color) ? "=" + color : "");
	}
	
	private int getRow() {
		return ( pos - 1 ) / 10;
	}
	
	private int getCol() {
		return ( pos - 1 ) % 10;
	}
	
	private Integer pos;
	private String color;
}
