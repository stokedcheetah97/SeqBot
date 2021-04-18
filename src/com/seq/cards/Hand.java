package com.seq.cards;

import java.util.*;

import com.seq.SeqBot;

public class Hand {

	public static List<Card> get() {
		List<Card> hand = new ArrayList<>();
		for( Card card: CARDS.keySet() ) {
			hand.add( card );
			if(CARDS.get( card ) == 2 ) hand.add( card );
		}
		return hand;
	}

	static boolean hasCard( Card card ) {
		return CARDS.keySet().contains( card );
	}
	
	public static void addCard( Card card ) throws Exception {
		if( get().size() >= SeqBot.NUM_CARDS )
			throw new Exception( "Cannot have more than " + SeqBot.NUM_CARDS + " cards in hand!" );
		if( CARDS.get( card ) == null ) CARDS.put( card, 0 );
		CARDS.put( card, CARDS.get( card ) + 1 );
		if( CARDS.get( card ) > 2 )
			throw new Exception( "Cannot have more than 2 of the same card in hand.  Found " + CARDS.get( card ) + " for card: " + card );
	}
	
	public static void removeCard( Card card ) throws Exception {
		if( CARDS.get( card ) == null ) 
			throw new Exception( "Cannot remove card " + card + " - it is not in hand." );
		if( CARDS.get( card ) == 2 ) CARDS.put( card, 1 );
		else CARDS.remove( card );
	}

	private static final Map<Card, Integer> CARDS = new TreeMap<>();
}
