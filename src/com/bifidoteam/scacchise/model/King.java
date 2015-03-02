package com.bifidoteam.scacchise.model;

import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

public class King extends Piece {

	//--------------------------------Costructors-------------------------------------------
	public King(int startingIndex, int startingColor) {
		super(startingIndex, startingColor);
	}
	//--------------------------------Costructors-------------------------------------------
	
	//-----------------------------Public functions-----------------------------------------
	@Override
	MedusaTree GetReachableIndices() {
		MedusaTree mt = new MedusaTree();
		int tempIndex;
		
		//Upper moves
		for(int i=-1;i<2;i++){
			tempIndex = this.index-Constants.MAX_INDEX_ROW+i;
			if(tempIndex/Constants.MAX_INDEX_ROW == (this.index/Constants.MAX_INDEX_ROW)-1 && tempIndex>0) mt.AddBranch(tempIndex);
		}
		//Sides moves
		tempIndex = this.index-1;
		if(tempIndex > 0 && tempIndex/Constants.MAX_INDEX_ROW == this.index/Constants.MAX_INDEX_ROW) mt.AddBranch(tempIndex);
		tempIndex = this.index+1;
		if(tempIndex < Constants.MAX_INDEX && tempIndex/Constants.MAX_INDEX_ROW == this.index/Constants.MAX_INDEX_ROW) mt.AddBranch(tempIndex);
		//LowerMoves
		for(int i=-1;i<2;i++){
			tempIndex = this.index+Constants.MAX_INDEX_ROW+i;
			if(tempIndex/Constants.MAX_INDEX_ROW == (this.index/Constants.MAX_INDEX_ROW)+1 && tempIndex<Constants.MAX_INDEX) mt.AddBranch(tempIndex);
		}
		return mt;
	}
	
	@Override
	public String ToString(){
		return "King, " + super.toString();
	}
	//-----------------------------Public functions-----------------------------------------
}
