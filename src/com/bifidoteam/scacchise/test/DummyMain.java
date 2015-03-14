package com.bifidoteam.scacchise.test;

import com.bifidoteam.scacchise.controller.GameManager;

public class DummyMain {

	public static void main(String[] args) {
		
		GameManager x = GameManager.getInstance();
		
		x.GameLoop();
		
	}
	
}