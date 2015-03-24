package com.bifidoteam.scacchise.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.Border;

import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.scacchise.model.Piece;
import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;
import com.bifidoteam.util.MedusaTree.CuttedIterator;

public class SwingComponent implements Runnable, ActionListener {

	ArrayDeque<Integer> eventsQueue;
	
	Chessboard board;
	JButton chessboardTiles[];
	ImageIcon textures[];
	
	Border blackline;
	boolean isInitialized;
	
	public SwingComponent(Chessboard base){
		board = base;
		eventsQueue= new ArrayDeque<Integer>();
		
		chessboardTiles = new JButton[Constants.MAX_INDEX];
		
		blackline = BorderFactory.createLineBorder(Color.black);
		
		isInitialized = false;
	}
	
	public void RenderWithMedusa(MedusaTree mt){
		UpdateViewCell();
		
		if(mt != null){
			CuttedIterator toAnalyze = mt.GetCuttedIterator();
			
			while(toAnalyze.hasNext()){
				int val = toAnalyze.next();
				
				// If there is another piece in the cell => i can eat! I sign it with another color!
				chessboardTiles[val].setBackground( board.getPiece(val) == null ? Color.yellow : Color.red);
				chessboardTiles[val].setBorder(blackline);
			}
		}
	}
	
	public boolean IsInitialize(){
		return isInitialized;
	}
	
	@Override
	public void run() {
		LoadTextures();
		SwingInit();
		
		isInitialized=true;
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
		// --> Update the background removing the medusa tree! Thread safe because is called by the same thread of the RenderWithMedusa <--
		RefreshBackground(); 
		
		System.out.println("Thread id:" + Thread.currentThread().getId());
		System.out.println("Consumed event : " + eventsQueue.peekFirst() + ". Number of new events: " + eventsQueue.size());
		return eventsQueue.removeFirst();
	}

	// *************************************************************************************************************************
	// *********************************************************** END *********************************************************
	// *************************************************************************************************************************
	
	
	

	// *************************************************************************************************************************
	// ************************************************* INIT PRIVATE FUNCTIONS ************************************************
	// *************************************************************************************************************************
	

	private void LoadTextures(){
		textures = new ImageIcon[Constants.MAX_TEXTURES];
		
		// P1
		textures[0] = new ImageIcon("res/ChessImgV1/0.png");		
		textures[1] = new ImageIcon("res/ChessImgV1/1.png");
		textures[2] = new ImageIcon("res/ChessImgV1/2.png");
		textures[3] = new ImageIcon("res/ChessImgV1/3.png");
		textures[4] = new ImageIcon("res/ChessImgV1/4.png");
		textures[5] = new ImageIcon("res/ChessImgV1/5.png");
		
		// P2
		textures[6] = new ImageIcon("res/ChessImgV1/6.png");		
		textures[7] = new ImageIcon("res/ChessImgV1/7.png");
		textures[8] = new ImageIcon("res/ChessImgV1/8.png");	
		textures[9] = new ImageIcon("res/ChessImgV1/9.png");
		textures[10] = new ImageIcon("res/ChessImgV1/10.png");	
		textures[11] = new ImageIcon("res/ChessImgV1/11.png");
	}

	private void SwingInit(){
		JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Badass Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700,700));
        
        //Set up the content pane.
        composeGrid(frame.getContentPane());

        frame.pack();
        frame.setVisible(true);
	}


	private void composeGrid(Container pane) {
		
		pane.setLayout(new GridBagLayout());
      
		for(int i=0; i < Constants.MAX_INDEX; ++i)
			AddCellToGrid(pane, i, board.getPiece(i));
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
        
        jbnButton.setPreferredSize(new Dimension(90, 90));
        jbnButton.setBorder(null);
        jbnButton.addActionListener(this);
        
        chessboardTiles[pos] = jbnButton;

	    GridBagConstraints gBC = new GridBagConstraints();
	    gBC.fill = GridBagConstraints.HORIZONTAL;
        gBC.gridx = pos % Constants.MAX_INDEX_ROW;
        gBC.gridy = pos / Constants.MAX_INDEX_ROW;
        
        pane.add(jbnButton, gBC);
	}

	
	// *************************************************************************************************************************
	// *********************************************************** END *********************************************************
	// *************************************************************************************************************************

	
	

	// *************************************************************************************************************************
	// ************************************************ PRIVATE UPDATE FUNCTIONS ***********************************************
	// *************************************************************************************************************************
	
	
	private void UpdateViewCell(){
		
		Piece actual;
		int index;
		
		for(int pos=0; pos< Constants.MAX_INDEX; ++pos){
			actual = board.getPiece(pos);
			
			if(actual != null){
				index = GetTextureArrayPosition(actual.GetSymbol());
				if(index >= 0)
					chessboardTiles[pos].setIcon(textures[index]);
				else
					chessboardTiles[pos].setIcon(null);
			}
			else{
				chessboardTiles[pos].setIcon(new ImageIcon());
			}
		}
	}
	
	private void RefreshBackground(){
		int offsetDueToOddRow;
		for(int pos=0; pos< Constants.MAX_INDEX; ++pos){
	        offsetDueToOddRow = (pos / Constants.MAX_INDEX_ROW) % 2;
	        chessboardTiles[pos].setBackground( ((pos + offsetDueToOddRow) % 2) == 0 ? Color.white : Color.black);
			chessboardTiles[pos].setBorder(null);
		}
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
