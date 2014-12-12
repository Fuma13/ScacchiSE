package com.bifidoteam.scacchise.model;


public class Pezzo {
	
	//private Variables
	private int index;
	private boolean color;
	//private Graphic_Info gi; 								-> creare la classe Graphic_Info
	
	//Costructors 
	public Pezzo(int startingIndex,boolean startingColor){
		this.index = startingIndex;
		this.color = startingColor;
	}
	
	
	
	//public functions
	//public MedusaTree GetReachableIndices();				-> creare la classe MedusaTreee
	
	
	//getter/setter
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isColor() {
		return color;
	}

	public void setColor(boolean color) {
		this.color = color;
	}
}
