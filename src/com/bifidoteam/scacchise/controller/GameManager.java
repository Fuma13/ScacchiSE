package com.bifidoteam.scacchise.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.bifidoteam.scacchise.interfaces.ControllerInterface;
import com.bifidoteam.scacchise.interfaces.ViewInterface;
import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.scacchise.model.Piece;
import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.scacchise.view.GameConsoleView;
import com.bifidoteam.util.MedusaTree;
import com.bifidoteam.util.MedusaTree.CuttableCuttedIterator;
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
	private MedusaTree medusaTreeSelectedIndex; //The mt of the selected index (piece), null otherwise
	
	//store the forecast <piecePosIndex,hisMedusaTree> where the index is the piece
	//owner of the mt
	private HashMap<Integer,MedusaTree> forecastMts;
	
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
		initGameManager(null);
	}
	
	private GameManager(Chessboard c) {
		initGameManager(c);	
	}
	
	private void initGameManager(Chessboard c){
		if(c == null){
			chessboard = new Chessboard();	
		}
		else {
			chessboard = c;
		}
		
		//TODO: suggerimento a cambiare struttura per Android O_o cosa ne pensate?
		this.forecastMts = new HashMap<Integer,MedusaTree>();
		
		//Genera gli MT dei Bianchi
		this.generateAllMtOfColor(Constants.WHITE);
		//Recupera la lista dei bianchi e li fa registrare sugli MT
		this.registerPiecesOnTheirMT(this.chessboard.getColorList(Constants.WHITE));
		this.generateAllMtOfColor(Constants.BLACK);
		this.registerPiecesOnTheirMT(this.chessboard.getColorList(Constants.BLACK));
		
		this.gameState = GameState.WAITING;
		viewComponent = new GameConsoleView(); // TODO: Sostituirlo con un factory esterno al GM?
	}
	//--------------------------------Costructors-------------------------------------------
	
	//-----------------------------Debug functions -----------------------------------------
	public MedusaTree getReachebleIndicesDebug(int index, int whiteTurn)
	{
		this.colorTurn = whiteTurn;
		return getPossibleMoves(index,whiteTurn);
	}
	//-----------------------------Debug functions -----------------------------------------
	
	//-----------------------------Controller Interface functions---------------------------
