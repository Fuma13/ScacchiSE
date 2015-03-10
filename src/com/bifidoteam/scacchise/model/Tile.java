package com.bifidoteam.scacchise.model;
import java.util.ArrayList;
import java.util.LinkedList;

import com.bifidoteam.scacchise.util.Constants;


public class Tile {
	
	//Ho usato l'arraylist perchè non sapevo come usare il set
	//in caso cambiate e sistemate i metodi =D
	private ArrayList<LinkedList<Integer>> registered;
	private int index;
	
	public Tile(){};		//serve il default?
	public Tile(int index){
		this.index = index;
		
		this.registered = new ArrayList<LinkedList<Integer>>(2);
		
		this.registered.add(new LinkedList<Integer>());
		this.registered.add(new LinkedList<Integer>());
	}
	
	//return the index of the tile
	public int getIndex(){
		return this.index;
	}
	
	//return the list of the registered pieces of color "Color"
	public LinkedList<Integer> getColorListRegistered(int Color){
		return this.registered.get(Color);
	}
	
	//register a piece from his index and his color
	public void registerPiece(int pieceIndex,int color){
		if(!this.registered.get(color).contains(pieceIndex)){
			this.registered.get(color).add(pieceIndex);
		}
	}
	
	//register a piece, added if usefull
	public void registerPiece(Piece piece){
		this.registerPiece(piece.getIndex(), piece.isWhite());
	}
	
	//unregister a piece from his index and color
	public void unregisterPiece(int pieceIndex,int color){
		if(this.registered.get(color).contains(pieceIndex)){
			this.registered.get(color).remove(pieceIndex);
		}
	}
	
	//unregister a piece, added if usefull
	public void unregisterPiece(Piece piece){
		this.unregisterPiece(piece.getIndex(), piece.isWhite());
	}
}
