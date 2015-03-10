package com.bifidoteam.scacchise.controller;

import com.bifidoteam.scacchise.interfaces.ControllerInterface;
import com.bifidoteam.scacchise.interfaces.ViewInterface;
import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.scacchise.view.GameConsoleView;
import com.bifidoteam.util.MedusaTree;

import java.util.*;

public class GameManager implements ControllerInterface{
	
	//-----------------------------Private Variables----------------------------------------
	private static GameManager instance = null;
	private enum GameState{WAITING ,SELECTED,MOVING, WIN, QUIT};
	//private int MAX_PLAYER = 2;
	
	private Chessboard chessboard;
	private int lastSelectedIndex; //-1 if there isn't a last index selected otherwise index form 0 to 64 (MAX_INDEX)
	private int whiteTurn; //0 if it is the white player turn, 1 black
	private GameState gameState;
	private MedusaTree medusaTreeSelectedIndex;
	
	private ViewInterface viewComponent;
	
//	private int[] kingPos;
	//-----------------------------Private Variables----------------------------------------
	
	//--------------------------------Costructors-------------------------------------------
	public static GameManager getInstance() {
		if(instance == null)
			instance = new GameManager();
		
		return instance;
	}
	
	public static GameManager getInstance(Chessboard c) {
		if(instance == null)
			instance = new GameManager(c);
		
		return instance;
	}
	
	private GameManager() {
		InitGameManager(null);
	}
	
	private GameManager(Chessboard c) {
		InitGameManager(c);	
	}
	
	private void InitGameManager(Chessboard c){
		if(c == null){
			chessboard = new Chessboard();
		}
		else {
			chessboard = c;
		}
//		kingPos = new int[Constants.MAX_PLAYERS];
		viewComponent = new GameConsoleView(); // TODO: Sostituirlo con un factory esterno al GM?
	}
	//--------------------------------Costructors-------------------------------------------
	
	//-----------------------------Debug functions -----------------------------------------
	public MedusaTree GetReachebleIndicesDebug(int index, int whiteTurn)
	{
		this.whiteTurn = whiteTurn;
		return GetReachableIndices(index);
	}
	//-----------------------------Debug functions -----------------------------------------
	
	//-----------------------------Controller Interface functions---------------------------
//	@Override
//	public void OnClick(int index) {
//		//TODO: Vedere cosa farne
//	}
	
	@Override
	public void Reset() {
		InitGameManager(null);
		
	}
	
	@Override
	public void Quit() {
		gameState = GameState.QUIT;
		
	}
	
	@Override
	public void OnMoveDone()
	{
		ChangePlayerTurn();
	}
	
	public MedusaTree GetReachableIndicesPlus(int index)
	{
		MedusaTree reachebleIndices = chessboard.GetRealIndices(index);
		if(reachebleIndices != null){
			MedusaTree.CompleteIterator mtIterator = reachebleIndices.GetCompleteIterator();
			while(mtIterator.hasNext()) {
				Integer reachebleIndex = mtIterator.next();
				//If there is another piece I leave this and cut after
				if(chessboard.GetPiece(reachebleIndex) != null){
						mtIterator.CutAfter();	
				}
			}
			if(chessboard.IsPieceSpecial(index)){
	
				MedusaTree eatableIndices = chessboard.GetEatableIndices(index);
				MedusaTree.CompleteIterator meEatableIterator = eatableIndices.GetCompleteIterator();
	
				while(meEatableIterator.hasNext()) {
					Integer eatableIndex = meEatableIterator.next();
					if(chessboard.GetPiece(eatableIndex) != null) {
						meEatableIterator.CutAfter();
					}
				}
	
				reachebleIndices.MergeMedusaTreeNewBanch(eatableIndices);
			}
		}
		return reachebleIndices;
	}
	
	public void GameLoop(){
		viewComponent.Init(chessboard);
		
		while(gameState != GameState.QUIT){
			
			int indexChosen = viewComponent.GetInput();
			
			ManageInput(indexChosen);
			
			viewComponent.Render(medusaTreeSelectedIndex); //TODO: Marco: passare il medusa tree da renderizzare
		}
		
	}
	//-----------------------------Controller Interface functions---------------------------
	
	//-----------------------------Private functions----------------------------------------

	void ManageInput(int index){
		switch (gameState) {
			case WAITING:
				Waiting(index);
				break;
			case SELECTED:
				SelectedPiece(index);
				break;
			case WIN:
				//TODO: Dichiarare il vincitore **** LASCIARE IL COLORE CHE VINCE **** 
				viewComponent.EndGame(whiteTurn);
				//ed attendere il reset
			default:
				break;
		}
	}
	
	private void Waiting(int index) {
		if(index >= 0 && index < Constants.MAX_INDEX && IsPlayerPiece(index)) {
				lastSelectedIndex = index;
				medusaTreeSelectedIndex = GetReachableIndices(index);
				// TODO: vuoto
				//viewComponent.DrowReacheblePosition(medusaTreeSelectedIndex);
				
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
				
				viewComponent.MoveFromStartIndexToEndIndex(lastSelectedIndex, index);

				ResetMoveState();
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
		ResetMoveState();
	}
	
	private void ResetMoveState(){
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
		if(reachebleIndices != null){
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
				}
	
				reachebleIndices.MergeMedusaTreeNewBanch(eatableIndices);
			}
		}
		return reachebleIndices;
	}
	
	
	//--------------------------------Private method for game stream------------------------	
	//check control for king "colorPlayer"
	private boolean isCheck(int colorPlayer) throws Exception{
		throw new Exception("Not Implemented yet");
	}
	
	//return the index of valid tiles around the king where he can moves
	private List<Integer> searchKingAdjacentSafe(int ColorPlayer) throws Exception{
		throw new Exception("Not Implemented yet");
	}
	
	//return the index of valid tiles usefull to block the check
	private List<Integer> searchDistantSafeMoves(int colorPlayer) throws Exception{
		throw new Exception("Not implemented yet");
	}
	//--------------------------------Private method for game stream------------------------
	
	
	
	//-----------------------------Private functions----------------------------------------
}
