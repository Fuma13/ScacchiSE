package com.bifidoteam.scacchise.model;

import com.bifidoteam.util.MedusaTree;


public abstract class Piece {	
		
	//-----------------------------Private Variables----------------------------------------
	protected int index;
	protected int white;
	protected MedusaTree mt;
	//private Graphic_Info gi; 								-> creare la classe Graphic_Info
	//-----------------------------Private Variables----------------------------------------
	
	
	//--------------------------------Costructors-------------------------------------------
	protected Piece(int startingIndex,int startingColor){
		this.index = startingIndex;
		this.white = startingColor;
		this.mt = null;
	}
	//--------------------------------Costructors-------------------------------------------
	
	
	
	//-----------------------------Public functions-----------------------------------------
	abstract MedusaTree GetReachableIndices();
	
	public abstract char GetSymbol();
	
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
	
	public MedusaTree getMedusaTree(){
		return this.mt;
	}
	public void setMedusaTree(MedusaTree mt){
		this.mt = mt;
	}
	//--------------------------------Getter/Setter-----------------------------------------
}
