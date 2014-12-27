package com.bifidoteam.scacchise.interfaces;

import com.bifidoteam.util.MedusaTree;

public interface ViewInterface
{
	void DrowReacheblePosition(MedusaTree reacheblePosition);
	void MoveFromStartIndexToEndIndex(int startIndex, int endIndex);
	void OnConfigurationChange();
}
