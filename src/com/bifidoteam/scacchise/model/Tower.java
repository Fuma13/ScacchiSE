package com.bifidoteam.scacchise.model;
import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

public class Tower extends Piece {

	//--------------------------------Costructors-------------------------------------------
	public Tower(int startingIndex, boolean startingColor) {
		super(startingIndex, startingColor);
	}
	//--------------------------------Costructors-------------------------------------------

	//-----------------------------Public functions-----------------------------------------
	@Override
	MedusaTree GetReachableIndices() {
		MedusaTree mt = new MedusaTree();
		
		int actualRow = this.index/Constants.MAX_INDEX_ROW;
		int nextIndex;
		
		//left branch
		nextIndex = this.index-1;
		if(nextIndex/Constants.MAX_INDEX_ROW == actualRow && nextIndex>=0){
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
		if(nextIndex/Constants.MAX_INDEX_ROW == actualRow && nextIndex<Constants.MAX_INDEX){
			mt.AddBranch(nextIndex);
			//continue to add feasable right moves
			int howManyLeftRemains = Constants.MAX_INDEX_ROW - (nextIndex%Constants.MAX_INDEX_ROW)-1;
			while(howManyLeftRemains > 0){
				howManyLeftRemains--;
				nextIndex++;
				mt.AddLeafToLastBranch(nextIndex);
			}
		}
		
		//upper branch
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
		return mt;
	}
	
	@Override
	public String ToString(){
		return "Tower, " + super.toString();
	}
	//-----------------------------Public functions-----------------------------------------
}
