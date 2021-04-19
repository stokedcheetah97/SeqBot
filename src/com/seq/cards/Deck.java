package com.seq.cards;

import java.util.*;
import com.seq.SeqBot;

/**
 * There are 96 board cards + 8 Jacks = 104 initial deck size
 *
 */
public class Deck {
	
	public static int countAvailableCards( Card card ) {
		return PLAYED_CARDS.get( card ) == null ? 2 : 2 - PLAYED_CARDS.get( card ) - SeqBot.NUM_CARDS;
	}
	
	public static void returnCard( Card card )  throws Exception {
		System.out.println( "Return card to deck: " + card );
		if( PLAYED_CARDS.get( card ) == null )
			throw new Exception( "Cannot return card to deck, it has not been played: " + card );
		if( PLAYED_CARDS.get( card ) == 1 ) PLAYED_CARDS.remove( card );
		else PLAYED_CARDS.put( card, PLAYED_CARDS.get( card ) + 1 );
	}
	
	public static void playCard( Card card )  throws Exception {
		System.out.println( "Play card: " + card );
		if( PLAYED_CARDS.get( card ) == null ) PLAYED_CARDS.put( card, 0 );
		PLAYED_CARDS.put( card, PLAYED_CARDS.get( card ) + 1 );
		if( PLAYED_CARDS.get( card ) > 2 ) 
			throw new Exception( "Cannot play 3 of the same card!  Card = " + card.toString() );
		
	}
	
	public static void playJack( Card card, Card target )  throws Exception {
		if( card.getSuit().equals( CardSuit.DIAMONDS ) || card.getSuit().equals( CardSuit.CLUBS ) ) {
			twoEyeJacks--;
			System.out.println( "Use Two-Eye Jack to play " + target );
		} else {
			oneEyeJacks--;
			System.out.println( "Use One-Eye Jack to remove " + target );
			if( PLAYED_CARDS.get( target ) == 0 ) 
				throw new Exception( "Cannot remove unplayed card: " + target );
			
			if( REMOVED_CARDS.get( target ) == null ) REMOVED_CARDS.put( target, 0 );
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

	private static Map<Card, Integer> PLAYED_CARDS = new TreeMap<>();
	private static Map<Card, Integer> REMOVED_CARDS = new TreeMap<>();
	private static int oneEyeJacks = 4;
	private static int twoEyeJacks = 4;
}
