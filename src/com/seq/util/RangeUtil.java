package com.seq.util;

import java.util.*;

import com.seq.board.*;
import com.seq.cards.Hand;

public class RangeUtil {

	public static void populateRanges() {
		for( Square square: Board.getOpenSquares() ) 
			for( int axis: AXIS_DIRECTIONS )
				setAxisRange( square, axis, getAxisSquares( square, axis ) );
	}

	private static void setAxisRange( Square square, Integer axis, Square[] squares ) {
		Set<Square> range = new TreeSet<>();
		for( int i=0; i<9; i++ )
			if( squares[i] == null ) continue;
			else if( Square.isOpponents( squares[i] ) )
				if( i < 4 ) range.clear();
				else break;
			else range.add( squares[i] );
		
		if( range.size() > 4 ) Hand.addAxisRange( square, axis, range );
	}
	
	private static Square[] getAxisSquares( Square square, int axis ) {
		Square[] squares = new Square [9];
		squares[4] = square;
		for( int i=4; i>0; i-- ) squares[i-1] = getNextSquare( squares[i], axis, false );
		for( int i=4; i<8; i++ ) squares[i+1] = getNextSquare( squares[i], axis, true );
		return squares;	
	}
	
	private static Square getNextSquare( Square square, int axis, boolean goForward ) {
		if( square == null ) return null;
		int step = getStepSize( axis ) * ( goForward ? 1 : -1 );
		return Board.getSquare( square.getPos() + step );
	}

	private static int getStepSize( int axis ) {
		return axis == NORTH_SOUTH ? 10 : axis == WEST_EAST ? 1 : axis == NW_SE ? 11 : axis == NE_SW ? 9 : -1;
	}
	
	private static final int NORTH_SOUTH = 1;
	private static final int WEST_EAST = 2;
	private static final int NW_SE = 3;
	private static final int NE_SW = 4;
	private static final int[] AXIS_DIRECTIONS = { NORTH_SOUTH, WEST_EAST, NW_SE, NE_SW };
}
