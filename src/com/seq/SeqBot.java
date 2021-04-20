package com.seq;

import org.apache.commons.lang3.StringUtils;
import com.seq.board.*;
import com.seq.cards.*;
import com.seq.gui.*;
import com.seq.util.*;

public class SeqBot {
	
	public static final int NUM_CARDS = 7;
	public static final boolean MOCK_HAND = true;
	
	public static void main( String[] args ) {
		System.out.println( "SeqBot loading..." );
		GuiController.get().init();
		System.out.println( "SeqBot has become self-aware" );
		if( MOCK_HAND ) {
			mockHand();
			get().setStatusMsg( "  My Hand:                    " + Hand.get() );
		} else 
			get().setStatusMsg( "  SeqBot must draw " + NUM_CARDS + " cards to begin" );
		
		GuiController.get().refresh();
	}
	
	private static String getConfirmationMsg( Square square, String jackSuit, String playTokenColor, String removeTokenColor ) {
		if(  StringUtils.isNotBlank(jackSuit) && (jackSuit.equals(CardSuit.SPADES) || jackSuit.equals(CardSuit.HEARTS)) )
			return "Remove " + removeTokenColor + " token using the " + new Card(jackSuit, CardRank.CARD_J) + " for " + square.getCard() + " @Pos #" + square.getPos();
		else if(  StringUtils.isNotBlank(jackSuit) )
			return "Play " + playTokenColor + " token using the " + new Card(jackSuit, CardRank.CARD_J) + " for " + square.getCard() + " @Pos #" + square.getPos();
		return "Play " + playTokenColor + " token for " + square.getCard() + " @Pos# " + square.getPos();
	}
	
