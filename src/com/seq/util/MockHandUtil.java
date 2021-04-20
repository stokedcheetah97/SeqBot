package com.seq.util;

import com.seq.SeqBot;
import com.seq.board.Board;
import com.seq.board.TokenColor;
import com.seq.cards.Card;
import com.seq.cards.CardRank;
import com.seq.cards.CardSuit;
import com.seq.cards.Deck;
import com.seq.cards.Hand;

public class MockHandUtil {

	public static void init() {
		try {
			SeqBot.get().setMyTokenColor( TokenColor.BLUE );
			SeqBot.get().setOpponentTokenColor( TokenColor.RED );
			testGameFinisher();
			System.out.println( "SeqBot loaded mock hand" );
			SeqBot.logGameState();
		} catch( Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void testGameFinisher() throws Exception {
		
		Hand.addCard( new Card(CardSuit.CLUBS, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.SPADES, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_A) );
		Hand.addCard( new Card(CardSuit.CLUBS, CardRank.CARD_6) );
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_A) );
		
		Board.addToken( 2, TokenColor.BLUE );
		Board.addToken( 3, TokenColor.BLUE );
		Board.addToken( 4, TokenColor.BLUE );
		Board.addToken( 15, TokenColor.BLUE );
		Board.addToken( 25, TokenColor.BLUE );
		Board.addToken( 35, TokenColor.BLUE );
		Board.addToken( 45, TokenColor.BLUE );
		Deck.playCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_6) );
		Deck.playCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_7) );
		Deck.playCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_8) );
		Deck.playCard( new Card(CardSuit.SPADES, CardRank.CARD_3) );
		Deck.playCard( new Card(CardSuit.CLUBS, CardRank.CARD_A) );
		Deck.playCard( new Card(CardSuit.HEARTS, CardRank.CARD_T) );
		Deck.playCard( new Card(CardSuit.HEARTS, CardRank.CARD_3) );
		
		Board.addToken( 92, TokenColor.RED );
		Board.addToken( 93, TokenColor.RED );
		Board.addToken( 30, TokenColor.RED );
		Deck.playCard( new Card(CardSuit.SPADES, CardRank.CARD_9) );
		Deck.playCard( new Card(CardSuit.SPADES, CardRank.CARD_8) );
		Deck.playCard( new Card(CardSuit.CLUBS, CardRank.CARD_K) );
	}
	
	@SuppressWarnings("unused")
	private static void testSeqFinisher() throws Exception {
		Hand.addCard( new Card(CardSuit.CLUBS, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.SPADES, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_A) );
		Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_A) );
		Hand.addCard( new Card(CardSuit.CLUBS, CardRank.CARD_6) );
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_A) );
		
		Board.addToken( 2, TokenColor.BLUE );
		Board.addToken( 3, TokenColor.BLUE );
		Board.addToken( 4, TokenColor.BLUE );
		Deck.playCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_6) );
		Deck.playCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_7) );
		Deck.playCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_8) );
		
		Board.addToken( 92, TokenColor.RED );
		Board.addToken( 93, TokenColor.RED );
		Board.addToken( 94, TokenColor.RED );
		Deck.playCard( new Card(CardSuit.SPADES, CardRank.CARD_9) );
		Deck.playCard( new Card(CardSuit.SPADES, CardRank.CARD_8) );
		Deck.playCard( new Card(CardSuit.SPADES, CardRank.CARD_7) );
	}

	@SuppressWarnings("unused")
	private static void testSeqBlocker() throws Exception {
		Hand.addCard( new Card(CardSuit.SPADES, CardRank.CARD_9) );
		Hand.addCard( new Card(CardSuit.SPADES, CardRank.CARD_8) );
		Hand.addCard( new Card(CardSuit.SPADES, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_A) );
		Hand.addCard( new Card(CardSuit.CLUBS, CardRank.CARD_6) );
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_A) );
		
		Board.addToken( 9, TokenColor.BLUE );
		Board.addToken( 20, TokenColor.BLUE );
		Board.addToken( 21, TokenColor.BLUE );
		Deck.playCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_A) );
		Deck.playCard( new Card(CardSuit.CLUBS, CardRank.CARD_A) );
		Deck.playCard( new Card(CardSuit.CLUBS, CardRank.CARD_K) );
		
		Board.addToken( 2, TokenColor.RED );
		Board.addToken( 3, TokenColor.RED );
		Board.addToken( 4, TokenColor.RED );
		Deck.playCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_6) );
		Deck.playCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_7) );
		Deck.playCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_8) );
	}
}
