package com.seq;

import java.util.Arrays;
import java.util.List;

public class CardRank {
	
	public static final String CARD_2 = "2";
	public static final String CARD_3 = "3";
	public static final String CARD_4 = "4";
	public static final String CARD_5 = "5";
	public static final String CARD_6 = "6";
	public static final String CARD_7 = "7";
	public static final String CARD_8 = "8";
	public static final String CARD_9 = "9";
	public static final String CARD_T = "10";
	
	// Hears + Spades = 1-Eyed Jacks
	public static final String CARD_J = "J";
	public static final String CARD_Q = "Q";
	public static final String CARD_K = "K";
	public static final String CARD_A = "A";
	public static final String WILD = "W";

	static List<String> rankNames = Arrays.asList( "", CARD_A, CARD_K, CARD_Q, CARD_J, CARD_T, CARD_9, CARD_8, CARD_7, CARD_6, CARD_5, CARD_4, CARD_3, CARD_2 );
	static List<Integer> rankValues = Arrays.asList( 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 );
}
