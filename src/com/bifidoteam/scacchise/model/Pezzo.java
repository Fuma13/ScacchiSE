package com.bifidoteam.scacchise.model;


public class Pezzo {	
		
	//-----------------------------Private Variables----------------------------------------
	private int index;
	private boolean color;
	//private Graphic_Info gi; 								-> creare la classe Graphic_Info
	//-----------------------------Private Variables----------------------------------------
	
	
	//--------------------------------Costructors-------------------------------------------
	public Pezzo(int startingIndex,boolean startingColor){
		this.index = startingIndex;
		this.color = startingColor;
	}
	//--------------------------------Costructors-------------------------------------------
	
	
	
	//-----------------------------Public functions-----------------------------------------
	//public MedusaTree GetReachableIndices();				-> creare la classe MedusaTreee
	public String toString(){
		return "IndexOnBoard:"+index+" Color:"+color;
	}
	//-----------------------------Public functions-----------------------------------------
	
	
	
	//--------------------------------Getter/Setter-----------------------------------------
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	public boolean getColor() {
		return color;
	}
	public void setColor(boolean color) {
		this.color = color;
	}
	//--------------------------------Getter/Setter-----------------------------------------
}
