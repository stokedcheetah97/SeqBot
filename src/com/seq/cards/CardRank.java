package com.seq.cards;

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

	public static final String [] RANK_NAMES = new String [] { "", CARD_A, CARD_K, CARD_Q, CARD_J, CARD_T, CARD_9, CARD_8, CARD_7, CARD_6, CARD_5, CARD_4, CARD_3, CARD_2 };
	public static final Integer [] RANK_VALUES = new Integer [] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

}
