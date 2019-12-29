package com.mraof.minestuck.event;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.Session;
import net.minecraft.server.MinecraftServer;

public class ConnectionCreatedEvent extends SburbEvent
{
	private final ConnectionType connectionType;
	private final SessionJoinType sessionJoinType;
	
	public ConnectionCreatedEvent(MinecraftServer mcServer, SburbConnection connection, Session session, ConnectionType connectionType, SessionJoinType sessionJoinType)
	{
		super(mcServer, connection, session);
		this.connectionType = connectionType;
		this.sessionJoinType = sessionJoinType;
	}
	
	public ConnectionType getConnectionType()
	{
		return connectionType;
	}
	
	public SessionJoinType getSessionJoinType()
	{
		return sessionJoinType;
	}
	
	public enum ConnectionType
	{
		REGULAR,
		SECONDARY,
		RESUME
	}
	
	public enum SessionJoinType
	{
		INTERNAL,
		JOIN,
		MERGE
	}
}