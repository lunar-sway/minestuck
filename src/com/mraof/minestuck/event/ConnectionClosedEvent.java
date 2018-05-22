package com.mraof.minestuck.event;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.Session;

public class ConnectionClosedEvent extends SburbEvent
{
	private final ConnectionCreatedEvent.ConnectionType connectionType;
	
	public ConnectionClosedEvent(SburbConnection connection, Session session, ConnectionCreatedEvent.ConnectionType connectionType)
	{
		super(connection, session);
		this.connectionType = connectionType;
	}
	
	public ConnectionCreatedEvent.ConnectionType getConnectionType()
	{
		return connectionType;
	}
}
