package com.bifidoteam.scacchise.model;

public class MedusaLeaf {
	
	//-----------------------------Private Variables----------------------------------------
	private int indexOnChessboard;
	private MedusaLeaf next;
	private boolean isCut;
	//-----------------------------Private Variables----------------------------------------
	
	
	
	//--------------------------------Costructors-------------------------------------------
	public MedusaLeaf(){
		this.indexOnChessboard = -1; 	//out of board
		this.next = null;				//no next leaf
		this.isCut = false;			//this leaf and next are cut
	}
	
	public MedusaLeaf(int newIndex){
		this.indexOnChessboard = newIndex;
		this.next = null;
		this.isCut = false;
	}
	
	public MedusaLeaf(int newIndex,MedusaLeaf newNext){
		this.indexOnChessboard = newIndex;
		this.next = newNext;
		this.isCut = false;
	}
	//--------------------------------Costructors-------------------------------------------

	

	//-----------------------------Public functions-----------------------------------------
	public String toString(){
		return "IndexOnBoard:"+indexOnChessboard+" NextLeaf:"+next;
	}
	//-----------------------------Public functions-----------------------------------------
	
	
	
	//--------------------------------Getter/Setter-----------------------------------------
	public int getIndexOnChessboard() {
		return indexOnChessboard;
	}
	public void setIndexOnChessboard(int indexOnChessboard) {
		this.indexOnChessboard = indexOnChessboard;
	}

	public MedusaLeaf getNext() {
		if(next.isCut)
			return null;
		else
			return next;
	}
	public void setNext(MedusaLeaf next) {
		this.next = next;
	}
	
	public boolean IsCut(){
		return isCut;
	}
	
	public void setIsCut(boolean isCut){
		this.isCut = isCut;
	}
	//--------------------------------Getter/Setter-----------------------------------------
		
}
