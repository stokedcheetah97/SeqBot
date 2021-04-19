package com.seq.util;

import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

import com.seq.SeqBot;
import com.seq.board.*;
import com.seq.cards.*;

public class MoveCalculator {
	
	public static Map<Integer, Double> get() throws Exception {
		Map<Integer, Double> map = new HashMap<>();
		List<Square> bestSquares = new ArrayList<>();
		double bestScore = 0.0;
		RangeUtil.populateRanges();
		
		if( RangeUtil.getGameFinisher() != null ) 
			map.put( RangeUtil.getGameFinisher().getPos(), 1.0 );
		else if( RangeUtil.getSeqFinisher() != null && SeqBot.MY_SEQ_COUNT == 1 ) 
			map.put( RangeUtil.getSeqFinisher().getPos(), 1.0 );
		else if( RangeUtil.getGameBlocker() != null )
			map.put( RangeUtil.getGameBlocker().getPos(), 1.0 );
		else if( RangeUtil.getSeqBlocker() != null && SeqBot.OPPONENT_SEQ_COUNT == 1 ) 
			map.put( RangeUtil.getSeqBlocker().getPos(), 1.0 );
		else if( RangeUtil.getSeqFinisher() != null ) {
			map.put( RangeUtil.getSeqFinisher().getPos(), 1.0 );
			SeqBot.MY_SEQ_COUNT++;
		} else if( RangeUtil.getSeqBlocker() != null ) {
			map.put( RangeUtil.getSeqBlocker().getPos(), 1.0 );
			SeqBot.OPPONENT_SEQ_COUNT++;
		}
		if( !map.isEmpty() ) return map;
	
		for( Square square: Hand.getAxisRanges().keySet() ) {
			double score = getScore( square, Hand.getAxisRanges().get( square ), SeqBot.get().getMyTokenColor() );
			if( score > 0 ) {
				System.out.println( "Score [ " + square +" ] = " + score );
				if( score == bestScore )
					bestSquares.add( square );
				else if( score > bestScore ) {
					bestSquares.clear();
					bestSquares.add( square );
					bestScore = score;
					System.out.println( "New Best Score [ " + square +" ] = " + bestScore );
				}  
			}
		}
		
		if( bestSquares.isEmpty() ) throw new Exception( "Failed to identify best square in range" );

		List<Integer> positions = bestSquares.stream().map(s -> s.getPos()).collect(Collectors.toList());
		for( Integer i: positions ) {
			map.put(i, bestScore);
			System.out.println( "Final Best Score [ " + Board.getSquare(i) +" ] = " + bestScore );
		}

		return map;
	}
	
	
	public static double getScore( Square square, Map<Integer, Set<Square>> axisRanges, String color ) throws Exception {
		System.out.println( "Calculating scores for: " + square );
		double score = 0.0;
		for( Integer axis: axisRanges.keySet() ) {
			if( axisRanges.get( axis ) == null ) continue;
			List<Square> squares = new ArrayList<>( axisRanges.get( axis ) );
			Collections.sort( squares );
			while( squares.size() > 4 ) {	
				if( color.equals(SeqBot.get().getMyTokenColor()) ) {
					List<Square> calcSquares = removeSquaresForCardsInHand( squares.subList( 0, 5 ) );
					score += calc( availableCardCounts(calcSquares), Deck.countTwoEyeJacks(), Deck.getDeckSize() );
				} else
					score += calcOpponent( availableCardCounts(squares), Deck.countTwoEyeJacks(), Deck.getDeckSize() );
				
				squares.remove( 0 );
			}
		}
		return score;
	}
	
	
	private static List<Square> removeSquaresForCardsInHand( List<Square> squares ) {
		List<Square> calcSquares = new ArrayList<>( squares );
		List<Square> haveCardsForTheseSquares = new ArrayList<>();
		for( Square square : calcSquares )
			if( Hand.get().contains(square.getCard()) && ( !haveCardsForTheseSquares.contains(square) || Hand.countInstancesofCardInHand(square.getCard()) > 1 ) )
				haveCardsForTheseSquares.add( square );
		
		for( Square s: haveCardsForTheseSquares )
			calcSquares.remove( s );
		return calcSquares;
	}

	private static boolean hasSequenceCardsInHand( Map<Square, Integer> counts ) {
		
		List<Card> countedCards = new ArrayList<>();
		for( Square square: counts.keySet() )
			if( Hand.get().contains(square.getCard()) && ( !countedCards.contains(square.getCard()) || Hand.countInstancesofCardInHand(square.getCard()) > 1 ) )
				countedCards.add( square.getCard() );

		int numCardsNeeded = counts.size() - Hand.getTwoEyeJacks().size() - countedCards.size();
		return numCardsNeeded < 1;		
	}
	
	private static double calcOpponent( Map<Square, Integer> counts, int numWild, int deckSize ) throws Exception {
		return 0.0;
	}
	
	private static double calc( Map<Square, Integer> counts, int numWild, int deckSize ) throws Exception {
		
		if( counts == null || counts.isEmpty() ) throw new Exception( "Counts should never be null in calc()" );
		if( hasSequenceCardsInHand(counts) ) return 1.0;
		
		Square square = counts.keySet().iterator().next();
		double numCards = new Double( counts.get( square ) );
		double dDeckSize = new Double( deckSize );
		double dNumWild = new Double( numWild );
		double naturalProb = (numCards/dDeckSize);
		double wildProb = (dNumWild/dDeckSize);
		counts.remove( square );
		Map<Square, Integer> copyCounts1 = updateCounts( counts, square.getCard() );
		Map<Square, Integer> copyCounts2 = updateCounts( counts, square.getCard() );

		if( counts.size() > 0 ) {
			naturalProb = naturalProb * calc( copyCounts1, numWild, deckSize - 1 );
			wildProb = wildProb * calc( copyCounts2, numWild > 0 ? numWild - 1 : 0, deckSize - 1 );
		}
		return naturalProb + wildProb;
	}
	
	private static  Map<Square, Integer> updateCounts( Map<Square, Integer> counts, Card card ) {
		Map<Square, Integer> adjustedCounts = new HashMap<>( counts );
		for( Square square: adjustedCounts.keySet() )
			if( square.getCard().equals( card ) )
				adjustedCounts.put(square, adjustedCounts.get( square ) - 1);
		return adjustedCounts;
	}
	
	private static Map<Square, Integer> availableCardCounts( List<Square> squares ) {
		Map<Square, Integer> counts = new HashMap<>();
		for( Square square: squares )
			if( StringUtils.isBlank( square.getColor() ) && !square.isWild() )
				counts.put( square, Deck.countAvailableCards( square.getCard() ) );
		return counts;
	}
	
}
