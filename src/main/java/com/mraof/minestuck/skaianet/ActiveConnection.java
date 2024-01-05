package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.player.PlayerIdentifier;

public record ActiveConnection(PlayerIdentifier client, ComputerReference clientComputer,
							   PlayerIdentifier server, ComputerReference serverComputer)
{
	ActiveConnection(SburbConnection connection, ComputerReference clientComputer, ComputerReference serverComputer)
	{
		this(connection.getClientIdentifier(), clientComputer, connection.getServerIdentifier(), serverComputer);
	}
	
	public boolean isClient(ISburbComputer computer)
	{
		return this.clientComputer().matches(computer) && this.client().equals(computer.getOwner());
	}
	
	public boolean isServer(ISburbComputer computer)
	{
		return this.serverComputer().matches(computer) && this.server().equals(computer.getOwner());
	}
}
