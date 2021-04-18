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
	
	public static void playCard( Card card )  throws Exception {
		System.out.println( "Play card: " + card );
		if( card.getRank().equals( CardRank.CARD_J ) ) 
			playJack( card );
		else {
			if( PLAYED_CARDS.get( card ) == null ) PLAYED_CARDS.put( card, 0 );
			PLAYED_CARDS.put( card, PLAYED_CARDS.get( card ) + 1 );
			if( PLAYED_CARDS.get( card ) > 2 ) 
				throw new Exception( "Cannot play 3 of the same card!  Card = " + card.toString() );
		}
	}
	
	private static void playJack( Card card )  throws Exception {
		
		if( card.getSuit().equals( CardSuit.DIAMONDS ) || card.getSuit().equals( CardSuit.CLUBS ) ) {
			twoEyeJacks--;
			System.out.println( "Play Two-Eye Jack as wild for: " + card );
		} else {
			oneEyeJacks--;
			System.out.println( "Play One-Eye Jack to remove: " + card );
			if( PLAYED_CARDS.get( card ) == 0 ) 
				throw new Exception( "Cannot remove unplayed card: " + card );
			
			if( REMOVED_CARDS.get( card ) == null ) REMOVED_CARDS.put( card, 0 );
			
			REMOVED_CARDS.put( card, REMOVED_CARDS.get( card ) + 1 );
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
