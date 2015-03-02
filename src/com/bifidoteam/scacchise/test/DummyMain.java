package com.bifidoteam.scacchise.test;

import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

public class DummyMain {

	public static void main(String[] args) {
		
		printMedusa(null);
		
	}
	
	private static void printMedusa(MedusaTree toPrint){
		
		// TODO: iter on the iterator and  call writeChar() to create the tree

		for(int i=0; i< Constants.MAX_INDEX_ROW; i++)
			writeWhiteLine();
	}
	
	private static void writeChar(char c){
		System.out.print("[" + c + "]");
	}
	
	private static void writeWhiteLine(){
		for(int i=0; i< Constants.MAX_INDEX_ROW; ++i){
			writeChar(' ');
		}
		System.out.println();
	}

}