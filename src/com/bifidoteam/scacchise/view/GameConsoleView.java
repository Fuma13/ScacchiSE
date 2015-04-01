package com.bifidoteam.scacchise.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.bifidoteam.scacchise.controller.GameManager;
import com.bifidoteam.scacchise.interfaces.ControllerInterface;
import com.bifidoteam.scacchise.interfaces.LogType;
import com.bifidoteam.scacchise.interfaces.ViewInterface;
import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

public class GameConsoleView implements ViewInterface {

	DrawComponent drawingClass;
	ControllerInterface gm;
	
	final private int A_ASCII_NUMBER = 65;
	final private int ZERO_ASCII_NUMBER= 48;
	
	public GameConsoleView() {	}
	
	@Override
	public void Init(Chessboard base) {
		gm = GameManager.getInstance();
		drawingClass = new DrawComponent(base);
		
		drawingClass.printMap("Test print:");
	}
	
	@Override
	public void Render(MedusaTree reacheblePosition) {
		drawingClass.writeMedusaOnMap(reacheblePosition, false);
		
		drawingClass.printMap("Test print:");
	}

	@Override
	public void MoveFromStartIndexToEndIndex(int startIndex, int endIndex) {
		
		gm.onMoveDone();
		
		return;
	}

	@Override
	public int GetInput() {
		
		System.out.println("Write next Move: (Syntax: \"A4\")");
		String in = null;
		int toReturn = -1;
	    InputStreamReader streamReader = new InputStreamReader(System.in);
	    BufferedReader bufferedReader = new BufferedReader(streamReader);

		try {
			while(toReturn < 0){
					in = bufferedReader.readLine();
				toReturn = ParseInput(in);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return toReturn;
	}

	@Override
	public void EndGame(int info) {
		System.out.println("CheckMate!!! Player " + (info == 0 ? "White" : "Black") + " Win!!");
	}
	
	
	
	
	private int ParseInput(String input){
		int toReturn = -1;
		char[] toParse = input.toCharArray();
		
		// Mossa diretta: Da cella dove sono a celladove voglio andare. Esempio A1 B6
		if(input.contains(" ")){
			// TODO: Vedere se gestire il movimento senza medusa
		}
		// Mossa indiretta: Scelgo il pezzo con cui mostrare il medusa tree. Esempio A1 
		else{
			if(input.length() == 2){
				int row = ((int) toParse[0]) - A_ASCII_NUMBER;
				int col = ((int) toParse[1]) - ZERO_ASCII_NUMBER;
				
				toReturn = col*Constants.MAX_INDEX_ROW + row;
				
				// Check se e' out of bound
				if(toReturn < 0 || toReturn > Constants.MAX_INDEX){
					toReturn = -1;
				}
			}
		}
		
		return toReturn;
	}

	@Override
	public void Log(String message, LogType type) {
		System.out.println(type.toString() + ": " + message);
	}

}
