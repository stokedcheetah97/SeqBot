package com.seq.cards;

import java.util.*;

public class CardSuit {
	
	public static final String SPADES = "S";
	public static final String DIAMONDS = "D";
	public static final String CLUBS = "C";
	public static final String HEARTS = "H";
	public static final String WILD = "W";
	
	public static HashMap<String, String> suits = new HashMap<>();
	
	static {
		suits.put( "Spades", SPADES );
		suits.put( "Hearts", HEARTS );
		suits.put( "Diamonds", DIAMONDS );
		suits.put( "Clubs", CLUBS );
	}

	public static final String [] SUIT_CODES = new String [] {  "", SPADES, HEARTS, DIAMONDS, CLUBS };
	public static final String [] SUIT_NAMES = new String [] {  "", "Spades", "Hearts", "Diamonds", "Clubs" };
	public static final Integer [] SUIT_VALUES = new Integer [] { 1, 2, 3, 4, 5 };
}
