package com.seq.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.seq.SeqBot;
import com.seq.board.Board;
import com.seq.cards.Card;
import com.seq.cards.CardSuit;
import com.seq.cards.Hand;

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
		
		JPanel checkboxPanel = new JPanel( new BorderLayout() );
		checkboxPanel.add( new JLabel( SPACER + "  One-Eye Jack:            " ), BorderLayout.WEST );
		checkboxPanel.add( GuiController.get().getOneEyedJackCheckbox(), BorderLayout.CENTER );

		JPanel basePanel = new JPanel( new BorderLayout() );
		basePanel.add( movePanel, BorderLayout.NORTH );
		basePanel.add( jackPanel, BorderLayout.CENTER );
		//ßbasePanel.add( checkboxPanel, BorderLayout.SOUTH );
		
		
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
		JButton cancelButton = new JButton( "Cancel" );
		JButton quitButton = new JButton( "Quit" );
		
		nextMoveButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				try {
					System.out.println( "Calculating next move..." );
					SeqBot.get().setCalcNextMove( true );
					SeqBot.get().setOpponentTokenColor( (String) GuiController.get().getOpponentColorPicklist().getSelectedItem() );
					SeqBot.get().setMyTokenColor( (String) GuiController.get().getMyColorPicklist().getSelectedItem() );
					GuiController.processRequest();
					if( SeqBot.get().getErrMsg() == null ) {
						String msg = "Play " + SeqBot.get().getMyTokenColor() + " token on " + Board.getSquare( SeqBot.get().getMyNextMove().get(0) ).getCard() + " @Pos # " + SeqBot.get().getMyNextMove().get(0);
						if( SeqBot.get().getMyNextMove().size() > 1 ) 
							 msg = "SeqBot found " + SeqBot.get().getMyNextMove().size() + " equal moves: " + SeqBot.get().getMyNextMove() + "\n\n" + msg;
						System.out.println( msg );
						GuiController.showInfo(  msg );
					} else
						GuiController.showError( SeqBot.get().getErrMsg() );
					SeqBot.get().setStatusMsg( "Hand [ " + Hand.get() + " ]" );
				} catch( Exception ex ) {
					JOptionPane.showMessageDialog( GuiController.get(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
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
					if( !pos.isEmpty() )
						SeqBot.get().setOpponentPos( Integer.valueOf( pos ) );
					String cardRank = (String) GuiController.get().getCardRankPicklist().getSelectedItem();
					int cardSuitIndex = GuiController.get().getCardSuitPicklist().getSelectedIndex();
					if( cardRank != null && cardSuitIndex > 0 ) {
						SeqBot.get().setMyNewCard( new Card( CardSuit.SUIT_CODES[cardSuitIndex], cardRank ) );
						GuiController.processRequest();
						if( SeqBot.get().getErrMsg() == null )
							System.out.println( "Add " + SeqBot.get().getMyNewCard() + " to hand" );
						else
							GuiController.showError( SeqBot.get().getErrMsg() );
						SeqBot.get().setStatusMsg("Hand [ " + Hand.get() + " ]");
					} else {
						System.out.println( "Register opponents move" );
						GuiController.processRequest();
						if( SeqBot.get().getErrMsg() == null )
							GuiController.showInfo( "Played " + SeqBot.get().getOpponentTokenColor() + " token for " + Board.getSquare( SeqBot.get().getOpponentPos() ).getCard() + " @Pos # " + SeqBot.get().getOpponentPos() );
						else
							GuiController.showError( SeqBot.get().getErrMsg() );
					}
				} catch( Exception ex ) {
					GuiController.showError( ex.getMessage() );
					ex.printStackTrace();
				}
				GuiController.get().refresh();
			}
		} );

		cancelButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				System.out.println( "CANCEL button activated" );
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
		p.add( cancelButton );
		p.add( quitButton );
		return p;
	}
	
	private static final String SPACER = "               ";
}
