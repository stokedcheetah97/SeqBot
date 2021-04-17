package com.seq;

import java.util.*;

public class CardSuit {
	
	static final String SPADES = "S";
	static final String DIAMONDS = "D";
	static final String CLUBS = "C";
	static final String HEARTS = "H";
	static final String WILD = "W";
	
	static HashMap<String, String> suits = new HashMap<>();
	
	static {
		suits.put( "Spades", SPADES );
		suits.put( "Hearts", HEARTS );
		suits.put( "Diamonds", DIAMONDS );
		suits.put( "Clubs", CLUBS );
	}

	static List<String> suitCodes = Arrays.asList( "", SPADES, HEARTS, DIAMONDS, CLUBS );
	static List<String> suitNames = Arrays.asList( "", "Spades", "Hearts", "Diamonds", "Clubs" );
	static List<Integer> suitValues = Arrays.asList( 1, 2, 3, 4, 5 );
}
