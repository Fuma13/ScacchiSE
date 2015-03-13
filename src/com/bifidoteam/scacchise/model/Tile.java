package com.bifidoteam.scacchise.model;
import java.util.HashSet;
import java.util.Set;

import com.bifidoteam.scacchise.util.Constants;


public class Tile {
	
	//Ho usato l'arraylist perchè non sapevo come usare il set
	//in caso cambiate e sistemate i metodi =D
	private Set<Integer> [] registered;
	private int index;
	
	public Tile(){};		//serve il default?
	
	@SuppressWarnings("unchecked")
	public Tile(int index){
		this.index = index;
		this.registered = new Set[Constants.MAX_PLAYERS];
		this.registered[0] = new HashSet<Integer>();
		this.registered[1] = new HashSet<Integer>();
	}
	
	//return the index of the tile
	public int getIndex(){
		return this.index;
	}
	
	//return the list of the registered pieces of color "Color"
	public Set<Integer> getColorListRegistered(int Color){
		return this.registered[Color];
	}
	
	//register a piece from his index and his color
	public void registerPiece(int pieceIndex,int color){
		if(!this.registered[color].contains(pieceIndex)){
			this.registered[color].add(pieceIndex);
		}
	}
	
	//register a piece, added if useful
	public void registerPiece(Piece piece){
		this.registerPiece(piece.getIndex(), piece.isWhite());
	}
	
	//unregister a piece from his index and color
	public void unregisterPiece(int pieceIndex,int color){
		if(this.registered[color].contains(pieceIndex)){
			this.registered[color].remove(pieceIndex);
		}
	}
	
	//unregister a piece, added if useful
	public void unregisterPiece(Piece piece){
		this.unregisterPiece(piece.getIndex(), piece.isWhite());
	}
	
	public int numberOfOpponentPiecesRegisteredOn(int colorPlayer) {
		if(colorPlayer == Constants.BLACK){
			return this.registered[Constants.WHITE].size();
		}else{
			return this.registered[Constants.BLACK].size();
		}
	}
}
