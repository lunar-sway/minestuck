package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Stream;

/**
 * This class is the centerpoint of various sburb-related data, which all goes in the "minestuck_skaianet.dat" data file.
 * @author kirderf1
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SkaianetData extends SavedData
{
	final InfoTracker infoTracker = new InfoTracker(this);
	final ComputerInteractions computerInteractions;
	final SburbConnections connections;
	final SessionHandler sessionHandler;
	private final Map<PlayerIdentifier, SburbPlayerData> playerDataMap = new HashMap<>();
	private final Map<PlayerIdentifier, PredefineData> predefineData = new HashMap<>();
	
	final MinecraftServer mcServer;
	
	private SkaianetData(MinecraftServer mcServer)
	{
		this.mcServer = mcServer;
		
		computerInteractions = new ComputerInteractions(this);
		connections = new SburbConnections(this);
		sessionHandler = SessionHandler.init(this);
	}
	
	private SkaianetData(MinecraftServer mcServer, CompoundTag nbt)
	{
		this.mcServer = mcServer;
		
		ListTag playerDataList = nbt.getList("player_data", Tag.TAG_COMPOUND);
		for(int i = 0; i < playerDataList.size(); i++)
		{
			CompoundTag playerDataTag = playerDataList.getCompound(i);
			PlayerIdentifier player = IdentifierHandler.load(playerDataTag, "player");
			getOrCreateData(player).read(playerDataTag);
		}
		
		if(nbt.contains("predefine_data", Tag.TAG_LIST))
		{
			ListTag list = nbt.getList("predefine_data", Tag.TAG_COMPOUND);
			for(int i = 0; i < list.size(); i++)
			{
				CompoundTag compound = list.getCompound(i);
				PlayerIdentifier player = IdentifierHandler.load(compound, "player");
				predefineData.put(player, new PredefineData(player).read(compound));
			}
		}
		
		computerInteractions = new ComputerInteractions(this, nbt);
		connections = new SburbConnections(this, nbt);
		
		sessionHandler = SessionHandler.load(nbt, this);
	}
	
	@Override
	public CompoundTag save(CompoundTag compound)
	{
		ListTag playerDataList = new ListTag();
		for(SburbPlayerData playerData : playerDataMap.values())
		{
			CompoundTag playerDataTag = new CompoundTag();
			playerData.playerId().saveToNBT(playerDataTag, "player");
			playerData.write(playerDataTag);
			playerDataList.add(playerDataTag);
		}
		compound.put("player_data", playerDataList);
		
		ListTag predefineList = new ListTag();
		for(Map.Entry<PlayerIdentifier, PredefineData> entry : predefineData.entrySet())
			predefineList.add(entry.getKey().saveToNBT(entry.getValue().write(), "player"));
		compound.put("predefine_data", predefineList);
		
		computerInteractions.write(compound);
		connections.write(compound);
		
		sessionHandler.write(compound);
		
		return compound;
	}
	
	Stream<PlayerIdentifier> players()
	{
		return playerDataMap.keySet().stream();
	}
	
	SburbPlayerData getOrCreateData(PlayerIdentifier player)
	{
		return this.playerDataMap.computeIfAbsent(player, playerId -> {
			var data = new SburbPlayerData(playerId, this.mcServer);
			SburbHandler.initNewData(data);
			return data;
		});
	}
	
	public Collection<SburbPlayerData> allPlayerData()
	{
		return this.playerDataMap.values();
	}
	
	/**
	 * Gets/creates an instance of predefine data for the given player.
	 * @return An empty optional if data can no longer be predefined for this player.
	 */
	public Optional<PredefineData> getOrCreatePredefineData(PlayerIdentifier player)
	{
		if(getOrCreateData(player).getLandDimension() != null)
			return Optional.empty();
		else
			return Optional.of(this.predefineData.computeIfAbsent(player, newPlayer -> {
				sessionHandler.newPredefineData(newPlayer);
				return new PredefineData(newPlayer);
			}));
	}
	
	/**
	 * Predefine data is only meant to exist up until the actual land and title have been generated for the player (which happens during entry preparations).
	 * After that, predefine data should be removed.
	 */
	void removePredefineData(PlayerIdentifier player)
	{
		predefineData.remove(player);
	}
	
	private static final String DATA_NAME = Minestuck.MOD_ID+"_skaianet";
	
	public static SkaianetData get(MinecraftServer server)
	{
		Objects.requireNonNull(server);
		
		ServerLevel level = server.overworld();
		
		DimensionDataStorage storage = level.getDataStorage();
		
		return storage.computeIfAbsent(nbt -> new SkaianetData(server, nbt), () -> new SkaianetData(server), DATA_NAME);
	}
	
	// Always save skaianet data, since it's difficult to reliably tell when skaianet data has changed.
	@Override
	public boolean isDirty()
	{
		return true;
	}
}
