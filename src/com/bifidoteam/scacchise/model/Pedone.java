package com.bifidoteam.scacchise.model;
import com.bifidoteam.scacchise.util.Constants;

public class Pedone extends Pezzo implements PedoneInterface {

	//--------------------------------Costructors-------------------------------------------
	public Pedone(int startingIndex, boolean startingColor) {
		super(startingIndex, startingColor);
	}
	//--------------------------------Costructors-------------------------------------------

	
	
	//-----------------------------Public functions-----------------------------------------
	@Override
	MedusaTree GetReachableIndices() {
		MedusaTree mt = new MedusaTree();
		
		int j = this.index;
		int segno = this.isWhite()? 1:-1;
		
		j += Constants.MAX_INDEX_ROW*segno;
		mt.AddBranch(j);
		
		return mt;
	}
	
	@Override
	public MedusaTree GetEatableIndices() {
		MedusaTree mt = new MedusaTree();
		
		int actualRowIndex;
		int j = this.index;
		int segno = this.isWhite()? 1:-1;
		
		actualRowIndex = this.index / Constants.MAX_INDEX_ROW;
		j += Constants.MAX_INDEX_ROW*segno-1;
		
		int count = 0;
		for(;count<2;count++){
			if(j>0 && j<Constants.MAX_INDEX && (j / Constants.MAX_INDEX_ROW == actualRowIndex + segno)){
				mt.AddBranch(j);
				j += 2;
			}
		}
		
		return mt;
	}
	
	@Override
	public String ToString(){
		return "Pedone, " + super.toString();
	}
	//-----------------------------Public functions-----------------------------------------
}
