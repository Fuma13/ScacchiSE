package com.bifidoteam.scacchise.model;

import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

import java.util.HashSet;
import java.util.Set;

public class Chessboard
{
	//-----------------------------Private Variables----------------------------------------
	private Piece[] chessboard = new Piece[Constants.MAX_INDEX];
	
	private Tile[] tiles = new Tile[Constants.MAX_INDEX];
	
	//Liste dei soli pezzi bianchi e neri.
	private HashSet<Integer> [] pieces;
	
	//TODO VALE: facciamo la verifica quando muoviamo che siamo un re ed aggiorniamo la pos o metto i piece che si portano la pos?
	private Piece[] kingPos;	
	//-----------------------------Private Variables----------------------------------------
	
	//-----------------------------Public functions-----------------------------------------
	public Chessboard() {
		//Init tiles
		this.InizializeTiles();
		
		//init the sets
		this.pieces = new HashSet[Constants.MAX_PLAYERS];
		this.pieces[0] = new HashSet<Integer>();
		this.pieces[1] = new HashSet<Integer>();
		
		//populates chessboard with piece and null, populates colored lists 
		InizializeChessboard();
	}
	
	
	public MedusaTree GetRealIndices(int index) {
		if(index >= 0 && index < Constants.MAX_INDEX && chessboard[index] != null) {
			return chessboard[index].GetReachableIndices();
		}
		return null;
	}
	
	public MedusaTree GetEatableIndices(int index) {
		if(IsPieceSpecial(index)) {
			return ((PawnInterface)chessboard[index]).GetEatableIndices();
		}
		return null;
	}
	
	public boolean IsPieceSpecial(int index){
		if(index >= 0 && index < Constants.MAX_INDEX && chessboard[index] != null && chessboard[index] instanceof PawnInterface) {
			return true;
		}
		return false;
	}
	
	//Assume that index >= 0 && index < Constants.MAX_INDEX && chessboard[index] != null
	public int IsPieceWhite(int index) {
		if(chessboard[index] == null)
			throw new NullPointerException("Try to get color of empty position");
		
		return chessboard[index].isWhite();
	}
	
	public Piece GetPiece(int index) {
		if(index >= 0 && index < Constants.MAX_INDEX)
			return chessboard[index];
		else
			return null;
	}
	
	public boolean MovePieceFromStartIndexToEndIndex(int startIndex, int endIndex) {
		if(startIndex >= 0 && startIndex < Constants.MAX_INDEX && endIndex >= 0 && endIndex < Constants.MAX_INDEX) {
			chessboard[endIndex] = chessboard[startIndex];
			chessboard[endIndex].index = endIndex;
			chessboard[startIndex] = null;
			return true;
		}	
		return false;
	}
	
	//return the list of pieces of color "Color"
	public Set<Integer> getColorList(int Color){
		return this.pieces[Color];
	}
	//add a piece in the list of his own color
	public void colorListAddPiece(int pieceIndex,int pieceColor){
		if(!this.pieces[pieceColor].contains(pieceIndex)){
			this.pieces[pieceColor].add(pieceIndex);
		}
	}
	
	public Tile getTile(int index){
		return this.tiles[index];
	}
	
	public int getKing(int colorPlayer){
		return this.kingPos[colorPlayer].getIndex();
	}
	
	//if you call setKing, it get the piece from the chessboard[index]
	public void setKing(int colorPlayer,int index){
		this.kingPos[colorPlayer] = this.chessboard[index];
	}
	
	//TODO: vale: aggiorniamo l'mt del pezzo soltanto? o dobbiamo aggiornare altre cose?
	public void setPieceMt(int pieceIndex,MedusaTree newMt){
		this.chessboard[pieceIndex].setMedusaTree(newMt);
	}
	
	//-----------------------------Public functions-----------------------------------------
	
