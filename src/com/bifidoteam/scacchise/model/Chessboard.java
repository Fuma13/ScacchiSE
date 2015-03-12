package com.bifidoteam.scacchise.model;

import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

import java.util.ArrayList;
import java.util.LinkedList;

public class Chessboard
{
	//-----------------------------Private Variables----------------------------------------
	private Piece[] chessboard = new Piece[Constants.MAX_INDEX];
	//VALE: Aggiunta la lista delle tile
	private Tile[] tiles = new Tile[Constants.MAX_INDEX];
	
	//VALE: Ho aggiunto le liste dei soli pezzi bianchi e neri.
	private ArrayList<LinkedList<Piece>> pieces;
	//-----------------------------Private Variables----------------------------------------
	
	//-----------------------------Public functions-----------------------------------------
	public Chessboard() {
		InizializeChessboard();
		//Init tiles
		this.InizializeTiles();
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
			return chessboard[index].isWhite();
	}
	
	public Piece GetPiece(int index) {
		if(index >= 0 && index < Constants.MAX_INDEX)
			return chessboard[index];
		else
			return null; //TODO:FUMA: distinguere null di cella vuota da out of bound
	}
	
	public boolean MovePieceFromStartIndexToEndIndex(int startIndex, int endIndex) {
		if(startIndex >= 0 && startIndex < Constants.MAX_INDEX && endIndex >= 0 && endIndex < Constants.MAX_INDEX) {
			chessboard[endIndex] = chessboard[startIndex];
			chessboard[startIndex] = null;
			return true;
		}	
		return false;
	}
	
	//return the list of pieces of color "Color"
	public LinkedList<Piece> getColorList(int Color){
		return this.pieces.get(Color);
	}
	//add a piece in the list of his own color
	public void colorListAddPiece(Piece piece){
		if(!this.pieces.get(piece.isWhite()).contains(piece)){
			this.pieces.get(piece.isWhite()).add(piece);
		}
	}
	public Tile getTile(int index){
		return this.tiles[index];
	}
	//-----------------------------Public functions-----------------------------------------
	
	//-----------------------------Private functions----------------------------------------
	private void InizializeChessboard() {
		//TODO:FUMA: Togliere i magic numbers (ma mi sa che non si puo')
		//VALE: No non credo, è la configurazione iniziale, ho messo i colori const
		
		//Black Pieces
		chessboard[0] = new Tower(0, Constants.BLACK);
		chessboard[1] = new Horse(1, Constants.BLACK);
		chessboard[2] = new Bishop(2, Constants.BLACK);
		chessboard[3] = new Queen(3, Constants.BLACK);
		chessboard[4] = new King(4, Constants.BLACK);
		chessboard[5] = new Bishop(5, Constants.BLACK);
		chessboard[6] = new Horse(6, Constants.BLACK);
		chessboard[7] = new Tower(7, Constants.BLACK);
		this.colorListAddPiece(this.chessboard[0]);
		this.colorListAddPiece(this.chessboard[1]);
		this.colorListAddPiece(this.chessboard[2]);
		this.colorListAddPiece(this.chessboard[3]);
		this.colorListAddPiece(this.chessboard[4]);
		this.colorListAddPiece(this.chessboard[5]);
		this.colorListAddPiece(this.chessboard[6]);
		this.colorListAddPiece(this.chessboard[7]);
		for(int row=1; row<Constants.MAX_INDEX_ROW-1; ++row) {
			for(int column=0; column<Constants.MAX_INDEX_ROW; ++column){
				if(row==1) {
					//Row of Black Pawn
					chessboard[row*Constants.MAX_INDEX_ROW + column] = new Pawn(8 + column, Constants.BLACK);
					this.colorListAddPiece(this.chessboard[row*Constants.MAX_INDEX_ROW + column]);
				} else if(row==Constants.MAX_INDEX_ROW-2) {
					//Row of White Pawn
					chessboard[row*Constants.MAX_INDEX_ROW + column] = new Pawn(48 + column, 0);
					this.colorListAddPiece(this.chessboard[row*Constants.MAX_INDEX_ROW + column]);
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
		chessboard[61] = new Bishop(61, Constants.WHITE);
		chessboard[62] = new Horse(62, Constants.WHITE);
		chessboard[63] = new Tower(63, Constants.WHITE);
		this.colorListAddPiece(this.chessboard[56]);
		this.colorListAddPiece(this.chessboard[57]);
		this.colorListAddPiece(this.chessboard[58]);
		this.colorListAddPiece(this.chessboard[59]);
		this.colorListAddPiece(this.chessboard[60]);
		this.colorListAddPiece(this.chessboard[61]);
		this.colorListAddPiece(this.chessboard[62]);
		this.colorListAddPiece(this.chessboard[63]);
	}
	
	private void InizializeTiles(){
		for(int i=0; i<Constants.MAX_INDEX; i++){
			this.tiles[i] = new Tile(i);
		}
	}
	//-----------------------------Private functions----------------------------------------
}
