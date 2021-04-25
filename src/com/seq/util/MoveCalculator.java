package com.seq.util;

import java.util.*;
import org.apache.commons.lang3.StringUtils;

import com.seq.SeqBot;
import com.seq.board.*;
import com.seq.cards.*;

public class MoveCalculator {

	public static Square get() throws Exception {
		Square bestMove = null;
		double bestScore = 0.0;
		RangeUtil.populateRanges();
	
		if (RangeUtil.getGameFinisher() != null) {
			AudioUtil.playGenius();
			AudioUtil.playSchwifty();
			return RangeUtil.getGameFinisher();
		}
		if (RangeUtil.getSeqFinisher() != null && SeqBot.MY_SEQ_1 != null) {
			AudioUtil.playGenius();
			AudioUtil.playSchwifty();
			return RangeUtil.getSeqFinisher();
		}
		if (RangeUtil.getOneEyeJackTarget() != null && SeqBot.OP_SEQ_1 != null) {
			AudioUtil.playGenius();
			AudioUtil.playWubbaLubbaDubDub();
			return RangeUtil.getOneEyeJackTarget();
		}
		if (RangeUtil.getGameBlocker() != null) {
			AudioUtil.playWubbaLubbaDubDub();
			return RangeUtil.getGameBlocker();
		}
		if (RangeUtil.getSeqBlocker() != null && SeqBot.OP_SEQ_1 != null) {
			AudioUtil.playGenius();
			AudioUtil.playWubbaLubbaDubDub();
			return RangeUtil.getSeqBlocker();
		}
		if (RangeUtil.getSeqFinisher() != null) {
			//AudioUtil.playGenius();
			AudioUtil.playHeaven();
			return RangeUtil.getSeqFinisher();
		}
		if (RangeUtil.getOneEyeJackTarget() != null) {
			AudioUtil.playGenius();
			AudioUtil.playWubbaLubbaDubDub();
			return RangeUtil.getOneEyeJackTarget();
		}
		if (RangeUtil.getSeqBlocker() != null) {
			AudioUtil.playGenius();
			AudioUtil.playWubbaLubbaDubDub();
			return RangeUtil.getSeqBlocker();
		}

		System.out.println("Check 4 BEST MOVE...");
		for (Square square : Hand.getAxisRanges().keySet()) {
			System.out.println("Calculating " + square + "...");
			Map<Square, Map<Integer, Set<Square>>> myAxisRanges = Hand.getAxisRanges();
			
			double score = getScore(square, myAxisRanges.get(square), SeqBot.get().getMyTokenColor(), false);
			double opponentScore = getScore(square, RangeUtil.getOpponentAxisRanges(square), SeqBot.get().getOpponentTokenColor(), false);
			double totalScore = score + opponentScore;
			System.out.println( BREAK );
			System.out.println( "ATTACK Score  [ " + square + " ]: " + score );
			System.out.println( "DEFENSE Score [ " + square + " ]: " + opponentScore );
			System.out.println( "TOTAL Score   [ " + square + " ]: " + totalScore );
			if (totalScore > bestScore) {
				bestMove = square;
				bestScore = totalScore;
				System.out.println("BEST Score    [ " + bestMove + " ]: " + bestScore);
			}
			Hand.updateScore( square.getCard(), totalScore );
			System.out.println( "" );
		}

		if (bestMove == null)
			throw new Exception("Failed to identify best square in range");
		return bestMove;
	}
	
	private static TreeSet<Square> getBlanks(Collection<Square> squares){
		TreeSet<Square> blanks = new TreeSet<>();
		 for( Square s: squares)
				if( StringUtils.isBlank(s.getColor()) && !s.isWild() )
					blanks.add( s );
		 return blanks;
	}
	
	private static boolean willCardImproveChanceOfSeq(Square tokenSquare, List<Square> range) {
		Set<Square> bestSquares = new HashSet<>();
		int bestCount = 5;
		try {
			Set<Square> allBlanksInRange = getBlanks(range);
			for(Square s: allBlanksInRange) {
				List<Square> rangeCopy = new ArrayList<>( range );
				while (rangeCopy.size() > 4) {
					List<Square> squares = rangeCopy.subList(0, 5);
					TreeSet<Square> blanks = getBlanks(squares);
					if(squares.contains( s ) &&  blanks.size() <= bestCount ) {
						if( blanks.size() < bestCount )
							bestSquares.clear();
						bestCount = blanks.size();
						bestSquares.add( s );
					}
					rangeCopy.remove( 0 );
				}
			}

		}catch( Exception ex ) {
			System.out.println("Error during check if play will improve change of sequence!");
			ex.printStackTrace();
		}
		return bestSquares.isEmpty() || bestSquares.contains( tokenSquare );
	}

