package com.seq;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SeqBotGUI extends JFrame {

	public SeqBotGUI() {
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setSize( 500, 300 );
		setLayout( new BorderLayout() );
		setVisible( true );
		add( getCardPanel(), BorderLayout.NORTH );
		add( getButtonPanel(), BorderLayout.SOUTH );
	}
	
	public void refresh() {
		SeqBotGUI.this.cardRankPicklist.setSelectedIndex( 0 );
		SeqBotGUI.this.cardSuitPicklist.setSelectedIndex( 0 );
		SeqBotGUI.this.tokenPosition.setText( "" );

		if( SeqBot.get().myTokenColor != null )
			SeqBotGUI.this.myColorPicklist.setSelectedItem( SeqBot.get().myTokenColor );
		else 
			SeqBotGUI.this.myColorPicklist.setSelectedIndex( 0 );
		
		if( SeqBot.get().opponentTokenColor != null )
			SeqBotGUI.this.opponentColorPicklist.setSelectedItem( SeqBot.get().opponentTokenColor );
		else 
			SeqBotGUI.this.opponentColorPicklist.setSelectedIndex( 0 );
		
		SeqBot.get().opponentTokenColor = "";
		SeqBot.get().opponentPos = null;
		SeqBot.get().myNewCard = null;
		SeqBot.get().calcNextMove = false;
		SeqBot.get().myNextMove = null;
		
		remove(SeqBotGUI.this.cardPanel);
		remove(SeqBotGUI.this.buttonPanel);
		add( getCardPanel(), BorderLayout.NORTH );
		add( getButtonPanel(), BorderLayout.SOUTH );
		revalidate();
		repaint();
		System.out.println( "SeqBot feels refreshed!" );
	}

	private JPanel getButtonPanel() {
		
		JButton nextMoveButton = new JButton( "Next Move" );
		JButton updateButton = new JButton( "Update" );
		JButton cancelButton = new JButton( "Cancel" );
		JButton quitButton = new JButton( "Quit" );
		
		nextMoveButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				try {
					System.out.println( "Calculating next move..." );
					SeqBot.get().calcNextMove = true;
					SeqBot.get().opponentTokenColor = (String) SeqBotGUI.this.opponentColorPicklist.getSelectedItem();
					SeqBot.get().myTokenColor = (String) SeqBotGUI.this.myColorPicklist.getSelectedItem();
					processRequest();
					if( SeqBot.get().errMsg == null )
						showInfo( "Play " + SeqBot.get().myTokenColor + " token on " + Board.get().getSquare( SeqBot.get().myNextMove ).getCard() + " @Pos # " + SeqBot.get().myNextMove );
					else
						showError( SeqBot.get().errMsg );
					SeqBot.get().statusMsg = "Hand [ " + Hand.get() + " ]";
				} catch( Exception ex ) {
					JOptionPane.showMessageDialog( SeqBotGUI.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
					ex.printStackTrace();
				}
				refresh();
			}
		} );

		updateButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				try {
					SeqBot.get().myTokenColor = (String) SeqBotGUI.this.myColorPicklist.getSelectedItem();
					SeqBot.get().calcNextMove = false;
					SeqBot.get().opponentTokenColor = (String) SeqBotGUI.this.opponentColorPicklist.getSelectedItem();
					String pos = SeqBotGUI.this.tokenPosition.getText();
					if( !pos.isEmpty() )
						SeqBot.get().opponentPos = Integer.valueOf( pos );
					String cardRank = (String) SeqBotGUI.this.cardRankPicklist.getSelectedItem();
					int cardSuitIndex = SeqBotGUI.this.cardSuitPicklist.getSelectedIndex();
					if( cardRank != null && cardSuitIndex > 0 ) {
						SeqBot.get().myNewCard = new Card( CardSuit.suitCodes.get( cardSuitIndex ), cardRank );
						processRequest();
						if( SeqBot.get().errMsg == null )
							showInfo( "Add " + SeqBot.get().myNewCard + " to hand." );
						else
							showError( SeqBot.get().errMsg );
						SeqBot.get().statusMsg = "Hand [ " + Hand.get() + " ]";
					} else {
						System.out.println( "Register opponents move" );
						processRequest();
						if( SeqBot.get().errMsg == null )
							showInfo( "Played " + SeqBot.get().opponentTokenColor + " token for " + Board.get().getSquare( SeqBot.get().opponentPos ).getCard() + " @Pos # " + SeqBot.get().opponentPos );
						else
							showError( SeqBot.get().errMsg );
					}
				} catch( Exception ex ) {
					showError( ex.getMessage() );
					ex.printStackTrace();
				}
				refresh();
			}
		} );

		cancelButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				System.out.println( "CANCEL button activated" );
				refresh();
			}
		} );

		quitButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				System.exit( 0 );
			}
		} );

		SeqBotGUI.this.buttonPanel = new JPanel();
		SeqBotGUI.this.buttonPanel.setLayout( new GridLayout( 0, 4 ) );
		SeqBotGUI.this.buttonPanel.add( updateButton );
		SeqBotGUI.this.buttonPanel.add( nextMoveButton );
		SeqBotGUI.this.buttonPanel.add( cancelButton );
		SeqBotGUI.this.buttonPanel.add( quitButton );
		return SeqBotGUI.this.buttonPanel;
	}

	private JPanel getCardPanel() {
		SeqBotGUI.this.cardPanel = new JPanel( new BorderLayout() );
		
		String SPACER = "               ";
		
		JPanel msgPanel = new JPanel( new BorderLayout() );
		msgPanel.add( new JLabel( SPACER + SeqBot.get().statusMsg ), BorderLayout.NORTH );
		msgPanel.add( new JSeparator(), BorderLayout.SOUTH );
		
		JPanel drawPanel = new JPanel( new BorderLayout() );
		drawPanel.add( new JLabel( SPACER + "  Draw Card:  "+ SPACER), BorderLayout.WEST );
		drawPanel.add( this.cardRankPicklist, BorderLayout.CENTER );
		drawPanel.add( this.cardSuitPicklist, BorderLayout.EAST );
		
		JPanel myColorPanel = new JPanel( new BorderLayout() );
		myColorPanel.add( new JLabel( SPACER + "  My color:    " + SPACER ), BorderLayout.WEST );
		myColorPanel.add( this.myColorPicklist, BorderLayout.CENTER );
		myColorPanel.add( new JLabel( SPACER + SPACER ), BorderLayout.EAST );
		
		JPanel opponentColorPanel = new JPanel( new BorderLayout() );
		opponentColorPanel.add( new JLabel( SPACER + "  Opponents Color:      " ), BorderLayout.WEST );
		opponentColorPanel.add( this.opponentColorPicklist, BorderLayout.CENTER );
		opponentColorPanel.add( new JLabel( SPACER + SPACER ), BorderLayout.EAST );
		
		JPanel colorPanel = new JPanel( new BorderLayout() );
		colorPanel.add( myColorPanel, BorderLayout.NORTH );
		colorPanel.add( opponentColorPanel, BorderLayout.SOUTH );
		
		JPanel movePanel = new JPanel( new BorderLayout() );
		movePanel.add( new JLabel( SPACER + "  Opponents move:       " ), BorderLayout.WEST );
		movePanel.add( this.tokenPosition, BorderLayout.CENTER );
		movePanel.add( new JLabel( SPACER + SPACER + SPACER ), BorderLayout.EAST );
		
		JPanel jackPanel = new JPanel( new BorderLayout() );
		
		
		
		JPanel northPanel = new JPanel( new BorderLayout() );
		northPanel.add( new JLabel( SPACER ), BorderLayout.NORTH );
		northPanel.add( msgPanel, BorderLayout.CENTER );
		northPanel.add( new JLabel( SPACER ), BorderLayout.SOUTH );
		SeqBotGUI.this.cardPanel.add( northPanel, BorderLayout.NORTH );
		
		JPanel southPanel = new JPanel( new BorderLayout() );
		southPanel.add( colorPanel, BorderLayout.NORTH );
		southPanel.add( drawPanel, BorderLayout.CENTER );
		southPanel.add( movePanel, BorderLayout.SOUTH );
		SeqBotGUI.this.cardPanel.add( southPanel, BorderLayout.SOUTH );
		

		return SeqBotGUI.this.cardPanel;
	}
	
	private static void processRequest() {
		try{
			new Thread( SeqBot.get() ).start();
        	Thread.sleep( 250 );
        } catch(Exception ex){ ex.printStackTrace(); }
	}
	
	
	
	static void showInfo( String msg ) {
		JOptionPane.showMessageDialog( SeqBotGUI.gui, msg, "Action Required", JOptionPane.OK_OPTION );
	}
	
	static void showError( String msg ) {
		JOptionPane.showMessageDialog( SeqBotGUI.gui, msg, "Error", JOptionPane.ERROR_MESSAGE );
	}
	
	static final long serialVersionUID = 8222590183237060065L;
	static SeqBotGUI gui = null;
	SeqBot seqBot;
	JPanel cardPanel;
	JPanel buttonPanel;
	JTextField tokenPosition = new JTextField();
	JComboBox<String> cardRankPicklist = new JComboBox<String>( ( (String[]) CardRank.rankNames.toArray() ) );
	JComboBox<String> cardSuitPicklist = new JComboBox<String>( ( (String[]) CardSuit.suitNames.toArray() ) );
	JComboBox<String> opponentColorPicklist = new JComboBox<String>( ( (String[]) Color.getColors().toArray() ) );
	JComboBox<String> myColorPicklist = new JComboBox<String>( ( (String[]) Color.getColors().toArray() ) );
}
