package com.seq.cards;

import java.util.Arrays;

public class Card implements Comparable<Card>{

	private String suit = "";
	private String rank = "";
	
	public Card( String suit, String rank ) {
		this.suit = suit;
		this.rank = rank;
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
	public int compareTo( Card o ) {
		int comp = Arrays.asList(CardRank.RANK_VALUES).get( Arrays.asList(CardRank.RANK_NAMES).indexOf( this.rank ) )
				.compareTo( Arrays.asList(CardRank.RANK_VALUES).get( Arrays.asList(CardRank.RANK_NAMES).indexOf( o.rank ) ) );
		if( comp != 0 ) return comp;
		return Arrays.asList(CardSuit.SUIT_VALUES).get( Arrays.asList(CardSuit.SUIT_CODES).indexOf( this.suit ) )
				.compareTo( Arrays.asList(CardSuit.SUIT_VALUES).get( Arrays.asList(CardSuit.SUIT_CODES).indexOf( o.suit ) ) );
	}
}
