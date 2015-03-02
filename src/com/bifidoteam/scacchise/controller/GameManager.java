package com.bifidoteam.scacchise.controller;

import java.util.Set;

import com.bifidoteam.scacchise.interfaces.ControllerInterface;
import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

public class GameManager implements ControllerInterface{
	
	//-----------------------------Private Variables----------------------------------------
	private static GameManager instance = null;
	private enum GameState{WAITING ,SELECTED,MOVING};
	//private int MAX_PLAYER = 2;
	
	private Chessboard chessboard;
	private int lastSelectedIndex; //-1 if there isn't a last index selected otherwise index form 0 to 64 (MAX_INDEX)
	private int whiteTurn; //0 if it is the white player turn, 1 black
	private GameState gameState;
	private MedusaTree medusaTreeSelectedIndex;
	
	private Set<Integer>[] possibleCheck;
	private Set<Integer>[] check;
	private int[] kingPos;
	//-----------------------------Private Variables----------------------------------------
	
	//--------------------------------Costructors-------------------------------------------
	private GameManager() {
		possibleCheck = new Set[Constants.MAX_PLAYERS];
		check = new Set[Constants.MAX_PLAYERS];
		kingPos = new int[Constants.MAX_PLAYERS];
	}
	
	public GameManager getInstance() {
		if(instance == null)
			instance = new GameManager();
		
		return instance;
	}
	//--------------------------------Costructors-------------------------------------------
	
	//-----------------------------Controller Interface functions---------------------------
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
	
	@Override
	public void OnMoveDone()
	{
		ChangePlayerTurn();
	}
	//-----------------------------Controller Interface functions---------------------------
	
	//-----------------------------Private functions----------------------------------------
	private void Waiting(int index) {
		if(index >= 0 && index < Constants.MAX_INDEX && IsPlayerPiece(index)) {
				lastSelectedIndex = index;
				medusaTreeSelectedIndex = GetReachableIndices(index);
				//TODO:FUMA: mandare alla view il MedusaTree da disegnare
				//view.DrowReacheblePosition(medusaTreeSelectedIndex);
				gameState = GameState.SELECTED;
		}
		else {
			SetWaitingState();
		}
	}
	
	private void SelectedPiece(int index) {
		if(index >= 0 && index < Constants.MAX_INDEX && index != lastSelectedIndex) {
			if(medusaTreeSelectedIndex != null && medusaTreeSelectedIndex.Contain(index)) {
				chessboard.MovePieceFromStartIndexToEndIndex(lastSelectedIndex, index);
				//TODO:FUMA: move the piece
				//view.MoveFromIndexToIndex(lastSelectedIndex, index);
				gameState = GameState.MOVING;
			}
			else {
				//Check if is another 
				Waiting(index);
			}
		}
		else {
			//Deselect the piece
			SetWaitingState();
		}
	}
	
	private void SetWaitingState(){
		gameState = GameState.WAITING;
		medusaTreeSelectedIndex = null;
		lastSelectedIndex = -1;
	}
	
	private void ChangePlayerTurn() {
		whiteTurn = OppositePlayer();
		CheckCheck();
		SetWaitingState();
	}
	
	private int OppositePlayer() {
		return (Constants.MAX_PLAYERS - 1) - whiteTurn;
	}
	
	//-----------------------------Chessboard functions
	private boolean CheckCheck(){
		return false;
	}
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
		 
		MedusaTree.CompleteIterator mtIterator = reachebleIndices.GetCompleteIterator();
		while(mtIterator.hasNext()) {
			Integer reachebleIndex = mtIterator.next();
			if(chessboard.GetPiece(reachebleIndex) != null){
				//If is special (Pawn) then the cut different or is a player piece
				if(chessboard.IsPieceSpecial(index) || chessboard.IsPieceWhite(reachebleIndex) == whiteTurn) {
					//This piece can't arrive in this cell
					mtIterator.CutThisAndAfter();
				}
				else {
					//Else is an opponent piece
					//then this piece can eat it and can't continue on this way 
					mtIterator.CutAfter();	
				}
			}
			
			
			if(!chessboard.IsPieceSpecial(index) && reachebleIndex == kingPos[OppositePlayer()]){
				if(mtIterator.IsThisCutted()) {
					possibleCheck[OppositePlayer()].add(index);
				}
				else
				{
					check[OppositePlayer()].add(index);
				}
			}
		}
		if(chessboard.IsPieceSpecial(index)){

			MedusaTree eatableIndices = chessboard.GetEatableIndices(index);
			MedusaTree.CompleteIterator meEatableIterator = eatableIndices.GetCompleteIterator();

			while(meEatableIterator.hasNext()) {
				Integer eatableIndex = meEatableIterator.next();
				if(chessboard.IsPieceWhite(eatableIndex) != whiteTurn) {//If is an opponent piece
					//then this piece can eat it and can't continue on this way 
					meEatableIterator.CutAfter();
				}
				else {
					//Else this piece can't arrive in this cell
					meEatableIterator.CutThisAndAfter();
				}
				
				if(eatableIndex == kingPos[OppositePlayer()]){
					if(mtIterator.IsThisCutted()) {
						possibleCheck[OppositePlayer()].add(index);
					}
					else
					{
						check[OppositePlayer()].add(index);
					}
				}
			}

			reachebleIndices.MergeMedusaTreeNewBanch(eatableIndices);
		}
		
		//Controllo che il re non sia in scacco (se c'e qualcosa nelle minacce vere)
		//simulando la mossa e controllando che tt quelli nella lista ancora non possano arrivare
		//se arrivano allora cancello la mossa se no e' valida
		//Modificare i cut in modo che taglino tutte le foglie OK
		//inserire un cut per tagliare la foglia intermedia OK 
		//creare un iteratore che mi cicli anche quelle tagliate OK
		//creare un iteratore che mi cicli solo quelle attive per la view OK
		return reachebleIndices;
	}
	//-----------------------------Private functions----------------------------------------
}
