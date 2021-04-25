package com.seq.cards;

import java.util.*;

/**
 * There are 96 board cards + 8 Jacks = 104 initial deck size
 *
 */
public class Deck {
	
	public static int countAvailableCards( Card card ) {
		return PLAYED_CARDS.get( card ) == null ? 2 : 2 - PLAYED_CARDS.get( card );
	}
	
	public static void add( Card jack )  throws Exception {
		System.out.println( "Add jack to deck: " + jack );
		if( JACKS.containsKey( jack ) )
			if( JACKS.get( jack ) == 2 ) 
				JACKS.put( jack, 1 ); 
			else
				JACKS.remove( jack );
		else if( !PLAYED_CARDS.containsKey( jack ) )
			throw new Exception( "Cannot return card to deck, it has not been played: " + jack );
		if( PLAYED_CARDS.containsKey( jack ) && PLAYED_CARDS.get( jack ) == 2 ) 
			PLAYED_CARDS.put( jack, 1 ); 
		else if( PLAYED_CARDS.containsKey( jack ) )
			PLAYED_CARDS.remove( jack );
	}
	
	public static void remove( Card card )  throws Exception {
		System.out.println( "Remove card from deck: " + card );
		if( !PLAYED_CARDS.containsKey( card ) ) 
			PLAYED_CARDS.put( card, 0 );
		PLAYED_CARDS.put( card, PLAYED_CARDS.get( card ) + 1 );
		if( PLAYED_CARDS.get( card ) > 2 ) 
			throw new Exception( "Cannot play 3 of the same card!  Card = " + card.toString() );
	}
	
	public static void remove( Card jack, Card target )  throws Exception {
		if( !JACKS.containsKey( jack ) ) 
			JACKS.put( jack, 0 );
		JACKS.put( jack, JACKS.get( jack ) + 1 );
		
		if( jack.isOneEyeJack() && target != null ) {
			if( !PLAYED_CARDS.containsKey( target ) ) 
				throw new Exception( "Cannot remove unplayed card: " + target );
			removeCard( target );
		}
	}
	
	public static void removeCard( Card card ) {
		if( !REMOVED_CARDS.containsKey( card ) ) 
			REMOVED_CARDS.put( card, 0 );
		REMOVED_CARDS.put( card, REMOVED_CARDS.get( card ) + 1 );
	}
	
	public static int getDeckSize() {
		return 104 - PLAYED_CARDS.size() - JACKS.size();
	}
	
	public static int countOneEyeJacks() {
		int count = 4;
		for( Card c: JACKS.keySet() )
			if( c.isOneEyeJack() )
				count--;
		return count;
	}
	
	public static int countTwoEyeJacks() {
		int count = 4;
		for( Card c: JACKS.keySet() )
			if( c.isTwoEyeJack() )
				count--;
		return count;
	}
	
	public static List<Card> getJacks() {
		List<Card> cards = new ArrayList<>();
		for( Card card: JACKS.keySet() ) {
			cards.add( card );
			if(JACKS.get( card ) == 2 ) 
				cards.add( card );
		}
		return cards;
	}
	
	public static List<Card> getPlayedCards() {
		List<Card> cards = new ArrayList<>();
		for( Card card: PLAYED_CARDS.keySet() ) {
			cards.add( card );
			if(PLAYED_CARDS.get( card ) == 2 ) 
				cards.add( card );
		}
		return cards;
	}
	
	public static List<Card> getRemovedCards() {
		List<Card> cards = new ArrayList<>();
		for( Card card: REMOVED_CARDS.keySet() ) {
			cards.add( card );
			if(REMOVED_CARDS.get( card ) == 2 ) 
				cards.add( card );
		}
		return cards;
	}

	private static Map<Card, Integer> JACKS = new TreeMap<>();
	private static Map<Card, Integer> PLAYED_CARDS = new TreeMap<>();
	private static Map<Card, Integer> REMOVED_CARDS = new TreeMap<>();
}
