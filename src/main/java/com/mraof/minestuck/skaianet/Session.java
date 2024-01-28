package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.alchemy.GristGutter;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

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
	final SkaianetData skaianetData;
	private final Set<PlayerIdentifier> players = new HashSet<>();
	private final GristGutter gutter;
	
	/**
	 * If the "connection circle" is whole, unused if globalSession == true.
	 */
	boolean completed;
	
	static Session createMergedSession(Collection<Session> sessions, SkaianetData skaianetData)
	{
		Session session = new Session(skaianetData);
		sessions.forEach(other -> {
			session.players.addAll(other.players);
			// Since the gutter capacity of the merged session should be the sum of the individual sessions,
			// the gutter should not go over capacity unless one of the previous gutters already were over capacity.
			session.gutter.addGristUnchecked(other.gutter.getCache());
		});
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
		if(this.players.isEmpty())
			return false;
		
		return this.players.stream().allMatch(player ->
				skaianetData.getOrCreateData(player).hasEntered()
						&& skaianetData.connections.primaryPartnerForClient(player).isPresent());
	}
	
	Session(SkaianetData skaianetData)
	{
		this.skaianetData = skaianetData;
		this.gutter = new GristGutter(skaianetData.mcServer, this);
	}
	
	private Session(CompoundTag nbt, SkaianetData skaianetData)
	{
		this.skaianetData = skaianetData;
		this.gutter = new GristGutter(skaianetData.mcServer, this, nbt.getList("gutter", Tag.TAG_COMPOUND));
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
		for(PlayerIdentifier player : players)
		{
			CompoundTag playerTag = new CompoundTag();
			player.saveToNBT(playerTag, "player");
			list.add(playerTag);
		}
		nbt.put("players", list);
		nbt.put("gutter", this.gutter.write());
		return nbt;
	}
	
	/**
	 * Reads data from the given nbt tag.
	 *
	 * @param nbt An CompoundNBT to read from.
	 * @return This.
	 */
	static Session read(CompoundTag nbt, SkaianetData skaianetData)
	{
		Session s = new Session(nbt, skaianetData);
		
		ListTag list = nbt.getList("players", Tag.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
			s.players.add(IdentifierHandler.load(list.getCompound(i), "player"));
		
		return s;
	}
	
	Session addPlayer(PlayerIdentifier player)
	{
		this.players.add(player);
		return this;
	}
	
	Session createSessionSplit(Set<PlayerIdentifier> players)
	{
		double originalGutterMultiplier = this.gutter.gutterMultiplierForSession();
		
		Session newSession = new Session(skaianetData);
		
		for(PlayerIdentifier player : players)
		{
			this.players.remove(player);
			newSession.players.add(player);
		}
		
		double gutterMultiplier = newSession.gutter.gutterMultiplierForSession();
		MutableGristSet takenGrist = this.gutter.takeFraction(gutterMultiplier/originalGutterMultiplier);
		newSession.gutter.addGristFrom(takenGrist);
		
		return newSession;
	}
}
