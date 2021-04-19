package com.seq.cards;

import java.util.*;

public class CardSuit {
	
	public static final String SPADES = "S";
	public static final String DIAMONDS = "D";
	public static final String CLUBS = "C";
	public static final String HEARTS = "H";
	
	public static final String SPADES_LABEL = "Spades";
	public static final String DIAMONDS_LABEL = "Diamonds";
	public static final String CLUBS_LABEL = "Clubs";
	public static final String HEARTS_LABEL = "Hearts";
	
	public static final String WILD = "W";
	
	public static HashMap<String, String> suits = new HashMap<>();
	
	static {
		suits.put( SPADES_LABEL, SPADES );
		suits.put( HEARTS_LABEL, HEARTS );
		suits.put( DIAMONDS_LABEL, DIAMONDS );
		suits.put( CLUBS_LABEL, CLUBS );
	}

	public static final String [] SUIT_CODES = new String [] {  "", SPADES, HEARTS, DIAMONDS, CLUBS };
	public static final String [] SUIT_NAMES = new String [] {  "", SPADES_LABEL, HEARTS_LABEL, DIAMONDS_LABEL, CLUBS_LABEL };
	public static final Integer [] SUIT_VALUES = new Integer [] { 1, 2, 3, 4, 5 };
}
