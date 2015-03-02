package com.bifidoteam.util;

public class MedusaLeaf {
	
	//-----------------------------Private Variables----------------------------------------
	private int value;
	private MedusaLeaf next;
	private boolean isCut;
	//-----------------------------Private Variables----------------------------------------
	
	
	
	//--------------------------------Costructors-------------------------------------------
	public MedusaLeaf(){
		this.value = -1; 			//null value
		this.next = null;			//no next leaf
		this.isCut = false;			//this leaf and next are not cut
	}
	
	public MedusaLeaf(int newIndex){
		this.value = newIndex;
		this.next = null;
		this.isCut = false;
	}
	
	public MedusaLeaf(int newIndex,MedusaLeaf newNext){
		this.value = newIndex;
		this.next = newNext;
		this.isCut = false;
	}
	//--------------------------------Costructors-------------------------------------------

	

	//-----------------------------Public functions-----------------------------------------
	public String toString(){
		return "Value:"+value+" NextLeaf:"+next;
	}
	//-----------------------------Public functions-----------------------------------------
	
	
	
	//--------------------------------Getter/Setter-----------------------------------------
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	public MedusaLeaf getNext() {
		if(next.isCut)
			return null;
		else
			return next;
	}
	//Return also cutted leaf
	public MedusaLeaf getRealNext(){
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
