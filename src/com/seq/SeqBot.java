package com.seq;

import java.util.*;
import javax.swing.*;

import com.seq.board.*;
import com.seq.cards.*;
import com.seq.gui.*;
import com.seq.util.*;

public class SeqBot implements Runnable {
	
	
	public static final int NUM_CARDS = 7;
	public static final boolean MOCK_HAND = true;

	public static void main( String[] args ) {
		System.out.println( "SeqBot loading..." );
		GuiController.get();
		System.out.println( "SeqBot is now self-aware" );
		if( MOCK_HAND ) mockHand();
		
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				GuiController.get().init();
				if( MOCK_HAND ) {
					get().setStatusMsg( "  My Hand:                    " + Hand.get() );
					GuiController.get().refresh();
				}
			}
		} );
	}


	@Override
	public void run() {
		if( isDrawCard() ) 
			try {
				System.out.println( "SeqBot drew card: " + myNewCard );
				Hand.addCard( myNewCard );
			} catch( Exception ex ) {
				logError( ex, "Failed to add card to hand" );
			}
		else if( isPlaceOpponentToken() ) 
			try {
				Board.get().addToken( opponentPos, opponentTokenColor );
				if( opponentJackSuit.isEmpty() ) 
					Deck.playCard( Board.get().getSquare( opponentPos ).getCard() );
				else
					Deck.playJack(Board.get().getSquare( opponentPos ).getCard(), opponentJackSuit );
			} catch( Exception ex) {
				logError( ex, "Failed to play " + opponentTokenColor + " token on " + Board.get().getSquare( opponentPos ).getCard() + " @pos: " + opponentPos );
			}
		else if( isCalculateMove() )
			try {
				System.out.println( "SeqBot calculating next move... " );
				if( Hand.get().size() != NUM_CARDS ) throw new Exception( "Must have " + NUM_CARDS + " in hand" );
				myNextMove = new ArrayList<>( MoveCalculator.get().keySet() );
				Collections.sort( myNextMove );	
				Card card = Board.get().getSquare( myNextMove.get(0) ).getCard();
				Hand.removeCard( card );
				Deck.playCard( card );
				Board.get().addToken( myNextMove.get(0), myTokenColor );
			} catch( Exception ex) {
				logError( ex, "Failed to calculate move" );
			}
		else logError( new Exception( "Invalid state" ), "SeqBot is confused" );
	}
	
	public static SeqBot get() {
		if( seqBot == null ) seqBot = new SeqBot();
		return seqBot;
	}

	
	
//	private Map<Square, Set<Set<Square>>> getSequenceSets( Square square ) {
//		Set<Square> squares = new TreeSet<>();
//		
//		Set<Set<Square>> seqSet = new TreeSet<>();
//		seqSet.add( squares );
//		Map<Square, Set<Set<Square>>> map = new HashMap<>();
//		map.put( square, seqSet );
//		return map;
//	}

	private boolean isDrawCard() {
		return myNewCard != null && !myNewCard.getRank().isEmpty() && !myNewCard.getSuit().isEmpty();
	}
	
	private boolean isPlaceOpponentToken() {
		return !isDrawCard() && !opponentTokenColor.isEmpty() && opponentPos != null;
	}
	
	private boolean isCalculateMove() {
		return !isDrawCard() && !isPlaceOpponentToken() && calcNextMove && !myTokenColor.isEmpty();
	}
	
	private void logError( Exception ex, String msg ) {
		errMsg = msg + " - " + ex.getMessage();
		System.out.println( msg );
		report( "Error-State Report" );
		ex.printStackTrace();
	}
	
	private void report(String id) {
		System.out.println( " ============ REPORT " + id + " ============" );
		System.out.println( "statusMsg: " + statusMsg );
		System.out.println( "myTokenColor: " + myTokenColor );
		System.out.println( "opponentTokenColor: " + opponentTokenColor );
		System.out.println( "calcNextMove: " + calcNextMove );
		System.out.println( "myNewCard: " + myNewCard );
		System.out.println( "myNextMove: " + myNextMove );
		
		System.out.println( "opponentJackSuit: " + opponentJackSuit );
		System.out.println( "opponentPos: " + opponentPos );
	}

	private static void mockHand() {
		try {
			Hand.addCard( new Card(CardSuit.SPADES, CardRank.CARD_2) );
			Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_3) );
			Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_Q) );
			Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_5) );
			Hand.addCard( new Card(CardSuit.CLUBS, CardRank.CARD_6) );
			Hand.addCard( new Card(CardSuit.CLUBS, CardRank.CARD_K) );
			Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_J) );
			
			SeqBot.get().setMyTokenColor( TokenColor.BLUE );
			SeqBot.get().setOpponentTokenColor( TokenColor.RED );
			
			//GuiController.get().getMyColorPicklist().setSelectedItem( TokenColor.BLUE );
			//GuiController.get().getOpponentColorPicklist().setSelectedItem( TokenColor.RED );
			
			System.out.println( "SeqBot loaded mock hand" );
		} catch( Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean isCalcNextMove() {
		return calcNextMove;
	}

	public void setCalcNextMove(boolean calcNextMove) {
		this.calcNextMove = calcNextMove;
	}

	public Card getMyNewCard() {
		return myNewCard;
	}

	public void setMyNewCard(Card myNewCard) {
		this.myNewCard = myNewCard;
	}

	public List<Integer> getMyNextMove() {
		return myNextMove;
	}

	public void setMyNextMove(List<Integer> myNextMove) {
		this.myNextMove = myNextMove;
	}

	public String getMyTokenColor() {
		return myTokenColor;
	}

	public void setMyTokenColor(String myTokenColor) {
		this.myTokenColor = myTokenColor;
	}

	public String getOpponentTokenColor() {
		return opponentTokenColor;
	}

	public void setOpponentTokenColor(String opponentTokenColor) {
		this.opponentTokenColor = opponentTokenColor;
	}

	public Integer getOpponentPos() {
		return opponentPos;
	}

	public void setOpponentPos(Integer opponentPos) {
		this.opponentPos = opponentPos;
	}
	
	public String getOpponentJackSuit() {
		return opponentJackSuit;
	}

	public void setOpponentJackSuit(String opponentJackSuit) {
		this.opponentJackSuit = opponentJackSuit;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	private static SeqBot seqBot = null;
	boolean calcNextMove = false;
	private Card myNewCard = null;
	private List<Integer> myNextMove = null;
	private String myTokenColor = "";
	private String opponentTokenColor = "";
	private String opponentJackSuit = "";
	private Integer opponentPos = null;
	private String errMsg = null;
	private String statusMsg = "SeqBot is ready!";
}