	public void processRequest() {
		if( isDrawCard() ) 
			try {
				if( NEXT_MOVE != null && NEXT_MOVE.equals( MY_MOVE ) ) throw new Exception( "Silly human - SeqBot must place a token!" );
				if( NEXT_MOVE != null && NEXT_MOVE.equals( OPPNENTS_MOVE ) ) throw new Exception( "Silly human - our opponent must place a token!" );
				if( myNewCard == null ) throw new Exception( "Silly human - tell me what card to draw!" );
				Hand.addCard( myNewCard );
				if( Hand.get().size() == NUM_CARDS)
					NEXT_MOVE = OPPNENTS_MOVE;
			} catch( Exception ex ) {
				logError( ex, "Failed to add card to hand" );
			}
		else if( isPlaceOpponentToken() ) 
			try {
				if( NEXT_MOVE != null &&  NEXT_MOVE.equals( DRAW_CARD ) ) 
					throw new Exception( "Silly human - SeqBot must draw a card!" );
				if( NEXT_MOVE != null &&  NEXT_MOVE.equals( MY_MOVE ) ) 
					throw new Exception( "Silly human - SeqBot must place a token!" );
				if( opponentPos == null ) 
					throw new Exception( "Silly human - tell me where our opponent moved!" );
				if( opponentPos == 1 || opponentPos == 10 || opponentPos == 90 || opponentPos == 100  ) 
					throw new Exception( "Silly human - cannot play on wilcard corner squares!" );
				if( opponentPos < 1 || opponentPos > 100   ) 
					throw new Exception( "Silly human - valid positions range from 2 - 99!" );
				if( StringUtils.isNotBlank(Board.getSquare( opponentPos ).getColor())  )
					throw new Exception( "Silly human - there is already a " + Board.getSquare( opponentPos ).getColor() + " token @pos #" + opponentPos );

				GuiController.showConfirmationWindow( getConfirmationMsg( Board.getSquare( opponentPos ), opponentJackSuit, opponentTokenColor, myTokenColor ) );
				
				Board.addToken( opponentPos, opponentTokenColor );
				if( StringUtils.isBlank(opponentJackSuit) )
					Deck.playCard( Board.getSquare( opponentPos ).getCard() );
				else
					Deck.playJack( new Card( opponentJackSuit, CardRank.CARD_J ), Board.getSquare( opponentPos ).getCard() );
				
				NEXT_MOVE = MY_MOVE;
			} catch( Exception ex) {
				logError( ex, "Failed to play " + opponentTokenColor + " token on " + Board.getSquare( opponentPos ).getCard() + " @pos: " + opponentPos );
			} 
		else if( isCalculateMove() )
			try {
				if( NEXT_MOVE != null && NEXT_MOVE.equals( DRAW_CARD ) ) throw new Exception( "Silly human - SeqBot must draw a card!" );
				if( NEXT_MOVE != null && NEXT_MOVE.equals( OPPNENTS_MOVE ) ) throw new Exception( "Silly human - our opponent must place a token!" );
				
				System.out.println( "SeqBot calculating next move... " );
				if( Hand.get().size() != NUM_CARDS ) throw new Exception( "Must have " + NUM_CARDS + " in hand" );
				Hand.clearAxisRanges();
				myNextMove = MoveCalculator.get();
				
				Card card = myNextMove.getCard();
				if( myNextMove.getColor() != null && myNextMove.getColor().equals( opponentTokenColor ) 
					&& !Hand.get().contains( myNextMove.getCard() ) && Hand.getOneEyeJacks().size() > 0 ) {
					card = Hand.getOneEyeJacks().iterator().next();
					Board.removeToken( myNextMove.getPos() );
				} else if( !Hand.get().contains( card ) && Hand.getTwoEyeJacks().size() > 0 ) {
					card = Hand.getTwoEyeJacks().iterator().next();
					Board.addToken( myNextMove.getPos(), myTokenColor );
				} else 
					Board.addToken( myNextMove.getPos(), myTokenColor );

				Deck.playCard( card );
				Hand.removeCard( card );
				
				String jackSuit = card.getRank().equals( CardRank.CARD_J ) ? card.getSuit() : null;
				GuiController.showInfo( getConfirmationMsg( myNextMove, jackSuit, myTokenColor, opponentTokenColor ) );
				NEXT_MOVE = DRAW_CARD;
			} catch( Exception ex) {
				logError( ex, "Failed to calculate move" );
			}
		else if( NEXT_MOVE != null && NEXT_MOVE.equals( DRAW_CARD ) ) 
			logError( new Exception( "Silly human - SeqBot must draw a card!" ), "SeqBot is confused" );
		else if( NEXT_MOVE != null && NEXT_MOVE.equals( MY_MOVE ) ) 
			logError( new Exception( "Silly human - SeqBot must place a token!" ), "SeqBot is confused" );
		else if( NEXT_MOVE != null && NEXT_MOVE.equals( OPPNENTS_MOVE ) ) 
			logError( new Exception( "Silly human - our opponent must place a token!" ), "SeqBot is confused" );
		else 
			logError( new Exception( "Invalid state" ), "SeqBot is confused" );
	}
	
	public static SeqBot get() {
		if( seqBot == null ) seqBot = new SeqBot();
		return seqBot;
	}
	
	public void undoLastRequest() {
		try {
			String msg = "Undo Action!  ";
			if( prevOpponentPos != null ) {
				Board.removeToken( prevOpponentPos );
				if( prevOpponentJackSuit != null ) {
					Card jack = new Card(prevOpponentJackSuit, CardRank.CARD_J);
					msg += " Return " + jack + " to the deck.  ";
					Deck.returnCard( jack );
				} else {
					msg += "Remove " + Board.getSquare( prevOpponentPos );
					Deck.returnCard( Board.getSquare( prevOpponentPos ).getCard() );
				}
				NEXT_MOVE = OPPNENTS_MOVE;
			} else if( prevMyNewCard != null ) {
				Hand.removeCard( prevMyNewCard );
				NEXT_MOVE = DRAW_CARD;
			}
			
			GuiController.showInfo( msg );
			
		} catch( Exception ex) {
			logError( ex, "Failed to undo last move" );
		}
	}

	private boolean isDrawCard() {
		return myNewCard != null && StringUtils.isNotEmpty( myNewCard.getRank() ) && StringUtils.isNotEmpty( myNewCard.getSuit() );
	}
	
