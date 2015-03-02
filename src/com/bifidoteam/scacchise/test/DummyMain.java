package com.bifidoteam.scacchise.test;

import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

public class DummyMain {

	public static void main(String[] args) {
		
		initializeMapWithMedusa(null);
		printMap();
		
	}
	
	private static char[] map = new char[Constants.MAX_INDEX];
	
	private static void initMap(char initValue){
		for(int i=0; i< map.length; ++i)
			map[i] = initValue;
	}
	
	private static void initializeMapWithMedusa(MedusaTree informations){

		initMap(' ');

		// TODO: iter on the iterator and updates the map[]
		
	}
	
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
	
	private static void printMap(){

		// ** Header
		System.out.print("   ");
		for(int j=0; j<Constants.MAX_INDEX_ROW; ++j){
			System.out.print(" " + ChessboardLetter.values()[j] + " ");
		}
		System.out.println();
		// ** END
		
		// TODO: for each object of map and call writeChar() to create stamp
		for(int i=0; i<Constants.MAX_INDEX_ROW; ++i){
			
			System.out.print(" " + i + " ");
			
			for(int j=0; j<Constants.MAX_INDEX_ROW; ++j){
				writeChar(map[i*Constants.MAX_INDEX_ROW + j]);
			}
			System.out.println();
		}
		
//		for(int i=0; i< Constants.MAX_INDEX_ROW; i++)
//			writeWhiteLine();
	}
	
	private static void writeChar(char c){
		System.out.print("[" + c + "]");
	}
	
//	private static void writeWhiteLine(){
//		for(int i=0; i< Constants.MAX_INDEX_ROW; ++i){
//			writeChar(' ');
//		}
//		System.out.println();
//	}

}