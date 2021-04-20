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
		if (RangeUtil.getSeqFinisher() != null && SeqBot.MY_SEQ_COUNT == 1) {
			AudioUtil.playSchwifty();
			return RangeUtil.getSeqFinisher();
		}
		if (RangeUtil.getOneEyeJackTarget() != null && SeqBot.OPPONENT_SEQ_COUNT == 1) {
			AudioUtil.playGenius();
			AudioUtil.playWubbaLubbaDubDub();
			return RangeUtil.getOneEyeJackTarget();
		}
		if (RangeUtil.getGameBlocker() != null) {
			AudioUtil.playGenius();
			AudioUtil.playWubbaLubbaDubDub();
			return RangeUtil.getGameBlocker();
		}
		
		if (RangeUtil.getSeqBlocker() != null && SeqBot.OPPONENT_SEQ_COUNT == 1) {
			AudioUtil.playWubbaLubbaDubDub();
			return RangeUtil.getSeqBlocker();
		}
		if (RangeUtil.getSeqFinisher() != null) {
			AudioUtil.playHeaven();
			SeqBot.MY_SEQ_COUNT++;
			return RangeUtil.getSeqFinisher();
		}

		if (RangeUtil.getOneEyeJackTarget() != null) {
			AudioUtil.playWubbaLubbaDubDub();
			return RangeUtil.getOneEyeJackTarget();
		}
		if (RangeUtil.getSeqBlocker() != null) {
			AudioUtil.playWubbaLubbaDubDub();
			return RangeUtil.getSeqBlocker();
		}

		for (Square square : Hand.getAxisRanges().keySet()) {
			double score = getScore(Hand.getAxisRanges().get(square), SeqBot.get().getMyTokenColor());
			if (score > 0) {
				System.out.println("Score [ " + square + " ] = " + score);
				if (score > bestScore) {
					bestMove = square;
					bestScore = score;
					System.out.println("New Best Score [ " + bestMove + " ] = " + bestScore);
				}
			}
		}

		if (bestMove == null)
			throw new Exception("Failed to identify best square in range");

		return bestMove;
	}

	public static double getScore(Map<Integer, Set<Square>> axisRanges, String color) throws Exception {
		double score = 0.0;
		for (Integer axis : axisRanges.keySet()) {
			if (axisRanges.get(axis) == null)
				continue;
			List<Square> squares = new ArrayList<>(axisRanges.get(axis));
			Collections.sort(squares);
			while (squares.size() > 4) {
				if (color.equals(SeqBot.get().getMyTokenColor())) {
					List<Square> calcSquares = removeSquaresForCardsInHand(squares.subList(0, 5));
					System.out.println( "Calc score: " + calcSquares );
					score += calc(availableCardCounts(calcSquares), Deck.countTwoEyeJacks(), Deck.getDeckSize());
				} else
					score += calcOpponent(availableCardCounts(squares), Deck.countTwoEyeJacks(), Deck.getDeckSize());

				squares.remove(0);
			}
		}
		return score;
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
			if (Hand.get().contains(square.getCard()) && (!countedCards.contains(square.getCard())
					|| Hand.countInstancesofCardInHand(square.getCard()) > 1))
				countedCards.add(square.getCard());

		int numCardsNeeded = counts.size() - Hand.getTwoEyeJacks().size() - countedCards.size();
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

		if (counts.size() > 0) {
			naturalProb = naturalProb * calc(copyCounts1, numWild, deckSize - 1);
			wildProb = wildProb * calc(copyCounts2, numWild > 0 ? numWild - 1 : 0, deckSize - 1);
		}
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
