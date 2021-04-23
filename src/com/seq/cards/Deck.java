package com.seq.cards;

import java.util.*;
import com.seq.SeqBot;

/**
 * There are 96 board cards + 8 Jacks = 104 initial deck size
 *
 */
public class Deck {
	
	public static int countAvailableCards( Card card ) {
		return PLAYED_CARDS.get( card ) == null ? 2 : 2 - PLAYED_CARDS.get( card );
	}
	
	public static void returnCard( Card card )  throws Exception {
		System.out.println( "Return card to deck: " + card );
		if( !PLAYED_CARDS.containsKey( card ) )
			throw new Exception( "Cannot return card to deck, it has not been played: " + card );
		if( PLAYED_CARDS.get( card ) == 2 ) 
			PLAYED_CARDS.put( card, 1 ); 
		else
			PLAYED_CARDS.remove( card );
	}
	
	public static void main(String[] args) {
		try {
			Card c = new Card(CardSuit.CLUBS, CardRank.CARD_K);
			Map<Card, Integer> map = new TreeMap<>();
			map.put( c, 1 );
			map.put(c, map.get( c ) + 1);
			System.out.println("Test inc: " + map.get( c ));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void playCard( Card card )  throws Exception {
		System.out.println( "Play card: " + card );
		if( !PLAYED_CARDS.containsKey( card ) ) 
			PLAYED_CARDS.put( card, 0 );
		PLAYED_CARDS.put( card, PLAYED_CARDS.get( card ) + 1 );
		if( PLAYED_CARDS.get( card ) > 2 ) 
			throw new Exception( "Cannot play 3 of the same card!  Card = " + card.toString() );
		
	}
	
	public static void playJack( Card jack, Card target )  throws Exception {
		if( jack.isTwoEyeJack() ) {
			twoEyeJacks--;
		}
		if( jack.isOneEyeJack() ) {
			oneEyeJacks--;
			if( !PLAYED_CARDS.containsKey( target ) ) 
				throw new Exception( "Cannot remove unplayed card: " + target );
			if( !REMOVED_CARDS.containsKey( target ) ) 
				REMOVED_CARDS.put( target, 0 );
			REMOVED_CARDS.put( target, REMOVED_CARDS.get( target ) + 1 );
		}
	}
	
	public static int getDeckSize() {
		return 104 - PLAYED_CARDS.size() - ( 4 - oneEyeJacks ) - ( 4 - twoEyeJacks );
	}
	
	public static int countOneEyeJacks() {
		return oneEyeJacks;
	}
	
	public static int countTwoEyeJacks() {
		return twoEyeJacks;
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

	private static Map<Card, Integer> PLAYED_CARDS = new TreeMap<>();
	private static Map<Card, Integer> REMOVED_CARDS = new TreeMap<>();
	private static int oneEyeJacks = 4;
	private static int twoEyeJacks = 4;
}
