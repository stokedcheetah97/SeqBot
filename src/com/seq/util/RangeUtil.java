package com.seq.util;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.seq.*;
import com.seq.board.*;
import com.seq.cards.*;

public class RangeUtil {

	public static void populateRanges() throws Exception {
		seqBlocker = null;
		seqFinisher = null;
		gameFinisher = null;
		gameBlocker = null;
		oneEyeJackTarget = null;
		twoEyeJackTarget = null;
		
		if( Hand.getTwoEyeJacks().size() > 0 ) {
			seqFinisher = getBestTwoEyeJackTarget( SeqBot.get().getMyTokenColor() );
			seqBlocker = getBestTwoEyeJackTarget( SeqBot.get().getOpponentTokenColor() );
		}
		
		if( Hand.getOneEyeJacks().size() > 0 )
			oneEyeJackTarget = getBestOneEyeJackTarget();
		
		if( seqFinisher == null && seqBlocker == null && oneEyeJackTarget == null )
			for( Square square: Board.getOpenSquares() ) 
				for( int axis: AXIS_DIRECTIONS )
					setAxisRange( square, axis, getAxisSquares( square, axis ) );
	}
	
	public static Square getOneEyeJackTarget() {
		return oneEyeJackTarget;
	}
	
	public static Square getTwoEyeJackTarget() {
		return twoEyeJackTarget;
	}
	
	public static Square getSeqFinisher() {
		return seqFinisher;
	}
	
	public static Square getSeqBlocker() {
		return seqBlocker;
	}
	
	public static Square getGameFinisher() {
		return gameFinisher;
	}
	
	public static Square getGameBlocker() {
		return gameBlocker;
	}
	
	private static Square getBestOneEyeJackTarget() throws Exception {
		Square target = getBestTwoEyeJackTarget( SeqBot.get().getOpponentTokenColor() );
		Map<Integer, Set<Square>> axisRanges = getOpponentAxisRange( target );
		double currentScore = MoveCalculator.getScore( axisRanges, SeqBot.get().getOpponentTokenColor() );
		
		Square bestTarget = null;
		double worstAlternateScore = currentScore;
		
		Set<Square> oppSquares = new TreeSet<>();
		for( Integer axis : axisRanges.keySet() )
			for( Square square: axisRanges.get(axis) )
				if( Square.isOpponents( square ) ) oppSquares.add( square );
		
		
		for( Square square: oppSquares ) {
			square.setColor( "" );
			Map<Integer, Set<Square>> altAxisRanges = new HashMap<>();
			for( Integer axis : axisRanges.keySet() ) {
				Set<Square> tmp = new TreeSet<>( axisRanges.get(axis) );
				tmp.remove( square );
				tmp.add( square );
				altAxisRanges.put( axis, tmp );
			}
			double alternateScore = MoveCalculator.getScore( altAxisRanges, SeqBot.get().getOpponentTokenColor() );
			if( alternateScore < worstAlternateScore ) {
				worstAlternateScore = alternateScore;
				bestTarget = square;
				System.out.println( "New worse Score [ " + square + " ] = " + worstAlternateScore );
			}
		}

		return bestTarget;
	}
		
	private static Square getBestTwoEyeJackTarget( String color ) throws Exception {
		Set<Square> targets = new HashSet<>();
		for( int i=2; i<100; i++ ) 
			if( isPotentialSequence( Board.getSquare(i), color ) ) targets.add(Board.getSquare(i));
		
		Square bestTarget = null;
		double bestScore = 0.0;
		for( Square square: targets ) 
			if( bestTarget == null ) bestTarget = square;
			else {
				Map<Integer, Set<Square>> range = color.equals( SeqBot.get().getMyTokenColor() ) ? Hand.getAxisRanges().get( square ) : getOpponentAxisRange( square );
				double score = MoveCalculator.getScore( range, color );
				if( score > bestScore ) {
					bestScore = score;
					bestTarget = square;
				}
			}

		return bestTarget;
	}

	private static Map<Integer, Set<Square>> getOpponentAxisRange( Square square ) {
		Map<Integer, Set<Square>> map = new HashMap<>();
		
		for( int axis: AXIS_DIRECTIONS ) {
			Square[] axisSquares = getAxisSquares( square, axis );
			if( axisSquares != null ) {
				List<Square> squares = new ArrayList<>( Arrays.asList( axisSquares ) );
				Set<Square> range = new HashSet<>();
				for( int i=0; i<squares.size(); i++ )
					if( squares.get(i) == null || Square.isMine(squares.get(i)) ) {
						if( range.size() > 4 ) break;
						range.clear();
					}
					else range.add( squares.get(i) );
				
				if( range.size() > 4 ) map.put( axis, range );
			}
		}
		return map;
	}

	private static boolean isPotentialSequence( Square square, String color) {
		
		if( StringUtils.isNotBlank(square.getColor()) ) return false;

		int mostPotentialSeqs = 0;
		for( int axis: AXIS_DIRECTIONS ) {
			int potentialSeqs = 0;
			Square[] axisSquares = getAxisSquares( square, axis );
			if( axisSquares != null ) {
				List<Square> squares = new ArrayList<>( Arrays.asList( axisSquares ) );
				int seqSize = 0;
				while( squares.size() > 4 ) {	
					List<Square> potentialSequence = squares.subList( 0, 5 );
					for( Square s: potentialSequence )
						if( s != null && s.getColor().equals( color ) ) seqSize++;
					squares.remove( 0 );
				}
				if( seqSize == 4 ) potentialSeqs++;
			}
			if( potentialSeqs > 1 && potentialSeqs > mostPotentialSeqs && color.equals( SeqBot.get().getMyTokenColor() ) )
				gameFinisher = square;
			else if( potentialSeqs > 1 && potentialSeqs > mostPotentialSeqs && color.equals( SeqBot.get().getOpponentTokenColor() ) )
				gameBlocker = square;
			if( potentialSeqs > 1 && potentialSeqs > mostPotentialSeqs ) mostPotentialSeqs = potentialSeqs;
			
		}
		return mostPotentialSeqs > 0;
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
	
	private static Square oneEyeJackTarget = null;
	private static Square twoEyeJackTarget = null;
	private static Square gameFinisher = null;
	private static Square gameBlocker = null;
	private static Square seqFinisher = null;
	private static Square seqBlocker = null;
	private static final int NORTH_SOUTH = 1;
	private static final int WEST_EAST = 2;
	private static final int NW_SE = 3;
	private static final int NE_SW = 4;
	private static final int[] AXIS_DIRECTIONS = { NORTH_SOUTH, WEST_EAST, NW_SE, NE_SW };
}
