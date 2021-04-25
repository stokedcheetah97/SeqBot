package com.seq.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.apache.commons.lang3.StringUtils;

import com.seq.SeqBot;
import com.seq.cards.*;
import com.seq.util.*;



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
		
		JTextArea msgBox = GuiAdapter.get().getMsgBox(); 
		msgBox.setFont(msgBox.getFont().deriveFont(14f));
		msgBox.getFont().isBold();
		msgBox.setText( SeqBot.get().getStatusMsg() );
		JPanel p = new JPanel( new BorderLayout() );
		p.add( msgBox, BorderLayout.NORTH );
		p.add( new JLabel( SPACER + SPACER ), BorderLayout.CENTER );
		p.add( new JSeparator(), BorderLayout.SOUTH );
		return p;
	}
	
	private static JPanel buildColorPanel() {
		
		JPanel myColorPanel = new JPanel( new BorderLayout() );
		myColorPanel.add( new JLabel( SPACER + "  My color:    " + SPACER ), BorderLayout.WEST );
		myColorPanel.add( GuiAdapter.get().getMyColorPicklist(), BorderLayout.CENTER );
		myColorPanel.add( new JLabel( SPACER + SPACER ), BorderLayout.EAST );
		
		JPanel opponentColorPanel = new JPanel( new BorderLayout() );
		opponentColorPanel.add( new JLabel( SPACER + "  Opponents Color:      " ), BorderLayout.WEST );
		opponentColorPanel.add( GuiAdapter.get().getOpponentColorPicklist(), BorderLayout.CENTER );
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
		drawPanel.add( GuiAdapter.get().getCardRankPicklist(), BorderLayout.CENTER );
		drawPanel.add( GuiAdapter.get().getCardSuitPicklist(), BorderLayout.EAST );
		
		JPanel p = new JPanel( new BorderLayout() );
		p.add( drawPanel, BorderLayout.WEST );
		p.add( new JLabel( SPACER + "   " ), BorderLayout.EAST );
		return p;
	}
	
	private static JPanel buildOppnentMovePanel() {
		
		JPanel movePanel = new JPanel( new BorderLayout() );
		movePanel.add( new JLabel( SPACER + "  Opponents move:       " ), BorderLayout.WEST );
		movePanel.add( GuiAdapter.get().getTokenPosition(), BorderLayout.CENTER );
		movePanel.add( new JLabel( SPACER + SPACER + SPACER ), BorderLayout.EAST );
		
		JPanel jackPanel = new JPanel( new BorderLayout() );
		jackPanel.add( new JLabel( SPACER + "  Jack Suit:                    " ), BorderLayout.WEST );
		jackPanel.add( GuiAdapter.get().getOpponentSuitPicklist(), BorderLayout.CENTER );
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
					SeqBot.get().setCalcNextMove( true );
					SeqBot.get().setOpponentTokenColor( (String) GuiAdapter.get().getOpponentColorPicklist().getSelectedItem() );
					SeqBot.get().setMyTokenColor( (String) GuiAdapter.get().getMyColorPicklist().getSelectedItem() );
					SeqBot.get().processRequest();
					SeqBot.get().setStatusMsg( Hand.getOrderedHand() );
				} catch( Exception ex ) {
					SeqBot.get().setErrMsg( ex.getMessage() );
					ex.printStackTrace();
				}
				GuiAdapter.get().refresh();
			}
		} );

		updateButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				try {
					SeqBot.get().setMyTokenColor( (String) GuiAdapter.get().getMyColorPicklist().getSelectedItem() );
					SeqBot.get().setCalcNextMove( false );
					SeqBot.get().setOpponentTokenColor( (String) GuiAdapter.get().getOpponentColorPicklist().getSelectedItem() );

					String pos = GuiAdapter.get().getTokenPosition().getText();
					String cardRank = (String) GuiAdapter.get().getCardRankPicklist().getSelectedItem();
					int cardSuitIndex = GuiAdapter.get().getCardSuitPicklist().getSelectedIndex();
					
					if( StringUtils.isNotBlank( cardRank ) && cardSuitIndex > 0 ) {
						SeqBot.get().setMyNewCard( new Card( CardSuit.SUIT_CODES[cardSuitIndex], cardRank ) );
						SeqBot.get().setMySuit( CardSuit.SUIT_CODES[cardSuitIndex] );
						SeqBot.get().processRequest();
						if( SeqBot.get().getErrMsg() == null )
							System.out.println( "Add " + SeqBot.get().getMyNewCard() + " to hand" );
						SeqBot.get().setStatusMsg( Hand.getOrderedHand() );
					} else if ( StringUtils.isNotBlank( SeqBot.get().getOpponentTokenColor() ) && !pos.isEmpty() ) {
						SeqBot.get().setOpponentPos( Integer.valueOf( pos ) );
						String jackSuit = (String) GuiAdapter.get().getOpponentSuitPicklist().getSelectedItem();
						SeqBot.get().setOpponentJackSuit( CardSuit.suits.get(jackSuit) );
						SeqBot.get().processRequest();
					} else 
						SeqBot.get().setErrMsg( "Either [SeqBot color+rank+suit] or [Opponent color+move] are required!" );
					
				} catch( Exception ex ) {
					SeqBot.get().setErrMsg( ex.getMessage() );
					ex.printStackTrace();
				}
				GuiAdapter.get().refresh();
			}
		} );

		undoButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				System.out.println( "UNDO button activated" );
				String opponentColor = (String) GuiAdapter.get().getOpponentColorPicklist().getSelectedItem();
				AudioUtil.playKit();
				SeqBot.get().undoLastRequest();
				SeqBot.get().clearPrev();
				SeqBot.get().setOpponentTokenColor( opponentColor );
				GuiAdapter.get().getOpponentColorPicklist().setSelectedItem( opponentColor );
				GuiAdapter.get().refresh();
				SeqBot.get().setOpponentTokenColor( opponentColor );
			}
		} );

		quitButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				AudioUtil.playQuit();
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
	
	// 15 chars
	public static final String SPACER = "               ";
}
