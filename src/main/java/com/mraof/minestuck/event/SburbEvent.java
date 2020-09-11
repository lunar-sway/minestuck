package com.mraof.minestuck.event;

import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.Session;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.Event;

public abstract class SburbEvent extends Event
{
	private final MinecraftServer mcServer;
	private final SburbConnection connection;
	private final Session session;
	
	public SburbEvent(MinecraftServer mcServer, SburbConnection connection, Session session)
	{
		this.connection = connection;
		this.session = session;
		this.mcServer = mcServer;
	}
	
	public SburbConnection getConnection()
	{
		return connection;
	}
	
	public Session getSession()
	{
		return session;
	}
	
	public MinecraftServer getMinecraftServer()
	{
		return mcServer;
	}
	
	public static class OnEntry extends SburbEvent
	{
		public OnEntry(MinecraftServer mcServer, SburbConnection connection, Session session)
		{
			super(mcServer, connection, session);
		}
	}
}
