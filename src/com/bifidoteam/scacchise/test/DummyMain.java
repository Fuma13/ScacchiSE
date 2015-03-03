package com.bifidoteam.scacchise.test;

import com.bifidoteam.scacchise.model.Chessboard;

public class DummyMain {

	public static void main(String[] args) {
		
		Chessboard x = new Chessboard();
		
		TestModule tm = new TestModule();
		
		
		tm.initializeMapWithMedusa(x.GetRealIndices(0), true);
		tm.printMap("LTower UP");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(1), true);
		tm.printMap("LHorse UP");

		tm.initializeMapWithMedusa(x.GetRealIndices(2), true);
		tm.printMap("LBishop UP");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(3), true);
		tm.printMap("Queen UP");

		tm.initializeMapWithMedusa(x.GetRealIndices(4), true);
		tm.printMap("King UP");

		tm.initializeMapWithMedusa(x.GetRealIndices(5), true);
		tm.printMap("RBishop UP");

		tm.initializeMapWithMedusa(x.GetRealIndices(6), true);
		tm.printMap("RHorse UP");

		tm.initializeMapWithMedusa(x.GetRealIndices(7), true);
		tm.printMap("RTower UP");
		

		
		tm.initializeMapWithMedusa(x.GetRealIndices(8), true);
		tm.printMap("Pawn 0");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(9), true);
		tm.printMap("Pawn 1");

		tm.initializeMapWithMedusa(x.GetRealIndices(10), true);
		tm.printMap("Pawn 2");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(11), true);
		tm.printMap("Pawn 3");

		tm.initializeMapWithMedusa(x.GetRealIndices(12), true);
		tm.printMap("Pawn 4");

		tm.initializeMapWithMedusa(x.GetRealIndices(13), true);
		tm.printMap("Pawn 5");

		tm.initializeMapWithMedusa(x.GetRealIndices(14), true);
		tm.printMap("Pawn 6");

		tm.initializeMapWithMedusa(x.GetRealIndices(15), true);
		tm.printMap("Pawn 7");
		
		
		
		
		tm.initializeMapWithMedusa(x.GetRealIndices(56), true);
		tm.printMap("LTower DW");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(57), true);
		tm.printMap("LHorse DW");

		tm.initializeMapWithMedusa(x.GetRealIndices(58), true);
		tm.printMap("LBishop DW");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(59), true);
		tm.printMap("Queen DW");

		tm.initializeMapWithMedusa(x.GetRealIndices(60), true);
		tm.printMap("King DW");

		tm.initializeMapWithMedusa(x.GetRealIndices(61), true);
		tm.printMap("RBishop DW");

		tm.initializeMapWithMedusa(x.GetRealIndices(62), true);
		tm.printMap("RHorse DW");

		tm.initializeMapWithMedusa(x.GetRealIndices(63), true);
		tm.printMap("RTower DW");
		

		
		tm.initializeMapWithMedusa(x.GetRealIndices(48), true);
		tm.printMap("Pawn DW 0");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(49), true);
		tm.printMap("Pawn DW 1");

		tm.initializeMapWithMedusa(x.GetRealIndices(50), true);
		tm.printMap("Pawn DW 2");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(51), true);
		tm.printMap("Pawn DW 3");

		tm.initializeMapWithMedusa(x.GetRealIndices(52), true);
		tm.printMap("Pawn DW 4");

		tm.initializeMapWithMedusa(x.GetRealIndices(53), true);
		tm.printMap("Pawn DW 5");

		tm.initializeMapWithMedusa(x.GetRealIndices(54), true);
		tm.printMap("Pawn DW 6");

		tm.initializeMapWithMedusa(x.GetRealIndices(55), true);
		tm.printMap("Pawn DW 7");
	}
	
}