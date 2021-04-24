package com.seq.cards;

import java.util.Arrays;

public class Card implements Comparable<Card>{

	private String suit = "";
	private String rank = "";
	
	public Card( String suit, String rank ) {
		this.suit = suit;
		this.rank = rank;
	}
	
	public boolean isJack() {
		return rank.equals( CardRank.CARD_J );
	}
		
	public boolean isOneEyeJack() {
		return isJack() && (suit.equals( CardSuit.HEARTS ) || suit.equals( CardSuit.SPADES ) );
	}
	
	public boolean isTwoEyeJack() {
		return isJack() && (suit.equals( CardSuit.DIAMONDS ) || suit.equals( CardSuit.CLUBS ) );
	}
	
	public String getSuit() {
		return suit;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return this.rank + this.suit;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( rank == null ) ? 0: rank.hashCode() );
		result = prime * result + ( ( suit == null ) ? 0: suit.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if( this == obj ) return true;
		if( !( obj instanceof Card ) ) return false;
		Card other = (Card) obj;
		if( rank == null ) {
			if( other.rank != null ) return false;
		} else if( !rank.equals( other.rank ) ) return false;
		if( suit == null ) {
			if( other.suit != null ) return false;
		} else if( !suit.equals( other.suit ) ) return false;
		return true;
	}

	@Override
	public int compareTo( Card o ) {
		int comp = Arrays.asList(CardRank.RANK_VALUES).get( Arrays.asList(CardRank.RANK_NAMES).indexOf( this.rank ) )
				.compareTo( Arrays.asList(CardRank.RANK_VALUES).get( Arrays.asList(CardRank.RANK_NAMES).indexOf( o.rank ) ) );
		if( comp != 0 ) return comp;
		return Arrays.asList(CardSuit.SUIT_VALUES).get( Arrays.asList(CardSuit.SUIT_CODES).indexOf( this.suit ) )
				.compareTo( Arrays.asList(CardSuit.SUIT_VALUES).get( Arrays.asList(CardSuit.SUIT_CODES).indexOf( o.suit ) ) );
	}
}
