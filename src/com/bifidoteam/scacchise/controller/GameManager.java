package com.bifidoteam.scacchise.controller;

import java.util.HashMap;
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
		return getPossibleMoves(index);
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
			medusaTreeSelectedIndex = getPossibleMoves(index);
				
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
					
					int numberOfOpponent = this.chessboard.getTile(index).numberOfOpponentPiecesRegisteredOn(this.colorTurn);
					//If the tile is free and no opponent can arrive here, the king can move
					if(numberOfOpponent == 0){
						chessboard.movePieceFromStartIndexToEndIndex(this.lastSelectedIndex, index);
						updateTilesUpdateStateUpdateView(this.lastSelectedIndex, index);
					}
					else{
						setWaitingState();
					}
				}
				//Is not the king
				else {
					if(isUnderCheck() <= 1){
						//So di avere ancora mosse perche' se fosse stato N>1 dovevo avere selezionato il re
						//sono al piu' in N=1 perche' altrimenti sarebbe scaccoMatto!
						//se avessi voluto muovere il re non sarei in questo branch
						
						//Salvo il pezzo temporaneamente.
						//se lo salvassi dentro chessboard non romperei la separazione controller-gameManager/model-chessboard
						//ma non è una merda?
						Piece piece = this.chessboard.getPiece(this.lastSelectedIndex);
						//Metto a null la posizione del pezzo registrato sulla chessboard;
						this.chessboard.setPiece(this.lastSelectedIndex, null);
						
						//controllo se nuovi pezzi sccaccano dalla vecchia pos.
						this.forecastMts.clear();
						
						//Per Ogni Opponent registrato sulla vechia tile[lastIndexSelected]:
						boolean contin = true;
						Set<Integer> opponents = this.chessboard.getTile(this.lastSelectedIndex).getColorListRegistered(this.oppositePlayer());
						Iterator<Integer> it = opponents.iterator();
						while(it.hasNext() && contin){
							int oppositeIndex = it.next();
							//calcolo MTipotetico e lo salvo in hashmap
							MedusaTree forecastMt = this.getPossibleMovesPlusFirstOccupated(oppositeIndex);
							if(forecastMt.Contain(this.chessboard.getKing(this.colorTurn))){
								contin = false;
								//annulla mossa, ora un nuovo pezzo scacca il re, e vale sia per N = 0 che N = 1
								//registra di nuovo il pezzo su chessBoard
								this.chessboard.setPiece(this.lastSelectedIndex, piece);
								//cambia stato e torna a selezione
								this.setWaitingState();
							}else{
								//aggiungo l'mt ipotetico,potrei usarlo dopo per la nuova configurazione sulle tile
								this.forecastMts.put(oppositeIndex, forecastMt);
							}
						}
						if(contin){
							//Prendo l'indice del pezzo scaccante
							Set<Integer> checkingPieceSet = this.chessboard.getTile(index).getColorListRegistered(this.oppositePlayer());
							it = checkingPieceSet.iterator();
							int checkingPieceIndex = it.next();
							
							//controllo se la destinazione è il pezzo che scacca
							if(checkingPieceIndex == index){
								//mossa:valida mangia il pezzo che scacca il re (che non dovrebbe essere piu' in scacco)
								
								//prendo la lista degli avversari e cancello il pezzo
								this.chessboard.getColorList(this.oppositePlayer()).remove(index);
								
								
								//TODO dubbio x Marco:  avendolo settato prima a null con lo stesso metodo che ho aggiunto
								//chiamo dopo il tuo metodo? mi sembra che faccia la stessa cosa
								//this.chessboard.movePieceFromStartIndexToEndIndex(this.lastSelectedIndex, index);
								//setto in chessBoard la dest del pezzo, cancella il pezzo avversario da chessboard
								this.chessboard.setPiece(index, piece); 
								
								//Prendo gli amici sulla tile di partenza
								Set<Integer> friendsOnStartIndex = this.chessboard.getTile(this.lastSelectedIndex).getColorListRegistered(this.colorTurn);
								
								//calcolo gli MT degli amici registrati sulla tile di partenza
								//TODO dubbio x Marco: devo rifare anche quelli dei nemici che erano sulla tile di partenza?
								//sembrerebbe che dalla tile di partenza con il pezzo già a null l'mt dei nemici sia giusto.
								it = friendsOnStartIndex.iterator();
								while(it.hasNext()){
									int friendIndex = it.next();
									this.forecastMts.put(friendIndex, this.getPossibleMovesPlusFirstOccupated(friendIndex));
								}
								//calcolo gli MT di tutti i pezzi amici registrati sulla dest
								Set<Integer> friendsOnDestIndex = this.chessboard.getTile(index).getColorListRegistered(colorTurn);
								it = friendsOnDestIndex.iterator();
								while(it.hasNext()){
									int friendIndex = it.next();
									this.forecastMts.put(friendIndex, this.getPossibleMovesPlusFirstOccupated(friendIndex));
								}
								//calcolo gli MT di tutti i pezzi nemii registrati sulla dest
								Set<Integer> opponentsOnDestIndex = this.chessboard.getTile(index).getColorListRegistered(this.oppositePlayer());
								it = opponentsOnDestIndex.iterator();
								while(it.hasNext()){
									int enemyIndex = it.next();
									this.forecastMts.put(enemyIndex, this.getPossibleMovesPlusFirstOccupated(enemyIndex));
								}
								
								//Unsco i set dovendo fare tutte le liste le stesse operazioni
								opponents.addAll(opponentsOnDestIndex);
								opponents.addAll(friendsOnStartIndex);
								opponents.addAll(friendsOnDestIndex);
								
								//deregistro dalle tile usando i vecchi MT
								if(opponents.size() > 0){
									this.deregisterPiecesFromTileInMt(opponents);
								}
								
								//aggiorno gli mt vecchi con i nuovi:
								this.updateForecastMt();
								
								//registro tutti i pezzi coinvolti coerentemente con i nuovi mt
								if(opponents.size() > 0){
									this.registerPiecesOnTheirMT(opponents);
								}
								
								//cambio stato in MOVING e turnColor
								this.setMovingState();
							}else{
								if(this.isUnderCheck()==1){
									//Mi basta controllare che ho intercettato chi scacca
									//Ritorna il mt con solo il ramo verso il re
//									MedusaTree mt = this.GetBranchToWantedIndex(checkingPieceIndex, chessboard.getKing(this.colorTurn));
									//IF( la tile[destIndex].getOpponents().contains(opponentIndex)){
										//mossa valida: il pezzo ha perlomeno intercettato il pezzo scaccante
										//setto in chessBoard la dest del pezzo
										//calcolo gli MT degli amici registrati sulla tile di partenza
										//calcolo gli MT di tutti i registrati sulla dest
										//deregistro sulle tile dai vecchi MT
										//registro tutti i pezzi coinvolti coerentemente con i nuovi mt
										////SetMovingState (cambio stato in MOVING e turnColor)
//									}else{
//										//annulla mossa, pezzo scaccante non mangiato/intercettato
//										//registra di nuovo il pezzo su chessBoard[lastTileSelected]
//										this.chessboard.setPiece(this.lastSelectedIndex, piece);
//										//cambia stato e torna a selezione
//										this.setWaitingState();
//									}
								}else{
									//non devo fare il controllo
									//mossaValida di un pezzo qualsiasi che non lascia il re sotto scacco
								}
							}
						}else{
							this.setWaitingState();
						}
					}else{
						//There are no possible moves of another piece that can safe the king
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
		//TODO: vedi che isUnderChekmate e' un boolean, se torna true allora l'altro ha vinto!
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
	private MedusaTree getPossibleMoves(int index)
	{
		MedusaTree reachebleIndices = chessboard.getPossibleMovementIndices(index);
		if(reachebleIndices != null){
			MedusaTree.CompleteIterator mtIterator = reachebleIndices.GetCompleteIterator();
			while(mtIterator.hasNext()) {
				Integer reachebleIndex = mtIterator.next();
				if(chessboard.getPiece(reachebleIndex) != null){
					//If is special (Pawn) then the cut different or is a player piece
					if(chessboard.isPieceSpecial(index) || isPlayerPiece(reachebleIndex, this.colorTurn)) {
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
	private MedusaTree getBranchToWantedIndex(int movingPieceIndex,int wantedIndex){
		MedusaTree mt = getPossibleMoves(movingPieceIndex);
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
	
	//Generate MT of a piece
	private void generateMt(int pieceIndex){
		this.chessboard.getPiece(pieceIndex).setMedusaTree(this.getPossibleMovesPlusFirstOccupated(pieceIndex));
	}
	
	//Generate MTs of a list of pieces
	private void generatePiecesMt(LinkedList<Integer> piecesList){
		Iterator<Integer> it = piecesList.iterator();
		while(it.hasNext()){
			this.generateMt(it.next());
		}
	}
	
	//Register a list of pieces on their own MT
	private void registerPiecesOnTheirMT(Set<Integer> piecesList){
		Iterator<Integer> it = piecesList.iterator();
		while(it.hasNext()){
			int i = it.next();
			this.registerPieceOnHisMT(i,this.chessboard.isPieceWhite(i));
		}
	}
	
	//Register a piece on its own MT
	private void registerPieceOnHisMT(int pieceIndex,int pieceColor){
		CuttedIterator it = this.chessboard.getPiece(pieceIndex).getMedusaTree().GetCuttedIterator();
		while(it.hasNext()){
			this.chessboard.getTile(it.next()).registerPiece(pieceIndex,pieceColor);
		}
	}
	
	//for each piece get his own mt and deregister from each tile contained in
	public void deregisterPiecesFromTileInMt(Set<Integer> pieces) {
		Iterator<Integer> it = pieces.iterator();
		while(it.hasNext()){
			int i = it.next();
			this.deregisterPieceFromTileInMt(i,this.chessboard.isPieceWhite(i));
		}
	}
	
	//deregister a piece from the tile of his own mt
	public void deregisterPieceFromTileInMt(int pieceIndex,int pieceColor) {
		CuttedIterator it = this.chessboard.getPiece(pieceIndex).getMedusaTree().GetCuttedIterator();
		while(it.hasNext()){
			this.chessboard.getTile(it.next()).unregisterPiece(pieceIndex,pieceColor);
		}
	}
	
	//update of all foreast mt
	public void updateForecastMt(){
		Set<Integer> keys = this.forecastMts.keySet();
		Iterator<Integer> it = keys.iterator();
		while(it.hasNext()){
			int pieceIndex = it.next();
			this.chessboard.getPiece(pieceIndex).setMedusaTree(this.forecastMts.get(pieceIndex));
		}
	}
	
	//check if king of ColorTurn is under checkMate
	private boolean isUnderCheckmate(){
		//get the king pos
		int kingPos = this.chessboard.getKing(this.colorTurn);
		
		int numberOfOpponent = this.chessboard.getTile(kingPos).numberOfOpponentPiecesRegisteredOn(this.colorTurn);
		//check if there is an opponent piece registered on the kingPos
		if(numberOfOpponent >0){
			LinkedList<Integer> validMoves = new LinkedList<Integer>();
			
			//add king's valid moves
			validMoves.addAll(this.searchKingAdjacentSafe());
			
			//if N = 1 it's possible eat/intercept the opponent piece checking the king
			if(numberOfOpponent == 1){
				validMoves.addAll(this.searchDistantSafeMoves());
			}
			
			if(validMoves.size() == 0){
				return true;
			}
		}
		//NO checkMate on the king
		return false;
	}
	
	//Check if king of color turn is under check
	private int isUnderCheck(){
		//get the king pos
		int kingPos = this.chessboard.getKing(this.colorTurn);
		return this.chessboard.getTile(kingPos).numberOfOpponentPiecesRegisteredOn(this.colorTurn);
	}
	
	//return the index of valid tiles around the king where he can moves
	//TODO: se da cambiare con il nuovo algoritmo potrebbe tornare un int soltanto
	private List<Integer> searchKingAdjacentSafe(){
		//list of valid moves for the king
		LinkedList<Integer> kingValidMoves = new LinkedList<Integer>();
		
		//get the king mt
		MedusaTree kingMt = this.getPossibleMoves(this.chessboard.getKing(this.colorTurn));
		
		//for each leaf gets the tile and checks how many opponents are registered on
		CuttedIterator it = kingMt.GetCuttedIterator();
		
		while(it.hasNext()){
			int leafIndex = it.next();
			int numOpponentsRegisteredOnLeaf = this.chessboard.getTile(leafIndex).numberOfOpponentPiecesRegisteredOn(this.colorTurn);
			
			//at least on enemy is registered on that tile
			if(numOpponentsRegisteredOnLeaf > 0){
				//if is exactly one, king can eat if the opponent piece is there
				if(numOpponentsRegisteredOnLeaf == 1){
					
					//Only one Opponent expected
					Set<Integer> registeredOpponents = this.chessboard.getTile(leafIndex).getColorListRegistered(oppositePlayer());					
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
	//TODO: se da cambiare con il nuovo algoritmo potrebbe tornare un int soltanto
	private List<Integer> searchDistantSafeMoves(){
		//list of valid moves
		LinkedList<Integer> validMoves = new LinkedList<Integer>();
		
		int kingIndex = this.chessboard.getKing(this.colorTurn);
		
		//Only one Opponent expected
		Set<Integer> registeredOpponents = this.chessboard.getTile(kingIndex).getColorListRegistered(oppositePlayer());
		
		//found opponentIndex
		int opponentIndex;
		Iterator<Integer> it = registeredOpponents.iterator();
		opponentIndex = it.next();
		
		//found the index of tiles between the rival and the king
		MedusaTree mtBetweenOpponentAndKing = this.getBranchToWantedIndex(opponentIndex,kingIndex);
		CuttedIterator it2 = mtBetweenOpponentAndKing.GetCuttedIterator();
		while(it.hasNext()){
			int tempoPos = it2.next();
			//if the tile in that index contains a friend piece, that piece can move and stop king's check
			if(this.chessboard.getTile(tempoPos).getColorListRegistered(colorTurn).size() > 0){
				validMoves.add(tempoPos);
			}
		}
				
		return validMoves;
	}
	
	
	private void validateForecastMts(){
		Set<Integer> keys = this.forecastMts.keySet();
		Iterator<Integer> it = keys.iterator();
		while(it.hasNext()){
			int key = it.next();
			this.chessboard.setPieceMt(key,this.forecastMts.get(key));
		}
	}
	 
	
	//--------------------------------Private method for game stream------------------------
	
	
	
	//-----------------------------Private functions----------------------------------------
}
