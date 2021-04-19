package com.seq.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.apache.commons.lang3.StringUtils;

import com.seq.SeqBot;
import com.seq.board.Board;
import com.seq.cards.*;


public class PanelBuilder {
	
	public static JPanel buildGuiPanel() {
		
		JPanel p = new JPanel( new BorderLayout() );
		p.add( buildCardPanel(), BorderLayout.NORTH );
		p.add( buildButtonPanel(), BorderLayout.CENTER );
		p.add( new JLabel( SPACER ), BorderLayout.SOUTH );
		return p;
	}

	private static JPanel buildCardPanel() {
		
		JPanel northPanel = new JPanel( new BorderLayout() );
		northPanel.add( new JLabel( SPACER ), BorderLayout.NORTH );
		northPanel.add( buildMsgPanel(), BorderLayout.CENTER );
		northPanel.add( new JLabel( SPACER ), BorderLayout.SOUTH );
		
		JPanel centerPanel = new JPanel( new BorderLayout() );
		centerPanel.add( buildColorPanel(), BorderLayout.NORTH );
		centerPanel.add( buildDrawPanel(), BorderLayout.CENTER );
		centerPanel.add( new JLabel( SPACER ), BorderLayout.SOUTH );
		
		JPanel southPanel = new JPanel( new BorderLayout() );
		southPanel.add( new JSeparator(), BorderLayout.NORTH );
		southPanel.add( new JLabel( SPACER + SPACER + SPACER ), BorderLayout.CENTER );
		southPanel.add( buildOppnentMovePanel(), BorderLayout.SOUTH );
		
		JPanel p = new JPanel( new BorderLayout() );
		p.add( northPanel, BorderLayout.NORTH );
		p.add( centerPanel, BorderLayout.CENTER );
		p.add( southPanel, BorderLayout.SOUTH );
		return p;
	}
	
	private static JPanel buildMsgPanel() {
		
		JPanel p = new JPanel( new BorderLayout() );
		p.add( new JLabel( SPACER + SeqBot.get().getStatusMsg() ), BorderLayout.NORTH );
		p.add( new JLabel( SPACER + SPACER ), BorderLayout.CENTER );
		p.add( new JSeparator(), BorderLayout.SOUTH );
		return p;
	}
	
	private static JPanel buildColorPanel() {
		
		JPanel myColorPanel = new JPanel( new BorderLayout() );
		myColorPanel.add( new JLabel( SPACER + "  My color:    " + SPACER ), BorderLayout.WEST );
		myColorPanel.add( GuiController.get().getMyColorPicklist(), BorderLayout.CENTER );
		myColorPanel.add( new JLabel( SPACER + SPACER ), BorderLayout.EAST );
		
		JPanel opponentColorPanel = new JPanel( new BorderLayout() );
		opponentColorPanel.add( new JLabel( SPACER + "  Opponents Color:      " ), BorderLayout.WEST );
		opponentColorPanel.add( GuiController.get().getOpponentColorPicklist(), BorderLayout.CENTER );
		opponentColorPanel.add( new JLabel( SPACER + SPACER ), BorderLayout.EAST );
		
		JPanel colorPanel = new JPanel( new BorderLayout() );
		colorPanel.add( myColorPanel, BorderLayout.NORTH );
		colorPanel.add( opponentColorPanel, BorderLayout.SOUTH );
		
		JPanel spacePanel = new JPanel( new BorderLayout() ); 
		spacePanel.add( new JLabel( SPACER ), BorderLayout.NORTH );
		spacePanel.add( new JSeparator(), BorderLayout.CENTER );
		spacePanel.add( new JLabel( SPACER ), BorderLayout.SOUTH );
		
		JPanel p = new JPanel( new BorderLayout() );
		p.add( new JLabel( SPACER + SPACER ), BorderLayout.NORTH );
		p.add( colorPanel, BorderLayout.CENTER );
		p.add( spacePanel, BorderLayout.SOUTH );
		return p;
	}
	
	private static JPanel buildDrawPanel() {
		
		JPanel drawPanel = new JPanel( new BorderLayout() );
		drawPanel.add( new JLabel( SPACER + "  Draw Card:  " + SPACER), BorderLayout.WEST );
		drawPanel.add( GuiController.get().getCardRankPicklist(), BorderLayout.CENTER );
		drawPanel.add( GuiController.get().getCardSuitPicklist(), BorderLayout.EAST );
		
		JPanel p = new JPanel( new BorderLayout() );
		p.add( drawPanel, BorderLayout.WEST );
		p.add( new JLabel( SPACER + "   " ), BorderLayout.EAST );
		return p;
	}
	
	private static JPanel buildOppnentMovePanel() {
		
		JPanel movePanel = new JPanel( new BorderLayout() );
		movePanel.add( new JLabel( SPACER + "  Opponents move:       " ), BorderLayout.WEST );
		movePanel.add( GuiController.get().getTokenPosition(), BorderLayout.CENTER );
		movePanel.add( new JLabel( SPACER + SPACER + SPACER ), BorderLayout.EAST );
		
		JPanel jackPanel = new JPanel( new BorderLayout() );
		jackPanel.add( new JLabel( SPACER + "  Jack Suit:                    " ), BorderLayout.WEST );
		jackPanel.add( GuiController.get().getOpponentSuitPicklist(), BorderLayout.CENTER );
		jackPanel.add( new JLabel( SPACER + SPACER + SPACER ), BorderLayout.EAST );
		
		JPanel basePanel = new JPanel( new BorderLayout() );
		basePanel.add( movePanel, BorderLayout.NORTH );
		basePanel.add( jackPanel, BorderLayout.CENTER );		
		
		JPanel spacePanel = new JPanel( new BorderLayout() ); 
		spacePanel.add( new JLabel( SPACER ), BorderLayout.NORTH );
		spacePanel.add( new JSeparator(), BorderLayout.SOUTH );
		
		JPanel p = new JPanel( new BorderLayout() );
		p.add( basePanel, BorderLayout.NORTH );
		p.add( spacePanel, BorderLayout.SOUTH );
		return p;
	}
	