	//-----------------------------Private functions----------------------------------------
	private void InizializeChessboard() {
		//Set initial king position, change manually to switch from standard initialization
		this.kingPos = new Piece [Constants.MAX_PLAYERS];
		
		//Black Pieces
		chessboard[0] = new Tower(0, Constants.BLACK);
		chessboard[1] = new Horse(1, Constants.BLACK);
		chessboard[2] = new Bishop(2, Constants.BLACK);
		chessboard[3] = new Queen(3, Constants.BLACK);
		chessboard[4] = new King(4, Constants.BLACK);
		this.kingPos[Constants.BLACK] = this.chessboard[4];
		chessboard[5] = new Bishop(5, Constants.BLACK);
		chessboard[6] = new Horse(6, Constants.BLACK);
		chessboard[7] = new Tower(7, Constants.BLACK);
		this.colorListAddPiece(this.chessboard[0].getIndex(),this.chessboard[0].isWhite());
		this.colorListAddPiece(this.chessboard[1].getIndex(),this.chessboard[1].isWhite());
		this.colorListAddPiece(this.chessboard[2].getIndex(),this.chessboard[2].isWhite());
		this.colorListAddPiece(this.chessboard[3].getIndex(),this.chessboard[3].isWhite());
		this.colorListAddPiece(this.chessboard[4].getIndex(),this.chessboard[4].isWhite());
		this.colorListAddPiece(this.chessboard[5].getIndex(),this.chessboard[5].isWhite());
		this.colorListAddPiece(this.chessboard[6].getIndex(),this.chessboard[6].isWhite());
		this.colorListAddPiece(this.chessboard[7].getIndex(),this.chessboard[7].isWhite());
		for(int row=1; row<Constants.MAX_INDEX_ROW-1; ++row) {
			for(int column=0; column<Constants.MAX_INDEX_ROW; ++column){
				if(row==1) {
					//Row of Black Pawn
					chessboard[row*Constants.MAX_INDEX_ROW + column] = new Pawn(8 + column, Constants.BLACK);
					this.colorListAddPiece(this.chessboard[row*Constants.MAX_INDEX_ROW + column].getIndex(),this.chessboard[row*Constants.MAX_INDEX_ROW + column].isWhite());
				} else if(row==Constants.MAX_INDEX_ROW-2) {
					//Row of White Pawn
					chessboard[row*Constants.MAX_INDEX_ROW + column] = new Pawn(48 + column, 0);
					this.colorListAddPiece(this.chessboard[row*Constants.MAX_INDEX_ROW + column].getIndex(),this.chessboard[row*Constants.MAX_INDEX_ROW + column].isWhite());
				}
				else {
					//Empty cells
					chessboard[row*Constants.MAX_INDEX_ROW + column] = null;
				}
			}
		}
		//White Pieces
		chessboard[56] = new Tower(56, Constants.WHITE);
		chessboard[57] = new Horse(57, Constants.WHITE);
		chessboard[58] = new Bishop(58, Constants.WHITE);
		chessboard[59] = new Queen(59, Constants.WHITE);
		chessboard[60] = new King(60, Constants.WHITE);
		this.kingPos[Constants.WHITE] = this.chessboard[60];
		chessboard[61] = new Bishop(61, Constants.WHITE);
		chessboard[62] = new Horse(62, Constants.WHITE);
		chessboard[63] = new Tower(63, Constants.WHITE);
		this.colorListAddPiece(this.chessboard[56].getIndex(),this.chessboard[56].isWhite());
		this.colorListAddPiece(this.chessboard[57].getIndex(),this.chessboard[57].isWhite());
		this.colorListAddPiece(this.chessboard[58].getIndex(),this.chessboard[58].isWhite());
		this.colorListAddPiece(this.chessboard[59].getIndex(),this.chessboard[59].isWhite());
		this.colorListAddPiece(this.chessboard[60].getIndex(),this.chessboard[60].isWhite());
		this.colorListAddPiece(this.chessboard[61].getIndex(),this.chessboard[61].isWhite());
		this.colorListAddPiece(this.chessboard[62].getIndex(),this.chessboard[62].isWhite());
		this.colorListAddPiece(this.chessboard[63].getIndex(),this.chessboard[63].isWhite());
	}
	
	private void InizializeTiles(){		
		for(int i=0; i<Constants.MAX_INDEX; i++){
			this.tiles[i] = new Tile(i);
		}
	}
	//-----------------------------Private functions----------------------------------------
}