	public static double getScore(Square tokenSquare, Map<Integer, Set<Square>> axisRanges, String color, boolean forJack) throws Exception {
		double score = 0.0;
		boolean isAttack = color.equals( SeqBot.get().getMyTokenColor() );
		for (Integer axis : axisRanges.keySet()) {
			if (axisRanges.get(axis) == null)
				continue;
			List<Square> squares = new ArrayList<>(axisRanges.get(axis));

			if( tokenSquare.getPos() == SeqBot.SCORE_BREAK )
				System.out.println("Found pos: " + SeqBot.SCORE_BREAK);
			
			
			String msg = (isAttack ? "Attack ": "Defense") + " Axis[" + RangeUtil.AXIS_MAP.get( axis ) + "]:  ";
			while (squares.size() > 4) {
				List<Square> testSquares = squares.subList(0, 5);
				if( testSquares.contains( tokenSquare ) )
					if (isAttack) {
						if( !willCardImproveChanceOfSeq(tokenSquare, new ArrayList<>(squares))) {
							squares.remove(0);
							continue;
						}
						double testScore = calc(availableCardCounts(testSquares), Deck.countTwoEyeJacks(), Deck.getDeckSize());
						double finalScore = testScore;
						List<Square> withoutHandSquares = removeSquaresForCardsInHand(testSquares);
						int numCards = 5-withoutHandSquares.size();
						double inHandScore = 0.0;
						if( numCards != 0 ) {
							inHandScore = getScoreConsideringInHandCards( withoutHandSquares );
							finalScore = ( testScore + inHandScore ) / ( numCards + 1 );
							if( SeqBot.PRINT_ACCESS_SCORES ) 
								System.out.println( "...........................Axis Score(less cards in hand):  " + inHandScore );
						}
						if( SeqBot.PRINT_ACCESS_SCORES ) {
							System.out.println( "...........................Axis SubSeq score: " + testScore );
							System.out.println( "...........................Axis Merged score: " + finalScore );
						}
						score += finalScore;
					} else
						score += calcOpponent(availableCardCounts(testSquares), Deck.countTwoEyeJacks(), Deck.getDeckSize());

				squares.remove(0);
			}
			if( !forJack ) System.out.println( msg + score );
		}
		return score;
	}
	
	private static final String BREAK = "-------------------------------------------------------------------";
	
	private static double getScoreConsideringInHandCards( List<Square> squares) throws Exception {
		return calc(availableCardCounts(squares), Deck.countTwoEyeJacks(), Deck.getDeckSize());
	}

	private static List<Square> removeSquaresForCardsInHand(List<Square> squares) {
		List<Square> calcSquares = new ArrayList<>(squares);
		List<Square> haveCardsForTheseSquares = new ArrayList<>();
		for (Square square : calcSquares)
			if (Hand.get().contains(square.getCard()) && (!haveCardsForTheseSquares.contains(square)
					|| Hand.countInstancesofCardInHand(square.getCard()) > 1))
				haveCardsForTheseSquares.add(square);

		for (Square s : haveCardsForTheseSquares)
			calcSquares.remove(s);
		return calcSquares;
	}

	private static boolean hasSequenceCardsInHand(Map<Square, Integer> counts) {
		List<Card> countedCards = new ArrayList<>();
		for (Square square : counts.keySet())
			if (Hand.get().contains(square.getCard()) && (!countedCards.contains(square.getCard()) || Hand.countInstancesofCardInHand(square.getCard()) > 1))
				countedCards.add(square.getCard());

		int numCardsNeeded = counts.size() - countedCards.size();
		return numCardsNeeded < 1;
	}

	private static double calcOpponent(Map<Square, Integer> counts, int numWild, int deckSize) throws Exception {
		if (counts == null || counts.isEmpty())
			return 0.0;
		Square square = counts.keySet().iterator().next();
		double numCards = new Double(counts.get(square));
		double dDeckSize = new Double(deckSize);
		double dNumWild = new Double(numWild);
		double naturalProb = (numCards / dDeckSize);
		double wildProb = (dNumWild / dDeckSize);
		counts.remove(square);
		Map<Square, Integer> copyCounts1 = updateCounts(counts, square.getCard());
		Map<Square, Integer> copyCounts2 = updateCounts(counts, square.getCard());

		if (counts.size() > 0) {
			naturalProb = naturalProb * calcOpponent(copyCounts1, numWild, deckSize - 1);
			wildProb = wildProb * calcOpponent(copyCounts2, numWild > 0 ? numWild - 1 : 0, deckSize - 1);
		}
		
		assert( naturalProb + wildProb < 0.0 );
		return naturalProb + wildProb;
	}

	private static double calc(Map<Square, Integer> counts, int numWild, int deckSize) throws Exception {

		if (counts == null || counts.isEmpty())
			return 0.0;
		if (hasSequenceCardsInHand(counts))
			return 1.0;

		Square square = counts.keySet().iterator().next();
		double numCards = new Double(counts.get(square));
		double dDeckSize = new Double(deckSize);
		double dNumWild = new Double(numWild);
		double naturalProb = (numCards / dDeckSize);
		double wildProb = (dNumWild / dDeckSize);
		counts.remove(square);
		Map<Square, Integer> copyCounts1 = updateCounts(counts, square.getCard());
		Map<Square, Integer> copyCounts2 = updateCounts(counts, square.getCard());

		if(!counts.isEmpty()) {
			naturalProb = naturalProb * calc(copyCounts1, numWild, deckSize - 1);
			wildProb = wildProb * calc(copyCounts2, numWild > 0 ? numWild - 1 : 0, deckSize - 1);
		}

		assert( naturalProb + wildProb < 0.0 );
		return naturalProb + wildProb;
	}

	private static Map<Square, Integer> updateCounts(Map<Square, Integer> counts, Card card) {
		Map<Square, Integer> adjustedCounts = new HashMap<>(counts);
		for (Square square : adjustedCounts.keySet())
			if (square.getCard().equals(card))
				adjustedCounts.put(square, adjustedCounts.get(square) - 1);
		return adjustedCounts;
	}

	private static Map<Square, Integer> availableCardCounts(List<Square> squares) {
		Map<Square, Integer> counts = new HashMap<>();
		for (Square square : squares)
			if (StringUtils.isBlank(square.getColor()) && !square.isWild())
				counts.put(square, Deck.countAvailableCards(square.getCard()));
		return counts;
	}

}
