package com.seq;

import java.util.*;

public class Deck {
	
	static Map<Card, Integer> PLAYED_CARDS = new TreeMap<>();
	
	static Map<Card, Integer> REMOVED_CARDS = new TreeMap<>();
	
	static int twoEyedJacks = 0;
	static int fourEyedJacks = 0;
	
	static int countAvailableCards( Card card ) {
		return PLAYED_CARDS.get( card ) == null ? 0 : PLAYED_CARDS.get( card );
	}
	
	static void playTwoEyedJack() {
		
	}

	static void playCard(Card card)  throws Exception {
		if( PLAYED_CARDS.get( card ) == null )
			PLAYED_CARDS.put( card, 0 );
		
		PLAYED_CARDS.put( card, PLAYED_CARDS.get( card ) + 1 );
		if( PLAYED_CARDS.get( card ) > 2 ) 
			throw new Exception( "Cannot play 3 of the same card!  Card = " + card.toString() );
	}
}
