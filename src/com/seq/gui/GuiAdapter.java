package com.seq.gui;

import java.awt.*;
import javax.swing.*;

import com.seq.*;
import com.seq.board.*;
import com.seq.cards.*;
import com.seq.util.*;


public class GuiAdapter extends JFrame {
	 
	public GuiAdapter() {}

	public void init() {
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setSize( 500, 564 );
		setLayout( new BorderLayout() );
		setVisible( true );
		gui = PanelBuilder.buildGuiPanel();
		add( gui, BorderLayout.CENTER );
	}
	
	public void refresh() {
		
		SeqBot.get().setMyTokenColor( TokenColor.BLUE );
		SeqBot.get().setOpponentTokenColor( TokenColor.RED );
		
		if( SeqBot.get().getErrMsg() != null ) 
			GuiAdapter.showError();

		cardRankPicklist.setSelectedIndex( 0 );
		opponentSuitPicklist.setSelectedIndex( 0 );
		tokenPosition.setText( "" );

		if( SeqBot.get().getMyTokenColor() != null ) {
			myColorPicklist.setSelectedItem( SeqBot.get().getMyTokenColor() );
		}
		if( SeqBot.get().getOpponentTokenColor() != null )
			opponentColorPicklist.setSelectedItem( SeqBot.get().getOpponentTokenColor() );
	
		if( SeqBot.get().getMySuit() != null )
			cardSuitPicklist.setSelectedItem( SeqBot.get().getMySuit() );
		
		SeqBot.get().setCalcNextMove(false);
		SeqBot.get().setOpponentTokenColor("");
		SeqBot.get().setOpponentJackSuit("");
		SeqBot.get().setOpponentPos(null);
		SeqBot.get().setMyNewCard(null);
		SeqBot.get().setMyNextMove(null);
		SeqBot.get().setErrMsg(null);
		
		SeqBot.get().setStatusMsg( SeqBot.get().getStatusMsg() );

		remove( get().getGuiPanel() );
		setGuiPanel( PanelBuilder.buildGuiPanel() );
		add( getGuiPanel(), BorderLayout.CENTER );
		revalidate();
		repaint();
		System.out.println( "SeqBot feels refreshed!" );
	}
	
	public static void showInfo( String msg ) {
		System.out.println( msg );
		JOptionPane.showMessageDialog( GuiAdapter.get(), msg, "Action Required", JOptionPane.OK_OPTION );
	}
	
	public static void showError() {
		AudioUtil.playAwJeez();
		System.out.println( SeqBot.get().getErrMsg() );
		JOptionPane.showMessageDialog( GuiAdapter.get(), SeqBot.get().getErrMsg(), "Error", JOptionPane.ERROR_MESSAGE );
		SeqBot.get().setErrMsg( null );
	}
	
	public static void showConfirmationWindow( String msg ) throws Exception {
		System.out.println( msg );
		int n = JOptionPane.showOptionDialog( GuiAdapter.get(), msg + "?", "Confirm Action", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] { "OK", "Cancel" }, "Cancel" );
		if( n == JOptionPane.NO_OPTION ) throw new Exception( "User cancelled action!" );
	}
	
	public static GuiAdapter get() {
		if( guiAdapter == null ) guiAdapter = new GuiAdapter();
		return guiAdapter;
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

	public JPanel getGuiPanel() {
		return gui;
	}
	
	public void setGuiPanel( JPanel gui ) {
		this.gui = gui;
	}
	public JTextArea getMsgBox() {
		return msgBox;
	}

	static final long serialVersionUID = 8222590183237060065L;
	private static GuiAdapter guiAdapter = null;
	private JPanel gui = null;
	private JTextField tokenPosition = new JTextField();
	private JComboBox<String> cardRankPicklist = new JComboBox<String>( CardRank.RANK_NAMES );
	private JComboBox<String> cardSuitPicklist = new JComboBox<String>( CardSuit.SUIT_NAMES );
	private JComboBox<String> opponentSuitPicklist = new JComboBox<String>( CardSuit.SUIT_NAMES );
	private JComboBox<String> opponentColorPicklist = new JComboBox<String>( TokenColor.COLORS );
	private JComboBox<String> myColorPicklist = new JComboBox<String>( TokenColor.COLORS );
	private JTextArea msgBox = new JTextArea();
}
