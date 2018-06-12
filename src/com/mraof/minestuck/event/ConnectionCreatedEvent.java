package com.mraof.minestuck.event;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.Session;

public class ConnectionCreatedEvent extends SburbEvent
{
	private final ConnectionType connectionType;
	private final SessionJoinType sessionJoinType;
	
	public ConnectionCreatedEvent(SburbConnection connection, Session session, ConnectionType connectionType, SessionJoinType sessionJoinType)
	{
		super(connection, session);
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