package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.player.PlayerIdentifier;

public interface IOwnable
{
	void setOwner(PlayerIdentifier identifier);
	PlayerIdentifier getOwner();
}
