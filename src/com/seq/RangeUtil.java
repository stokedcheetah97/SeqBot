package com.seq;

import java.util.*;

public class RangeUtil {

	public static Set<Square> getRange() {
		Set<Square> squares = new TreeSet<>();
		for( Card card: Hand.get() )
			squares.addAll( Board.get().getOpenSquares( card ) );

		Set<Square> range = new TreeSet<>();
		for( Square square: squares )
			range.addAll( getRange(square) );
		System.out.println( "Full-Hand Range = " + range );
		return range;
	}

	private static Set<Square> getRange( Square square ) {
		Set<Square> range = new TreeSet<>();
		for( int axis: AXIS_DIRECTIONS ) {
			Set<Square> axisRange = getAxisRange( getAxisSquares( square, axis ) );
			System.out.println( "Range[" + square + "]-Axis[" + axis + "]  = " + axisRange );
			if( axisRange != null ) {
				square.myAxisRanges.put( axis, axisRange );
				range.addAll( axisRange );
			}
		}
		System.out.println( "Range [" + square + "] " + range );
		return range;
	}

	private static Set<Square> getAxisRange( Square[] squares ) {
		Set<Square> range = new TreeSet<>();
		for( int i=0; i<9; i++ )
			if( squares[i] == null ) continue;
			else if( Square.isOpponents( squares[i] ) )
				if( i < 4 ) range.clear();
				else break;
			else range.add( squares[i] );
		
		if( range.size() < 5 ) range = null;
		else 
			for( Square square: range ) 
				if( Hand.get().contains( square.getCard() ) )
					square.addToMyRange( range );
		return range;
	}
	
	private static Square[] getAxisSquares( Square square, int axis ) {
		Square[] squares = new Square [9];
		squares[4] = square;
		for( int i=4; i>0; i-- )
			squares[i-1] = getNextSquare( squares[i], axis, false );
		for( int i=4; i<9; i++ )
			squares[i+1] = getNextSquare( squares[i], axis, true );
		
		System.out.println( "Found axis [ " + axis + " ] squares: " + squares );
		return squares;	
	}
	
	private static Square getNextSquare( Square square, int axis, boolean goForward ) {
		int step = getStepSize( axis ) * ( goForward ? 1 : -1 );
		return Board.get().getSquare( square.positionNumber + step );
	}

	private static int getStepSize( int axis ) {
		return axis == NORTH_SOUTH ? 10 : axis == WEST_EAST ? 1 : axis == NW_SE ? 11 : axis == NE_SW ? 9 : -1;
	}
	
	private static final int NORTH_SOUTH = 1;
	private static final int WEST_EAST = 2;
	private static final int NW_SE = 3;
	private static final int NE_SW = 4;
	private static final int[] AXIS_DIRECTIONS = { NORTH_SOUTH, WEST_EAST, NW_SE, NE_SW };
	
//	private static boolean isEmpty( Square square ) {
//		return square != null && square.color.isEmpty();
//	}
//	
//	private static boolean isMine( Square square ) {
//		return square != null && !square.color.isEmpty() && square.color.equals( SeqBot.get().myTokenColor );
//	}
//
//	private static double calc_NS( Square square ) {
//		
//		int index = 4;
//		int pos = square.positionNumber - 10;
//		
//		Square[] tokens = new Square[9];
//		tokens[index] = square;
//		
//		while( pos > 0 && index-- >= 0) {
//			if( Board.getBoard().getSquare( pos ).color.isEmpty() || Board.getBoard().getSquare( pos ).color.equals( square.color ) ) {
//				tokens[index] = Board.getBoard().getSquare( pos );
//				pos -= 10;
//			} 
//			else break;	
//		}
//		
//		index = 4;
//		pos = square.positionNumber + 10;
//		
//		while( pos <= 100 && index++ < 9) {
//			if( Board.getBoard().getSquare( pos ).color.isEmpty() || Board.getBoard().getSquare( pos ).color.equals( square.color ) ) {
//				tokens[index] = Board.getBoard().getSquare( pos );
//				pos += 10;
//			} 
//			else break;	
//		}
//		
//		double score = 0.0;
//		
//		for(int i=0; i<9; i++) {
//			Map<Card, Integer> cardMap = new HashMap<>();
//			for(int j=i; j<j+5; j++) 
//				if( tokens[j].color.isEmpty() ) {
//					if( !cardMap.keySet().contains( tokens[j].getCard() ) )
//						cardMap.put( tokens[j].getCard() , 0 );
//					cardMap.put( tokens[j].getCard() , cardMap.get( tokens[j].getCard() ) + 1 );
//				}
//			
//		}
//		
//		return score;
//	}
//
//	
}
