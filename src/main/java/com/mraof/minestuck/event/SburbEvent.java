package com.mraof.minestuck.event;

import com.mraof.minestuck.skaianet.ActiveConnection;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.Event;

public abstract class SburbEvent extends Event
{
	private final MinecraftServer mcServer;
	private final ActiveConnection connection;
	
	public SburbEvent(MinecraftServer mcServer, ActiveConnection connection)
	{
		this.connection = connection;
		this.mcServer = mcServer;
	}
	
	public ActiveConnection getConnection()
	{
		return connection;
	}
	
	public MinecraftServer getMinecraftServer()
	{
		return mcServer;
	}
	
	
	public static class ConnectionCreated extends SburbEvent
	{
		private final ConnectionType connectionType;
		
		public ConnectionCreated(MinecraftServer mcServer, ActiveConnection connection, ConnectionType connectionType)
		{
			super(mcServer, connection);
			this.connectionType = connectionType;
		}
		
		@SuppressWarnings("unused")
		public ConnectionType getConnectionType()
		{
			return connectionType;
		}
		
	}
	
	public static final class ConnectionClosed extends SburbEvent
	{
		public ConnectionClosed(MinecraftServer mcServer, ActiveConnection connection)
		{
			super(mcServer, connection);
		}
	}
	
	public enum ConnectionType
	{
		REGULAR,
		SECONDARY,
		RESUME,
		NEW_SERVER
	}
}
