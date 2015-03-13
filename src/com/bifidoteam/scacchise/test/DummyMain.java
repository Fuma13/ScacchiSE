package com.bifidoteam.scacchise.test;

import com.bifidoteam.scacchise.controller.GameManager;
import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.scacchise.util.Constants;

public class DummyMain {

	public static void main(String[] args) {
		
		Chessboard x = new Chessboard();
		
		TestModule tm = new TestModule();
		
		tm.initializeMapWithMedusa(x.GetRealIndices(0), true);
		tm.writePieceOnMap(0, x.GetPiece(0));
		tm.printMap("LTower UP");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(1), true);
		tm.writePieceOnMap(1, x.GetPiece(1));
		tm.printMap("LHorse UP");

		tm.initializeMapWithMedusa(x.GetRealIndices(2), true);
		tm.writePieceOnMap(2, x.GetPiece(2));
		tm.printMap("LBishop UP");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(3), true);
		tm.writePieceOnMap(3, x.GetPiece(3));
		tm.printMap("Queen UP");

		tm.initializeMapWithMedusa(x.GetRealIndices(4), true);
		tm.writePieceOnMap(4, x.GetPiece(4));
		tm.printMap("King UP");

		tm.initializeMapWithMedusa(x.GetRealIndices(5), true);
		tm.writePieceOnMap(5, x.GetPiece(5));
		tm.printMap("RBishop UP");

		tm.initializeMapWithMedusa(x.GetRealIndices(6), true);
		tm.writePieceOnMap(6, x.GetPiece(6));
		tm.printMap("RHorse UP");

		tm.initializeMapWithMedusa(x.GetRealIndices(7), true);
		tm.writePieceOnMap(7, x.GetPiece(7));
		tm.printMap("RTower UP");
		

		
		tm.initializeMapWithMedusa(x.GetRealIndices(8), true);
		tm.writePieceOnMap(8, x.GetPiece(8));
		tm.printMap("Pawn 0");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(9), true);
		tm.writePieceOnMap(9, x.GetPiece(9));
		tm.printMap("Pawn 1");

		tm.initializeMapWithMedusa(x.GetRealIndices(10), true);
		tm.writePieceOnMap(10, x.GetPiece(10));
		tm.printMap("Pawn 2");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(11), true);
		tm.writePieceOnMap(11, x.GetPiece(11));
		tm.printMap("Pawn 3");

		tm.initializeMapWithMedusa(x.GetRealIndices(12), true);
		tm.writePieceOnMap(12, x.GetPiece(12));
		tm.printMap("Pawn 4");

		tm.initializeMapWithMedusa(x.GetRealIndices(13), true);
		tm.writePieceOnMap(13, x.GetPiece(13));
		tm.printMap("Pawn 5");

		tm.initializeMapWithMedusa(x.GetRealIndices(14), true);
		tm.writePieceOnMap(14, x.GetPiece(14));
		tm.printMap("Pawn 6");

		tm.initializeMapWithMedusa(x.GetRealIndices(15), true);
		tm.writePieceOnMap(15, x.GetPiece(15));
		tm.printMap("Pawn 7");
		
		
		
		
		tm.initializeMapWithMedusa(x.GetRealIndices(56), true);
		tm.writePieceOnMap(56, x.GetPiece(56));
		tm.printMap("LTower DW");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(57), true);
		tm.writePieceOnMap(57, x.GetPiece(57));
		tm.printMap("LHorse DW");

		tm.initializeMapWithMedusa(x.GetRealIndices(58), true);
		tm.writePieceOnMap(58, x.GetPiece(58));
		tm.printMap("LBishop DW");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(59), true);
		tm.writePieceOnMap(59, x.GetPiece(59));
		tm.printMap("Queen DW");

		tm.initializeMapWithMedusa(x.GetRealIndices(60), true);
		tm.writePieceOnMap(60, x.GetPiece(60));
		tm.printMap("King DW");

		tm.initializeMapWithMedusa(x.GetRealIndices(61), true);
		tm.writePieceOnMap(61, x.GetPiece(61));
		tm.printMap("RBishop DW");

		tm.initializeMapWithMedusa(x.GetRealIndices(62), true);
		tm.writePieceOnMap(62, x.GetPiece(62));
		tm.printMap("RHorse DW");

		tm.initializeMapWithMedusa(x.GetRealIndices(63), true);
		tm.writePieceOnMap(63, x.GetPiece(63));
		tm.printMap("RTower DW");
		

		
		tm.initializeMapWithMedusa(x.GetRealIndices(48), true);
		tm.writePieceOnMap(48, x.GetPiece(48));
		tm.printMap("Pawn DW 0");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(49), true);
		tm.writePieceOnMap(49, x.GetPiece(49));
		tm.printMap("Pawn DW 1");

		tm.initializeMapWithMedusa(x.GetRealIndices(50), true);
		tm.writePieceOnMap(50, x.GetPiece(50));
		tm.printMap("Pawn DW 2");
		
		tm.initializeMapWithMedusa(x.GetRealIndices(51), true);
		tm.writePieceOnMap(51, x.GetPiece(51));
		tm.printMap("Pawn DW 3");

		tm.initializeMapWithMedusa(x.GetRealIndices(52), true);
		tm.writePieceOnMap(52, x.GetPiece(52));
		tm.printMap("Pawn DW 4");

		tm.initializeMapWithMedusa(x.GetRealIndices(53), true);
		tm.writePieceOnMap(53, x.GetPiece(53));
		tm.printMap("Pawn DW 5");

		tm.initializeMapWithMedusa(x.GetRealIndices(54), true);
		tm.writePieceOnMap(54, x.GetPiece(54));
		tm.printMap("Pawn DW 6");

		tm.initializeMapWithMedusa(x.GetRealIndices(55), true);
		tm.writePieceOnMap(55, x.GetPiece(55));
		tm.printMap("Pawn DW 7");
		
		// COMPLEX EXAMPLE:
		tm = new TestModule();
		TestModule tm2 = new TestModule();
		for(int i=0; i<Constants.MAX_INDEX_ROW*2; ++i){
			tm.writePieceOnMap(i, x.GetPiece(i));
			tm2.writePieceOnMap(i, x.GetPiece(i));
		}
		for(int i=0; i<Constants.MAX_INDEX_ROW*2; ++i){
			tm.writePieceOnMap(Constants.MAX_INDEX -1 - i, x.GetPiece(Constants.MAX_INDEX -1 - i));
			tm2.writePieceOnMap(Constants.MAX_INDEX -1 - i, x.GetPiece(Constants.MAX_INDEX -1 - i));
		}
		
		GameManager gm = GameManager.getInstance(x);
		
		int piece = 63;
		int color = 0;
		tm.writeMedusaOnMap(gm.GetReachebleIndicesDebug(piece,color), false);
		tm2.writeMedusaOnMap(gm.GetReachebleIndicesDebug(piece,color), true);
		
		
		tm2.printMap("Complex example = full chessboard + all movements");
		tm.printMap("Complex example = full chessboard + possibile movements");
		
	}
	
}