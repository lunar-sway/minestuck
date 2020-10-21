package com.mraof.minestuck.event;

import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.Session;
import net.minecraft.server.MinecraftServer;

public class ConnectionCreatedEvent extends SburbEvent
{
	private final ConnectionType connectionType;
	
	public ConnectionCreatedEvent(MinecraftServer mcServer, SburbConnection connection, Session session, ConnectionType connectionType)
	{
		super(mcServer, connection, session);
		this.connectionType = connectionType;
	}
	
	public ConnectionType getConnectionType()
	{
		return connectionType;
	}
	
	public enum ConnectionType
	{
		REGULAR,
		SECONDARY,
		RESUME,
		NEW_SERVER
	}
}