package com.mraof.minestuck.computer;

import com.mraof.minestuck.player.PlayerIdentifier;

public interface ISburbComputer
{
	default void setIsResuming(boolean isClient)
	{
		if(isClient)
			putClientBoolean("isResuming", true);
		else putServerBoolean("isOpen", true);
	}
	
	boolean getClientBoolean(String name);
	boolean getServerBoolean(String name);
	void putClientBoolean(String name, boolean value);
	void putServerBoolean(String name, boolean value);
	
	String getProgramMessage(int id);
	void putProgramMessage(int id, String message);
	
	void connected(PlayerIdentifier player, boolean isClient);
	void clearConnectedClient();
	
	PlayerIdentifier getOwner();
	ComputerReference createReference();
	
	Theme getTheme();
	void setTheme(Theme theme);
}
