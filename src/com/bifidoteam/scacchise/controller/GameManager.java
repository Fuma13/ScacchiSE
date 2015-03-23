package com.bifidoteam.scacchise.controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.bifidoteam.scacchise.interfaces.ControllerInterface;
import com.bifidoteam.scacchise.interfaces.ViewInterface;
import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.scacchise.view.GameConsoleView;
import com.bifidoteam.scacchise.view.SwingView;
import com.bifidoteam.util.MedusaTree;
import com.bifidoteam.util.MedusaTree.CuttedIterator;

public class GameManager implements ControllerInterface{
	
	//-----------------------------Private Variables----------------------------------------
	private static GameManager instance = null;
	private enum GameState{WAITING ,SELECTED,MOVING, WIN, QUIT};
	//private int MAX_PLAYER = 2;
	
	private Chessboard chessboard;
	private int lastSelectedIndex; //-1 if there isn't a last index selected otherwise index form 0 to 64 (MAX_INDEX)
	private int colorTurn; //0 if it is the white player turn, 1 black
	private GameState gameState;
	private MedusaTree medusaTreeSelectedIndex;
	
	private ViewInterface viewComponent;		
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
			
			//Genera gli MT dei Bianchi
			this.generateAllMtOfColor(Constants.WHITE);
			//Recupera la lista dei bianchi e li fa registrare sugli MT
			this.registerPiecesOnTheirMT(this.chessboard.getColorList(Constants.WHITE));
			this.generateAllMtOfColor(Constants.BLACK);
			this.registerPiecesOnTheirMT(this.chessboard.getColorList(Constants.BLACK));	
			
		}
		else {
			chessboard = c;
		}
//		kingPos = new int[Constants.MAX_PLAYERS];
		this.gameState = GameState.WAITING;
		viewComponent = new SwingView(); // TODO: Sostituirlo con un factory esterno al GM?
	}
	//--------------------------------Costructors-------------------------------------------
	
	//-----------------------------Debug functions -----------------------------------------
	public MedusaTree GetReachebleIndicesDebug(int index, int whiteTurn)
	{
		this.colorTurn = whiteTurn;
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
	
	private MedusaTree GetReachableIndicesPlus(int index)
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
				viewComponent.EndGame(colorTurn);
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
				
				// ** NOTA PER MARCO: ho spostato queste 2 righe sopra la chiamata alla view perche',
				// 		non essendo su diversi thread, viene chiamato prima il onMoveDone che setta il GameState.Waiting
				//		prima di raggiungere queste due righe. Su multi thread ok, in single thread no >.<
				ResetMoveState();
				gameState = GameState.MOVING;
				
				viewComponent.MoveFromStartIndexToEndIndex(lastSelectedIndex, index);

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
		colorTurn = OppositePlayer();
		CheckCheck();
		SetWaitingState();
	}
	
	private int OppositePlayer() {
		return (Constants.MAX_PLAYERS - 1) - colorTurn;
	}
	
	//-----------------------------Chessboard functions
	private boolean IsPlayerPiece(int index)
	{
		boolean isPlayerPiece = false;
		if(index >= 0 && index < Constants.MAX_INDEX)
		{
			if(chessboard.GetPiece(index) != null && chessboard.IsPieceWhite(index) == colorTurn) {
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
					if(chessboard.IsPieceSpecial(index) || chessboard.IsPieceWhite(reachebleIndex) == colorTurn) {
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
					if(chessboard.IsPieceWhite(eatableIndex) != colorTurn) {//If is an opponent piece
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

	
	//Generate all MT of pieces of color "colorPlayer"
	public void generateAllMtOfColor(int colorPlayer){
		Set<Integer> piecesList;
		Iterator<Integer> it;
		
		piecesList = this.chessboard.getColorList(colorPlayer);
		it = piecesList.iterator();
		while(it.hasNext()){
			int next = it.next();
			this.chessboard.GetPiece(next).setMedusaTree(this.GetReachableIndicesPlus(next));
		}
	}
	
	//Generate MT of a piece
	public void generateMt(int pieceIndex){
		this.chessboard.GetPiece(pieceIndex).setMedusaTree(this.GetReachableIndicesPlus(pieceIndex));
	}
	
	//Generate MTs of a list of pieces
	public void generatePiecesMt(LinkedList<Integer> piecesList){
		Iterator<Integer> it = piecesList.iterator();
		while(it.hasNext()){
			this.generateMt(it.next());
		}
	}
	
	//Register a list of pieces on their own MT
	public void registerPiecesOnTheirMT(Set<Integer> piecesList){
		Iterator<Integer> it = piecesList.iterator();
		while(it.hasNext()){
			int i = it.next();
			this.registerPieceOnHisMT(i,this.chessboard.IsPieceWhite(i));
		}
	}
	
	//Register a piece on its own MT
	public void registerPieceOnHisMT(int pieceIndex,int pieceColor){
		CuttedIterator it = this.chessboard.GetPiece(pieceIndex).getMedusaTree().GetCuttedIterator();
		while(it.hasNext()){
			this.chessboard.getTile(it.next()).registerPiece(pieceIndex,pieceColor);
		}
	}
	
	private boolean CheckCheck(){
		//get the king pos
		int kingPos = this.chessboard.getKing(this.colorTurn);
		
		int numberOfOpponent = this.chessboard.getTile(kingPos).numberOfOpponentPiecesRegisteredOn(this.colorTurn);
		//check if there is an opponent piece registered on the kingPos
		if(numberOfOpponent >0){
			LinkedList<Integer> validMoves = new LinkedList<Integer>();
			validMoves.addAll(this.searchKingAdjacentSafe());
			//TODO to continue after implement searchKingAdjacentSafe
			return true;
		}else{
			//NO check/checkMate on the king
			return false;
		}
	}
	
	//return the index of valid tiles around the king where he can moves
	public List<Integer> searchKingAdjacentSafe(){
		//list of valid moves for the king
		LinkedList<Integer> kingValidMoves = new LinkedList<Integer>();
		
		//get the king mt
		MedusaTree kingMt = this.GetReachableIndices(this.chessboard.getKing(this.colorTurn));
		
		//for each leaf gets the tile and checks how many opponents are registered on
		CuttedIterator it = kingMt.GetCuttedIterator(); //TODO ritorna l'mt completo del re?
		while(it.hasNext()){
			int leafIndex = it.next();
			int numOpponentsRegisteredOnLeaf = this.chessboard.getTile(leafIndex).numberOfOpponentPiecesRegisteredOn(this.colorTurn);
			
			//at least on enemy is registered on that tile
			if(numOpponentsRegisteredOnLeaf > 0){
				//if is exactly one, king can eat if the opponent piece is there
				if(numOpponentsRegisteredOnLeaf == 1){
					
					//Only one Opponent expected
					Set<Integer> registeredOpponents = this.chessboard.getTile(leafIndex).getColorListRegistered(OppositePlayer());					
					if(registeredOpponents.contains(leafIndex)){
						kingValidMoves.add(leafIndex);	
					}
				}
			}else{
				//check if there is a friend
				Set<Integer> registeredFriends = this.chessboard.getTile(leafIndex).getColorListRegistered(this.colorTurn);
				if(registeredFriends.size() == 0 ){
					kingValidMoves.add(leafIndex);
				}
			}
		}
		return kingValidMoves;
	}
	
	//return the index of valid tiles usefull to block the check
	public List<Integer> searchDistantSafeMoves(int colorPlayer) throws Exception{
		throw new Exception("Not implemented yet");
	}
	
	//--------------------------------Private method for game stream------------------------
	
	
	
	//-----------------------------Private functions----------------------------------------
}
