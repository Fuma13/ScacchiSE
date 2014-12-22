package com.bifidoteam.scacchise.model;

import com.bifidoteam.scacchise.util.Constants;

public class Chessboard
{
	//-----------------------------Private Variables----------------------------------------
	private Piece[] chessboard = new Piece[Constants.MAX_INDEX];
	//-----------------------------Private Variables----------------------------------------
	
	//-----------------------------Public functions-----------------------------------------
	public MedusaTree GetRealIndices(int index) {
		if(index >= 0 && index < Constants.MAX_INDEX && chessboard[index] != null) {
			return chessboard[index].GetReachableIndices();
		}
		return null;
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
	//-----------------------------Public functions-----------------------------------------
}
