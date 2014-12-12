package com.bifidoteam.scacchise.model;


public class Pezzo {
	
	//private Variables
	protected int index;
	protected boolean color;
	//private Graphic_Info gi; 								-> creare la classe Graphic_Info
	
	//Costructors 
	public Pezzo(int startingIndex,boolean startingColor){
		this.index = startingIndex;
		this.color = startingColor;
	}
		
	//public functions
	//public MedusaTree GetReachableIndices();				-> creare la classe MedusaTreee
	
	
	//--------------------------------getter/setter-----------------------------------------
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
	//--------------------------------getter/setter-----------------------------------------
}
