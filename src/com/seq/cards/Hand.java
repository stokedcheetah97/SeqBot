package com.seq.cards;

import java.util.*;

import com.seq.*;
import com.seq.board.*;
import com.seq.gui.PanelBuilder;

public class Hand {

	public static List<Card> get() {
		List<Card> hand = new ArrayList<>();
		for( Card card: CARDS.keySet() ) {
			hand.add( card );
			if(CARDS.get( card ) == 2 ) hand.add( card );
		}
		return hand;
	}
	
	public static String getOrderedHand() {
		if( SCORES.isEmpty() ) return get().toString();
		List<Card> cards = new ArrayList<>( SCORES.keySet() );
		List<Double> scores = new ArrayList<>();
		for( Card c: cards )
			scores.add( cards.indexOf( c ), SCORES.get( c ) );
		
		for(int j=cards.size() -1; j>0; j--) 
			for(int i=0; i<j; i++) {
				double s1 = scores.get(i);
				double s2 = scores.get(i+1);
				Card c1 = cards.get(i);
				Card c2 = cards.get(i+1);
				if( s1 < s2 ) {
					cards.remove(i);
					cards.add(i, c2);
					cards.remove(i+1);
					cards.add(i+1, c1);
					scores.remove(i);
					scores.add(i, s2);
					scores.remove(i+1);
					scores.add(i+1, s1);
				}
			}

		//Map<Card, Double> temp = new HashMap<>( SCORES ); 
		String output = "";
		
		for( Card c: cards ) {
			output += (output.isEmpty() ? "" : "\n") + SPACER + c.toString() + ":  " + SCORES.get(c).toString();
			if( CARDS.get( c ) == 2 )
				output += "\n" + SPACER + c.toString() + ":  " + SCORES.get(c).toString();
		}
		
		if( !twoEyeJacks.isEmpty() || !oneEyeJacks.isEmpty() ) {
			for( Card c: twoEyeJacks )
				output += "\n" + SPACER  + c.toString();
			for( Card c: oneEyeJacks )
				output += "\n" + SPACER + c.toString();
		}
		
		return output;
	}
	
	public static void updateScore(Card card, Double score) {
		if( !card.isOneEyeJack()  && !card.isTwoEyeJack() && score > SCORES.get( card ) ) 
			SCORES.put( card, score );
	}
	
	public static int countInstancesofCardInHand( Card card ) {
		int count = 0;
		for( Card c: get() ) 
			if( card.equals( c ) ) count++;
		return count;
	}
	
	public static Set<Card> getTwoEyeJacks() {
		return twoEyeJacks;
	}
	
	public static Set<Card> getOneEyeJacks() {
		return oneEyeJacks;
	}
	
	public static void addCard( Card card ) throws Exception {
		if( get().size() > SeqBot.NUM_CARDS )
			throw new Exception( "Cannot have more than " + SeqBot.NUM_CARDS + " cards in hand!" );
		if( !CARDS.containsKey( card ) ) CARDS.put( card, 0 );
		CARDS.put( card, CARDS.get( card ) + 1 );
		if( CARDS.get( card ) > 2 )
			throw new Exception( "Cannot have more than 2 of the same card in hand.  Found " + CARDS.get( card ) + " for card: " + card );
		System.out.println( "SeqBot drew card: " + card );
		if( card.isOneEyeJack() ) oneEyeJacks.add( card );
		if( card.isTwoEyeJack() ) twoEyeJacks.add( card );
		if( !SCORES.containsKey( card ) && !card.getRank().equals( CardRank.CARD_J ) ) 
			SCORES.put( card, 0.0 );
	}
	
	public static void removeCard( Card card ) throws Exception {
		if( !CARDS.containsKey( card )  ) 
			throw new Exception( "Cannot remove card " + card + " - it is not in hand." );
		if( CARDS.get( card ) == 2 ) {
			CARDS.remove( card );
			CARDS.put( card, 1 );
		}
		else {
			CARDS.remove( card );
			SCORES.remove( card );
		}
		if( card.isOneEyeJack() ) 
			oneEyeJacks.remove( card );
		if( card.isTwoEyeJack() ) 
			twoEyeJacks.remove( card );
	}
	
	public static void addAxisRange( Square square, Integer axis, Set<Square> range ) {
		if( !AXIS_RANGES.containsKey( square )  )
			AXIS_RANGES.put( square, new HashMap<Integer, Set<Square>>() );
		AXIS_RANGES.get( square ).put( axis, range );
		//System.out.println( "Set AXIS_RANGE[" + square + "]-Axis#" + axis + " = " + range );
	}
	
	public static void clearAxisRanges() {
		AXIS_RANGES.clear();
	}
	
	public static Map<Square, Map<Integer, Set<Square>>> getAxisRanges() {
		return AXIS_RANGES;
	}
	
	// 45 chars
	private static final String SPACER = PanelBuilder.SPACER + "                              ";
	private static Map<Card, Double> SCORES = new TreeMap<>();
	private static final Set<Card> twoEyeJacks = new HashSet<>();
	private static final Set<Card> oneEyeJacks = new HashSet<>();
	private static final Map<Card, Integer> CARDS = new TreeMap<>();
	private static final Map<Square, Map<Integer, Set<Square>>> AXIS_RANGES = new HashMap<>();
}
