package com.bifidoteam.scacchise.model;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class MedusaTree implements Iterable<MedusaLeaf>,Iterator<MedusaLeaf>{
	
	//-----------------------------Private Variables----------------------------------------
	private List<MedusaLeaf> nearPositions;
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
	public void setNearPositions(List<MedusaLeaf> nearPositions) {
		this.nearPositions = nearPositions;
	}
	//--------------------------------Getter/Setter-----------------------------------------
	
	//-----------------------------Public functions-----------------------------------------
	public String toString(){
		String s = "";
		for(MedusaLeaf m:nearPositions)
		{
			s += m.toString() + "\n";
		}
		
		return s;
	}
	
	//Aggiunge alla fine dell'ultimo ramo creato
	public void AddLeafToLastBranch(int index){
		
	}
	
	//Crea nuovo ramo
	public void AddBranch(int index){
//		nearPositions.add(m);
//		return nearPositions.size()-1;
	}
	//-----------------------------Public functions-----------------------------------------

	//--------------------------------Iterator functions------------------------------------
	@Override
	public boolean hasNext()
	{
		//Continue in this branch
		if(currentLeaf.getNext() != null){
			return true;
		}
		//Change branch
		else {
			int tempCurrentNearPos = currentNearPos;
			while(tempCurrentNearPos +1 < nearPositions.size()){ //if is not the last
				tempCurrentNearPos += 1;
				//if this branch is not cut 
				if(!nearPositions.get(tempCurrentNearPos).IsCut()){
					return true;
				}
			}
		}
		return false;
			
	}

	@Override
	public MedusaLeaf next()
	{	
		if(currentLeaf.getNext() != null){
			//Next leaf in this branch
			currentLeaf = currentLeaf.getNext();
			return currentLeaf;
		}
		else {
			//while if is not the last branch
			while(currentNearPos +1 < nearPositions.size()){
				//Change branch
				currentNearPos += 1;
				//Take the first leaf in this branch
				currentLeaf = nearPositions.get(currentNearPos);
				//if this leaf is not cut 
				if(!currentLeaf.IsCut()){
					return currentLeaf;
				}
			}
		}
		
		//There are not next leaf or branch
		throw new NoSuchElementException();
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
		
	}

	@Override
	public Iterator<MedusaLeaf> iterator()
	{
		currentNearPos = 0;
		currentLeaf = nearPositions.get(currentNearPos);
		return this;
	}
	//--------------------------------Iterator functions------------------------------------
}
