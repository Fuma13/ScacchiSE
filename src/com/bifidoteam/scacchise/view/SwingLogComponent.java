package com.bifidoteam.scacchise.view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.bifidoteam.scacchise.interfaces.LogType;

public class SwingLogComponent {

	JFrame frame;
	DefaultStyledDocument doc;
	Style textStyles[];
	
	
	public SwingLogComponent() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		
       	frame = new JFrame("Log console");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setBounds(850, 150, 400, 400);
        

        // TEXT DOC THAT WILL BE PRINT (The one to update)
        StyleContext sc = new StyleContext();
        doc = new DefaultStyledDocument(sc);
        
        InitializeStyles(sc);
        logMessage("*** LOG START ***", LogType.LOG);
        
        // TEXT BOX
    	JTextPane text = new JTextPane(doc);
        text.setEditable(false);
		text.setAutoscrolls(true);
		
		// OUTER SCROLL PANE
		JScrollPane scrollPane = new JScrollPane(text);
		scrollPane.setAlignmentY(0);

		frame.add(scrollPane);
		
        frame.setVisible(true);
	}

	public void logMessage(String message, LogType type) {
        try {
    		doc.insertString(doc.getEndPosition().getOffset(), message + "\n", GetMessageTypeStyle(type));
    		
		} catch (BadLocationException e) {
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
	
}
