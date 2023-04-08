package com.mraof.minestuck.computer;

import com.mraof.minestuck.player.PlayerIdentifier;

public interface ISburbComputer
{
	default void setIsResuming(boolean isClient)
	{
		if(isClient)
			putProgramBoolean(0, "isResuming", true);
		else putProgramBoolean(1, "isOpen", true);
	}
	
	boolean getProgramBoolean(int id, String name);
	void putProgramBoolean(int id, String name, boolean value);
	String getProgramMessage(int id);
	void putProgramMessage(int id, String message);
	
	void connected(PlayerIdentifier player, boolean isClient);
	void clearConnectedClient();
	
	PlayerIdentifier getOwner();
	ComputerReference createReference();
	
	Theme getTheme();
	void setTheme(Theme theme);
}
