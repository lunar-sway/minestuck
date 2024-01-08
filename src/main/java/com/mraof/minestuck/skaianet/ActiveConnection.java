package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.computer.ComputerReference;
import com.mraof.minestuck.computer.ISburbComputer;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public final class ActiveConnection
{
	private final PlayerIdentifier client;
	private final ComputerReference clientComputer;
	private final PlayerIdentifier server;
	private final ComputerReference serverComputer;
	
	ActiveConnection(PlayerIdentifier client, ComputerReference clientComputer,
							PlayerIdentifier server, ComputerReference serverComputer)
	{
		this.client = client;
		this.clientComputer = clientComputer;
		this.server = server;
		this.serverComputer = serverComputer;
	}
	
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
	
	boolean hasPlayer(PlayerIdentifier player)
	{
		return this.client().equals(player) || this.server().equals(player);
	}
	
	public PlayerIdentifier client()
	{
		return client;
	}
	
	public ComputerReference clientComputer()
	{
		return clientComputer;
	}
	
	public PlayerIdentifier server()
	{
		return server;
	}
	
	public ComputerReference serverComputer()
	{
		return serverComputer;
	}
	
	@Nullable
	public Vec3 lastEditmodePosition;
}
