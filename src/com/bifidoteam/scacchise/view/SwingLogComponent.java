package com.bifidoteam.scacchise.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.bifidoteam.scacchise.interfaces.LogType;
import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.scacchise.model.Piece;
import com.bifidoteam.scacchise.util.Constants;

public class SwingLogComponent implements ActionListener {

	JFrame frame;
	DefaultStyledDocument doc;
	Style textStyles[];
	
	PrintStream toPrint;
	JTextPane text;
	
	Chessboard chessboard;
	
	public SwingLogComponent(Chessboard base) {
		
		//Mac graphics fix!
		 try {
			    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
			 } catch (Exception e) {
			            e.printStackTrace();
			 }
		 
		chessboard = base;
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		
       	frame = new JFrame("Log console");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setBounds(850, 150, 400, 400);
        
        JMenuBar menu = new JMenuBar();
        JMenu tab1 =new JMenu("File");
        menu.add(tab1);
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(this);
        save.setPreferredSize(new Dimension(100,20));
        JMenuItem clean = new JMenuItem("Clean");
        clean.setPreferredSize(new Dimension(100,20));
        clean.addActionListener(this);
        tab1.add(save);
        tab1.add(clean);
        
        frame.setJMenuBar(menu);

        // TEXT DOC THAT WILL BE PRINT (The one to update)
        StyleContext sc = new StyleContext();
        doc = new DefaultStyledDocument(sc);
        
        InitializeStyles(sc);
        
        // TEXT BOX
    	text = new JTextPane(doc);
        text.setEditable(false);
		text.setAutoscrolls(true);
		
		// OUTER SCROLL PANE
		JScrollPane scrollPane = new JScrollPane(text);
		scrollPane.setAlignmentY(0);

		frame.add(scrollPane);
		
        frame.setVisible(true);

        // ****** LOG INIT ******
        if(Constants.DEBUG_MODE){
	        // Init of the file log
			try {
				SimpleDateFormat ft = new SimpleDateFormat ("'Log\\Log'-ddMMyy_hhmm'.txt'");
		        toPrint = new PrintStream(ft.format(new Date()), "UTF-8"); 
			} catch (FileNotFoundException e) {
				logMessage(e.getMessage(), LogType.ERROR);
			} catch (UnsupportedEncodingException e) {
				logMessage(e.getMessage(), LogType.ERROR);
			}
        }
        
        logMessage("*** LOG START ***", LogType.LOG);
	}

	public void logMessage(String message, LogType type) {
    		try {
				doc.insertString(doc.getEndPosition().getOffset(), message + "\n", GetMessageTypeStyle(type));
				
				// If in debugMode => saving into file
	    		if(Constants.DEBUG_MODE){
	    			toPrint.append(message + "\n");
	    		}
			} catch (BadLocationException e) {
				// The only one that doesn't call itself! (otherwise => deadlock)
				e.printStackTrace();
			} 
	}
	
	
	
	
	private void InitializeStyles(StyleContext sc){
		textStyles = new Style[3];
		
		final Style errorStyle = sc.addStyle("ERROR", null);
		errorStyle.addAttribute(StyleConstants.Foreground, Color.red);
		errorStyle.addAttribute(StyleConstants.FontSize, new Integer(12));
//		errorStyle.addAttribute(StyleConstants.FontFamily, "serif");
		errorStyle.addAttribute(StyleConstants.Bold, new Boolean(true));
		textStyles[0]= errorStyle;

		final Style warningStyle = sc.addStyle("WARNING", null);
		warningStyle.addAttribute(StyleConstants.Foreground, Color.orange);
		warningStyle.addAttribute(StyleConstants.FontSize, new Integer(12));
//		warningStyle.addAttribute(StyleConstants.FontFamily, "serif");
		warningStyle.addAttribute(StyleConstants.Italic, new Boolean(true));
		textStyles[1]= warningStyle;

		final Style logStyle = sc.addStyle("WARNING", null);
		logStyle.addAttribute(StyleConstants.Foreground, Color.black);
		logStyle.addAttribute(StyleConstants.FontSize, new Integer(12));
//		logStyle.addAttribute(StyleConstants.FontFamily, "serif");
		textStyles[2]= logStyle;
	}
	
	private Style GetMessageTypeStyle(LogType type){
		switch(type){
		case ERROR:
			return textStyles[0];
		case WARNING:
			return textStyles[1];
		case LOG:
			return textStyles[2];
		default:
			return null;
		}
	}

	
	public void LogOnFileActualChessboard(){

		String toLog = "";
		// ** Header
		toLog += "Chessboard Status: \n";
		toLog += "    A  B  C  D  E  F  G  H \n";
		// ** END
		
		char actualSymbol = ' ';
		Piece actualPiece = null;
		
		for(int i=0; i<Constants.MAX_INDEX_ROW; ++i){
			
			toLog += " " + i + " ";
			
			for(int j=0; j<Constants.MAX_INDEX_ROW; ++j){
				actualPiece = chessboard.getPiece(i*Constants.MAX_INDEX_ROW + j);
				if(actualPiece != null)
					actualSymbol = actualPiece.GetSymbol();
				else 
					actualSymbol =  ' ';
				
				toLog += ("[" + actualSymbol + "]");
			}
			toLog += "\n";
		}
		toLog += ("\n\n");
		
		toPrint.append(toLog);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Pressing the button
		if(arg0.getActionCommand() == "Clean"){
	        doc = new DefaultStyledDocument();
			text.setDocument(doc);
			
			logMessage("Cleaned!", LogType.ERROR);
		}
//		else if(arg0.getActionCommand() == "Save"){
//			String x = null;
//			
//			try {
//				x = doc.getText(0, doc.getEndPosition().getOffset());
//			} catch (BadLocationException e) {
//				logMessage(e.getMessage(), LogType.ERROR);
//			}
//			
//			toPrint.append(x);
//			toPrint.close();
//			logMessage("Saving!", LogType.ERROR);
//		}
	}
	
}
