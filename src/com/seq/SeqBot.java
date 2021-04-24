package com.seq;

import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

import com.seq.board.*;
import com.seq.cards.*;
import com.seq.gui.*;
import com.seq.util.*;

public class SeqBot {
	
	public static final int NUM_CARDS = 7;
	public static final boolean RELOAD = true;
	public static final boolean MOCK_HAND = false;
	public static final boolean ENABLE_SOUND = true;
	public static final boolean PRINT_ACCESS_SCORES = false;
	//public static final String SLASH = "\\";
	//public static final String IMAGE_PATH = "C:\\Users\\mikes\\git\\SeqBot\\img\\board.jpeg";
	//public static final String AUDIO_PATH = "C:\\Users\\mikes\\git\\SeqBot\\sound\\";
	
	public static final String SLASH = "/";
	public static final String IMAGE_PATH = "/Users/msioda/git/SeqBot/img/board.jpeg";
	public static final String AUDIO_PATH = "/Users/msioda/git/SeqBot/sound/";
	
	public static void main( String[] args ) {

		System.out.println( "SeqBot loading..." );
		GuiAdapter.get().init();
		System.out.println( "SeqBot has become self-aware" );
		if( MOCK_HAND ) {
			MockHandUtil.init();
			get().setStatusMsg( Hand.getOrderedHand() );
		} else if( RELOAD )
			ReloadUtil.reload();
		else 
			get().setStatusMsg( "  SeqBot must draw " + NUM_CARDS + " cards to begin" );
		

		GuiAdapter.get().refresh();
		AudioUtil.playShowGot();
	}
	