	private static JPanel buildButtonPanel() {
		
		JButton nextMoveButton = new JButton( "Next Move" );
		JButton updateButton = new JButton( "Update" );
		JButton undoButton = new JButton( "Undo" );
		JButton quitButton = new JButton( "Quit" );
		
		nextMoveButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				try {
					System.out.println( "Calculating next move..." );
					SeqBot.get().clearPrev();
					SeqBot.get().setCalcNextMove( true );
					SeqBot.get().setOpponentTokenColor( (String) GuiController.get().getOpponentColorPicklist().getSelectedItem() );
					SeqBot.get().setMyTokenColor( (String) GuiController.get().getMyColorPicklist().getSelectedItem() );
					SeqBot.get().processRequest();
					if( SeqBot.get().getErrMsg() == null ) {
						String msg = "Play " + SeqBot.get().getMyTokenColor() + " token on " + Board.getSquare( SeqBot.get().getMyNextMove().get(0) ).getCard() + " @Pos # " + SeqBot.get().getMyNextMove().get(0);
						if( SeqBot.get().getMyNextMove().size() > 1 ) 
							 msg = "SeqBot found " + SeqBot.get().getMyNextMove().size() + " equal moves: " + SeqBot.get().getMyNextMove() + "\n\n" + msg;
						System.out.println( msg );
						GuiController.showInfo(  msg );
					} 
					SeqBot.get().setStatusMsg( "Hand [ " + Hand.get() + " ]" );
				} catch( Exception ex ) {
					SeqBot.get().setErrMsg( ex.getMessage() );
					ex.printStackTrace();
				}
				GuiController.get().refresh();
			}
		} );

		updateButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				try {
					SeqBot.get().setMyTokenColor( (String) GuiController.get().getMyColorPicklist().getSelectedItem() );
					SeqBot.get().setCalcNextMove( false );
					SeqBot.get().setOpponentTokenColor( (String) GuiController.get().getOpponentColorPicklist().getSelectedItem() );

					String pos = GuiController.get().getTokenPosition().getText();
					String cardRank = (String) GuiController.get().getCardRankPicklist().getSelectedItem();
					int cardSuitIndex = GuiController.get().getCardSuitPicklist().getSelectedIndex();
					
					if( cardRank != null && cardSuitIndex > 0 ) {
						SeqBot.get().clearPrev();
						SeqBot.get().setMyNewCard( new Card( CardSuit.SUIT_CODES[cardSuitIndex], cardRank ) );
						SeqBot.get().processRequest();
						if( SeqBot.get().getErrMsg() == null )
							System.out.println( "Add " + SeqBot.get().getMyNewCard() + " to hand" );
						SeqBot.get().setStatusMsg("Hand [ " + Hand.get() + " ]");
					} else if ( StringUtils.isNotBlank( SeqBot.get().getOpponentTokenColor() ) && !pos.isEmpty() ) {
						SeqBot.get().clearPrev();
						SeqBot.get().setOpponentPos( Integer.valueOf( pos ) );
						String jackSuit = (String) GuiController.get().getOpponentSuitPicklist().getSelectedItem();
						if( StringUtils.isNotBlank(jackSuit) ) jackSuit = CardSuit.suits.get(jackSuit);
						SeqBot.get().setOpponentJackSuit( jackSuit );
						SeqBot.get().setPrevOpponentPos( Integer.valueOf( pos ) );
						SeqBot.get().setPrevOpponentJackSuit(jackSuit);
						
						String jackMsg = StringUtils.isNotBlank(jackSuit) ? " using the " + new Card(jackSuit, CardRank.CARD_J) : "";
						String msg = null;
						if( jackSuit.equals(CardSuit.SPADES) || jackSuit.equals(CardSuit.HEARTS) )
							msg = "Remove " + SeqBot.get().getMyTokenColor() + " token" + jackMsg + " for " + Board.getSquare( SeqBot.get().getOpponentPos() ).getCard() + " @Pos# " + SeqBot.get().getOpponentPos() + "?";
						else if( jackSuit.equals(CardSuit.CLUBS) || jackSuit.equals(CardSuit.DIAMONDS) )
							msg = "Play " + SeqBot.get().getOpponentTokenColor() + " token" + jackMsg + " for " + Board.getSquare( SeqBot.get().getOpponentPos() ).getCard() + " @Pos# " + SeqBot.get().getOpponentPos() + "?";
				
						GuiController.showConfirmationWindow( msg );
						SeqBot.get().processRequest();
					} else 
						SeqBot.get().setErrMsg( "Either [SeqBot color+rank+suit] or [Opponent color+move] are required!" );
					
				} catch( Exception ex ) {
					SeqBot.get().setErrMsg( ex.getMessage() );
					ex.printStackTrace();
				}
				GuiController.get().refresh();
			}
		} );

		undoButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				System.out.println( "UNDO button activated" );
				SeqBot.get().undoLastRequest();
				SeqBot.get().clearPrev();
				GuiController.get().refresh();
			}
		} );

		quitButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				System.exit( 0 );
			}
		} );

		JPanel p = new JPanel();
		p.setLayout( new GridLayout( 0, 4 ) );
		p.add( updateButton );
		p.add( nextMoveButton );
		p.add( undoButton );
		p.add( quitButton );
		return p;
	}
	
	private static final String SPACER = "               ";
}
