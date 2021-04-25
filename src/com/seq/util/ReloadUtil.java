package com.seq.util;

import java.util.*;
import com.seq.SeqBot;
import com.seq.board.*;
import com.seq.cards.*;

public class ReloadUtil {

	public static String HAND = "QS, JS, 8C, 6S, 5C, 4H, 4D";
	
	public static String BOARD = "3H@12=Blue, AC@20=Green, KD@23=Blue, TC@28=Green, KC@30=Blue, QH@34=Blue, QC@40=Green, TD@43=Blue, KH@44=Blue, 3H@45=Blue, 2H@46=Green, 7H@47=Blue, TC@50=Green, AS@51=Green, 9D@53=Green, AH@54=Green, 4H@55=Green, 5H@56=Green, 7C@58=Blue, 9C@60=Blue, 8D@63=Blue, 2C@64=Green, 3C@65=Blue, 4C@66=Green, 5C@67=Blue, KS@69=Blue, 7D@73=Green, 6D@74=Green, 5D@75=Green, 3D@77=Green, 2D@78=Blue, AH@85=Green, 8S@93=Green, 5S@96=Blue, 4S@97=Blue, 2S@99=Blue";
		
	public static String JACKS = "JD, JC, JC";
	
	public static String REMOVED = "";
	
	public static String MY_SEQ_SQUARES = "3H@12=Blue, KD@23=Blue, QH@34=Blue, 3H@45=Blue";
	
	public static String OP_SEQ_SQUARES = "";

	public static void reload() {
		try {
			
			StringTokenizer st6 = new StringTokenizer(OP_SEQ_SQUARES, ", " );
			Set<Square> opSeqSquares = new TreeSet<>(); 
			while(st6.hasMoreElements() ) {
				String token = st6.nextToken();
				String pos = token.substring( 3, token.indexOf( "=" ) );
				String color = token.substring( 3 + pos.length() + 1 );
				Square square = Board.getSquare( Integer.valueOf( pos ) );
				square.setColor( color );
				opSeqSquares.add( square );
			}
			
			if( !opSeqSquares.isEmpty() )
				SeqBot.OP_SEQ_1 = opSeqSquares;
			
			StringTokenizer st5 = new StringTokenizer(MY_SEQ_SQUARES, ", " );
			Set<Square> mySeqSquares = new TreeSet<>(); 
			while(st5.hasMoreElements() ) {
				String token = st5.nextToken();
				String pos = token.substring( 3, token.indexOf( "=" ) );
				String color = token.substring( 3 + pos.length() + 1 );
				Square square = Board.getSquare( Integer.valueOf( pos ) );
				square.setColor( color );
				mySeqSquares.add( square );
			}
			
			if( !mySeqSquares.isEmpty() )
				SeqBot.MY_SEQ_1 = mySeqSquares;
			
			StringTokenizer st3 = new StringTokenizer(HAND, ", " );
			while(st3.hasMoreElements() ) {
				String token = st3.nextToken();
				String rank = token.substring( 0, 1 );
				String suit = token.substring( 1, 2 );
				Card card = new Card( suit, rank );
				Hand.addCard( card );
			}
		
			StringTokenizer st2 = new StringTokenizer(BOARD, ", " );
			while(st2.hasMoreElements() ) {
				String token = st2.nextToken();
				String rank = token.substring( 0, 1 );
				String suit = token.substring( 1, 2 );
				String pos = token.substring( 3, token.indexOf( "=" ) );
				String color = token.substring( 3 + pos.length() + 1 );
				Card card = new Card( suit, rank );
				Square square = Board.getSquare( Integer.valueOf( pos ) );
				square.setColor( color );
				Deck.remove( card );
				Board.addToken( square.getPos(), color);
			}
			
			StringTokenizer st = new StringTokenizer(JACKS, ", " );
			while(st.hasMoreElements() ) {
				String token = st.nextToken();
				String rank = token.substring( 0, 1 );
				String suit = token.substring( 1, 2 );
				Card jack = new Card( suit, rank );
				Deck.remove( jack, null );
			}	
			
			StringTokenizer st4 = new StringTokenizer(REMOVED, ", " );
			while(st4.hasMoreElements() ) {
				String token = st4.nextToken();
				String rank = token.substring( 0, 1 );
				String suit = token.substring( 1, 2 );
				Card card = new Card( suit, rank );
				Deck.removeCard( card );
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
