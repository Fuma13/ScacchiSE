package com.bifidoteam.scacchise.view;

import java.awt.Color;

import com.bifidoteam.scacchise.controller.GameManager;
import com.bifidoteam.scacchise.interfaces.ControllerInterface;
import com.bifidoteam.scacchise.interfaces.LogType;
import com.bifidoteam.scacchise.interfaces.ViewInterface;
import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.scacchise.util.Constants;
import com.bifidoteam.util.MedusaTree;

public class SwingView implements ViewInterface {

	ControllerInterface gm;
	SwingComponent sc;
	SwingLogComponent log;
	
	@Override
	public void Init(Chessboard base) {
		gm = GameManager.getInstance();
		sc = new SwingComponent(base);
		
		if(Constants.DEBUG_MODE)
			log = new SwingLogComponent();
		
		javax.swing.SwingUtilities.invokeLater(sc);
	}
	
	@Override
	public void Render(MedusaTree reacheblePosition) {
		if(sc.IsInitialize())
			sc.RenderWithMedusa(reacheblePosition);
	}

	@Override
	public void MoveFromStartIndexToEndIndex(int startIndex, int endIndex) {
		// TODO: (Ricky) Farlo testuale nella schermata!
		Log("Player move from " + startIndex + " to " + endIndex, LogType.LOG);
		Log("Player end turn", LogType.LOG);

		gm.onMoveDone();
	}

	@Override
	public void EndGame(int info) {
		// TODO: (Ricky) Farlo testuale nella schermata!
		System.out.println("CheckMate!!! Player " + (info == 0 ? "White" : "Black") + " Win!!");
	}

	@Override
	public int GetInput() {

		// *** NOTA: Qui non e' corretto!! Bisogna gestirlo lato Controller!!! (Non attesa). Parlare con gli altri
		while(!sc.HasNewEvent()){}
			return sc.GetNextEvent();
			
//		if(sc.HasNewEvent())
//			return sc.GetNextEvent();
//		else{
//			return -1;
//		}
	}

	@Override
	public void Log(String message, LogType type) {

		if(Constants.DEBUG_MODE){
			log.logMessage(message, type);
		}
	}

}
