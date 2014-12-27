package com.bifidoteam.scacchise.model;

import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

public class Chessboard
{
	//-----------------------------Private Variables----------------------------------------
	private Piece[] chessboard = new Piece[Constants.MAX_INDEX];
	//-----------------------------Private Variables----------------------------------------
	
	//-----------------------------Public functions-----------------------------------------
	public Chessboard() {
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
	public boolean IsPieceWhite(int index) {
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
	//-----------------------------Public functions-----------------------------------------
	
	//-----------------------------Private functions----------------------------------------
	private void InizializeChessboard() {
		//TODO:FUMA: Togliere i magic numbers (ma mi sa che non si puo')
		//Black Pieces
		chessboard[0] = new Tower(0, false);
		chessboard[1] = new Horse(1, false);
		chessboard[2] = new Bishop(2, false);
		chessboard[3] = new Queen(3, false);
		chessboard[4] = new King(4, false);
		chessboard[5] = new Bishop(5, false);
		chessboard[6] = new Horse(6, false);
		chessboard[7] = new Tower(7, false);
		for(int row=1; row<Constants.MAX_INDEX_ROW-1; ++row) {
			for(int column=0; column<Constants.MAX_INDEX_ROW; ++column){
				if(row==1) {
					//Row of Black Pawn
					chessboard[row*Constants.MAX_INDEX_ROW + column] = new Pawn(8 + column, false);
				} else if(row==Constants.MAX_INDEX_ROW-1) {
					//Row of White Pawn
					chessboard[row*Constants.MAX_INDEX_ROW + column] = new Pawn(48 + column, true);
				}
				else {
					//Empty cells
					chessboard[row*Constants.MAX_INDEX_ROW + column] = null;
				}
			}
		}
		//White Pieces
		chessboard[56] = new Tower(56, true);
		chessboard[57] = new Horse(57, true);
		chessboard[58] = new Bishop(58, true);
		chessboard[59] = new Queen(59, true);
		chessboard[60] = new King(60, true);
		chessboard[61] = new Bishop(61, true);
		chessboard[62] = new Horse(62, true);
		chessboard[63] = new Tower(63, true);
	}
	//-----------------------------Private functions----------------------------------------
}
