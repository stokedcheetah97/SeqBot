package com.seq.util;

import java.util.*;
import java.util.stream.Collectors;

import com.seq.board.*;
import com.seq.cards.*;

public class MoveCalculator {

	public static Map<Integer, Double> get() throws Exception {
		
		Map<Integer, Double> map = new HashMap<>();
		
		List<Square> bestSquares = new ArrayList<>();
		double bestScore = 0.0;
		
		for( Square square: RangeUtil.getRange() ) {
			double score = 0.0;
			for( Integer axis: square.getMyAxisRanges().keySet() ) {
				if( square.getMyAxisRanges().get( axis ) == null ) continue;
				List<Square> squares = new ArrayList<>( square.getMyAxisRanges().get( axis ) );
				Collections.sort( squares );
				while( squares.size() > 4 ) {
					score += calcGap( availableCardCounts(squares.subList( 0, 5 )), Deck.countTwoEyeJacks(), Deck.getDeckSize() );
					squares.remove( 0 );
				}
			}
			
			System.out.println( "Card: " + square.getCard() + " @Pos # " + square.getPositionNumber() + " score = " + score );
			
			if( score == bestScore ) {
				bestSquares.add( square );
				System.out.println( "New best score = " + score );
			} else if( score > bestScore ) {
				bestSquares.clear();
				bestSquares.add( square );
				bestScore = score;
			}  
		}
		
		if( bestSquares.isEmpty() )
			throw new Exception( "Failed to identify best square in range" );

		List<Integer> positions = bestSquares.stream().map(s -> s.getPositionNumber()).collect(Collectors.toList());
		for( Integer i: positions ) 
			map.put(i, bestScore);

		return map;
	}

	
	
	private static double calcGap( Map<Square, Integer> counts, int numWild, int deckSize ) {
		
		Square square = counts.keySet().iterator().next();
		int numCards = counts.get( square );
		counts.remove( square );
		
		double naturalProb = (numCards/deckSize);
		double wildProb = (numWild/deckSize);
		
		if( counts.size() > 0 ) {
			naturalProb = (numCards/deckSize) * calcGap( updateCounts(counts, square.getCard()), numWild, deckSize - 1 );
			wildProb = (numWild/deckSize) * calcGap( counts, numWild - 1, deckSize - 1 );
		}
		
		return naturalProb + wildProb;
	}
	
	private static  Map<Square, Integer> updateCounts( Map<Square, Integer> counts, Card card ) {
		for( Square s: counts.keySet() )
			if( s.getCard().equals( card ) ) counts.put(s, counts.get(s) - 1);
		return counts;
	}
	
	private static Map<Square, Integer> availableCardCounts( List<Square> squares ) {
		Map<Square, Integer> counts = new HashMap<>();
		for( Square square: squares )
			if( square.getColor().isEmpty() )
				counts.put( square, Deck.countAvailableCards( square.getCard() ) );
		return counts;
	}
	
}
