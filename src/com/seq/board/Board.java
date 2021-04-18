package com.seq.board;

import java.util.*;

import com.seq.cards.*;

public class Board {
	
	public Set<Square> getOpenSquares( Card card ) {
		Set<Square> squares = new TreeSet<>();
		for( int i=1; i<101; i++ ) {
			Square square = getSquare(i);
			if( square != null && square.getColor().isEmpty() && square.getCard().equals( card ) ) 
				squares.add( square );
		}
		return squares;
	}

	public void addToken( int positionNumber, String color ) {
		this.tokens.add( new Square( positionNumber, color ) );
	}
	
	public Square getSquare( int positionNumber ) {
		if( positionNumber < 1 || positionNumber > 100 ) return null;
		for( Square t: this.tokens )
			if( t.getPositionNumber() == positionNumber ) return t;
		return new Square( positionNumber, "" );
	}
	
	public void removeToken( int positionNumber, String color ) {
		this.tokens.add( new Square( positionNumber, color ) );
	}
	
	public static Board get() {
		return myBoard;
	}
	
	public Card [] [] getCardMatrix() {
		return cardMatrix;
	}

	private Board() {
		
		cardMatrix [0] [0] = new Card( CardSuit.WILD, CardRank.WILD );
		cardMatrix [0] [1] = new Card( CardSuit.DIAMONDS, CardRank.CARD_6 );
		cardMatrix [0] [2] = new Card( CardSuit.DIAMONDS, CardRank.CARD_7 );
		cardMatrix [0] [3] = new Card( CardSuit.DIAMONDS, CardRank.CARD_8 );
		cardMatrix [0] [4] = new Card( CardSuit.DIAMONDS, CardRank.CARD_9 );
		cardMatrix [0] [5] = new Card( CardSuit.DIAMONDS, CardRank.CARD_T );
		cardMatrix [0] [6] = new Card( CardSuit.DIAMONDS, CardRank.CARD_Q );
		cardMatrix [0] [7] = new Card( CardSuit.DIAMONDS, CardRank.CARD_K );
		cardMatrix [0] [8] = new Card( CardSuit.DIAMONDS, CardRank.CARD_A );
		cardMatrix [0] [9] = new Card( CardSuit.WILD, CardRank.WILD );
		
		cardMatrix [1] [0] = new Card( CardSuit.DIAMONDS, CardRank.CARD_5 );
		cardMatrix [1] [1] = new Card( CardSuit.HEARTS, CardRank.CARD_3 );
		cardMatrix [1] [2] = new Card( CardSuit.HEARTS, CardRank.CARD_2 );
		cardMatrix [1] [3] = new Card( CardSuit.SPADES, CardRank.CARD_2 );
		cardMatrix [1] [4] = new Card( CardSuit.SPADES, CardRank.CARD_3 );
		cardMatrix [1] [5] = new Card( CardSuit.SPADES, CardRank.CARD_4 );
		cardMatrix [1] [6] = new Card( CardSuit.SPADES, CardRank.CARD_5 );
		cardMatrix [1] [7] = new Card( CardSuit.SPADES, CardRank.CARD_6 );
		cardMatrix [1] [8] = new Card( CardSuit.SPADES, CardRank.CARD_7 );
		cardMatrix [1] [9] = new Card( CardSuit.CLUBS, CardRank.CARD_A );
		
		cardMatrix [2] [0] = new Card( CardSuit.DIAMONDS, CardRank.CARD_4 );
		cardMatrix [2] [1] = new Card( CardSuit.HEARTS, CardRank.CARD_4 );
		cardMatrix [2] [2] = new Card( CardSuit.DIAMONDS, CardRank.CARD_K );
		cardMatrix [2] [3] = new Card( CardSuit.DIAMONDS, CardRank.CARD_A );
		cardMatrix [2] [4] = new Card( CardSuit.CLUBS, CardRank.CARD_A );
		cardMatrix [2] [5] = new Card( CardSuit.CLUBS, CardRank.CARD_K );
		cardMatrix [2] [6] = new Card( CardSuit.CLUBS, CardRank.CARD_Q );
		cardMatrix [2] [7] = new Card( CardSuit.CLUBS, CardRank.CARD_T );
		cardMatrix [2] [8] = new Card( CardSuit.SPADES, CardRank.CARD_8 );
		cardMatrix [2] [9] = new Card( CardSuit.CLUBS, CardRank.CARD_K );
		
		cardMatrix [3] [0] = new Card( CardSuit.DIAMONDS, CardRank.CARD_3 );
		cardMatrix [3] [1] = new Card( CardSuit.HEARTS, CardRank.CARD_5 );
		cardMatrix [3] [2] = new Card( CardSuit.DIAMONDS, CardRank.CARD_Q );
		cardMatrix [3] [3] = new Card( CardSuit.HEARTS, CardRank.CARD_Q );
		cardMatrix [3] [4] = new Card( CardSuit.HEARTS, CardRank.CARD_T );
		cardMatrix [3] [5] = new Card( CardSuit.HEARTS, CardRank.CARD_9 );
		cardMatrix [3] [6] = new Card( CardSuit.HEARTS, CardRank.CARD_8 );
		cardMatrix [3] [7] = new Card( CardSuit.CLUBS, CardRank.CARD_9 );
		cardMatrix [3] [8] = new Card( CardSuit.SPADES, CardRank.CARD_9 );
		cardMatrix [3] [9] = new Card( CardSuit.CLUBS, CardRank.CARD_Q );
		
		cardMatrix [4] [0] = new Card( CardSuit.DIAMONDS, CardRank.CARD_2 );
		cardMatrix [4] [1] = new Card( CardSuit.HEARTS, CardRank.CARD_6 );
		cardMatrix [4] [2] = new Card( CardSuit.DIAMONDS, CardRank.CARD_T );
		cardMatrix [4] [3] = new Card( CardSuit.HEARTS, CardRank.CARD_K );
		cardMatrix [4] [4] = new Card( CardSuit.HEARTS, CardRank.CARD_3 );
		cardMatrix [4] [5] = new Card( CardSuit.HEARTS, CardRank.CARD_2 );
		cardMatrix [4] [6] = new Card( CardSuit.HEARTS, CardRank.CARD_7 );
		cardMatrix [4] [7] = new Card( CardSuit.CLUBS, CardRank.CARD_8 );
		cardMatrix [4] [8] = new Card( CardSuit.SPADES, CardRank.CARD_T );
		cardMatrix [4] [9] = new Card( CardSuit.CLUBS, CardRank.CARD_T );
		
		cardMatrix [5] [0] = new Card( CardSuit.SPADES, CardRank.CARD_A );
		cardMatrix [5] [1] = new Card( CardSuit.HEARTS, CardRank.CARD_7 );
		cardMatrix [5] [2] = new Card( CardSuit.DIAMONDS, CardRank.CARD_9 );
		cardMatrix [5] [3] = new Card( CardSuit.HEARTS, CardRank.CARD_A );
		cardMatrix [5] [4] = new Card( CardSuit.HEARTS, CardRank.CARD_4 );
		cardMatrix [5] [5] = new Card( CardSuit.HEARTS, CardRank.CARD_5 );
		cardMatrix [5] [6] = new Card( CardSuit.HEARTS, CardRank.CARD_6 );
		cardMatrix [5] [7] = new Card( CardSuit.CLUBS, CardRank.CARD_7 );
		cardMatrix [5] [8] = new Card( CardSuit.SPADES, CardRank.CARD_Q );
		cardMatrix [5] [9] = new Card( CardSuit.CLUBS, CardRank.CARD_9 );
		
		cardMatrix [6] [0] = new Card( CardSuit.SPADES, CardRank.CARD_K );
		cardMatrix [6] [1] = new Card( CardSuit.HEARTS, CardRank.CARD_8 );
		cardMatrix [6] [2] = new Card( CardSuit.DIAMONDS, CardRank.CARD_8 );
		cardMatrix [6] [3] = new Card( CardSuit.CLUBS, CardRank.CARD_2 );
		cardMatrix [6] [4] = new Card( CardSuit.CLUBS, CardRank.CARD_3 );
		cardMatrix [6] [5] = new Card( CardSuit.CLUBS, CardRank.CARD_4 );
		cardMatrix [6] [6] = new Card( CardSuit.CLUBS, CardRank.CARD_5 );
		cardMatrix [6] [7] = new Card( CardSuit.CLUBS, CardRank.CARD_6 );
		cardMatrix [6] [8] = new Card( CardSuit.SPADES, CardRank.CARD_K );
		cardMatrix [6] [9] = new Card( CardSuit.CLUBS, CardRank.CARD_8 );
		
		cardMatrix [7] [0] = new Card( CardSuit.SPADES, CardRank.CARD_Q );
		cardMatrix [7] [1] = new Card( CardSuit.HEARTS, CardRank.CARD_9 );
		cardMatrix [7] [2] = new Card( CardSuit.DIAMONDS, CardRank.CARD_7 );
		cardMatrix [7] [3] = new Card( CardSuit.DIAMONDS, CardRank.CARD_6 );
		cardMatrix [7] [4] = new Card( CardSuit.DIAMONDS, CardRank.CARD_5 );
		cardMatrix [7] [5] = new Card( CardSuit.DIAMONDS, CardRank.CARD_4 );
		cardMatrix [7] [6] = new Card( CardSuit.DIAMONDS, CardRank.CARD_3 );
		cardMatrix [7] [7] = new Card( CardSuit.DIAMONDS, CardRank.CARD_2 );
		cardMatrix [7] [8] = new Card( CardSuit.SPADES, CardRank.CARD_A );
		cardMatrix [7] [9] = new Card( CardSuit.CLUBS, CardRank.CARD_7 );
		
		
	}

	private TreeSet<Square> tokens = new TreeSet<>();
	private static Card [] [] cardMatrix = new Card[10][10];	
	private static Board myBoard = new Board();
}
