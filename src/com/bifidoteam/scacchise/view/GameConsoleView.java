package com.bifidoteam.scacchise.view;

import com.bifidoteam.scacchise.controller.GameManager;
import com.bifidoteam.scacchise.interfaces.ControllerInterface;
import com.bifidoteam.scacchise.interfaces.ViewInterface;
import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.util.MedusaTree;

public class GameConsoleView implements ViewInterface {

	DrawComponent drawingClass;
	ControllerInterface gm;
	
	public GameConsoleView() {
		gm = GameManager.getInstance();
	}
	
	@Override
	public void Init(Chessboard base) {
		drawingClass = new DrawComponent(base);
	}
	
	@Override
	public void Render(MedusaTree reacheblePosition) {
		drawingClass.writeMedusaOnMap(reacheblePosition, false);
		
		drawingClass.printMap("Test print:");
	}

	@Override
	public void MoveFromStartIndexToEndIndex(int startIndex, int endIndex) {
		
		gm.OnMoveDone();
		
		return;
	}

	@Override
	public int GetInput() {
		
		System.out.println("Write next Move: (Syntax: \"king\" or \"A4\")");
		String in = System.console().readLine();
		
		return ParseInput(in);
	}

	@Override
	public void EndGame(int info) {
		System.out.println("CheckMate!!! Player " + (info == 0 ? "White" : "Black") + " Win!!");
	}
	
	
	
	
	private int ParseInput(String input){
		// TODO: Implement the parser
		return 0;
	}

}
