package com.seq.cards;

import java.util.*;

import com.seq.*;
import com.seq.board.*;

public class Hand {

	public static List<Card> get() {
		List<Card> hand = new ArrayList<>();
		for( Card card: CARDS.keySet() ) {
			hand.add( card );
			if(CARDS.get( card ) == 2 ) hand.add( card );
		}
		return hand;
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
		if( CARDS.get( card ) == null ) CARDS.put( card, 0 );
		CARDS.put( card, CARDS.get( card ) + 1 );
		if( CARDS.get( card ) > 2 )
			throw new Exception( "Cannot have more than 2 of the same card in hand.  Found " + CARDS.get( card ) + " for card: " + card );
		System.out.println( "SeqBot drew card: " + card );
		if( card.isOneEyeJack() ) oneEyeJacks.add( card );
		if( card.isTwoEyeJack() ) twoEyeJacks.add( card );
	}
	
	public static void removeCard( Card card ) throws Exception {
		if( CARDS.get( card ) == null ) 
			throw new Exception( "Cannot remove card " + card + " - it is not in hand." );
		if( CARDS.get( card ) == 2 ) CARDS.put( card, 1 );
		else CARDS.remove( card );
		if( card.isOneEyeJack() ) oneEyeJacks.remove( card );
		if( card.isTwoEyeJack() ) twoEyeJacks.remove( card );
	}
	
	public static void addAxisRange( Square square, Integer axis, Set<Square> range ) {
		if( AXIS_RANGES.get( square ) == null )
			AXIS_RANGES.put( square, new HashMap<Integer, Set<Square>>() );
		AXIS_RANGES.get( square ).put( axis, range );
		System.out.println( "Set AXIS_RANGE[" + square + "]-Axis#" + axis + " = " + range );
	}
	
	public static void clearAxisRanges() {
		AXIS_RANGES.clear();
	}
	
	public static Map<Square, Map<Integer, Set<Square>>> getAxisRanges() {
		return AXIS_RANGES;
	}
	
	private static final Set<Card> twoEyeJacks = new HashSet<>();
	private static final Set<Card> oneEyeJacks = new HashSet<>();
	private static final Map<Card, Integer> CARDS = new TreeMap<>();
	private static final Map<Square, Map<Integer, Set<Square>>> AXIS_RANGES = new HashMap<>();
}
