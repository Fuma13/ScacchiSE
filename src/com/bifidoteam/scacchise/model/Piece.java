package com.bifidoteam.scacchise.model;

import com.bifidoteam.util.MedusaTree;


abstract class Piece {	
		
	//-----------------------------Private Variables----------------------------------------
	protected int index;
	protected boolean white;
	//private Graphic_Info gi; 								-> creare la classe Graphic_Info
	//-----------------------------Private Variables----------------------------------------
	
	
	//--------------------------------Costructors-------------------------------------------
	protected Piece(int startingIndex,boolean startingColor){
		this.index = startingIndex;
		this.white = startingColor;
	}
	//--------------------------------Costructors-------------------------------------------
	
	
	
	//-----------------------------Public functions-----------------------------------------
	abstract MedusaTree GetReachableIndices();
	
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

	public boolean isWhite() {
		return white;
	}
	public void setWhite(boolean color) {
		this.white = color;
	}
	//--------------------------------Getter/Setter-----------------------------------------
}
