package com.bifidoteam.scacchise.model;
import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

import java.lang.Math;

public class Bishop extends Piece {

	//--------------------------------Costructors-------------------------------------------
	public Bishop(int startingIndex, boolean startingColor) {
		super(startingIndex, startingColor);
	}
	//--------------------------------Costructors-------------------------------------------

	//-----------------------------Public functions-----------------------------------------
	@Override
	MedusaTree GetReachableIndices() {
		MedusaTree mt = new MedusaTree();
		int nextIndex;
		
		//TopLeft
		int feasableMoves = Math.min(this.index/Constants.MAX_INDEX_ROW, this.index%Constants.MAX_INDEX_ROW);
		if(feasableMoves>0){
			nextIndex = this.index-Constants.MAX_INDEX_ROW-1;
			mt.AddBranch(nextIndex);
			feasableMoves--;
			for(;feasableMoves>0;feasableMoves--){
				nextIndex -= Constants.MAX_INDEX_ROW-1;
				mt.AddLeafToLastBranch(nextIndex);
			}
		}
		
		//TopRight
		feasableMoves = Math.min(this.index/Constants.MAX_INDEX_ROW, Constants.MAX_INDEX_ROW-this.index%Constants.MAX_INDEX_ROW-1);
		if(feasableMoves>0){
			nextIndex = this.index-Constants.MAX_INDEX_ROW+1;
			mt.AddBranch(nextIndex);
			feasableMoves--;
			for(;feasableMoves>0;feasableMoves--){
				nextIndex -= Constants.MAX_INDEX_ROW+1;
				mt.AddLeafToLastBranch(nextIndex);
			}
		}
		
		//DownLeft
		feasableMoves = Math.min(Constants.MAX_INDEX_ROW - this.index/Constants.MAX_INDEX_ROW - 1, this.index%Constants.MAX_INDEX_ROW);
		if(feasableMoves>0){
			nextIndex = this.index+Constants.MAX_INDEX_ROW-1;
			mt.AddBranch(nextIndex);
			feasableMoves--;
			for(;feasableMoves>0;feasableMoves--){
				nextIndex += Constants.MAX_INDEX_ROW-1;
				mt.AddLeafToLastBranch(nextIndex);
			}
		}
		//DownRight
		feasableMoves = Math.min(Constants.MAX_INDEX_ROW - this.index/Constants.MAX_INDEX_ROW - 1, Constants.MAX_INDEX_ROW-this.index%Constants.MAX_INDEX_ROW-1);
		if(feasableMoves>0){
			nextIndex = this.index+Constants.MAX_INDEX_ROW+1;
			mt.AddBranch(nextIndex);
			feasableMoves--;
			for(;feasableMoves>0;feasableMoves--){
				nextIndex += Constants.MAX_INDEX_ROW+1;
				mt.AddLeafToLastBranch(nextIndex);
			}
		}
		
		return mt;
	}
	
	@Override
	public String ToString(){
		return "Alfiere, " + super.toString();
	}
	//-----------------------------Public functions-----------------------------------------
}
