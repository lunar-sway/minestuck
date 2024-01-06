package com.mraof.minestuck.skaianet.client;

import com.mraof.minestuck.player.NamedPlayerId;
import com.mraof.minestuck.skaianet.SburbConnection;
import net.minecraft.network.FriendlyByteBuf;

/**
 * The client side version of {@link SburbConnection}
 */
public class ReducedConnection
{
	private final NamedPlayerId client, server;
	
	private final boolean isActive;
	private final boolean isMain;
	private final boolean hasEntered;
	
	//client side
	public String getClientDisplayName() {return client.name();}
	public String getServerDisplayName() {return server.name();}
	public int getClientId() {return client.id();}
	public int getServerId() {return server.id();}
	public boolean isActive() {return isActive;}
	public boolean isMain() {return isMain;}
	public boolean hasEntered() {return hasEntered;}
	
	/**
	 * Reads a connection from a network buffer. Must match with {@link SburbConnection#toBuffer}.
	 */
	public static ReducedConnection read(FriendlyByteBuf buffer)
	{
		return new ReducedConnection(buffer);
	}
	
	private ReducedConnection(FriendlyByteBuf buffer)
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
		client = NamedPlayerId.read(buffer);
		server = NamedPlayerId.read(buffer);
	}
}