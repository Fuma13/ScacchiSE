package com.bifidoteam.scacchise.interfaces;

import com.bifidoteam.scacchise.model.Chessboard;
import com.bifidoteam.util.MedusaTree;

public interface ViewInterface
{
	void Init(Chessboard base);
	void Render(MedusaTree reacheblePosition);
	void Log(String message, LogType type);
	void MoveFromStartIndexToEndIndex(int startIndex, int endIndex);
	void EndGame(int info);
	//void OnConfigurationChange();
	int GetInput();
}
