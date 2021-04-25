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
			SeqBot.get().setOpponentTokenColor( SeqBot.OP_COLOR );
			
			//testNWScenario();
			//testNormal();
			//brokeFGame();
			//testGameFinisher();
			testSeqFinisher();
			//testOneEyeJack();
			System.out.println( "SeqBot loaded mock hand" );
		} catch( Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void brokeFGame() throws Exception {
		Hand.addCard( new Card(CardSuit.SPADES, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.CLUBS, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_A) );
		Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_K) );
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_4) );
		Hand.addCard( new Card(CardSuit.SPADES, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.SPADES, CardRank.CARD_9) );
		
		// 3S@15=Red, AD@24=Red, KC@26=Red, 10H@35=Red,
		// 9C@38=Red, KH@44=Red, 8C@48=Blue, AS@51=Blue, 
		// 9D@53=Red, 4H@55=Red, 5H@56=Red, 					
		// 6H@57=Blue, QS@59=Red, KS@61=Blue, 8H@62=Red, 
		// 8D@63=Blue, 2C@64=Blue, 3C@65=Red, 6C@68=Red, 
		// QS@71=Blue, 9H@72=Blue, 7D@73=Red, 6D@74=Blue, 
		// 10S@81=Blue, 8S@93=Blue, 7S@94=Blue, 5S@96=Blue]

		// AH@54=Blue removed
		
		Board.addToken( 15, TokenColor.RED );
		Board.addToken( 24, TokenColor.RED );
		Board.addToken( 26, TokenColor.RED );
		Board.addToken( 35, TokenColor.RED );
		Board.addToken( 38, TokenColor.RED );
		Board.addToken( 44, TokenColor.RED );
		Board.addToken( 48, TokenColor.BLUE );
		Board.addToken( 48, TokenColor.BLUE );
		
		Board.addToken( 53, TokenColor.RED );
		Board.addToken( 55, TokenColor.BLUE );
		Board.addToken( 56, TokenColor.RED );
		Board.addToken( 57, TokenColor.BLUE );
		Board.addToken( 59, TokenColor.RED );
		Board.addToken( 61, TokenColor.RED );
		Board.addToken( 62, TokenColor.RED );

		Board.addToken( 63, TokenColor.BLUE );
		Board.addToken( 64, TokenColor.BLUE );
		Board.addToken( 65, TokenColor.RED );
		Board.addToken( 68, TokenColor.RED );
		Board.addToken( 71, TokenColor.BLUE );
		Board.addToken( 72, TokenColor.BLUE );
		Board.addToken( 73, TokenColor.RED );
		Board.addToken( 74, TokenColor.BLUE );
		
		Board.addToken( 81, TokenColor.BLUE );
		Board.addToken( 93, TokenColor.BLUE );
		Board.addToken( 94, TokenColor.BLUE );
		Board.addToken( 96, TokenColor.BLUE );
	}
	
	@SuppressWarnings("unused")
	private static void testNWScenario() throws Exception {
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.CLUBS, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_A) );
		Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_K) );
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_4) );
		Hand.addCard( new Card(CardSuit.SPADES, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.SPADES, CardRank.CARD_9) );
		
		Board.addToken( 12, TokenColor.BLUE );
		Board.addToken( 34, TokenColor.BLUE );
		Board.addToken( 45, TokenColor.BLUE );

		Deck.remove( new Card(CardSuit.HEARTS, CardRank.CARD_3) );
		Deck.remove( new Card(CardSuit.HEARTS, CardRank.CARD_Q) );
		Deck.remove( new Card(CardSuit.HEARTS, CardRank.CARD_3) );	
	}
	
	@SuppressWarnings("unused")
	private static void testNormal() throws Exception {
		
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_8) );
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_6) );
		Hand.addCard( new Card(CardSuit.CLUBS, CardRank.CARD_6) );
		Hand.addCard( new Card(CardSuit.CLUBS, CardRank.CARD_3) );
		Hand.addCard( new Card(CardSuit.DIAMONDS, CardRank.CARD_2) );
		Hand.addCard( new Card(CardSuit.HEARTS, CardRank.CARD_J) );
		Hand.addCard( new Card(CardSuit.CLUBS, CardRank.CARD_J) );
		
		Board.addToken( 12, TokenColor.BLUE );
		Board.addToken( 23, TokenColor.BLUE );
		Board.addToken( 86, TokenColor.BLUE );

		Deck.remove( new Card(CardSuit.HEARTS, CardRank.CARD_3) );
		Deck.remove( new Card(CardSuit.DIAMONDS, CardRank.CARD_K) );
		Deck.remove( new Card(CardSuit.HEARTS, CardRank.CARD_3) );
		Deck.remove( new Card(CardSuit.CLUBS, CardRank.CARD_2) );
		
		Board.addToken( 33, TokenColor.RED );
		Board.addToken( 44, TokenColor.RED );
		Board.addToken( 26, TokenColor.RED );
		
		Deck.remove( new Card(CardSuit.DIAMONDS, CardRank.CARD_Q) );
		Deck.remove( new Card(CardSuit.HEARTS, CardRank.CARD_K) );
		Deck.remove( new Card(CardSuit.CLUBS, CardRank.CARD_K) );
		Deck.remove( new Card(CardSuit.HEARTS, CardRank.CARD_J) );
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
		Deck.remove( new Card(CardSuit.DIAMONDS, CardRank.CARD_6) );
		Deck.remove( new Card(CardSuit.DIAMONDS, CardRank.CARD_7) );
		Deck.remove( new Card(CardSuit.DIAMONDS, CardRank.CARD_8) );
		Deck.remove( new Card(CardSuit.SPADES, CardRank.CARD_3) );
		Deck.remove( new Card(CardSuit.CLUBS, CardRank.CARD_A) );
		Deck.remove( new Card(CardSuit.HEARTS, CardRank.CARD_T) );
		Deck.remove( new Card(CardSuit.HEARTS, CardRank.CARD_3) );
		
		Board.addToken( 92, TokenColor.RED );
		Board.addToken( 93, TokenColor.RED );
		Board.addToken( 30, TokenColor.RED );
		Deck.remove( new Card(CardSuit.SPADES, CardRank.CARD_9) );
		Deck.remove( new Card(CardSuit.SPADES, CardRank.CARD_8) );
		Deck.remove( new Card(CardSuit.CLUBS, CardRank.CARD_K) );
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
		Deck.remove( new Card(CardSuit.DIAMONDS, CardRank.CARD_6) );
		Deck.remove( new Card(CardSuit.DIAMONDS, CardRank.CARD_7) );
		Deck.remove( new Card(CardSuit.DIAMONDS, CardRank.CARD_8) );
		
		Board.addToken( 92, TokenColor.RED );
		Board.addToken( 93, TokenColor.RED );
		Board.addToken( 94, TokenColor.RED );
		Deck.remove( new Card(CardSuit.SPADES, CardRank.CARD_9) );
		Deck.remove( new Card(CardSuit.SPADES, CardRank.CARD_8) );
		Deck.remove( new Card(CardSuit.SPADES, CardRank.CARD_7) );
	}

	@SuppressWarnings("unused")
	private static void testOneEyeJack() throws Exception {
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
		Deck.remove( new Card(CardSuit.DIAMONDS, CardRank.CARD_A) );
		Deck.remove( new Card(CardSuit.CLUBS, CardRank.CARD_A) );
		Deck.remove( new Card(CardSuit.CLUBS, CardRank.CARD_K) );
		
		Board.addToken( 2, TokenColor.RED );
		Board.addToken( 3, TokenColor.RED );
		Board.addToken( 4, TokenColor.RED );
		Deck.remove( new Card(CardSuit.DIAMONDS, CardRank.CARD_6) );
		Deck.remove( new Card(CardSuit.DIAMONDS, CardRank.CARD_7) );
		Deck.remove( new Card(CardSuit.DIAMONDS, CardRank.CARD_8) );
	}
}
