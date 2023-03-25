package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristGutter;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
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
import java.util.*;

/**
 * A data structure that contains all related connections, along with any related data, such as predefine data.
 *
 * @author kirderf1
 */
public final class Session
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	final Map<PlayerIdentifier, PredefineData> predefinedPlayers;
	final Set<SburbConnection> connections;
	private final GristGutter gutter;
	String name;
	
	/**
	 * If the "connection circle" is whole, unused if globalSession == true.
	 */
	boolean completed;
	boolean locked;
	
	void addConnection(SburbConnection connection)
	{
		connections.add(connection);
		connection.setSession(this);
	}
	
	void finishMergeOrSplit()
	{
		connections.forEach(connection -> connection.setSession(this));
	}
	
	/**
	 * If the function throws an exception, this session should no longer be considered valid
	 */
	void inheritFrom(Session other) throws MergeResult.SessionMergeException
	{
		if(locked)
			throw MergeResult.LOCKED.exception();
		else locked = other.locked;
		
		if(other.isCustom())
		{
			if(!isCustom())
				name = other.name;
			else throw MergeResult.BOTH_CUSTOM.exception();
		}
		
		if(other.predefinedPlayers.entrySet().stream().allMatch(entry -> canAdd(entry.getKey(), entry.getValue())))
			predefinedPlayers.putAll(other.predefinedPlayers);
		else throw MergeResult.GENERIC_FAIL.exception();
		
		connections.addAll(other.connections);
		
		if(MinestuckConfig.SERVER.forceMaxSize && getPlayerList().size() > SessionHandler.MAX_SIZE)
			throw MergeResult.MERGED_SESSION_FULL.exception();
		
		// Since the gutter capacity of the merged session should be the sum of the individual sessions,
		// the gutter should not go over capacity unless one of the previous gutters already were over capacity.
		this.gutter.addGristUnchecked(other.gutter.getCache());
	}
	
	private boolean canAdd(PlayerIdentifier player, PredefineData data)
	{
		return true;
	}
	
	/**
	 * Sets `completed` to true if everyone in the session has entered and has completed connections.
	 */
	void checkIfCompleted()
	{
		if(connections.isEmpty())
		{
			completed = false;
			return;
		}
		if(connections.stream().anyMatch(c -> !c.hasServerPlayer()))
		{
			completed = false;
			return;
		}
		Set<PlayerIdentifier> players = this.getPlayerList();
		for(PlayerIdentifier player : players)
		{
			if(connections.stream().noneMatch(c ->
					c.getClientIdentifier().equals(player) && c.hasEntered()))
			{
				completed = false;
				return;
			}
		}
		completed = true;
	}
	
	Session()
	{
		connections = new HashSet<>();
		predefinedPlayers = new HashMap<>();
		this.gutter = new GristGutter(this);
	}
	
	private Session(CompoundTag nbt)
	{
		connections = new HashSet<>();
		predefinedPlayers = new HashMap<>();
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
	
	boolean isTitleUsed(@Nonnull Title newTitle)
	{
		for(SburbConnection c : connections)
		{
			Title title = c.getClientTitle();
			if(newTitle.equals(title))
				return true;
		}
		
		for(PredefineData data : predefinedPlayers.values())
			if(newTitle.equals(data.getTitle()))
				return true;
		
		return false;
	}
	
	Set<Title> getUsedTitles()
	{
		return getUsedTitles(null);
	}
	
	Set<Title> getUsedTitles(PlayerIdentifier ignore)
	{
		Set<Title> titles = new HashSet<>();
		for(SburbConnection c : connections)
		{
			if(!c.getClientIdentifier().equals(ignore))
			{
				Title title = c.getClientTitle();
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
	
	List<TitleLandType> getUsedTitleLandTypes(MinecraftServer server, PlayerIdentifier ignore)
	{
		List<TitleLandType> types = new ArrayList<>();
		for(SburbConnection c : connections)
		{
			if(!c.getClientIdentifier().equals(ignore))
			{
				LandTypePair.getTypes(server, c.getClientDimension())
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
	
	List<TerrainLandType> getUsedTerrainLandTypes(MinecraftServer server, PlayerIdentifier ignore)
	{
		List<TerrainLandType> types = new ArrayList<>();
		for(SburbConnection c : connections)
		{
			if(!c.getClientIdentifier().equals(ignore))
			{
				LandTypePair.getTypes(server, c.getClientDimension())
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
			data = new PredefineData(player, this);
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
		
		if(isCustom())
			nbt.putString("name", name);
		ListTag list = new ListTag();
		for(SburbConnection c : connections) list.add(c.write());
		nbt.put("connections", list);
		ListTag predefineList = new ListTag();
		for(Map.Entry<PlayerIdentifier, PredefineData> entry : predefinedPlayers.entrySet())
			predefineList.add(entry.getKey().saveToNBT(entry.getValue().write(), "player"));
		nbt.put("predefinedPlayers", predefineList);
		nbt.putBoolean("locked", locked);
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
		if(nbt.contains("name", Tag.TAG_STRING))
			s.name = nbt.getString("name");
		else s.name = null;
		
		ListTag list = nbt.getList("connections", Tag.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			try
			{
				SburbConnection c = new SburbConnection(list.getCompound(i), handler);
				if(c.isActive() || c.isMain())
					s.connections.add(c);
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
				s.predefinedPlayers.put(player, new PredefineData(player, s).read(compound));
			}
		}
		
		s.locked = nbt.getBoolean("locked");
		
		s.checkIfCompleted();
		return s;
	}
	
	public boolean isCustom()
	{
		return name != null;
	}
	
	public boolean isEmpty()
	{
		return connections.isEmpty() && predefinedPlayers.isEmpty();
	}
}
