package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.player.PlayerIdentifier;

public interface IOwnable
{
	void setOwner(PlayerIdentifier identifier);
	PlayerIdentifier getOwner();
}
