package com.bifidoteam.scacchise.controller;

import java.util.Iterator;

import com.bifidoteam.scacchise.interfaces.ControllerInterface;
import com.bifidoteam.scacchise.model.MedusaTree;
import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.scacchise.util.Constants;

public class GameManager implements ControllerInterface{
	
	//-----------------------------Private Variables----------------------------------------
	private static GameManager instance = null;
	private enum GameState{WAITING ,SELECTED,MOVING};
	
	private Chessboard chessboard;
	private int lastSelectedIndex; //-1 if there isn't a last index selected otherwise index form 0 to 64 (MAX_INDEX)
	private boolean whiteTurn; //true if it is the white player turn, false otherwise
	private GameState gameState;
	private MedusaTree medusaTreeSelectedIndex;
	//-----------------------------Private Variables----------------------------------------
	
	//--------------------------------Costructors-------------------------------------------
	private GameManager()
	{
		
	}
	
	public GameManager getInstance()
	{
		if(instance == null)
			instance = new GameManager();
		
		return instance;
	}
	//--------------------------------Costructors-------------------------------------------
	
	//-----------------------------Public functions-----------------------------------------
	@Override
	public void OnClick(int index) {
		switch (gameState) {
			case WAITING:
				Waiting(index);
				break;
			case SELECTED:
				SelectedPiece(index);
				break;
			default:
				break;
		}
		
	}
	//-----------------------------Public functions-----------------------------------------
	
	//-----------------------------Private functions----------------------------------------
	private void Waiting(int index) {
		if(index >= 0 && index < Constants.MAX_INDEX && IsPlayerPiece(index)) {
				lastSelectedIndex = index;
				medusaTreeSelectedIndex = GetReachableIndices(index);
				//TODO:FUMA: mandare alla view il MedusaTree da disegnare
				gameState = GameState.SELECTED;
		}
		else {
			SetWaitingState();
		}
	}
	
	private void SelectedPiece(int index) {
		if(index >= 0 && index < Constants.MAX_INDEX && index != lastSelectedIndex) {
			if(medusaTreeSelectedIndex != null && medusaTreeSelectedIndex.Contain(index)) {
				//TODO:FUMA: move the piece
				gameState = GameState.MOVING;
			}
			else {
				//Check if is another 
				Waiting(index);
			}
		}
		else {
			SetWaitingState();
		}
	}
	
	private void SetWaitingState(){
		gameState = GameState.WAITING;
		medusaTreeSelectedIndex = null;
		lastSelectedIndex = -1;
	}
	
//	private void ChangePlayerTurn() {
//		whiteTurn = !whiteTurn;
//		SetWaitingState();
//	}
	
	//-----------------------------Chessboard functions
	private boolean IsPlayerPiece(int index)
	{
		boolean isPlayerPiece = false;
		if(index >= 0 && index < Constants.MAX_INDEX)
		{
			if(chessboard.GetPiece(index) != null && chessboard.IsPieceWhite(index) == whiteTurn) {
					isPlayerPiece = true;
			}
		}
		
		return isPlayerPiece;
	}
	
	private MedusaTree GetReachableIndices(int index)
	{
		MedusaTree reachebleIndices = chessboard.GetRealIndices(index);
		//if is not special!!!!
		Iterator<Integer> mtIterator = reachebleIndices.iterator();
		while(mtIterator.hasNext()) {
			Integer reachebleIndex = mtIterator.next();
			if(chessboard.GetPiece(reachebleIndex) != null){
				if(chessboard.IsPieceWhite(reachebleIndex) != whiteTurn) {//If is an opponent piece
					//then the piece can eat it and can't continue on this way 
					reachebleIndices.CutAfter();
				}
				else {
					//Else the piece can't arrive in this cell
					reachebleIndices.CutBeforeAndAfter();
				}
			}
		}
		return reachebleIndices;
	}
	//-----------------------------Private functions----------------------------------------
}
