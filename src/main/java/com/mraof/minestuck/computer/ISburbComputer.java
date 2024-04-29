package com.mraof.minestuck.computer;

import com.mraof.minestuck.player.PlayerIdentifier;

public interface ISburbComputer
{
	PlayerIdentifier getOwner();
	
	SburbClientData getSburbClientData();
	
	boolean getServerBoolean(String name);
	
	void putServerBoolean(String name, boolean value);
	
	void clearConnectedClient();
	
	void putClientMessage(String message);
	void putServerMessage(String message);
	
	ComputerReference createReference();
}
