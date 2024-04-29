package com.mraof.minestuck.computer;

import com.mraof.minestuck.player.PlayerIdentifier;

public interface ISburbComputer
{
	PlayerIdentifier getOwner();
	
	SburbClientData getSburbClientData();
	SburbServerData getSburbServerData();
	
	void putClientMessage(String message);
	void putServerMessage(String message);
	
	ComputerReference createReference();
}
