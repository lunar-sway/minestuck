package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.alchemy.GristGutter;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

/**
 * A data structure that contains all related connections, along with any related data, such as predefine data.
 *
 * @author kirderf1
 */
public final class Session
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	final Map<PlayerIdentifier, PredefineData> predefinedPlayers = new HashMap<>();
	private final Set<SburbConnection> connections = new HashSet<>();
	private final GristGutter gutter;
	
	/**
	 * If the "connection circle" is whole, unused if globalSession == true.
	 */
	boolean completed;
	
	/**
	 * If the function throws an exception, this session should no longer be considered valid
	 */
	void inheritFrom(Session other)
	{
		predefinedPlayers.putAll(other.predefinedPlayers);
		
		connections.addAll(other.connections);
		
		// Since the gutter capacity of the merged session should be the sum of the individual sessions,
		// the gutter should not go over capacity unless one of the previous gutters already were over capacity.
		this.gutter.addGristUnchecked(other.gutter.getCache());
	}
	
	void copyPredefineDataForPlayers(Set<PlayerIdentifier> players, Session otherSession)
	{
		players.forEach(player ->
				otherSession.predefineData(player).ifPresent(data -> predefinedPlayers.put(player, data)));
	}
	
	/**
	 * Sets `completed` to true if everyone in the session has entered and has completed connections.
	 */
	void checkIfCompleted(SkaianetHandler skaianetHandler)
	{
		completed = computeIsComplete(skaianetHandler);
	}
	
	boolean computeIsComplete(SkaianetHandler skaianetHandler)
	{
		if(this.connections.isEmpty())
			return false;
		
		return this.getPlayerList().stream().allMatch(player -> {
			SburbPlayerData playerData = skaianetHandler.getOrCreateData(player);
			return playerData.hasEntered() && playerData.primaryServerPlayer().isPresent();
		});
	}
	
	Session()
	{
		this.gutter = new GristGutter(this);
	}
	
	private Session(CompoundTag nbt)
	{
		this.gutter = new GristGutter(this, nbt.getList("gutter", Tag.TAG_COMPOUND));
	}
	
	/**
	 * Checks if a certain player is in the connection list.
	 * @param player The username of the player.
	 * @return If the player was found.
	 */
	public boolean containsPlayer(PlayerIdentifier player)
	{
		if(player.equals(IdentifierHandler.NULL_IDENTIFIER))
			return false;
		if(predefinedPlayers.containsKey(player))
			return true;
		for(SburbConnection c : connections)
			if(c.getClientIdentifier().equals(player) || c.getServerIdentifier().equals(player))
				return true;
		return false;
	}
	
	/**
	 * Creates a list with all players in the session.
	 * @return Returns a list with the players identifiers.
	 */
	public Set<PlayerIdentifier> getPlayerList()
	{
		Set<PlayerIdentifier> list = new HashSet<>();
		for(SburbConnection c : this.connections)
		{
			list.add(c.getClientIdentifier());
			if(c.hasServerPlayer())
				list.add(c.getServerIdentifier());
		}
		list.addAll(predefinedPlayers.keySet());
		return list;
	}
	
	boolean isTitleUsed(@Nonnull Title newTitle, MinecraftServer server)
	{
		for(PlayerIdentifier player : this.getPlayerList())
		{
			Title title = PlayerSavedData.getData(player, server).getTitle();
			if(newTitle.equals(title))
				return true;
		}
		
		for(PredefineData data : predefinedPlayers.values())
			if(newTitle.equals(data.getTitle()))
				return true;
		
		return false;
	}
	
	Set<Title> getUsedTitles(MinecraftServer server)
	{
		return getUsedTitles(server, null);
	}
	
	Set<Title> getUsedTitles(MinecraftServer server, @Nullable PlayerIdentifier ignore)
	{
		Set<Title> titles = new HashSet<>();
		for(PlayerIdentifier player : this.getPlayerList())
		{
			if(!player.equals(ignore))
			{
				Title title = PlayerSavedData.getData(player, server).getTitle();
				if(title != null)
					titles.add(title);
			}
		}
		
		for(PredefineData data : predefinedPlayers.values())
			if(!data.getPlayer().equals(ignore) && data.getTitle() != null)
				titles.add(data.getTitle());
		
		return titles;
	}
	
	List<TitleLandType> getUsedTitleLandTypes(MinecraftServer server)
	{
		return getUsedTitleLandTypes(server, null);
	}
	
	List<TitleLandType> getUsedTitleLandTypes(MinecraftServer server, @Nullable PlayerIdentifier ignore)
	{
		List<TitleLandType> types = new ArrayList<>();
		for(PlayerIdentifier player : this.getPlayerList())
		{
			if(!player.equals(ignore))
			{
				LandTypePair.getTypes(server, SburbPlayerData.get(player, server).getLandDimension())
						.ifPresent(landTypes -> types.add(landTypes.getTitle()));
			}
		}
		
		for(PredefineData data : predefinedPlayers.values())
			if(!data.getPlayer().equals(ignore) && data.getTitleLandType() != null)
				types.add(data.getTitleLandType());
		
		return types;
	}
	
	List<TerrainLandType> getUsedTerrainLandTypes(MinecraftServer server)
	{
		return getUsedTerrainLandTypes(server, null);
	}
	
	List<TerrainLandType> getUsedTerrainLandTypes(MinecraftServer server, @Nullable PlayerIdentifier ignore)
	{
		List<TerrainLandType> types = new ArrayList<>();
		for(PlayerIdentifier player : this.getPlayerList())
		{
			if(!player.equals(ignore))
			{
				LandTypePair.getTypes(server, SburbPlayerData.get(player, server).getLandDimension())
						.ifPresent(landTypes -> types.add(landTypes.getTerrain()));
			}
		}
		
		for(PredefineData data : predefinedPlayers.values())
			if(!data.getPlayer().equals(ignore) && data.getTerrainLandType() != null)
				types.add(data.getTerrainLandType());
		
		return types;
	}
	
	void predefineCall(PlayerIdentifier player, SkaianetException.SkaianetConsumer<PredefineData> consumer) throws SkaianetException
	{
		PredefineData data = predefinedPlayers.get(player);
		if(data == null)    //TODO Do not create data for players that have entered (and clear predefined data when no longer needed)
			data = new PredefineData(player);
		consumer.consume(data);
		predefinedPlayers.put(player, data);
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
		for(SburbConnection c : connections) list.add(c.write());
		nbt.put("connections", list);
		ListTag predefineList = new ListTag();
		for(Map.Entry<PlayerIdentifier, PredefineData> entry : predefinedPlayers.entrySet())
			predefineList.add(entry.getKey().saveToNBT(entry.getValue().write(), "player"));
		nbt.put("predefinedPlayers", predefineList);
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
		Session s = new Session(nbt);
		
		ListTag list = nbt.getList("connections", Tag.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			try
			{
				s.connections.add(new SburbConnection(list.getCompound(i), handler));
			} catch(Exception e)
			{
				LOGGER.error("Unable to read sburb connection from tag {}. Forced to skip connection. Caused by:", list.getCompound(i), e);
			}
		}
		
		if(nbt.contains("predefinedPlayers", Tag.TAG_LIST))    //If it is a tag list
		{
			list = nbt.getList("predefinedPlayers", Tag.TAG_COMPOUND);
			for(int i = 0; i < list.size(); i++)
			{
				CompoundTag compound = list.getCompound(i);
				PlayerIdentifier player = IdentifierHandler.load(compound, "player");
				s.predefinedPlayers.put(player, new PredefineData(player).read(compound));
			}
		}
		
		return s;
	}
	
	public boolean isEmpty()
	{
		return connections.isEmpty() && predefinedPlayers.isEmpty();
	}
	
	void addConnection(PlayerIdentifier client, PlayerIdentifier server, SkaianetHandler skaianet)
	{
		connections.add(new SburbConnection(client, server, skaianet));
	}
	
	void removeConnectionIfPresent(PlayerIdentifier client, PlayerIdentifier server)
	{
		connections.removeIf(connection -> connection.getClientIdentifier().equals(client) && connection.getServerIdentifier().equals(server));
	}
	
	void removeOverlap(Session otherSession)
	{
		connections.removeAll(otherSession.connections);
		predefinedPlayers.keySet().removeAll(otherSession.predefinedPlayers.keySet());
	}
	
	Stream<SburbConnection> primaryConnections(SkaianetHandler skaianetHandler)
	{
		return connections.stream().filter(connection -> skaianetHandler.getOrCreateData(connection.getClientIdentifier()).hasPrimaryConnection());
	}
	
	Optional<PredefineData> predefineData(PlayerIdentifier player)
	{
		return Optional.ofNullable(this.predefinedPlayers.get(player));
	}
}
