package com.bifidoteam.util;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class MedusaTree {//implements Iterable<Integer>,Iterator<Integer>{
	
	//-----------------------------Private Variables----------------------------------------
	private List<MedusaLeaf> nearPositions;
	private MedusaLeaf lastLeaf;
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
//	private void setNearPositions(List<MedusaLeaf> nearPositions) {
//		this.nearPositions = nearPositions;
//	}
	//--------------------------------Getter/Setter-----------------------------------------
	
	//-----------------------------Private functions----------------------------------------
	private BranchIterator GetBranchIterator()
	{
		return new BranchIterator().iterator();
	}
	//-----------------------------Private functions----------------------------------------
	
	//-----------------------------Public functions-----------------------------------------
//	public String toString() {
//		String s = "";
//		for(MedusaLeaf m:nearPositions) {
//			s += m.toString() + "\n";
//		}
//		
//		return s;
//	}
	
	//Add a leaf to the last branch crated
	public void AddLeafToLastBranch(int index) {
		MedusaLeaf newLeaf = new MedusaLeaf(index);
		lastLeaf.setNext(newLeaf);
		lastLeaf = newLeaf;
	}
	
	//Create a new branch with the first leaf
	public void AddBranch(int index) {
		MedusaLeaf newLeaf = new MedusaLeaf(index);
		nearPositions.add(newLeaf);
		lastLeaf = newLeaf;
	}
	
	public void AddExistingBranch(MedusaLeaf leaf) {
		nearPositions.add(leaf);
		while(leaf.getNext() != null)
		{
			leaf = leaf.getNext();
		}
		lastLeaf = leaf;
	}
	
	public boolean Contain(int index) {
		//Iterator<Integer> mtIterator = iterator();
		CuttedIterator mtIterator = GetCuttedIterator();
		boolean found = false;
		while(mtIterator.hasNext() && !found) {
			if(mtIterator.next() == index)
				found = true;
		}
		return found;
	}

	public void MergeMedusaTreeNewBanch(MedusaTree other){
		
		BranchIterator bi = other.GetBranchIterator();
		
		while(bi.hasNext())
		{
			AddExistingBranch(bi.next());
		}
		
	}
	
	public CompleteIterator GetCompleteIterator()
	{
		return new CompleteIterator().iterator();
	}
	
	public CuttedIterator GetCuttedIterator()
	{
		return new CuttedIterator().iterator();
	}
	
	//-----------------------------Public functions-----------------------------------------

	//--------------------------------Iterator functions------------------------------------
	public class CuttedIterator implements Iterable<Integer>,Iterator<Integer>{
		//-----------------------------Iterator variables
		private int currentNearPos;
		private MedusaLeaf currentLeaf;
		
		@Override
		public boolean hasNext() {
			boolean hasNextValue = false;
			if(currentNearPos == -1) {//check on the first one
				if(!nearPositions.isEmpty()){
					MedusaLeaf tempCurrentLeaf = nearPositions.get(0);
					if(tempCurrentLeaf != null) {
						if(!tempCurrentLeaf.IsCut())
							hasNextValue = true;
						//Search in depth or Change branch
						else {
							hasNextValue = hasNextUncutted(tempCurrentLeaf,0);
						}
					}
				}//Else Tree Empty
			}
			else {
				if(currentLeaf != null) {
					//Continue in this branch
					if(!currentLeaf.IsCut() && currentLeaf.getNext() != null){
						hasNextValue = true;
					}
					//Search in depth or Change branch
					else {
						hasNextValue = hasNextUncutted(currentLeaf,currentNearPos);
					}
				}
			}
			return hasNextValue;
				
		}
		
		//Search the first uncutted in this branch
		//If there is not change branch
		private boolean hasNextUncutted(MedusaLeaf tempCurrentLeaf, int tempCurrentNearPos) {
			boolean hasNextValue = false;
			tempCurrentLeaf = tempCurrentLeaf.getRealNext();
			do{
				do{
					if(tempCurrentLeaf!= null){
						if(!tempCurrentLeaf.IsCut()){
							hasNextValue = true;
						}
						else {
							tempCurrentLeaf = tempCurrentLeaf.getRealNext();
						}
					}
				}while(tempCurrentLeaf != null && !hasNextValue);
				
				//Change branch
				if(!hasNextValue && tempCurrentNearPos + 1 < nearPositions.size()) {
					tempCurrentNearPos += 1;
					tempCurrentLeaf = nearPositions.get(tempCurrentNearPos);
				}
			
			}while(!hasNextValue && tempCurrentLeaf != null);
			
			return hasNextValue;
		}
	
		//TODO:FUMA: out of bound
		@Override
		public Integer next() {
			Integer nextIndex = null;
			if(currentNearPos == -1){
				if(!nearPositions.isEmpty()){
					currentNearPos = 0;
					currentLeaf = nearPositions.get(currentNearPos);
					
					if(currentLeaf != null) {
						if(!currentLeaf.IsCut()) {
							nextIndex = currentLeaf.getValue();
						}
						else{
							nextIndex = NextUncutted();
						}
					}
				}// Else Tree Empty
			}else{
				if(currentLeaf.getNext() != null){
					//Next leaf in this branch
					currentLeaf = currentLeaf.getNext();
					nextIndex =  currentLeaf.getValue();
				}
				else {
					nextIndex = NextUncutted();
				}
			
			}
			
			if(nextIndex == null)
				throw new NoSuchElementException();
			//There are not next leaf or branch
			return nextIndex;
		}
		
		//Search the first uncutted in this branch
		//If there is not change branch
		private Integer NextUncutted() {
			Integer nextIndex = null;
			currentLeaf = currentLeaf.getRealNext();
			do{
				do{
					if(currentLeaf!= null) {
						if(!currentLeaf.IsCut()){
							nextIndex = currentLeaf.getValue();
						}
						else
						{
							currentLeaf = currentLeaf.getRealNext();
						}	
					}
					
				}while(currentLeaf != null && nextIndex == null);
				
				//Change branch
				if(nextIndex == null && currentNearPos + 1 < nearPositions.size()) {
					currentNearPos += 1;
					currentLeaf = nearPositions.get(currentNearPos);
				}
			
			}while(nextIndex == null && currentLeaf != null);
			
			return nextIndex;
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
			
		}
	
		@Override
		public CuttedIterator iterator() {
			currentNearPos = -1;
			currentLeaf = null;
			return this;
		}
	}
	
	public class CompleteIterator implements Iterable<Integer>,Iterator<Integer>{

		//-----------------------------Iterator variables
		private int currentNearPos;
		private MedusaLeaf currentLeaf;
		
		@Override
		public boolean hasNext()
		{
			boolean hasNextValue = false;
			if(currentNearPos == -1) {//check on the first one
				if(!nearPositions.isEmpty()){
					MedusaLeaf tempCurrentLeaf = nearPositions.get(0);
					if(tempCurrentLeaf != null) {
							hasNextValue = true;
					}
				}//Else Tree Empty
			}
			else {
				//Continue in this branch
				if(currentLeaf.getRealNext() != null){
					hasNextValue = true;
				}
				//Change branch
				else {
					hasNextValue = hasNextChangeBranch();
				}
			}
			return hasNextValue;
		}
		
		private boolean hasNextChangeBranch() {
			boolean hasNextValue = false;
			int tempCurrentNearPos = currentNearPos;
			while(tempCurrentNearPos +1 < nearPositions.size()){ //if is not the last
				tempCurrentNearPos += 1;
				//if this branch is not cut 
				if(nearPositions.get(tempCurrentNearPos)!= null){
					hasNextValue = true;
				}
			}
			return hasNextValue;
		}

		//TODO:FUMA: out of bound
		@Override
		public Integer next()
		{
			Integer nextIndex = null;
			if(currentNearPos == -1) {//return the first one
				if(!nearPositions.isEmpty()){
					currentNearPos = 0;
					currentLeaf = nearPositions.get(currentNearPos);
					if(currentLeaf != null) {
							nextIndex = currentLeaf.getValue();
					}
				}// Else Tree Empty
			}
			else {
				if(currentLeaf.getRealNext() != null){
					//Next leaf in this branch
					currentLeaf = currentLeaf.getRealNext();
					nextIndex =  currentLeaf.getValue();
				}
				else {
					nextIndex = NextChangeBranch();
				}
			}

			//There are not next leaf or branch
			if(nextIndex == null)
				throw new NoSuchElementException();
			
			return nextIndex;
		}
		
		private Integer NextChangeBranch() {
			Integer nextIndex = null;
			//while if is not the last branch
			if(currentNearPos +1 < nearPositions.size()){
				//Change branch
				currentNearPos += 1;
				//Take the first leaf in this branch
				currentLeaf = nearPositions.get(currentNearPos);
				nextIndex = currentLeaf.getValue();
			}
			return nextIndex;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public CompleteIterator iterator()
		{
			currentNearPos = -1;
			currentLeaf = null;
			return this;
		}
		
		public void CutThisAndAfter() {
			if(currentNearPos != -1) {
				if(currentLeaf != null) {
					//Cut the current leaf and the rest of the branch
					//currentLeaf.setIsCut(true);
					CutThisBranch(currentLeaf);
//					if(currentLeaf.getNext() != null) {
//						//Cut also the next so the iterator can change branch
//						currentLeaf.getNext().setIsCut(true);
//					}
				}
			}
		}
		
		public void CutAfter() {
			if(currentNearPos != -1) {
				if(currentLeaf != null && currentLeaf.getRealNext() != null) { //Check if it is not the last
					//Remove the branch from the next leaf
					CutThisBranch(currentLeaf.getRealNext());
					//currentLeaf.getNext().setIsCut(true);
				} //if is the last there is nothing to cut
			}
		}
		
		private void CutThisBranch(MedusaLeaf currentLeaf)
		{
			do
			{
				currentLeaf.setIsCut(true);
				currentLeaf = currentLeaf.getRealNext();
			}while(currentLeaf != null);
		}
		
		public void CutThis(){
			if(currentNearPos != -1) {
				if(currentLeaf != null) {
					currentLeaf.setIsCut(true);
				}
			}
		}
		
		public Boolean IsThisCutted(){
			return currentLeaf.IsCut();
		}
	
	}
	
	private class BranchIterator implements Iterable<MedusaLeaf>,Iterator<MedusaLeaf>{

		private int currentNearPos;
		private MedusaLeaf currentLeaf;
		
		@Override
		public boolean hasNext()
		{
			boolean hasNextValue = false;
			if(currentNearPos == -1) {//check on the first one
				if(!nearPositions.isEmpty()){
					MedusaLeaf tempCurrentLeaf = nearPositions.get(0);
					if(tempCurrentLeaf != null){
						hasNextValue = true;
					}//Else Tree Empty
				}
			}
			else if(currentNearPos +1 < nearPositions.size()) {
				MedusaLeaf tempCurrentLeaf = nearPositions.get(currentNearPos+1);
				if(tempCurrentLeaf != null){
					hasNextValue = true;
				}
			}
			
			return hasNextValue;
		}

		@Override
		public MedusaLeaf next()
		{
			if(currentNearPos == -1) {//return the first one
				currentNearPos = 0;
				currentLeaf = nearPositions.get(currentNearPos);
			}
			else if(currentNearPos +1 < nearPositions.size()){
					//Next branch
					currentNearPos += 1;
					currentLeaf = nearPositions.get(currentNearPos);
				}
			else {
				//TODO:FUMA: out of bound
				throw new NoSuchElementException();
			}
			return currentLeaf;
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
			
		}

		@Override
		public BranchIterator iterator()
		{
			currentNearPos = -1;
			currentLeaf = null;
			return this;
		}
		
	}
	//--------------------------------Iterator functions------------------------------------
}
