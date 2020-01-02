package com.mraof.minestuck.event;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.Session;
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
}
