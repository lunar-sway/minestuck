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
	private final String clientName, serverName;
	/**
	 * Id for identifying players clientside
	 */
	private final int clientId, serverId;
	
	private final boolean isActive;
	private final boolean isMain;
	private final boolean hasEntered;
	
	//client side
	public String getClientDisplayName() {return clientName;}
	public String getServerDisplayName() {return serverName;}
	public int getClientId() {return clientId;}
	public int getServerId() {return serverId;}
	public boolean isActive() {return isActive;}
	public boolean isMain() {return isMain;}
	public boolean hasEntered() {return hasEntered;}
	
	/**
	 * Reads a connection from a network buffer. Must match with {@link SburbConnection#toBuffer}.
	 */
	public static ReducedConnection read(PacketBuffer buffer)
	{
		return new ReducedConnection(buffer);
	}
	
	private ReducedConnection(PacketBuffer buffer)
	{
		isMain = buffer.readBoolean();
		if(isMain)
		{
			isActive = buffer.readBoolean();
			hasEntered = buffer.readBoolean();
		} else
		{
			isActive = true;
			hasEntered = false;
		}
		clientId = buffer.readInt();
		clientName = buffer.readString(16);
		serverId = buffer.readInt();
		serverName = buffer.readString(16);
	}
}