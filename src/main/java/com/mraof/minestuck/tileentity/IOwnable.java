package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.util.PlayerIdentifier;

public interface IOwnable
{
	void setOwner(PlayerIdentifier identifier);
	PlayerIdentifier getOwner();
}
