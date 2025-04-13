package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.computer.SburbServerData;

import java.util.Optional;

final class ServerAccess
{
	private final ISburbComputer computer;
	private final SburbServerData data;
	
	private ServerAccess(ISburbComputer computer, SburbServerData data)
	{
		this.computer = computer;
		this.data = data;
	}
	
	static Optional<ServerAccess> tryGet(ISburbComputer computer)
	{
		return computer.getSburbServerData().map(data -> new ServerAccess(computer, data));
	}
	
	ISburbComputer computer()
	{
		return computer;
	}
	
	SburbServerData data()
	{
		return data;
	}
}