//	@Override
//	public void OnClick(int index) {
//		//TODO: Vedere cosa farne
//	}
	
	//TODO: Rivedere il reset con ricky
	@Override
	public void reset() {
		initGameManager(null);
		
	}
	
	@Override
	public void quit() {
		gameState = GameState.QUIT;
		
	}
	
	@Override
	public void onMoveDone()
	{
		changePlayerTurn();
	}
	
	public void gameLoop(){
		viewComponent.Init(chessboard);
		
		while(gameState != GameState.QUIT){
			
			int indexChosen = viewComponent.GetInput();
			
			manageInput(indexChosen);

			viewComponent.Render(medusaTreeSelectedIndex);
		}
		
	}
	//-----------------------------Controller Interface functions---------------------------
	
	//-----------------------------Private functions----------------------------------------

	private void manageInput(int index){
		switch (gameState) {
			case WAITING:
				selectPiece(index);
				break;
			case SELECTED:
				movePiece(index);
				break;
			case WIN:
				//ed attendere il reset
			//When is in MOVING state the GameManager wait the callback function OnMoveDone
			default:
				break;
		}
	}
	
	//Check if the new index is valid, if true set the selected piece and generate its mt
	//and pass to the selected state, otherwise reset the state and return in waiting
	private void selectPiece(int index) {
		if(index >= 0 && index < Constants.MAX_INDEX && isPlayerPiece(index, this.colorTurn)) {
			lastSelectedIndex = index;
			medusaTreeSelectedIndex = getPossibleMoves(index,this.colorTurn);
				
			gameState = GameState.SELECTED;
		}
		else {
			setWaitingState();
		}
	}
	
	//Check if the new index is a possible move for the selected piece
	//if true update the chessboard, check if the move is valid, update the view and
	//set the state in Moving that wait the view
	private void movePiece(int index) {
		if(index >= 0 && index < Constants.MAX_INDEX && index != this.lastSelectedIndex) {
			
			if(this.medusaTreeSelectedIndex != null && this.medusaTreeSelectedIndex.Contain(index)) {
				
				if(this.chessboard.isKingPiece(this.lastSelectedIndex, this.colorTurn)){
					
					int numberOfOpponentOnDestIndex = this.chessboard.getTile(index).numberOfOpponentPiecesRegisteredOn(this.colorTurn);
					//If the tile is free and no opponent can arrive here, the king can move
					if(numberOfOpponentOnDestIndex == 0){
						//Valid move
						//he tile is free and no opponent can arrive here, the king can move
						//get all opponents pieces index from starting move position
						
						System.out.println("King moves safely");
						
						//update chessboad
						this.chessboard.simulateMovePieceFromStartToEnd(this.lastSelectedIndex, index);
						
						//King could eat a piece. if exist need to deregister from Tile and colorList
						MedusaTree possibleAtePiece = this.chessboard.confirmMovePiece();
						this.deregisterPieceFromTileInMt(possibleAtePiece,index,this.oppositePlayer());
						
						Set<Integer> opponents = this.chessboard.getTile(this.lastSelectedIndex).getColorListRegistered(this.oppositePlayer());
						
						this.SetPieceDestAndValidateMt(index,opponents);
						this.EndValidMove(this.lastSelectedIndex, index);
					}
					else{
						System.out.println("King DOESN'T move safely");
						setWaitingState();
					}
				}
				//Is not the king
				else {
					if(isUnderCheck() <= 1){
						//if was N>1 can only select king
						//if want move king i'm in the wrong branch
						
						//simulate the move
						this.chessboard.simulateMovePieceFromStartToEnd(this.lastSelectedIndex, index);

						//clear old forecastMt
						this.forecastMts.clear();
						
						//get all opponents pieces index from starting move position
						Set<Integer> opponents = this.chessboard.getTile(this.lastSelectedIndex).getColorListRegistered(this.oppositePlayer());
						//check if the moving piece creates new check when leaving the starting position
						if(!this.thereAreNewCheckFromMovingPiece(opponents)){
							//get the index of checking piece
							Set<Integer> checkingPieceSet = this.chessboard.getTile(index).getColorListRegistered(this.oppositePlayer());
							
							//if N = 0 there isn't a checkingPiece
							Iterator<Integer> it;
							int checkingPieceIndex = -1;
							if(checkingPieceSet.size()>0){
								it = checkingPieceSet.iterator();
								checkingPieceIndex = it.next();
							}
							//check if destination is the checking piece
							if(checkingPieceIndex == index){
								//valid move: the moved piece eat the checking piece
								
								System.out.println("NOT king, eats Checking Piece");
								
								//Piece eat a piece. Need to deregister from Tile and colorList
								MedusaTree possibleAtePiece = this.chessboard.confirmMovePiece();
								this.deregisterPieceFromTileInMt(possibleAtePiece,index,this.oppositePlayer());
								
								this.SetPieceDestAndValidateMt(index,opponents);
								this.EndValidMove(this.lastSelectedIndex, index);
							}else{
								if(this.isUnderCheck()==1){
									//get the branch of mt that is between checking piece and king
									MedusaTree mtBranchToKing = this.getBranchToWantedIndex(checkingPieceIndex,this.oppositePlayer(), chessboard.getKing(this.colorTurn));
									//need to check if moving piece intercept the checking one
									if( mtBranchToKing.IsEmpty()){
										//Valid move: the piece moved at least has intercepted the checking piece
										System.out.println("NOT king, intercepts Checking Piece");
										
										//TODO can be removed?
										//Piece could eat a piece checking the king.
										MedusaTree possibleAtePiece = this.chessboard.confirmMovePiece();
										this.deregisterPieceFromTileInMt(possibleAtePiece,index,this.oppositePlayer());
										
										this.SetPieceDestAndValidateMt(index,opponents);
										this.EndValidMove(this.lastSelectedIndex, index);
									}else{
										//Invalid move, checking piece not eat/intercepted
										//register again the moving piece on chessBoard
										System.out.println("NOT king, DOESN'T eat/intercept Checking Piece");
										
										this.chessboard.rollbackMovePiece(this.lastSelectedIndex, index);
										this.setWaitingState();
									}
								}else{
									//Valid moves of a piece (not the king) that doesn't leave the king under check
									System.out.println("NOT king moves without leave king under check");
									
									//Piece could eat an opposite piece. if exist need to deregister from Tile and colorList
									MedusaTree possibleAtePiece = this.chessboard.confirmMovePiece();
									this.deregisterPieceFromTileInMt(possibleAtePiece,index,this.oppositePlayer());
									
									this.SetPieceDestAndValidateMt(index,opponents);
									this.EndValidMove(this.lastSelectedIndex, index);
								}
							}
						}else{
							System.out.println("Moving peace creates a new check");
							//register again the moving piece on chessBoard
							this.chessboard.rollbackMovePiece(this.lastSelectedIndex, index);
							this.setWaitingState();
						}
					}else{
						//There are no possible moves of another piece that can safe the king
						System.out.println("N>1 but king wasn't selected");
						
						setWaitingState();
					}					
				}
			}
			else {
				//Check if is another 
				selectPiece(index);
			}
		}
		else {
			//Deselect the piece
			setWaitingState();
		}
	}

	private void updateTiles(int startIndex, int endIndex)
	{
		// TODO VALE: mossa valida quindi devo aggiornare le tile
		//** si ma quali? (vale)
		
	}

	private void setWaitingState(){
		gameState = GameState.WAITING;
		resetMoveState();
	}
	
	private void resetMoveState(){
		medusaTreeSelectedIndex = null;
		lastSelectedIndex = -1;
	}
	
	private void setMovingState(){
		gameState = GameState.MOVING;
		resetMoveState();
	}
	
	private void setWinState(){
		gameState = GameState.WIN;
		viewComponent.EndGame(oppositePlayer());
	}
	
	private void changePlayerTurn() {
		colorTurn = oppositePlayer();
		if(isUnderCheckmate())
			setWinState();
		setWaitingState();
	}
	
	private int oppositePlayer() {
		return (Constants.MAX_PLAYERS - 1) - colorTurn;
	}
	
	//-----------------------------Chessboard functions
	private boolean isPlayerPiece(int index, int colorTurn)
	{
		boolean isPlayerPiece = false;
		if(index >= 0 && index < Constants.MAX_INDEX)
		{
			if(chessboard.getPiece(index) != null && chessboard.isPieceWhite(index) == colorTurn) {
					isPlayerPiece = true;
			}
		}
		
		return isPlayerPiece;
	}
	
	//Cut the mt of the piece in that index
	//Valid positions are: free positions and enemy positions that the piece can eat
	private MedusaTree getPossibleMoves(int index, int colorPiece)
	{
		MedusaTree reachebleIndices = chessboard.getPossibleMovementIndices(index);
		if(reachebleIndices != null){
			MedusaTree.CompleteIterator mtIterator = reachebleIndices.GetCompleteIterator();
			while(mtIterator.hasNext()) {
				Integer reachebleIndex = mtIterator.next();
				if(chessboard.getPiece(reachebleIndex) != null){
					//If is special (Pawn) then the cut different or is a player piece
					if(chessboard.isPieceSpecial(index) || isPlayerPiece(reachebleIndex, colorPiece)) {
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
			if(chessboard.isPieceSpecial(index)){
	
				MedusaTree eatableIndices = chessboard.getPossibleEatIndices(index);
				MedusaTree.CompleteIterator meEatableIterator = eatableIndices.GetCompleteIterator();
	
				while(meEatableIterator.hasNext()) {
					Integer eatableIndex = meEatableIterator.next();
					if(chessboard.getPiece(eatableIndex) != null && isPlayerPiece(eatableIndex, oppositePlayer())) {//If is an opponent piece
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
	
	//Cut the mt of the piece in that index
	//Valid positions are: free positions, enemy positions and friend positions
	private MedusaTree getPossibleMovesPlusFirstOccupated(int index)
	{
		MedusaTree reachebleIndices = chessboard.getPossibleMovementIndices(index);
		if(reachebleIndices != null){
			MedusaTree.CompleteIterator mtIterator = reachebleIndices.GetCompleteIterator();
			while(mtIterator.hasNext()) {
				Integer reachebleIndex = mtIterator.next();
				//If there is another piece I leave this and cut after
				if(chessboard.getPiece(reachebleIndex) != null){
						mtIterator.CutAfter();	
				}
			}
			if(chessboard.isPieceSpecial(index)){
	
				MedusaTree eatableIndices = chessboard.getPossibleEatIndices(index);
				MedusaTree.CompleteIterator meEatableIterator = eatableIndices.GetCompleteIterator();
	
				while(meEatableIterator.hasNext()) {
					Integer eatableIndex = meEatableIterator.next();
					if(chessboard.getPiece(eatableIndex) != null) {
						meEatableIterator.CutAfter();
					}
				}
	
				reachebleIndices.MergeMedusaTreeNewBanch(eatableIndices);
			}
		}
		return reachebleIndices;
	}
	
	//Return a mt that have as valid only a branch that connect the movingPiece with the wantedIndex
	private MedusaTree getBranchToWantedIndex(int movingPieceIndex,int movingPieceColor,int wantedIndex){
		MedusaTree mt = getPossibleMoves(movingPieceIndex,movingPieceColor);
		CuttableCuttedIterator it = mt.GetCuttableCuttedIterator();
		Boolean kingFind = false;
		while(it.hasNext() && !kingFind){
			if(it.next() == wantedIndex){
				it.CutThisAndAfter();
				it.CutOtherBranches();
				kingFind = true;
			}
		}
		
		if(!kingFind){
			it.CutAllBranches();
		}
		
		return mt;
	}
	
	private void updateTilesUpdateStateUpdateView(int from, int to){
		updateTiles(from,to);
		setMovingState();
		viewComponent.MoveFromStartIndexToEndIndex(from, to);
	}
	
	
	//--------------------------------Private method for game stream------------------------	

	
	//Generate all MT of pieces of color "colorPlayer"
	private void generateAllMtOfColor(int colorPlayer){
		Set<Integer> piecesList;
		Iterator<Integer> it;
		
		piecesList = this.chessboard.getColorList(colorPlayer);
		it = piecesList.iterator();
		while(it.hasNext()){
			int next = it.next();
			this.chessboard.getPiece(next).setMedusaTree(this.getPossibleMovesPlusFirstOccupated(next));
		}
	}
	
	//Register a list of pieces on their own MT
	private void registerPiecesOnTheirMT(Set<Integer> piecesList){
		Iterator<Integer> it = piecesList.iterator();
		while(it.hasNext()){
			int i = it.next();
			System.out.println("Register piece index = " + i);
			this.registerPieceOnHisMT(i,this.chessboard.isPieceWhite(i));
		}
	}
	
	//Register a piece on its own MT
	private void registerPieceOnHisMT(int pieceIndex,int pieceColor){
		CuttedIterator it = this.chessboard.getPiece(pieceIndex).getMedusaTree().GetCuttedIterator();
		while(it.hasNext()){
			int i = it.next();
			System.out.println(pieceIndex+" registering in " +i);
			this.chessboard.getTile(i).registerPiece(pieceIndex,pieceColor);
		}
	}
	
	//for each piece get his own mt and deregister from each tile contained in
	private void deregisterPiecesFromTileInMt(Set<Integer> pieces) {
		Iterator<Integer> it = pieces.iterator();
		while(it.hasNext()){
			int i = it.next();
			System.out.println("Deregister piece index = " + i);
			this.deregisterPieceFromTileInMt(i,this.chessboard.isPieceWhite(i));
		}
	}
	
	//deregister a piece from the tile of his own mt
	private void deregisterPieceFromTileInMt(int pieceIndex,int pieceColor) {
		this.deregisterPieceFromTileInMt(this.chessboard.getMedusaTree(pieceIndex), pieceIndex, pieceColor);
	}
	
	private void deregisterPieceFromTileInMt(MedusaTree confirmMovePiece,int index,int color) {
		CuttedIterator it = confirmMovePiece.GetCuttedIterator();
		while(it.hasNext()){
			int tileIndex = it.next();
			System.out.println(index+" deregistering from " +tileIndex);
			this.chessboard.getTile(tileIndex).unregisterPiece(index,color);
		}
	}
	
	//update of all foreast mt
	private void updateForecastMt(){
		Set<Integer> keys = this.forecastMts.keySet();
		Iterator<Integer> it = keys.iterator();
		while(it.hasNext()){
			int pieceIndex = it.next();
			this.chessboard.getPiece(pieceIndex).setMedusaTree(this.forecastMts.get(pieceIndex));
		}
	}
	
	//check if king of ColorTurn is under checkMate
	private boolean isUnderCheckmate(){
		boolean result = false;
		
		//get the king pos
		int kingPos = this.chessboard.getKing(this.colorTurn);
		
		int numberOfOpponent = this.chessboard.getTile(kingPos).numberOfOpponentPiecesRegisteredOn(this.colorTurn);
		//check if there is an opponent piece registered on the kingPos
		if(numberOfOpponent >0 && !this.searchKingAdjacentSafe()){
			//if N = 1 it's possible eat/intercept the opponent piece checking the king
			if(numberOfOpponent != 1 || !this.searchDistantSafeMoves()){
				result = true;
			}
		}
		//NO checkMate on the king
		return result;
	}
	
	//Check if king of color turn is under check
	private int isUnderCheck(){
		//get the king pos
		int kingPos = this.chessboard.getKing(this.colorTurn);
		return this.chessboard.getTile(kingPos).numberOfOpponentPiecesRegisteredOn(this.colorTurn);
	}
	
	//return the index of valid tiles around the king where he can moves
	//TODO: se da cambiare con il nuovo algoritmo potrebbe tornare un int soltanto
	private boolean searchKingAdjacentSafe(){
		boolean existValidMove = false;
		
		//get the king mt
		MedusaTree kingMt = this.getPossibleMoves(this.chessboard.getKing(this.colorTurn),this.colorTurn);
		
		//for each leaf gets the tile and checks how many opponents are registered on
		CuttedIterator it = kingMt.GetCuttedIterator();
		
		while(it.hasNext() && !existValidMove){
			int leafIndex = it.next();
			
			//checking if covering pieces exist
			int numOpponentsRegisteredOnLeaf = this.chessboard.getTile(leafIndex).numberOfOpponentPiecesRegisteredOn(this.colorTurn);
			
			//no covering pieces or tile empty
			if(numOpponentsRegisteredOnLeaf == 0){
				
				existValidMove = true;
				
//				//if is exactly one, king can eat if the opponent piece is there
//				if(numOpponentsRegisteredOnLeaf == 1){
//					
//					//Only one Opponent expected
//					Set<Integer> registeredOpponents = this.chessboard.getTile(leafIndex).getColorListRegistered(oppositePlayer());					
//					if(registeredOpponents.contains(leafIndex)){
//						kingValidMoves.add(leafIndex);	
//					}
//				}
			}
//			else{
				
				
//				//check if there is a friend
//				Set<Integer> registeredFriends = this.chessboard.getTile(leafIndex).getColorListRegistered(this.colorTurn);
//				if(registeredFriends.size() == 0 ){
//					kingValidMoves.add(leafIndex);
//				}
//			}
		}
		
		return existValidMove;
	}
	
	//return the index of valid tiles usefull to block the check
	//TODO: se da cambiare con il nuovo algoritmo potrebbe tornare un int soltanto
	private boolean searchDistantSafeMoves(){
		boolean existValidMove = false;
		
		int kingIndex = this.chessboard.getKing(this.colorTurn);
		
		//Only one Opponent expected
		Set<Integer> registeredOpponents = this.chessboard.getTile(kingIndex).getColorListRegistered(oppositePlayer());
		
		//found opponentIndex
		int opponentIndex;
		Iterator<Integer> it = registeredOpponents.iterator();
		opponentIndex = it.next();
		
		//TODO aggiungere check sse qualcuno pu� mangiare chi scacca
		Set<Integer> pieceEatingCheckingPiece = this.chessboard.getTile(opponentIndex).getColorListRegistered(this.colorTurn);
		if(pieceEatingCheckingPiece.size() >0 ){
			existValidMove = true;
		}
		
		//found the index of tiles between the rival and the king
		MedusaTree mtBetweenOpponentAndKing = this.getBranchToWantedIndex(opponentIndex,this.oppositePlayer(),kingIndex);
		CuttedIterator it2 = mtBetweenOpponentAndKing.GetCuttedIterator();
		while(it.hasNext() && !existValidMove){
			int tempoPos = it2.next();
			//if the tile in that index contains a friend piece, that piece can move and stop king's check
			if(this.chessboard.getTile(tempoPos).getColorListRegistered(colorTurn).size() > 0){
				existValidMove = true;
			}
		}
				
		return existValidMove;
	}
	
	private boolean thereAreNewCheckFromMovingPiece(Set<Integer> opponentPiecesIndexOnStartingMovePosition){
		//for each opponent registered on starting index position
		boolean newCheck = false;
		Iterator<Integer> it = opponentPiecesIndexOnStartingMovePosition.iterator();
		while(it.hasNext() && newCheck){
			int oppositeIndex = it.next();
			//create the forecast mt
			MedusaTree forecastMt = this.getPossibleMovesPlusFirstOccupated(oppositeIndex);
			//check if forecast mt contain king position
			if(forecastMt.Contain(this.chessboard.getKing(this.colorTurn))){
				newCheck = true;
				//break, new check on king, is it valid either N = 0 either N = 1
			}else{
				//add forecast mt on hashmap, will be used later or deleted
				this.forecastMts.put(oppositeIndex, forecastMt);
			}
		}
		return newCheck;
	}
	
	private void SetPieceDestAndValidateMt(int destIndex,Set<Integer> opponents){
		
		//TODO Bug1 piece moved so start is empty and must valorize piece with new index and new mt
		//bug1 fix
		
		this.chessboard.simulateMovePieceFromStartToEnd(destIndex, this.lastSelectedIndex);
		this.deregisterPieceFromTileInMt(this.lastSelectedIndex, this.chessboard.isPieceWhite(this.lastSelectedIndex));
		this.chessboard.simulateMovePieceFromStartToEnd(this.lastSelectedIndex, destIndex);
		MedusaTree forecastPieceMoved = this.getPossibleMovesPlusFirstOccupated(destIndex);
		this.chessboard.getPiece(destIndex).setMedusaTree(forecastPieceMoved);
		this.registerPieceOnHisMT(this.chessboard.getPiece(destIndex).getIndex(), this.chessboard.getPiece(destIndex).isWhite());
		//bug1 fix
		
		//get friend pieces on starting index 
		Set<Integer> temp = this.chessboard.getTile(this.lastSelectedIndex).getColorListRegistered(this.colorTurn);
		//get friend pieces on dest index
		temp.addAll(this.chessboard.getTile(destIndex).getColorListRegistered(this.colorTurn));
		//get opposite pieces registered on dest index
		temp.addAll(this.chessboard.getTile(destIndex).getColorListRegistered(this.oppositePlayer()));
		
		Set<Integer> involvedPieces = new HashSet<Integer>(temp);
		
		//bug1 Fix
		involvedPieces.remove(this.lastSelectedIndex);
		//bug1 fix
		
		Iterator<Integer> it = involvedPieces.iterator();
		
		while(it.hasNext()){
			int involvedPieceIndex = it.next();
			this.forecastMts.put(involvedPieceIndex, this.getPossibleMovesPlusFirstOccupated(involvedPieceIndex));
		}
		
		//merge also the list of opponent on starting index with the list of piece involved
		involvedPieces.addAll(opponents);
		
		System.out.println("PieceInvolved :" +involvedPieces.size());
		
		//deregister all piece from their tile using old MT
		if(involvedPieces.size() > 0){
			this.deregisterPiecesFromTileInMt(involvedPieces);
		}
		
		//update old mt with forecast ones
		this.updateForecastMt();
		
		//register all pieces using their new MT
		if(involvedPieces.size() > 0){
			this.registerPiecesOnTheirMT(involvedPieces);
		}
		
	}
	
	private void EndValidMove(int startIndex,int endIndex){
		//cambio stato in MOVING e turnColor
		this.setMovingState();
		viewComponent.MoveFromStartIndexToEndIndex(startIndex, endIndex);
	}
	 
	
	//--------------------------------Private method for game stream------------------------
	
	
	
	//-----------------------------Private functions----------------------------------------
}
