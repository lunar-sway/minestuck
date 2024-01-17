package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.alchemy.GristGutter;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A data structure that contains all related connections, along with any related data, such as predefine data.
 *
 * @author kirderf1
 */
public final class Session
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	final SkaianetHandler skaianetHandler;
	private final Set<PlayerIdentifier> connectedClients = new HashSet<>();
	private final Set<PlayerIdentifier> players = new HashSet<>();
	private final GristGutter gutter;
	
	/**
	 * If the "connection circle" is whole, unused if globalSession == true.
	 */
	boolean completed;
	
	static Session createMergedSession(Collection<Session> sessions, SkaianetHandler skaianetHandler)
	{
		Session session = new Session(skaianetHandler);
		sessions.forEach(other -> {
			session.connectedClients.addAll(other.connectedClients);
			// Since the gutter capacity of the merged session should be the sum of the individual sessions,
			// the gutter should not go over capacity unless one of the previous gutters already were over capacity.
			session.gutter.addGristUnchecked(other.gutter.getCache());
		});
		session.updatePlayerSet();
		return session;
	}
	
	/**
	 * Sets `completed` to true if everyone in the session has entered and has completed connections.
	 */
	void checkIfCompleted()
	{
		completed = computeIsComplete();
	}
	
	boolean computeIsComplete()
	{
		if(this.connectedClients.isEmpty())
			return false;
		
		return this.getPlayers().stream().allMatch(player -> {
			SburbPlayerData playerData = skaianetHandler.getOrCreateData(player);
			return playerData.hasEntered() && playerData.primaryServerPlayer().isPresent();
		});
	}
	
	Session(SkaianetHandler skaianetHandler)
	{
		this.skaianetHandler = skaianetHandler;
		this.gutter = new GristGutter(skaianetHandler.mcServer, this);
	}
	
	private Session(CompoundTag nbt, SkaianetHandler skaianetHandler)
	{
		this.skaianetHandler = skaianetHandler;
		this.gutter = new GristGutter(skaianetHandler.mcServer, this, nbt.getList("gutter", Tag.TAG_COMPOUND));
	}
	
	/**
	 * Checks if a certain player is in the connection list.
	 * @param player The username of the player.
	 * @return If the player was found.
	 */
	public boolean containsPlayer(PlayerIdentifier player)
	{
		return players.contains(player);
	}
	
	/**
	 * Creates a list with all players in the session.
	 * @return Returns a list with the players identifiers.
	 */
	public Set<PlayerIdentifier> getPlayers()
	{
		return players;
	}
	
	void updatePlayerSet()
	{
		players.clear();
		for(PlayerIdentifier player : this.connectedClients)
		{
			players.add(player);
			SburbPlayerData playerData = skaianetHandler.getOrCreateData(player);
			if(playerData.hasPrimaryConnection())
				playerData.primaryServerPlayer().ifPresent(players::add);
			else
				players.add(skaianetHandler.getActiveConnection(player).orElseThrow().server());
		}
		
		if(skaianetHandler.sessionHandler instanceof GlobalSessionHandler)
			players.addAll(skaianetHandler.predefineData.keySet());
	}
	
	public GristGutter getGristGutter()
	{
		return gutter;
	}
	
	/**
	 * Writes this session to an nbt tag.
	 * Note that this will only work as long as <code>SkaianetHandler.connections</code> remains unmodified.
	 *
	 * @return An CompoundNBT representing this session.
	 */
	CompoundTag write()
	{
		CompoundTag nbt = new CompoundTag();
		
		ListTag list = new ListTag();
		for(PlayerIdentifier player : connectedClients)
		{
			CompoundTag playerTag = new CompoundTag();
			player.saveToNBT(playerTag, "client");
			list.add(playerTag);
		}
		nbt.put("connections", list);
		nbt.put("gutter", this.gutter.write());
		return nbt;
	}
	
	/**
	 * Reads data from the given nbt tag.
	 *
	 * @param nbt An CompoundNBT to read from.
	 * @return This.
	 */
	static Session read(CompoundTag nbt, SkaianetHandler handler)
	{
		Session s = new Session(nbt, handler);
		
		ListTag list = nbt.getList("connections", Tag.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			try
			{
				s.connectedClients.add(IdentifierHandler.load(list.getCompound(i), "client"));
			} catch(Exception e)
			{
				LOGGER.error("Unable to read sburb connection from tag {}. Forced to skip connection. Caused by:", list.getCompound(i), e);
			}
		}
		
		return s;
	}
	
	void addConnectedClient(PlayerIdentifier client)
	{
		connectedClients.add(client);
		updatePlayerSet();
	}
	
	void removeConnection(PlayerIdentifier client)
	{
		connectedClients.remove(client);
		updatePlayerSet();
	}
	
	Session createSessionSplit(Set<PlayerIdentifier> clientPlayers)
	{
		double originalGutterMultiplier = this.gutter.gutterMultiplierForSession();
		
		Session newSession = new Session(skaianetHandler);
		
		for(PlayerIdentifier player : clientPlayers)
		{
			if(connectedClients.remove(player))
				newSession.connectedClients.add(player);
		}
		newSession.updatePlayerSet();
		this.updatePlayerSet();
		
		double gutterMultiplier = newSession.gutter.gutterMultiplierForSession();
		MutableGristSet takenGrist = this.gutter.takeFraction(gutterMultiplier/originalGutterMultiplier);
		newSession.gutter.addGristFrom(takenGrist);
		
		return newSession;
	}
}
