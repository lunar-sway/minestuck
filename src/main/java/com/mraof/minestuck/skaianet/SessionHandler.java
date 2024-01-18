package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;

import java.util.*;
import java.util.stream.Stream;

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
	
	boolean doesSessionHaveMaxTier(Session session)
	{
		return session.completed;
	}
	
	boolean canMakeSecondaryConnection(PlayerIdentifier client, PlayerIdentifier server)
	{
		return MinestuckConfig.SERVER.allowSecondaryConnections.get()
				&& skaianetHandler.hasPrimaryConnectionForClient(client)
				&& getPlayerSession(client) == getPlayerSession(server);
	}
	
	abstract void onConnect(PlayerIdentifier client, PlayerIdentifier server);
	
	abstract void onDisconnect(PlayerIdentifier client, PlayerIdentifier server);
	
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
	
	/**
	 * @return all players whose title or land types (predefined or not) should influence the selection of the same for the specified player.
	 */
	Stream<PlayerIdentifier> playersToCheckForDataSelection(PlayerIdentifier player)
	{
		Session session = this.getPlayerSession(player);
		return session != null
				? session.getPlayers().stream().filter(otherPlayer -> !otherPlayer.equals(player))
				: Stream.empty();
	}
	
	public static SessionHandler get(MinecraftServer server)
	{
		return SkaianetHandler.get(server).sessionHandler;
	}
}
