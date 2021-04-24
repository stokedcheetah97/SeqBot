package com.seq.util;

import java.util.StringTokenizer;
import com.seq.board.*;
import com.seq.cards.*;

public class ReloadUtil {

	public static String HAND = "KD, JD, TD, 9H, 8H, 6S, 6C";
	public static String BOARD = "4S@16=Blue, 5S@17=Red, KD@23=Blue, KC@26=Red, QC@27=Red, TC@28=Red, 8S@29=Red, KC@30=Blue, QH@34=Blue, TH@35=Red, 9C@38=Red, 9S@39=Red, QC@40=Red, 6H@42=Red, 3H@45=Blue, 7H@47=Red, 8C@48=Blue, TS@49=Red, AS@51=Blue, 5H@56=Blue, 6H@57=Blue, KS@61=Blue, 3C@65=Red, 5C@67=Blue, 6C@68=Blue";

	public static String JACKS = "JS, JH, JC";
	
	public static String REMOVED = "8S, 8H";

	public static void reload() {
		try {
			
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
				if( square != null )
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
