package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.computer.SburbClientData;

import java.util.Optional;

final class ClientAccess
{
	private final ISburbComputer computer;
	private final SburbClientData data;
	
	private ClientAccess(ISburbComputer computer, SburbClientData data)
	{
		this.computer = computer;
		this.data = data;
	}
	
	static Optional<ClientAccess> tryGet(ISburbComputer computer)
	{
		return computer.getSburbClientData().map(data -> new ClientAccess(computer, data));
	}
	
	ISburbComputer computer()
	{
		return computer;
	}
	
	SburbClientData data()
	{
		return data;
	}
}
