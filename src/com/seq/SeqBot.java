package com.seq;

import java.util.*;
import javax.swing.SwingUtilities;

public class SeqBot implements Runnable {

	public static void main( String[] args ) {
		System.out.println( "SeqBot loading..." );
		System.out.println( "SeqBot is now self-aware" );
		if( useMockHand ) mockHand();
		
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				SeqBotGUI.gui = new SeqBotGUI();
				if( useMockHand ) {
					get().statusMsg = "  My Hand:                  " + Hand.get();
					SeqBotGUI.gui.refresh();
				}
			}
		} );
	}


	@Override
	public void run() {
		if( isDrawCard() ) 
			try {
				Hand.addCard( this.myNewCard );
			} catch( Exception ex ) {
				logError( ex, "Failed to add card to hand" );
			}
		else if( isPlaceOpponentToken() ) 
			try {
				Board.get().addToken( this.opponentPos, this.opponentTokenColor );
				Deck.playCard( Board.get().getSquare( this.opponentPos ).getCard() );
			} catch( Exception ex) {
				logError( ex, "Failed to play " + this.opponentTokenColor + " token on " + Board.get().getSquare( this.opponentPos ).getCard() + " @pos: " + this.opponentPos );
			}
		else if( isCalculateMove() )
			try {
				if( Hand.get().size() != NUM_CARDS ) throw new Exception( "Must have " + NUM_CARDS + " in hand" );
				this.myNextMove = calculateNextMove();
				Card card = Board.get().getSquare( this.myNextMove ).getCard();
				Hand.removeCard( card );
				Deck.playCard( card );
				Board.get().addToken( this.myNextMove, this.myTokenColor );
			} catch( Exception ex) {
				logError( ex, "Failed to calculate move" );
			}
		else logError( new Exception( "Invalid state" ), "SeqBot is confused" );
	}
	

	private Integer calculateNextMove() throws Exception {
		Square bestSquare = null;
		double bestScore = 0.0;
		for( Square square: RangeUtil.getRange() ) {
			double score = 0.0;
			for( Integer axis: square.myAxisRanges.keySet() ) {
				if( square.myAxisRanges.get( axis ) == null ) continue;
				List<Square> squares = new ArrayList<>( square.myAxisRanges.get( axis ) );
				Collections.sort( squares );
				while( squares.size() > 4 ) {
					score += calcAxisProb( squares.subList( 0, 5 ) );
					squares.remove( 0 );
				}
			}
			
			if( score > bestScore ) {
				bestSquare = square;
				bestScore = score;
			}
		}
		if( bestSquare == null )
			throw new Exception( "Failed to identify best square in range" );
		this.myNextMove = bestSquare.positionNumber;
		
		return 2;
		//return this.myNextMove;
	}
	
	private double calcAxisProb(List<Square> squares) {
		double score = 0.0;
		int numFourEyedJacksAvail = Deck.countAvailableCards( new Card(CardSuit.CLUBS, CardRank.CARD_J ) );
		for( Square square: squares ) {
			if( square.color.isEmpty() ) {
				int numCardsAvail = Deck.countAvailableCards( square.getCard() );
			}
		}
		return score;
	}
	
	
	private Map<Square, Set<Set<Square>>> getSequenceSets( Square square ) {
		Set<Square> squares = new TreeSet<>();
		
		Set<Set<Square>> seqSet = new TreeSet<>();
		seqSet.add( squares );
		Map<Square, Set<Set<Square>>> map = new HashMap<>();
		map.put( square, seqSet );
		return map;
	}

	private boolean isDrawCard() {
		return this.myNewCard != null && !this.myNewCard.rank.isEmpty() && !this.myNewCard.suit.isEmpty();
	}
	
	private boolean isPlaceOpponentToken() {
		return !isDrawCard() && !this.opponentTokenColor.isEmpty() && this.opponentPos != null;
	}
	
	private boolean isCalculateMove() {
		return !isDrawCard() && !isPlaceOpponentToken() && this.calcNextMove && !this.myTokenColor.isEmpty();
	}
	
	private void logError( Exception ex, String msg ) {
		this.errMsg = msg + " - " + ex.getMessage();
		System.out.println( msg );
		report( "Error-State Report" );
		ex.printStackTrace();
	}
	
	private void report(String id) {
		System.out.println( " ============ REPORT " + id + " ============" );
		System.out.println( "calcNextMove: " + this.calcNextMove );
		System.out.println( "myNewCard: " + this.myNewCard );
		System.out.println( "myNextMove: " + this.myNextMove );
		System.out.println( "myTokenColor: " + this.myTokenColor );
		System.out.println( "opponentTokenColor: " + this.opponentTokenColor );
		System.out.println( "opponentPos: " + this.opponentPos );
		System.out.println( "statusMsg: " + this.statusMsg );
	}

	private static void mockHand() {
		try {
			Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_2) );
			Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_3) );
			Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_4) );
			Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_5) );
			Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_6) );
			Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_7) );
			Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_8) );
			System.out.println( "SeqBot loaded mock hand" );
		} catch( Exception ex) {
			ex.printStackTrace();
		}
	}
	
	static SeqBot get() {
		if( seqBot == null ) seqBot = new SeqBot();
		return seqBot;
	}
	
	static SeqBot seqBot = null;
	static final int NUM_CARDS = 7;
	static boolean useMockHand = true;
	boolean calcNextMove = false;
	Card myNewCard = null;
	Integer myNextMove = null;
	String myTokenColor = "";
	String opponentTokenColor = "";
	Integer opponentPos = null;
	String errMsg = null;
	String statusMsg = "SeqBot is ready!";
}