	private boolean isPlaceOpponentToken() {
		return !isDrawCard() && StringUtils.isNotEmpty( opponentTokenColor ) && opponentPos != null;
	}
	
	private boolean isCalculateMove() {
		return !isDrawCard() && !isPlaceOpponentToken() && calcNextMove && StringUtils.isNotEmpty( myTokenColor );
	}
	
	private void logError( Exception ex, String msg ) {
		errMsg = ( ex.getMessage().startsWith( "Silly human" ) ? "" : msg  + " - " ) + ex.getMessage();
		System.out.println( msg );
		if( !ex.getMessage().startsWith( "Silly human" ) ) {
			report( "Error-State Report" );
			ex.printStackTrace();
		}
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

			Hand.addCard( new Card(CardSuit.CLUBS, CardRank.CARD_J) );
			Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_J) );
			Hand.addCard( new Card(CardSuit.SPADES, CardRank.CARD_J) );
			Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_A) );
			Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_A) );
			Hand.addCard( new Card(CardSuit.SPADES, CardRank.CARD_6) );
			Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_A) );
			
			SeqBot.get().setMyTokenColor( TokenColor.BLUE );
			SeqBot.get().setOpponentTokenColor( TokenColor.RED );
			
			Board.addToken( 2, TokenColor.BLUE );
			Board.addToken( 3, TokenColor.BLUE );
			Board.addToken( 4, TokenColor.BLUE );
			Deck.playCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_6) );
			Deck.playCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_7) );
			Deck.playCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_8) );
			
			Board.addToken( 92, TokenColor.RED );
			Board.addToken( 93, TokenColor.RED );
			Board.addToken( 44, TokenColor.RED );
			Deck.playCard( new Card(CardSuit.SPADES, CardRank.CARD_9) );
			Deck.playCard( new Card(CardSuit.SPADES, CardRank.CARD_8) );
			Deck.playCard( new Card(CardSuit.SPADES, CardRank.CARD_7) );
			
			
			
			
			
			
			
			System.out.println( "SeqBot loaded mock hand" );
		} catch( Exception ex) {
			ex.printStackTrace();
		}
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
	
	public void setPrevMyNewCard(Card prevMyNewCard) {
		this.prevMyNewCard = prevMyNewCard;
	}

	public Square getMyNextMove() {
		return myNextMove;
	}

	public void setMyNextMove(Square myNextMove) {
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
	
	public void setPrevOpponentPos(Integer prevOpponentPos) {
		this.prevOpponentPos = prevOpponentPos;
	}
	
	public String getOpponentJackSuit() {
		return opponentJackSuit;
	}

	public void setOpponentJackSuit(String opponentJackSuit) {
		this.opponentJackSuit = opponentJackSuit;
	}
	
	public void setPrevOpponentJackSuit(String prevOpponentJackSuit) {
		this.prevOpponentJackSuit = prevOpponentJackSuit;
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
	
	public void clearPrev() {
		prevOpponentPos = null;
		prevOpponentJackSuit = "";
		prevMyNewCard = null;
	}

	private static SeqBot seqBot = null;
	private static final String MY_MOVE = "MY_MOVE";
	private static final String DRAW_CARD = "DRAW_CARD";
	private static final String OPPNENTS_MOVE = "OPPNENTS_MOVE";
	private static String NEXT_MOVE = null;
	boolean calcNextMove = false;
	private Card myNewCard = null;
	private Square myNextMove = null;
	private String myTokenColor = "";
	private String opponentTokenColor = "";
	private String opponentJackSuit = "";
	private Integer opponentPos = null;
	private String errMsg = null;
	private String statusMsg = "SeqBot is ready!";
	
	private Integer prevOpponentPos = null;
	private String prevOpponentJackSuit = "";
	private Card prevMyNewCard = null;
	
	public static int MY_SEQ_COUNT = 0;
	public static int OPPONENT_SEQ_COUNT = 0;
}
