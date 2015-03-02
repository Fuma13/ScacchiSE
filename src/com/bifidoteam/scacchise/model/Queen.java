package com.bifidoteam.scacchise.model;

import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

public class Queen extends Piece {

	//--------------------------------Costructors-------------------------------------------
	public Queen(int startingIndex, int startingColor) {
		super(startingIndex, startingColor);
	}
	//--------------------------------Costructors-------------------------------------------

	//-----------------------------Public functions-----------------------------------------
	@Override
	MedusaTree GetReachableIndices() {
		MedusaTree mt = new MedusaTree();
		
		//TopLeft
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
		//Top
		nextIndex = this.index-Constants.MAX_INDEX_ROW;
		if(nextIndex>=0){
			mt.AddBranch(nextIndex);
			int howManyLeftRemains = (nextIndex/Constants.MAX_INDEX_ROW);
			while(howManyLeftRemains > 0){
				howManyLeftRemains--;
				nextIndex -= Constants.MAX_INDEX_ROW;
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
		//left branch
		nextIndex = this.index-1;
		if(nextIndex/Constants.MAX_INDEX_ROW == this.index/Constants.MAX_INDEX_ROW && nextIndex>=0){
			mt.AddBranch(nextIndex);
			//continue to add feasable left moves
			int howManyLeftRemains = (nextIndex%Constants.MAX_INDEX_ROW);
			while(howManyLeftRemains > 0){
				howManyLeftRemains--;
				nextIndex--;
				mt.AddLeafToLastBranch(nextIndex);
			}
		}
		//right Branch
		nextIndex = this.index+1;
		if(nextIndex/Constants.MAX_INDEX_ROW == this.index/Constants.MAX_INDEX_ROW && nextIndex<Constants.MAX_INDEX){
			mt.AddBranch(nextIndex);
			//continue to add feasable right moves
			int howManyLeftRemains = Constants.MAX_INDEX_ROW - (nextIndex%Constants.MAX_INDEX_ROW)-1;
			while(howManyLeftRemains > 0){
				howManyLeftRemains--;
				nextIndex++;
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
		//down branch
		nextIndex = this.index+Constants.MAX_INDEX_ROW;
		if(nextIndex<Constants.MAX_INDEX){
			mt.AddBranch(nextIndex);
			int howManyLeftRemains = Constants.MAX_INDEX_ROW - (nextIndex/Constants.MAX_INDEX_ROW)-1;
			while(howManyLeftRemains > 0){
				howManyLeftRemains--;
				nextIndex += Constants.MAX_INDEX_ROW;
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
		return "Queen, " + super.toString();
	}
	//-----------------------------Public functions-----------------------------------------

}
