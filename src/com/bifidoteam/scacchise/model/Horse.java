package com.bifidoteam.scacchise.model;
import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

public class Horse extends Piece {

	//--------------------------------Costructors-------------------------------------------
	public Horse(int startingIndex, int startingColor) {
		super(startingIndex, startingColor);
	}
	//--------------------------------Costructors-------------------------------------------

	//-----------------------------Public functions-----------------------------------------
	@Override
	MedusaTree GetReachableIndices(){
		MedusaTree mt = new MedusaTree();
		int actualRow = this.index/Constants.MAX_INDEX_ROW;
		int indexToAdd;
		
		
		//TopLeft
		indexToAdd = this.index-Constants.MAX_INDEX_ROW*2-1;
		if(indexToAdd>=0 && indexToAdd<Constants.MAX_INDEX && indexToAdd/Constants.MAX_INDEX_ROW==actualRow-2){
			mt.AddBranch(indexToAdd);
		}
		//TopRight
		indexToAdd = this.index-Constants.MAX_INDEX_ROW*2+1;
		if(indexToAdd>=0 && indexToAdd<Constants.MAX_INDEX && indexToAdd/Constants.MAX_INDEX_ROW==actualRow-2){
			mt.AddBranch(indexToAdd);
		}
		//TopMiddleLeft
		indexToAdd = this.index-Constants.MAX_INDEX_ROW-2;
		if(indexToAdd>=0 && indexToAdd<Constants.MAX_INDEX && indexToAdd/Constants.MAX_INDEX_ROW==actualRow-1){
			mt.AddBranch(indexToAdd);
		}
		//TopMiddleLeft
		indexToAdd = this.index-Constants.MAX_INDEX_ROW+2;
		if(indexToAdd>=0 && indexToAdd<Constants.MAX_INDEX && indexToAdd/Constants.MAX_INDEX_ROW==actualRow-1){
			mt.AddBranch(indexToAdd);
		}
		//DownMiddleLeft
		indexToAdd = this.index+Constants.MAX_INDEX_ROW-2;
		if(indexToAdd>=0 && indexToAdd<Constants.MAX_INDEX && indexToAdd/Constants.MAX_INDEX_ROW==actualRow+1){
			mt.AddBranch(indexToAdd);
		}
		//DownMiddleRight
		indexToAdd = this.index+Constants.MAX_INDEX_ROW+2;
		if(indexToAdd>=0 && indexToAdd<Constants.MAX_INDEX && indexToAdd/Constants.MAX_INDEX_ROW==actualRow+1){
			mt.AddBranch(indexToAdd);
		}
		//DownLeft
		indexToAdd = this.index+Constants.MAX_INDEX_ROW*2-1;
		if(indexToAdd>=0 && indexToAdd<Constants.MAX_INDEX && indexToAdd/Constants.MAX_INDEX_ROW==actualRow+2){
			mt.AddBranch(indexToAdd);
		}
		//DownRight
		indexToAdd = this.index+Constants.MAX_INDEX_ROW*2+1;
		if(indexToAdd>=0 && indexToAdd<Constants.MAX_INDEX && indexToAdd/Constants.MAX_INDEX_ROW==actualRow+2){
			mt.AddBranch(indexToAdd);
		}
		
		return mt;
	}
	
	@Override
	public String ToString(){
		return "Horse, " + super.toString();
	}
	//-----------------------------Public functions-----------------------------------------
}
