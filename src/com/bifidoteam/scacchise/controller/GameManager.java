package com.bifidoteam.scacchise.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.bifidoteam.scacchise.interfaces.ControllerInterface;
import com.bifidoteam.scacchise.interfaces.ViewInterface;
import com.bifidoteam.scacchise.model.Chessboard;
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
	public MedusaTree GetReachebleIndicesDebug(int index, int whiteTurn)
	{
		this.colorTurn = whiteTurn;
		return GetPossibleMoves(index);
	}
	//-----------------------------Debug functions -----------------------------------------
	
	//-----------------------------Controller Interface functions---------------------------
//	@Override
//	public void OnClick(int index) {
//		//TODO: Vedere cosa farne
//	}
	
	//TODO: Rivedere il reset con ricky
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
	
	public void GameLoop(){
		viewComponent.Init(chessboard);
		
		while(gameState != GameState.QUIT){
			
			int indexChosen = viewComponent.GetInput();
			
			ManageInput(indexChosen);

			viewComponent.Render(medusaTreeSelectedIndex);
		}
		
	}
	//-----------------------------Controller Interface functions---------------------------
	
	//-----------------------------Private functions----------------------------------------

	void ManageInput(int index){
		switch (gameState) {
			case WAITING:
				SelectPiece(index);
				break;
			case SELECTED:
				MovePiece(index);
				break;
			case WIN:
				//TODO: Dichiarare il vincitore **** LASCIARE IL COLORE CHE VINCE **** 
				viewComponent.EndGame(colorTurn);
				//ed attendere il reset
			//When is in MOVING state the GameManager wait the callback function OnMoveDone
			default:
				break;
		}
	}
	
	//Check if the new index is valid, if true set the selected piece and generate its mt
	//and pass to the selected state, otherwise reset the state and return in waiting
	private void SelectPiece(int index) {
		if(index >= 0 && index < Constants.MAX_INDEX && IsPlayerPiece(index)) {
			lastSelectedIndex = index;
			medusaTreeSelectedIndex = GetPossibleMoves(index);
				
			gameState = GameState.SELECTED;
		}
		else {
			SetWaitingState();
		}
	}
	
	//Check if the new index is a possible move for the selected piece
	//if true update the chessboard, check if the move is valid, update the view and
	//set the state in Moving that wait the view
	private void MovePiece(int index) {
		if(index >= 0 && index < Constants.MAX_INDEX && index != lastSelectedIndex) {
			if(medusaTreeSelectedIndex != null && medusaTreeSelectedIndex.Contain(index)) {
				
				//TODO**** MARCO: vuoi dilettarti con questa parte?
				//if( è stato selezionato il re)
				//devo controllare se sia stato selezionato il re ( lui si potrebbe spostare)
				
				//**questo controllo lo mettiamo prima a prescindere dall'N cosi facciamo questa cosa una
				//**volta sola (anche prima del controllo se e' sotto scacco)
				//****Si ci ottimizza!
				//****Se non è sotto scacco e voglio muovere il re devo controllare il suo MT,
				//****Solo che l'mt va tagliato anche con le tile che hanno un qualsiasi opponent registrato
				//****la destinazione è in quel che resta l'mt?
					//if(KingMt.Contains(destIndex)){
						
						//se N>1 e non ho selezionato il re la mossa
						//è sicuramente invalida
						//ma anche che fosse N=1, dal momento che ho selezionato il re
						
						//calcolo gli MT di tutti i registrati sulla tile di partenza
						//calcolo gli MT di tutti i registrati sulla dest
						//deregistro sulle tile dai vecchi MT
						//registro i pezzi sulle tile coerentemente con i nuovi mt
						////SetMovingState (cambio stato in MOVING e turnColor)
					//}
					//else{
						//Entrambe confluiscono qui
						//SetWaitingState (cambio stato in WAITING per una nuova selezione di pezzo.) 
						//**TODO notifichiamo al giocatore? (dovremmo ma ora non abbiamo il modo)
					//}
				//}
				
				//TODO:Vale
				//So di avere ancora mosse perchè se fosse stato N>1 dovevo avere selezionato il re
				//sono al più in N=1 perchè altrimenti sarebbe scaccoMatto!
				//se avessi voluto muovere il re non sarei in questo branch
				//
				//else(pezzo selezionato non è il re){
					//Metto a null la posizione del pezzo registrato sulla chessboard e controllo se nuovi pezzi sccaccano
					//Per Ogni Opponent registrato sulla vechia tile[lastIndexSelected]:
					//calcolo MTipotetico e lo salvo in una hashmap<index,mtIpotetico>
					//IF(MTIpotetico contiene re){
						//annulla mossa, ora un nuovo pezzo scacca il re, e vale sia per N = 0 che N = 1
						//registra di nuovo il pezzo su chessBoard[lastTileSelected]
						//cambia stato e torna a selezione
					//}else{
						//OpponentIndex = l'index del pezzo che scacca il re ( lo sa' dalla tile del re)
						//If( destIndex == opponentIndex){
							//mossa:valida mangia il pezzo che scacca il re (che non dovrebbe essere piu' in scacco)
							//setto in chessBoard la dest del pezzo
							//calcolo gli MT degli amici registrati sulla tile di partenza
							//calcolo gli MT di tutti i registrati sulla dest
							//deregistro sulle tile dai vecchi MT
							//registro tutti i pezzi coinvolti coerentemente con i nuovi mt
							//SetMovingState (cambio stato in MOVING e turnColor)
							//forse ci viene già con il ramo else....se il pezzo è registrato sulla tile dove sta si.
						//}else{
								//Mi basta controllare che ho intercettato chi scacca
								//IF( la tile[destIndex].getOpponents().contains(opponentIndex)){
									//mossa valida: il pezzo ha perlomeno intercettato il pezzo scaccante
									//setto in chessBoard la dest del pezzo
									//calcolo gli MT degli amici registrati sulla tile di partenza
									//calcolo gli MT di tutti i registrati sulla dest
									//deregistro sulle tile dai vecchi MT
									//registro tutti i pezzi coinvolti coerentemente con i nuovi mt
									////SetMovingState (cambio stato in MOVING e turnColor)
								//}else{
									//annulla mossa, pezzo scaccante non mangiato/intercettato
									//registra di nuovo il pezzo su chessBoard[lastTileSelected]
									//cambia stato e torna a selezione
								//}
						//}
					//}
				
				//Lascio per vedere il movimento
				chessboard.MovePieceFromStartIndexToEndIndex(lastSelectedIndex, index);
				
				//Se la mossa e' valida continuo
				ResetMoveState();
				gameState = GameState.MOVING;
				
				viewComponent.MoveFromStartIndexToEndIndex(lastSelectedIndex, index);

			}
			else {
				//Check if is another 
				SelectPiece(index);
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
	
	private void SetMovingState(){
		gameState = GameState.MOVING;
		ResetMoveState();
	}
	
	private void ChangePlayerTurn() {
		colorTurn = OppositePlayer();
		//TODO: vedi che isUnderChekmate è un boolean, se torna true allora l'altro ha vinto!
		isUnderCheckmate();
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
	
	//Cut the mt of the piece in that index
	//Valid positions are: free positions and enemy positions that the piece can eat
	private MedusaTree GetPossibleMoves(int index)
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
					if(chessboard.GetPiece(eatableIndex) != null && chessboard.IsPieceWhite(eatableIndex) == OppositePlayer()) {//If is an opponent piece
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
	private MedusaTree GetPossibleMovesPlusFirstOccupated(int index)
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
	
	//Return a mt that have as valid only a branch that connect the movingPiece with the wantedIndex
	private MedusaTree GetBranchToWantedIndex(int movingPieceIndex,int wantedIndex){
		MedusaTree mt = GetPossibleMoves(movingPieceIndex);
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
	
	
	//--------------------------------Private method for game stream------------------------	

	
	//Generate all MT of pieces of color "colorPlayer"
	private void generateAllMtOfColor(int colorPlayer){
		Set<Integer> piecesList;
		Iterator<Integer> it;
		
		piecesList = this.chessboard.getColorList(colorPlayer);
		it = piecesList.iterator();
		while(it.hasNext()){
			int next = it.next();
			this.chessboard.GetPiece(next).setMedusaTree(this.GetPossibleMovesPlusFirstOccupated(next));
		}
	}
	
	//Generate MT of a piece
	private void generateMt(int pieceIndex){
		this.chessboard.GetPiece(pieceIndex).setMedusaTree(this.GetPossibleMovesPlusFirstOccupated(pieceIndex));
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
			this.registerPieceOnHisMT(i,this.chessboard.IsPieceWhite(i));
		}
	}
	
	//Register a piece on its own MT
	private void registerPieceOnHisMT(int pieceIndex,int pieceColor){
		CuttedIterator it = this.chessboard.GetPiece(pieceIndex).getMedusaTree().GetCuttedIterator();
		while(it.hasNext()){
			this.chessboard.getTile(it.next()).registerPiece(pieceIndex,pieceColor);
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
		MedusaTree kingMt = this.GetPossibleMoves(this.chessboard.getKing(this.colorTurn));
		
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
	//TODO: se da cambiare con il nuovo algoritmo potrebbe tornare un int soltanto
	private List<Integer> searchDistantSafeMoves(){
		//list of valid moves
		LinkedList<Integer> validMoves = new LinkedList<Integer>();
		
		int kingIndex = this.chessboard.getKing(this.colorTurn);
		
		//Only one Opponent expected
		Set<Integer> registeredOpponents = this.chessboard.getTile(kingIndex).getColorListRegistered(OppositePlayer());
		
		//found opponentIndex
		int opponentIndex;
		Iterator<Integer> it = registeredOpponents.iterator();
		opponentIndex = it.next();
		
		//found the index of tiles between the rival and the king
		MedusaTree mtBetweenOpponentAndKing = this.GetBranchToWantedIndex(opponentIndex,kingIndex);
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
	
	
	private void ValidateForecastMts(){
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
