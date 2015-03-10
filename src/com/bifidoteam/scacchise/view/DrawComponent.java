package com.bifidoteam.scacchise.view;

import java.util.Iterator;

import com.bifidoteam.scacchise.model.Piece;
import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

public class DrawComponent {

	private char[] map = new char[Constants.MAX_INDEX];
	private char[] medusaMap = new char[Constants.MAX_INDEX];
	

	// ******************************************************************************************************************************
	// ****************************************************** PUBLIC FUNCTION *******************************************************
	// ******************************************************************************************************************************
	
	public void printMap(String printIntestation){

		// ** Header
		System.out.println("Print: " + printIntestation);
		System.out.print("   ");
		for(int j=0; j<Constants.MAX_INDEX_ROW; ++j){
			System.out.print(" " + ChessboardLetter.values()[j] + " ");
		}
		System.out.println();
		// ** END
		
		for(int i=0; i<Constants.MAX_INDEX_ROW; ++i){
			
			System.out.print(" " + i + " ");
			
			for(int j=0; j<Constants.MAX_INDEX_ROW; ++j){
				writeChar(map[i*Constants.MAX_INDEX_ROW + j]);
			}
			System.out.println();
		}
		System.out.println("\n\n");
		
		initMedusaMap(' ');
	}

	public DrawComponent(){
		initMap(' ');
		initMedusaMap(' ');
	}
	
	public void writePieceOnMap(int pos, Piece x){
		if(pos >= 0 && pos < Constants.MAX_INDEX)
			map[pos] = (char) (x.isWhite() == 0 ? x.GetSymbol(): (x.GetSymbol() +32));
	}
	
	public void writeMedusaOnMap(MedusaTree informations, boolean completed){
		
		if(informations != null){
			Iterator<Integer> medusaIterator = completed ? informations.GetCompleteIterator(): informations.GetCuttedIterator();

			while(medusaIterator.hasNext()){
				medusaMap[medusaIterator.next()] = 'X';
			}
		}
		
	}
	
	
	
	
	// ******************************************************************************************************************************
	// ****************************************************** PRIVATE FUNCTION ******************************************************
	// ******************************************************************************************************************************

	
	enum ChessboardLetter {
		A,
		B,
		C,
		D,
		E,
		F,
		G,
		H
	}
	
	private void initMap(char initValue){
		for(int i=0; i< map.length; ++i)
			map[i] = initValue;
	}
	
	private void initMedusaMap(char initValue){
		for(int i=0; i< medusaMap.length; ++i)
			medusaMap[i] = initValue;
	}
	
	private void writeChar(char c){
		System.out.print("[" + c + "]");
	}

	private void writeSpecialChar(char c){
		System.out.print("(" + c + ")");
	}
}
