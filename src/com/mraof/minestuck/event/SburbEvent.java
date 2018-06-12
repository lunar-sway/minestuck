package com.mraof.minestuck.event;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.Session;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class SburbEvent extends Event
{
	private final SburbConnection connection;
	private final Session session;
	
	public SburbEvent(SburbConnection connection, Session session)
	{
		this.connection = connection;
		this.session = session;
	}
	
	public SburbConnection getConnection()
	{
		return connection;
	}
	
	public Session getSession()
	{
		return session;
	}
}
