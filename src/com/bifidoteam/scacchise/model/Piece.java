package com.bifidoteam.scacchise.model;

import com.bifidoteam.util.MedusaTree;


abstract class Piece {	
		
	//-----------------------------Private Variables----------------------------------------
	protected int index;
	protected int white; //0 white, 1 black
	//private Graphic_Info gi; 								-> creare la classe Graphic_Info
	//-----------------------------Private Variables----------------------------------------
	
	
	//--------------------------------Costructors-------------------------------------------
	protected Piece(int startingIndex,int startingColor){
		this.index = startingIndex;
		this.white = startingColor;
	}
	//--------------------------------Costructors-------------------------------------------
	
	
	
	//-----------------------------Public functions-----------------------------------------
	abstract MedusaTree GetReachableIndices();
	
	abstract char GetSymbol();
	
	protected String ToString(){
		return "IndexOnBoard:"+index+" White?"+white;
	}
	//-----------------------------Public functions-----------------------------------------
	
	
	
	//--------------------------------Getter/Setter-----------------------------------------
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	public int isWhite() {
		return white;
	}
	public void setWhite(int color) {
		this.white = color;
	}
	//--------------------------------Getter/Setter-----------------------------------------
}
