package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;

import java.util.*;

/**
 * An extension to SkaianetHandler with a focus on sessions
 * @author kirderf1
 */
public abstract class SessionHandler
{
	final SkaianetHandler skaianetHandler;
	
	SessionHandler(SkaianetHandler skaianetHandler)
	{
		this.skaianetHandler = skaianetHandler;
	}
	
	abstract void write(CompoundTag compound);
	
	abstract SessionHandler getActual();
	
	/**
	 * Looks for the session that the player is a part of.
	 * @param player A string of the player's username.
	 * @return A session that contains at least one connection, that the player is a part of.
	 */
	public abstract Session getPlayerSession(PlayerIdentifier player);
	
	public abstract Set<Session> getSessions();
	
	abstract Session prepareSessionFor(PlayerIdentifier... players);
	
	boolean doesSessionHaveMaxTier(Session session)
	{
		return session.completed;
	}
	
	void onConnectionChainBroken(Session session)
	{}
	
	boolean canMakeSecondaryConnection(PlayerIdentifier client, PlayerIdentifier server)
	{
		return MinestuckConfig.SERVER.allowSecondaryConnections.get()
				&& skaianetHandler.hasPrimaryConnectionForClient(client)
				&& getPlayerSession(client) == getPlayerSession(server);
	}
	
	void onConnectionClosed(ActiveConnection connection)
	{
		Session s = Objects.requireNonNull(getPlayerSession(connection.client()));
		
		SburbPlayerData playerData = skaianetHandler.getOrCreateData(connection.client());
		if(!playerData.hasPrimaryConnection() || !playerData.isPrimaryServerPlayer(connection.server()))
		{
			if(!playerData.hasPrimaryConnection())
				s.removeConnection(connection.client());
			onConnectionChainBroken(s);
		}
	}
	
	Map<Integer, String> getServerList(PlayerIdentifier client)
	{
		Optional<PlayerIdentifier> primaryServer = skaianetHandler.primaryPartnerForClient(client);
		Map<Integer, String> map = new HashMap<>();
		for(PlayerIdentifier server : skaianetHandler.openedServers.getPlayers())
		{
			
			if(primaryServer.isPresent() && primaryServer.get().equals(server)
					|| primaryServer.isEmpty() && skaianetHandler.canMakeNewRegularConnectionAsServer(server)
					|| canMakeSecondaryConnection(client, server))
				map.put(server.getId(), server.getUsername());
		}
		return map;
	}
	
	public static SessionHandler get(MinecraftServer server)
	{
		return SkaianetHandler.get(server).sessionHandler;
	}
}
