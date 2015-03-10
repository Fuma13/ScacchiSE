package com.bifidoteam.scacchise.view;

import com.bifidoteam.scacchise.controller.GameManager;
import com.bifidoteam.scacchise.interfaces.ViewInterface;
import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.util.MedusaTree;

public class GameConsoleView implements ViewInterface {

	Chessboard toRender; //TODO: Creare un drawcomponent con al posto di map la chessboard
	
	@Override
	public void Init(Chessboard base) {
		toRender = base;
	}
	
	@Override
	public void Render(MedusaTree reacheblePosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void MoveFromStartIndexToEndIndex(int startIndex, int endIndex) {
		
		GameManager.getInstance().OnMoveDone();
		
		return;
	}

//	@Override
//	public void OnConfigurationChange() {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public int GetInput() {
		return 0;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void EndGame(int info) {
		// TODO Auto-generated method stub
		
	}

}
