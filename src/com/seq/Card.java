package com.seq;

public class Card implements Comparable<Card>{

	String suit = "";
	String rank = "";
	
	public Card( String suit, String rank ) {
		this.suit = suit;
		this.rank = rank;
	}
	
	@Override
	public String toString() {
		return this.rank + this.suit;
	}

	@Override
	public int compareTo( Card o ) {
		int comp = CardRank.rankValues.get( CardRank.rankNames.indexOf( this.rank ) ).compareTo( CardRank.rankValues.get( CardRank.rankNames.indexOf( o.rank ) ) );
		if( comp != 0 ) 
			return comp;
		return CardSuit.suitValues.get( CardSuit.suitCodes.indexOf( this.suit ) ).compareTo( CardSuit.suitValues.get( CardSuit.suitCodes.indexOf( o.suit ) ) );
	}
}
