package com.bifidoteam.scacchise.model;

public class MedusaLeaf {
	
	//-----------------------------Private Variables----------------------------------------
	private int indexOnChessboard;
	private MedusaLeaf next;
	//-----------------------------Private Variables----------------------------------------
	
	
	
	//--------------------------------Costructors-------------------------------------------
	public MedusaLeaf(){
		this.indexOnChessboard = -1; 	//out of board
		this.next = null;				//no next leaf
	}
	
	public MedusaLeaf(int newIndex,MedusaLeaf newNext){
		this.indexOnChessboard = newIndex;
		this.next = newNext;
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
		return next;
	}
	public void setNext(MedusaLeaf next) {
		this.next = next;
	}
	//--------------------------------Getter/Setter-----------------------------------------
		
}
