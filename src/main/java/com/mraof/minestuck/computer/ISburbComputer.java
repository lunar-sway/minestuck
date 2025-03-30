package com.mraof.minestuck.computer;

import com.mraof.minestuck.player.PlayerIdentifier;

import java.util.Optional;

public interface ISburbComputer
{
	PlayerIdentifier getOwner();
	
	Optional<SburbClientData> getSburbClientData();
	Optional<SburbServerData> getSburbServerData();
	
	ComputerReference createReference();
}
