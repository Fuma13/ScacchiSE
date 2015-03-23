package com.bifidoteam.scacchise.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javafx.scene.layout.Border;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.bifidoteam.scacchise.controller.GameManager;
import com.bifidoteam.scacchise.interfaces.ControllerInterface;
import com.bifidoteam.scacchise.interfaces.ViewInterface;
import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.util.MedusaTree;

public class SwingView implements ViewInterface {

	ControllerInterface gm;
	SwingComponent sc;
	
	@Override
	public void Init(Chessboard base) {
		gm = GameManager.getInstance();
		sc = new SwingComponent();
		
		javax.swing.SwingUtilities.invokeLater(sc);
	}
	
	@Override
	public void Render(MedusaTree reacheblePosition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void MoveFromStartIndexToEndIndex(int startIndex, int endIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void EndGame(int info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int GetInput() {
		if(sc.HasNewEvent())
			return sc.GetNextEvent();
		else
			return -1;
	}

}
