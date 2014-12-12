package com.bifidoteam.scacchise.model;
import java.util.LinkedList;
import java.util.List;

public class MedusaTree {
	
	//-----------------------------Private Variables----------------------------------------
	private List<MedusaLeaf> nearPositions;
	//-----------------------------Private Variables----------------------------------------
	
	
	
	//--------------------------------Costructors-------------------------------------------
	public MedusaTree(){
		this.nearPositions = new LinkedList<MedusaLeaf>();
	}
	//--------------------------------Costructors-------------------------------------------
	
	
	
	//--------------------------------Getter/Setter-----------------------------------------
	public List<MedusaLeaf> getNearPositions() {
		return nearPositions;
	}
	public void setNearPositions(List<MedusaLeaf> nearPositions) {
		this.nearPositions = nearPositions;
	}
	//--------------------------------Getter/Setter-----------------------------------------
}