	public static void logGameState() {
		System.out.println( "\n==================================================================" );
		System.out.println( " HAND:" + Hand.get() );
		System.out.println( "------------------------------------------------------------------" );
		System.out.println( " SCORES:" + Hand.getOrderedHand().substring( 8 ) );
		System.out.println( "------------------------------------------------------------------" );
		System.out.println( " BOARD:         " + new ArrayList<Square>(Board.getTokens()) );
		System.out.println( "------------------------------------------------------------------" );
		System.out.println( " JACKS:  " + Deck.getJacks() );
		System.out.println( "------------------------------------------------------------------" );
		System.out.println( " REMOVED: " + Deck.getRemovedCards() );
		System.out.println( "==================================================================\n" );
	}

	
	public void processRequest() {
		if( isDrawCard() ) 
			try {
				if( NEXT_MOVE != null && NEXT_MOVE.equals( MY_MOVE ) ) 
					throw new Exception( "Silly human - SeqBot must place a token!" );
				if( NEXT_MOVE != null && NEXT_MOVE.equals( OPPNENTS_MOVE ) ) 
					throw new Exception( "Silly human - our opponent must place a token!" );
				if( myNewCard == null ) 
					throw new Exception( "Silly human - tell me what card to draw!" );
				Hand.addCard( myNewCard );
				prevMyNewCard = myNewCard;
				if( Hand.get().size() == NUM_CARDS)
					NEXT_MOVE = OPPNENTS_MOVE;
			} catch( Exception ex ) {
				logError( ex, "Failed to add card to hand" );
			}
		else if( isPlaceOpponentToken() ) 
			try {
				Square square = Board.getSquare(opponentPos);
				square.setColor( opponentTokenColor );
				if( NEXT_MOVE != null &&  NEXT_MOVE.equals( DRAW_CARD ) ) 
					throw new Exception( "Silly human - SeqBot must draw a card!" );
				if( NEXT_MOVE != null &&  NEXT_MOVE.equals( MY_MOVE ) ) 
					throw new Exception( "Silly human - SeqBot must place a token!" );
				if( opponentPos == null ) 
					throw new Exception( "Silly human - tell me where our opponent moved!" );
				if( opponentPos == 1 || opponentPos == 10 || opponentPos == 91 || opponentPos == 100  ) 
					throw new Exception( "Silly human - cannot play on wilcard corner squares!" );
				if( opponentPos < 2 || opponentPos > 99 ) 
					throw new Exception( "Silly human - valid positions range from 2 - 99!" );
				if( !isEmptySquare(opponentPos) && !isOneEyeJack(opponentJackSuit) )
					throw new Exception( "Silly human - there is already a " + Board.getSquare( opponentPos ).getColor() + " token @pos: " + opponentPos );

				String msg =  getConfirmationMsg( square, opponentJackSuit, myTokenColor );
				GuiAdapter.showConfirmationWindow( msg );
				
				Card jack = StringUtils.isNotBlank(opponentJackSuit) ? new Card( opponentJackSuit, CardRank.CARD_J ) : null;
				square.setColor( opponentTokenColor );
				playToken( square, jack );
				setOppSeqCount();
				
				prevOpponentPos = opponentPos;
				prevOpponentJackSuit = opponentJackSuit;
				System.out.println( "===============================================================");
				System.out.println( msg );
				System.out.println( "===============================================================");
				NEXT_MOVE = MY_MOVE;
			} catch( Exception ex) {
				logError( ex, "Failed to play " + opponentTokenColor + " token on " + Board.getSquare( opponentPos ).getCard() + " @pos: " + opponentPos );
			} 
		else if( isCalculateMove() )
			try {
				System.out.println( "SeqBot calculating next move... " );
				if( NEXT_MOVE != null && NEXT_MOVE.equals( DRAW_CARD ) ) 
					throw new Exception( "Silly human - SeqBot must draw a card!" );
				if( NEXT_MOVE != null && NEXT_MOVE.equals( OPPNENTS_MOVE ) ) 
					throw new Exception( "Silly human - our opponent must place a token!" );
				if( Hand.get().size() != NUM_CARDS ) 
					throw new Exception( "Must have " + NUM_CARDS + " in hand" );
				Hand.clearAxisRanges();
				myNextMove = MoveCalculator.get();
				prevMyNextMove = myNextMove;
				prevMyJack = myJack;
				
				
				if( myNextMove.isOpponents() )
					myJack = Hand.getOneEyeJacks().iterator().next();
				else if( !Hand.get().contains( myNextMove.getCard() ) )
					myJack = Hand.getTwoEyeJacks().iterator().next();
				
				String jackSuit = myJack == null ? null : myJack.getSuit();
				if( myJack == null || !myJack.isOneEyeJack() )
					myNextMove.setColor( myTokenColor );
				
				GuiAdapter.showInfo( getConfirmationMsg( myNextMove, jackSuit, opponentTokenColor ) );
				
				if( myJack != null && myJack.isOneEyeJack() )
					myNextMove.setColor( opponentTokenColor );

				playToken( myNextMove, myJack );
				
				if( myJack == null )
					Hand.removeCard( myNextMove.getCard() );
				else
					Hand.removeCard( myJack );

				if( MY_SEQ_COUNT > 1 )
					System.out.println("SeqBot wins!");
				
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
	
	public static void playToken( Square square, Card jack ) throws Exception {
		if( jack != null && jack.isOneEyeJack() ) {
			if( square.isMine() )
			AudioUtil.playLikeGot();
			Deck.remove( jack, square.getCard() );
			Board.removeToken( square.getPos() );
		} 
		else if( jack != null && jack.isTwoEyeJack() ) {
			Deck.remove( jack, square.getCard() );
			Board.addToken( square.getPos(), square.getColor() );
		} 
		else {
			Deck.remove( square.getCard() );
			Board.addToken( square.getPos(), square.getColor()  );
		}
	}
	
	public static SeqBot get() {
		if( seqBot == null ) 
			seqBot = new SeqBot();
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
					Deck.add( jack );
				} else {
					msg += "Remove " + Board.getSquare( prevOpponentPos );
					Deck.add( Board.getSquare( prevOpponentPos ).getCard() );
				}
				OPPONENT_SEQ_COUNT = prevOppSeqCount;
				NEXT_MOVE = OPPNENTS_MOVE;
			} else if( prevMyNewCard != null ) {
				Hand.removeCard( prevMyNewCard );
				NEXT_MOVE = DRAW_CARD;
			} else if( prevMyNextMove != null ) {
				Board.removeToken( prevMyNextMove.getPos() );
				if( prevMyJack == null ) {
					Hand.addCard( prevMyNextMove.getCard() );
					Deck.add( prevMyNextMove.getCard() );
				} else {
					Hand.addCard( prevMyJack );
					Deck.add( prevMyNextMove.getCard() );
				}
				MY_SEQ_COUNT = prevMoveSeqCount;
				NEXT_MOVE = MY_MOVE;
			}
			
			SeqBot.get().setStatusMsg( Hand.getOrderedHand() ); 
			GuiAdapter.showInfo( msg );
			
		} catch( Exception ex) {
			logError( ex, "Failed to undo last move" );
		}
	}
	
	private void setOppSeqCount() {
		prevOppSeqCount = OPPONENT_SEQ_COUNT;
		OPPONENT_SEQ_COUNT = 0;
		List<Square> squares =  Board.getTokens().stream().filter( s -> s.getColor().equals( opponentTokenColor ) ).collect( Collectors.toList() );
		Collections.sort( squares );
		for(Square square: squares) 
			for( int axis: RangeUtil.AXIS_DIRECTIONS ) {
				int count = 1;
				Square test = RangeUtil.getNextSquare( square, axis, false );
				while( test != null )
					if( test.getColor().equals( opponentTokenColor ) ) {
						count++;
						test = RangeUtil.getNextSquare( test, axis, false );
					} else 
						break;

				test = RangeUtil.getNextSquare( square, axis, true );
				while( test != null )
					if( test.getColor().equals( opponentTokenColor ) ) {
						count++;
						test = RangeUtil.getNextSquare( test, axis, true );
					} else 
						break;
				
				if( count > 4 ) 
					OPPONENT_SEQ_COUNT++;
				if( count == 10 ) 
					OPPONENT_SEQ_COUNT++;
			}
		
		if( OPPONENT_SEQ_COUNT > 1 )
			System.out.println( "Our opponent has won!");
	}
	
	private static String getConfirmationMsg( Square square, String jackSuit, String removeTokenColor ) {
		if( isOneEyeJack(jackSuit) )
			return "Remove " + removeTokenColor + " token using " + new Card(jackSuit, CardRank.CARD_J) + " for " + square.getCard() + " @pos: " + square.getPos();
		else if(  StringUtils.isNotBlank(jackSuit) )
			return "Play " + square.getColor() + " token using " + new Card(jackSuit, CardRank.CARD_J) + " for " + square.getCard() + " @pos: " + square.getPos();
		return "Play " + square.getColor() + " token for " + square.getCard() + " @pos: " + square.getPos();
	}
	
	public static boolean isEmptySquare( int pos ) {
		String squareColor = Board.getSquare( pos ).getColor();
		return StringUtils.isBlank(squareColor);
	}
	
	public static boolean isOneEyeJack( String suit ) {
		boolean isJack = StringUtils.isNotBlank(suit);
		return isJack && (suit.equals( CardSuit.SPADES ) || suit.equals( CardSuit.HEARTS ));
	}

	private boolean isDrawCard() {
		return myNewCard != null && StringUtils.isNotEmpty( myNewCard.getRank() ) && StringUtils.isNotEmpty( myNewCard.getSuit() );
	}
	
	private boolean isPlaceOpponentToken() {
		return !isDrawCard() && StringUtils.isNotEmpty( opponentTokenColor ) && opponentPos != null;
	}
	
	private boolean isCalculateMove() {
		return !isDrawCard() && !isPlaceOpponentToken() && calcNextMove;
	}
	
	private void logError( Exception ex, String msg ) {
		errMsg = ( ex.getMessage().startsWith( "Silly human" ) ? "" : msg  + " - " ) + ex.getMessage();
		System.out.println( msg );
		if( !ex.getMessage().startsWith( "Silly human" ) ) {
			//report( "Error-State Report" );
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
	
	public void clearPrev() {
		prevOpponentPos = null;
		prevOpponentJackSuit = "";
		prevMyNewCard = null;
		prevMyNextMove = null;
		prevMyJack = null;
	}
	
	public void setPrevMoveMySeq(int i) {
		prevMoveSeqCount = i;
	}
	
	public void setPrevMoveOppSeq(int i) {
		prevMoveSeqCount = i;
	}
	
	public void setMySuit( String suit ) {
		this.mySuit = suit;
	}
	
	public String getMySuit() {
		return mySuit;
	}
	
	public void setMyJack( Card jack ) {
		this.myJack = jack;
	}
	
	

	private static SeqBot seqBot = null;
	private static final String MY_MOVE = "MY_MOVE";
	private static final String DRAW_CARD = "DRAW_CARD";
	private static final String OPPNENTS_MOVE = "OPPNENTS_MOVE";
	private static String NEXT_MOVE = null;
	boolean calcNextMove = false;
	private Card myNewCard = null;
	private Square myNextMove = null;
	private Card myJack = null;
	private String mySuit = null;
	private String myTokenColor = "";
	private String opponentTokenColor = "";
	private String opponentJackSuit = "";
	private Integer opponentPos = null;
	private String errMsg = null;
	private String statusMsg = "SeqBot is ready!";
	
	private Integer prevOpponentPos = null;
	private String prevOpponentJackSuit = "";
	private Card prevMyNewCard = null;
	private Square prevMyNextMove = null;
	private Card prevMyJack = null;
	private int prevMoveSeqCount = 0;
	private int prevOppSeqCount = 0;
	
	public static int MY_SEQ_COUNT = 0;
	public static int OPPONENT_SEQ_COUNT = 0;
}
