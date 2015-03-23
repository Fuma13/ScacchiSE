package com.bifidoteam.scacchise.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;

import javafx.scene.chart.PieChartBuilder;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.scacchise.model.Piece;
import com.bifidoteam.scacchise.util.Constants;

public class SwingComponent implements Runnable, ActionListener {

	ArrayDeque<Integer> eventsQueue;
	
	Chessboard board;
	JButton chessboardTiles[];
	ImageIcon textures[];
	
	
	public SwingComponent(Chessboard base){
		board = base;
		eventsQueue= new ArrayDeque<Integer>();
		
		chessboardTiles = new JButton[Constants.MAX_INDEX];
	}
	
	@Override
	public void run() {
		LoadTextures();
		SwingInit();
	}

	private void LoadTextures(){
		textures = new ImageIcon[Constants.MAX_TEXTURES];
		
		// P1
		textures[0] = new ImageIcon("res/claptrap2.png");		
		textures[1] = new ImageIcon("res/claptrap2.png");
		textures[2] = new ImageIcon("res/claptrap2.png");
		textures[3] = new ImageIcon("res/claptrap2.png");
		textures[4] = new ImageIcon("res/claptrap2.png");
		textures[5] = new ImageIcon("res/claptrap2.png");
		
		// P2
		textures[6] = new ImageIcon("res/claptrap2.png");		
		textures[7] = new ImageIcon("res/claptrap2.png");
		textures[8] = new ImageIcon("res/claptrap2.png");	
		textures[9] = new ImageIcon("res/claptrap2.png");
		textures[10] = new ImageIcon("res/claptrap2.png");	
		textures[11] = new ImageIcon("res/claptrap2.png");
	}

	private void SwingInit(){
		JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Badass Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        composeGrid(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
	}


	private void composeGrid(Container pane) {
		
		pane.setLayout(new GridBagLayout());
      
		for(int i=0; i < Constants.MAX_INDEX; ++i)
			AddCellToGrid(pane, i, board.GetPiece(i));
	}

	private void AddCellToGrid(Container pane, int pos, Piece piece){
		JButton jbnButton;

		if(piece != null){
			
			int value = GetTextureArrayPosition(piece.GetSymbol());
			jbnButton = new JButton(textures[value]);
			
		}
		else{
			
			jbnButton = new JButton();
		
		}
		
        jbnButton.setName(Integer.toString(pos));
        
        // offsetDueToOddRow is used to alternate colors between adjacent rows
        int offsetDueToOddRow = (pos / Constants.MAX_INDEX_ROW) % 2;
        jbnButton.setBackground( ((pos + offsetDueToOddRow) % 2) == 0 ? Color.white : Color.black);
        
        jbnButton.setPreferredSize(new Dimension(80, 80));
        jbnButton.setBorder(null);
        jbnButton.addActionListener(this);

	    GridBagConstraints gBC = new GridBagConstraints();
	    gBC.fill = GridBagConstraints.HORIZONTAL;
        gBC.gridx = pos % Constants.MAX_INDEX_ROW;
        gBC.gridy = pos / Constants.MAX_INDEX_ROW;
        
        pane.add(jbnButton, gBC);
	}
	
	// *************************************************************************************************************************
	// **************************************************** EVENTS MANAGEMENT **************************************************
	// *************************************************************************************************************************
	
	@Override
	// Event listener: Thread safe.
	public synchronized void actionPerformed(ActionEvent arg0) {
		if(((JButton) arg0.getSource()) != null){
			JButton x = (JButton) arg0.getSource();
			eventsQueue.push(Integer.valueOf(x.getName()));

			System.out.println("Thread id:" + Thread.currentThread().getId());
			System.out.println("Added event in the queue: " + eventsQueue.peekLast() + ". Size of new events: " + eventsQueue.size());
		}
	}
	
	// Event peek: Thread safe.
	public synchronized boolean HasNewEvent(){
		return !eventsQueue.isEmpty();
	}
	
	// Event eater: Thread safe.
	public synchronized int GetNextEvent(){
		System.out.println("Thread id:" + Thread.currentThread().getId());
		System.out.println("Consumed event : " + eventsQueue.peekFirst() + ". Number of new events: " + eventsQueue.size());
		return eventsQueue.removeFirst();
	}

	// *************************************************************************************************************************
	// *********************************************************** END *********************************************************
	// *************************************************************************************************************************
	
	
	private int GetTextureArrayPosition(char pieceSymbol){
		int toReturn = -1;
		
		switch(pieceSymbol){
			case 'k':
				++toReturn;
			case 'q':
				++toReturn;
			case 'b':
				++toReturn;
			case 'h':
				++toReturn;
			case 't':
				++toReturn;
			case 'p':
				++toReturn;
			case 'K':
				++toReturn;
			case 'Q':
				++toReturn;
			case 'B':
				++toReturn;
			case 'H':
				++toReturn;
			case 'T':
				++toReturn;
			case 'P':
				++toReturn;
		};
		
		return toReturn;
	}
}
