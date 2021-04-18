package com.seq.gui;

import java.awt.*;
import javax.swing.*;

import com.seq.*;
import com.seq.board.*;
import com.seq.cards.*;

public class GuiController extends JFrame {
	
	public GuiController() {}

	public void init() {
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setSize( 500, 410 );
		setLayout( new BorderLayout() );
		setVisible( true );
		guiPanel = PanelBuilder.buildGuiPanel();
		add( guiPanel, BorderLayout.CENTER );
	}
	
	public void refresh() {
		cardRankPicklist.setSelectedIndex( 0 );
		cardSuitPicklist.setSelectedIndex( 0 );
		tokenPosition.setText( "" );
		oneEyedJackCheckbox.setSelected( false );

		if( SeqBot.get().getMyTokenColor() != null )
			myColorPicklist.setSelectedItem( SeqBot.get().getMyTokenColor() );
		else 
			myColorPicklist.setSelectedIndex( 0 );
		
		if( SeqBot.get().getOpponentTokenColor() != null )
			opponentColorPicklist.setSelectedItem( SeqBot.get().getOpponentTokenColor() );
		else 
			opponentColorPicklist.setSelectedIndex( 0 );
		
		SeqBot.get().setOpponentTokenColor("");
		SeqBot.get().setOpponentPos(null);
		SeqBot.get().setMyNewCard(null);
		SeqBot.get().setCalcNextMove(false);
		SeqBot.get().setMyNextMove(null);
		SeqBot.get().setOpponentJackSuit("");
		
		remove( get().getGuiPanel() );
		setGuiPanel( PanelBuilder.buildGuiPanel() );
		add( getGuiPanel(), BorderLayout.CENTER );
		revalidate();
		repaint();
		System.out.println( "SeqBot feels refreshed!" );
	}

	public static void processRequest() {
		try{
			new Thread( SeqBot.get() ).start();
        	Thread.sleep( 250 );
        } catch(Exception ex){ ex.printStackTrace(); }
	}
	
	public static void showInfo( String msg ) {
		JOptionPane.showMessageDialog( GuiController.get(), msg, "Action Required", JOptionPane.OK_OPTION );
	}
	
	public static void showError( String msg ) {
		JOptionPane.showMessageDialog( GuiController.get(), msg, "Error", JOptionPane.ERROR_MESSAGE );
	}
	
	public static GuiController get() {
		if( gui == null ) gui = new GuiController();
		return gui;
	}
	
	public JTextField getTokenPosition() {
		return tokenPosition;
	}

	public JComboBox<String> getCardRankPicklist() {
		return cardRankPicklist;
	}

	public JComboBox<String> getCardSuitPicklist() {
		return cardSuitPicklist;
	}

	public JComboBox<String> getOpponentSuitPicklist() {
		return opponentSuitPicklist;
	}
	
	public JComboBox<String> getOpponentColorPicklist() {
		return opponentColorPicklist;
	}

	public JComboBox<String> getMyColorPicklist() {
		return myColorPicklist;
	}
	
	public JCheckBox getOneEyedJackCheckbox() {
		return oneEyedJackCheckbox;
	}

	public JPanel getGuiPanel() {
		return guiPanel;
	}
	
	public void setGuiPanel( JPanel guiPanel ) {
		this.guiPanel = guiPanel;
	}

	static final long serialVersionUID = 8222590183237060065L;
	private static GuiController gui = null;
	private JPanel guiPanel = null;
	private JTextField tokenPosition = new JTextField();
	private JCheckBox oneEyedJackCheckbox = new JCheckBox();
	private JComboBox<String> cardRankPicklist = new JComboBox<String>( ( (String[]) CardRank.RANK_NAMES ) );
	private JComboBox<String> cardSuitPicklist = new JComboBox<String>( ( (String[]) CardSuit.SUIT_NAMES ) );
	private JComboBox<String> opponentSuitPicklist = new JComboBox<String>( ( (String[]) CardSuit.SUIT_NAMES ) );
	private JComboBox<String> opponentColorPicklist = new JComboBox<String>( ( (String[]) TokenColor.COLORS ) );
	private JComboBox<String> myColorPicklist = new JComboBox<String>( ( (String[]) TokenColor.COLORS ) );
}
