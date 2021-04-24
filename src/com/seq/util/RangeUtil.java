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
		
		if( !Hand.getTwoEyeJacks().isEmpty() ) {
			System.out.println(">>>>>>>>>>>>>>>>   Check 4 MY 4-Quences for 2-Eye Jack...");
			seqFinisher = getBestTwoEyeJackTarget( SeqBot.get().getMyTokenColor() );
			if( seqFinisher != null )
				System.out.println(">>>>>>>>>>>>>>>>   SEQ_FINISHER -----------------------> " + seqFinisher);
			System.out.println(">>>>>>>>>>>>>>>>   Check 4 OPPONENT 4-Quences for 2-Eye Jack...");
			seqBlocker = getBestTwoEyeJackTarget( SeqBot.get().getOpponentTokenColor() );
			if( seqBlocker != null )
				System.out.println(">>>>>>>>>>>>>>>>   SEQ_BLOCKER ------------------------> " + seqBlocker);
		}
		
		if( !Hand.getOneEyeJacks().isEmpty() ) {
			System.out.println(">>>>>>>>>>>>>>>>   Check 4 OPPONENT 4-quences for 1-Eye Jack...");
			oneEyeJackTarget = getBestOneEyeJackTarget();
			if( oneEyeJackTarget != null )
				System.out.println(">>>>>>>>>>>>>>>>   ONE_EYE_JACK_TARGET ----------------> " + oneEyeJackTarget);
		}
		
		if( seqFinisher == null && seqBlocker == null && oneEyeJackTarget == null )
			for( Square square: Board.getOpenSquares() ) 
				for( int axis: AXIS_DIRECTIONS ) 
					setAxisRange( square, axis, getAxisSquares( square, axis ) );
				
		System.out.println("Range populated!");	
	}
	
	public static Square getOneEyeJackTarget() {
		if( oneEyeJackTarget!= null ) oneEyeJackTarget.setColor(SeqBot.get().getOpponentTokenColor());
		return oneEyeJackTarget;
	}

	public static Square getSeqFinisher() {
		if( seqFinisher!= null ) seqFinisher.setColor(SeqBot.get().getMyTokenColor());
		return seqFinisher;
	}
	
	public static Square getSeqBlocker() {
		if( seqBlocker!= null ) seqBlocker.setColor(SeqBot.get().getMyTokenColor());
		return seqBlocker;
	}
	
	public static Square getGameFinisher() {
		if( gameFinisher!= null ) gameFinisher.setColor(SeqBot.get().getMyTokenColor());
		return gameFinisher;
	}
	
	public static Square getGameBlocker() {
		if( gameBlocker!= null ) gameBlocker.setColor(SeqBot.get().getMyTokenColor());
		return gameBlocker;
	}
	
	private static Set<Square> getOpponentSquares(Map<Integer, Set<Square>> axisRanges) {
		Set<Square> squares = new TreeSet<>();
		for( Integer axis : axisRanges.keySet() )
			for( Square square: axisRanges.get(axis) )
				if( square.isOpponents() ) 
					squares.add( square );
		return squares;
	}
	
	private static Map<Integer, Set<Square>> getAxiRangeWithoutSquare(Map<Integer, Set<Square>> axisRanges, Square square){
		Map<Integer, Set<Square>> range = new HashMap<>();
		for( Integer axis : axisRanges.keySet() ) {
			Set<Square> tmp = new TreeSet<>( axisRanges.get(axis) );
			tmp.remove( square );
			range.put( axis, tmp );
		}
		return range;
	}
	
	private static Square getBestOneEyeJackTarget() throws Exception {
		Square target = getBestTwoEyeJackTarget( SeqBot.get().getOpponentTokenColor() );
		if( target == null )
			return null;
		Map<Integer, Set<Square>> axisRanges = getOpponentAxisRanges( target );
		Set<Square> oppSquares = getOpponentSquares( axisRanges );
		Square bestTarget = null;
		double biggestDiff = 0.0;
		
		for( Square square: oppSquares ) {
			
			Map<Integer, Set<Square>> oppRanges = getOpponentAxisRanges(square);
			Map<Integer, Set<Square>> lesserRanges = getAxiRangeWithoutSquare(oppRanges, square);
			double score = MoveCalculator.getScore( square, oppRanges, SeqBot.get().getOpponentTokenColor(), true );
			double lesserScore = MoveCalculator.getScore( square, lesserRanges, SeqBot.get().getOpponentTokenColor(), true );
			double diff = score - lesserScore;

			if( biggestDiff < diff ) {
				biggestDiff = diff;
				bestTarget = square;
				System.out.println( "Biggest Diff for One-EyEye Jack Target [ " + square + " ]: " + biggestDiff );
			}
		}

		return bestTarget;
	}
	
	public static Map<Integer, Set<Square>> getMyAxisRanges( Square square ) {
		if( Hand.getAxisRanges().keySet().contains( square ) )
			return Hand.getAxisRanges().get( square );
		return new HashMap<>();
	}
		
	
	public static Map<Integer, Set<Square>> getOpponentAxisRanges( Square square ) {
		Map<Integer, Set<Square>> map = new HashMap<>();
		for( int axis: AXIS_DIRECTIONS ) {
			TreeSet<Square> axisSquares = getAxisSquares( square, axis );
			Set<Square> range = getSeqRange( square, axisSquares, SeqBot.get().getMyTokenColor() );
			if( isSeqRange(square, range) ) 
				map.put( axis, range );
		}
		return map;
	}
	
	
	private static Square getBestTwoEyeJackTarget( String color ) throws Exception {
		Set<Square> targets = new HashSet<>();
		for( int i=2; i<100; i++ ) 
			if( isPotentialSequence( Board.getSquare(i), color ) ) 
				targets.add(Board.getSquare(i));
		
		Square bestMove = null;
		double bestScore = 0.0;
		for( Square square: targets ) 
			if( bestMove == null ) 
				bestMove = square;
			else {
				Map<Integer, Set<Square>> range = color.equals( SeqBot.get().getMyTokenColor() ) ? getMyAxisRanges(square) : getOpponentAxisRanges(square);
				double score = MoveCalculator.getScore( square, range, color, true );
				if( score > bestScore ) {
					bestScore = score;
					bestMove = square;
				}
			}

		return bestMove;
	}


	
	private static void setAxisRange( Square square, Integer axis, TreeSet<Square> squares ) {
		Set<Square> range = getSeqRange( square, squares, SeqBot.get().getOpponentTokenColor() );
		if( isSeqRange( square, range ) ) {
			System.out.println("Add " + square + " range["+AXIS_MAP.get(axis)+"]: " + range );
			Hand.addAxisRange( square, axis, range );
		}
	}
	
	private static boolean isOtherColor( Square square, String color ) {
		return square.getColor() != null && square.getColor().equals( color ) ;
	}
	
	
	private static boolean isSeqRange( Square target, Collection<Square> squares ) {
		return squares != null && squares.size() > 4 && squares.contains( target );
	}
	
	private static int getSeqSize( List<Square> squares, String color ) {
		int seqSize = 0;
		for( Square s: squares )
			if( s != null && ( s.getColor().equals( color ) || s.isWild() ) ) 
				seqSize++;
		return seqSize;
	}
	
	private static Set<Square> getSeqRange( Square square, TreeSet<Square> squares, String otherColor  ) {
		Set<Square> range = new TreeSet<>();
		Set<Square> testRange = new TreeSet<>();
		try {
			Iterator<Square> it = squares.iterator();
			while( it.hasNext() ) {
				Square testSquare = it.next();
				if( isOtherColor(testSquare, otherColor)  ) {
					if( !range.isEmpty() ) {
						if( !testRange.isEmpty() )
							range.addAll( testRange );
						testRange.clear();
						break;
					}
					
					if( isSeqRange(square, testRange)  ) {
						range.addAll( testRange );
						testRange.clear();
					}
				} else 
					testRange.add( testSquare );
			}
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
		
		if( !testRange.isEmpty() && (!range.isEmpty() || isSeqRange(square, testRange)) )
			range.addAll( testRange );
	
		return range;
	}
	
	private static String getOtherColor( String color ) {
		if( SeqBot.get().getMyTokenColor().equals( color ) )
			return SeqBot.get().getOpponentTokenColor();
		return SeqBot.get().getMyTokenColor();
	}
	
	private static int countConsecutiveTokens( List<Square> squares, String color ) {
		int bestCount = 0;
		int count = 0;
		for( Square square: squares ) {
			if( square.isWild() || (square.getColor() != null && square.getColor().equals(color)) ) {
				count++;
			} else {
				if( count > bestCount )
					bestCount = count;
				count = 0;
			}
		}
		return bestCount;
	}
	
	private static int countTokens( Set<Square> squares, String color ) {
		int count = 0;
		for( Square square: squares )
			if( square.isWild() || (square.getColor() != null && square.getColor().equals(color)) ) 
				count++;
		return count;
	}

	private static boolean isPotentialSequence( Square square, String color ) {
		if( StringUtils.isNotBlank(square.getColor()) || square.isWild() ) 
			return false;
		int potentialSeqs = 0;
		for( int axis: AXIS_DIRECTIONS ) {
			Set<Square> potentialSeq = new TreeSet<>();
			TreeSet<Square> axisSquares = getAxisSquares( square, axis );
			if( axisSquares != null ) {
				List<Square> squares = new ArrayList<>( getSeqRange(square, axisSquares, getOtherColor(color)) );
				while( squares.size() > 4 ) {
					List<Square> testSquares = squares.subList(0, 5);
					if( getSeqSize( testSquares, color ) > 3 ) 
						potentialSeq.addAll( testSquares );
					squares.remove( 0 );
				}
				int totalTokens = countTokens(potentialSeq, color);
				int consecutiveTokens = countConsecutiveTokens(new ArrayList<>(potentialSeq), color);
				if( consecutiveTokens > 4 ) {
					if( potentialSeq.size() == 10 && totalTokens == 9) 
						potentialSeqs++;
				} 
				else if( potentialSeq.contains( square ) )
					potentialSeqs++;
			}
		}	

		if( potentialSeqs > 1  && color.equals( SeqBot.get().getMyTokenColor() ) )
			gameFinisher = square;
		else if( potentialSeqs > 1 && color.equals( SeqBot.get().getOpponentTokenColor() ) )
			gameBlocker = square;	
		
		return potentialSeqs > 0;
	}

	
	
	private static TreeSet<Square> getAxisSquares( Square square, int axis ) {
		TreeSet<Square> squares = new TreeSet<>();
		try {
			Square test = square;
			while( test != null ) {
				squares.add( test );
				test = getNextSquare( test, axis, false );
			}
			test = square;
			while( test != null ) {
				squares.add( test );
				test = getNextSquare( test, axis, true );
			}
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
		return squares;	
	}
	public static Square getNextSquare( Square square, int axis, boolean goForward ) {
		if( square == null || isOnEdge( square, axis, goForward ? 9 : 0) ) return null;
		int stepSize = getStepSize( axis ) * ( goForward ? 1 : -1 );
		return Board.getSquare( square.getPos() + stepSize );
	}
	
	private static boolean isOnEdge( Square square, int axis, int edge ) {
		int oppositeEdge = edge == 9 ? 0 : 9;
		return 	(axis == NORTH_SOUTH && square.getRow() == edge) ||
				(axis == WEST_EAST && square.getCol() == edge) ||
				(axis == NW_SE && (square.getCol() == edge || square.getRow() == edge)) ||
				(axis == NE_SW && ( square.getCol() == oppositeEdge || square.getRow() == edge));
	}

	private static int getStepSize( int axis ) {
		return axis == NORTH_SOUTH ? 10 : axis == WEST_EAST ? 1 : axis == NW_SE ? 11 : axis == NE_SW ? 9 : -1;
	}
	
	private static Square oneEyeJackTarget = null;
	private static Square gameFinisher = null;
	private static Square gameBlocker = null;
	private static Square seqFinisher = null;
	private static Square seqBlocker = null;
	private static final String NS = "NS";
	private static final String WE = "WE";
	private static final String NW = "NW";
	private static final String NE = "NE";
	public static HashMap<Integer, String> AXIS_MAP = new HashMap<>();
	private static final int NORTH_SOUTH = 1;
	private static final int WEST_EAST = 2;
	private static final int NW_SE = 3;
	private static final int NE_SW = 4;
	public static final int[] AXIS_DIRECTIONS = { NORTH_SOUTH, WEST_EAST, NW_SE, NE_SW };
	static {
		AXIS_MAP.put( NORTH_SOUTH, NS );
		AXIS_MAP.put( WEST_EAST, WE );
		AXIS_MAP.put( NW_SE, NW );
		AXIS_MAP.put( NE_SW, NE );
	}
}
