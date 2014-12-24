package com.bifidoteam.util;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MedusaTree implements Iterable<Integer>,Iterator<Integer>{
	
	//-----------------------------Private Variables----------------------------------------
	private List<MedusaLeaf> nearPositions;
	private MedusaLeaf lastLeaf;
	//-----------------------------Iterator variables
	private int currentNearPos;
	private MedusaLeaf currentLeaf;
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
		Iterator<Integer> mtIterator = iterator();
		boolean notFound = true;
		while(mtIterator.hasNext() && notFound) {
			if(mtIterator.next() == index)
				notFound = true;
		}
		return notFound;
	}
	
	public void CutBeforeAndAfter() {
		if(currentNearPos != -1) {
			if(currentLeaf != null) {
				//Cut the current leaf
				currentLeaf.setIsCut(true);
				if(currentLeaf.getNext() != null) {
					//Cut also the next so the iterator can change branch
					currentLeaf.getNext().setIsCut(true);
				}
			}
		}
		
/*		if(currentNearPos != -1) {
			if(nearPositions.get(currentNearPos) == currentLeaf) { //If it is the first leaf, then cut this branch
				nearPositions.remove(currentNearPos);
				currentLeaf = nearPositions.get(currentNearPos);
				if(currentNearPos != 0) { //If it wasn't the first one, then set the last leaf of the previous branch
					currentNearPos--;
					while(currentLeaf.getNext() != null) {
						currentLeaf = currentLeaf.getNext();
					}
				} //else the currentNearPos is 0 and the currentLeaf is the first of the new 0 branch
			}
			else { //Searching the parent of the current leaf and cut it
				MedusaLeaf tempCurrentLeaf = nearPositions.get(currentNearPos);
				while(tempCurrentLeaf.getNext() != currentLeaf) {
					tempCurrentLeaf = tempCurrentLeaf.getNext();
				}
				
				if(tempCurrentLeaf.getNext() == currentLeaf) {
					tempCurrentLeaf.setNext(null);
				}
			}
		}
 */
	}
	
	public void CutAfter() {
		if(currentNearPos != -1) {
			if(currentLeaf != null && currentLeaf.getNext() != null) { //Check if it is not the last
				//Remove the next leaf
				currentLeaf.getNext().setIsCut(true);
			} //if is the last there is nothing to cut
		}
	}
	//-----------------------------Public functions-----------------------------------------

	//--------------------------------Iterator functions------------------------------------
	//TODO:FUMA: Controllare che l'iterator sia giusto
	@Override
	public boolean hasNext() {
		boolean hasNextValue = false;
		if(currentNearPos == -1) {//check on the first one
			MedusaLeaf tempCurrentLeaf = nearPositions.get(0);
			if(tempCurrentLeaf != null) {
				if(!tempCurrentLeaf.IsCut())
					hasNextValue = true;
				//Change branch
				else {
					hasNextValue = hasNextChangeBranch();
				}
			}//Else Tree Empty
		}
		else {
			//Continue in this branch
			if(!currentLeaf.IsCut() && currentLeaf.getNext() != null){
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
			if(nearPositions.get(tempCurrentNearPos)!= null && !nearPositions.get(tempCurrentNearPos).IsCut()){
				hasNextValue = true;
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
					nextIndex = NextChangeBranch();
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
				nextIndex = NextChangeBranch();
			}
		}
		//There are not next leaf or branch
		return nextIndex;
	}
	
	private Integer NextChangeBranch() {
		Integer nextIndex = null;
		//while if is not the last branch
		while(currentNearPos +1 < nearPositions.size()){
			//Change branch
			currentNearPos += 1;
			//Take the first leaf in this branch
			currentLeaf = nearPositions.get(currentNearPos);
			//if this leaf is not cut 
			if(!currentLeaf.IsCut()){
				nextIndex = currentLeaf.getValue();
			}
		}
		return nextIndex;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public Iterator<Integer> iterator() {
		currentNearPos = -1;
		currentLeaf = null;
		return this;
	}
	//--------------------------------Iterator functions------------------------------------
}
