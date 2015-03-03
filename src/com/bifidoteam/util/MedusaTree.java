package com.bifidoteam.util;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.R.bool;

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
	
	//-----------------------------Public functions-----------------------------------------
	public String toString() {
		String s = "";
		for(MedusaLeaf m:nearPositions) {
			s += m.toString() + "\n";
		}
		
		return s;
	}
	
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
	
	//TODO:FUMA: Funzione merge
	public void MergeMedusaTreeNewBanch(MedusaTree other){
		
//		CompleteIterator mtIterator = other.GetCompleteIterator();
//		while(mtIterator.hasNext())
//		{
//			Integer leaf = mtIterator.next();
//			AddBranch(leaf);
//		}
		
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
		//TODO:FUMA: Controllare che l'iterator sia giusto
		//-----------------------------Iterator variables
		private int currentNearPos;
		private MedusaLeaf currentLeaf;
		
		@Override
		public boolean hasNext() {
			boolean hasNextValue = false;
			if(currentNearPos == -1) {//check on the first one
				MedusaLeaf tempCurrentLeaf = nearPositions.get(0);
				if(tempCurrentLeaf != null) {
					if(!tempCurrentLeaf.IsCut())
						hasNextValue = true;
					//Search in depth or Change branch
					else {
						hasNextValue = hasNextUncutted();
					}
				}//Else Tree Empty
			}
			else {
				//Continue in this branch
				if(!currentLeaf.IsCut() && currentLeaf.getNext() != null){
					hasNextValue = true;
				}
				//Search in depth or Change branch
				else {
					hasNextValue = hasNextUncutted();
				}
			}
			return hasNextValue;
				
		}
		
		//Search the first uncutted in this branch
		//If there is not change branch
		private boolean hasNextUncutted() {
			boolean hasNextValue = false;
			MedusaLeaf tempCurrentLeaf = currentLeaf;
			do{
				tempCurrentLeaf = tempCurrentLeaf.getRealNext();
				if(tempCurrentLeaf!= null && !tempCurrentLeaf.IsCut()){
					hasNextValue = true;
					break;
				}
			}while(tempCurrentLeaf != null);
			
			if(tempCurrentLeaf == null) {
				hasNextValue = hasNextChangeBranch();
			}
			
			return hasNextValue;
		}
		
		private boolean hasNextChangeBranch() {
			boolean hasNextValue = false;
			int tempCurrentNearPos = currentNearPos;
			while(tempCurrentNearPos +1 < nearPositions.size() && !hasNextValue){ //if is not the last
				tempCurrentNearPos += 1;
				if(nearPositions.get(tempCurrentNearPos)!= null) {
					//if this branch is not cut 
					if(!nearPositions.get(tempCurrentNearPos).IsCut()){
						hasNextValue = true;
					}
					else {
						hasNextValue = hasNextUncutted();
					}
				}
			}
			return hasNextValue;
		}
	
		@Override
		public Integer next() {
			Integer nextIndex = null;
			if(currentNearPos == -1) {//return the first one
				currentNearPos = 0;
				currentLeaf = nearPositions.get(currentNearPos);
				if(currentLeaf != null) {
					if(!currentLeaf.IsCut()) {
						nextIndex = currentLeaf.getValue();
					}
					else {
						nextIndex = NextUncutted();
					}
				}// Else Tree Empty
			}
			else {
				if(currentLeaf.getNext() != null){
					//Next leaf in this branch
					currentLeaf = currentLeaf.getNext();
					nextIndex =  currentLeaf.getValue();
				}
				else {
					
					nextIndex = NextUncutted();
				}
			}
			//There are not next leaf or branch
			return nextIndex;
		}
		
		//Search the first uncutted in this branch
		//If there is not change branch
		private Integer NextUncutted() {
			Integer nextIndex = null;
			do{
				currentLeaf = currentLeaf.getRealNext();
				if(currentLeaf!= null && !currentLeaf.IsCut()){
					nextIndex = currentLeaf.getValue();
					break;
				}
			}while(currentLeaf != null);
			
			if(currentLeaf == null) {
				nextIndex = NextChangeBranch();
			}
			
			return nextIndex;
		}
		
		//Change branch, If the first  is cutted
		//Search the first uncutted in this branch
		private Integer NextChangeBranch() {
			Integer nextIndex = null;
			//while if is not the last branch
			while(currentNearPos +1 < nearPositions.size() && nextIndex == null){
				//Change branch
				currentNearPos += 1;
				//Take the first leaf in this branch
				currentLeaf = nearPositions.get(currentNearPos);
				//if this leaf is not cut 
				if(!currentLeaf.IsCut()){
					nextIndex = currentLeaf.getValue();
				}
				else {
					nextIndex = NextUncutted();
				}
			}
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
				MedusaLeaf tempCurrentLeaf = nearPositions.get(0);
				if(tempCurrentLeaf != null) {
						hasNextValue = true;
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

		@Override
		public Integer next()
		{
			Integer nextIndex = null;
			if(currentNearPos == -1) {//return the first one
				currentNearPos = 0;
				currentLeaf = nearPositions.get(currentNearPos);
				if(currentLeaf != null) {
						nextIndex = currentLeaf.getValue();
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
			return nextIndex;
		}
		
		private Integer NextChangeBranch() {
			Integer nextIndex = null;
			//while if is not the last branch
			if(currentNearPos +1 < nearPositions.size() && nextIndex == null){
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
	
	//--------------------------------Iterator functions------------------------------------
}
