package com.mraof.minestuck.computer;

import com.mraof.minestuck.player.PlayerIdentifier;

public interface ISburbComputer
{
	PlayerIdentifier getOwner();
	
	boolean getClientBoolean(String name);
	boolean getServerBoolean(String name);
	
	void putClientBoolean(String name, boolean value);
	void putServerBoolean(String name, boolean value);
	void clearConnectedClient();
	
	void putClientMessage(String message);
	void putServerMessage(String message);
	
	void connected(PlayerIdentifier player, boolean isClient);
}
