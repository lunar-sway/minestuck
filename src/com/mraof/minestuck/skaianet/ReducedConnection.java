package com.mraof.minestuck.skaianet;

import net.minecraft.network.PacketBuffer;

/**
 * The client side version of {@link SburbConnection}
 */
public class ReducedConnection
{
	/**
	 * Display name used by computer guis
	 */
	String clientName, serverName;
	/**
	 * Id for identifying players clientside
	 */
	int clientId, serverId;
	
	boolean isActive;
	boolean isMain;
	boolean hasEntered;
	
	private ReducedConnection()
	{
		this.isActive = true;
	}
	
	//client side
	public String getClientDisplayName() {return clientName;}
	public String getServerDisplayName() {return serverName;}
	public int getClientId() {return clientId;}
	public int getServerId() {return serverId;}
	
	/**
	 * Reads a connection from a network buffer. Must match with {@link SburbConnection#toBuffer}.
	 */
	public static ReducedConnection read(PacketBuffer buffer)
	{
		ReducedConnection c = new ReducedConnection();
		
		c.isMain = buffer.readBoolean();
		if(c.isMain)
		{
			c.isActive = buffer.readBoolean();
			c.hasEntered = buffer.readBoolean();
		}
		c.clientId = buffer.readInt();
		c.clientName = buffer.readString(16);
		c.serverId = buffer.readInt();
		c.serverName = buffer.readString(16);
		
		return c;
	}
}