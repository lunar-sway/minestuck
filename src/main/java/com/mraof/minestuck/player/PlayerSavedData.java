package com.mraof.minestuck.player;

import com.mraof.minestuck.Minestuck;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Stores all instances of {@link PlayerData}.
 * This class is for server-side use only.
 * @author kirderf1
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class PlayerSavedData extends SavedData
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DATA_NAME = Minestuck.MOD_ID+"_player_data";
	
	private final Map<PlayerIdentifier, PlayerData> dataMap = new HashMap<>();
	public final MinecraftServer mcServer;
	
	private PlayerSavedData(MinecraftServer server)
	{
		mcServer = server;
	}
	
	public static PlayerSavedData get(Level level)
	{
		MinecraftServer server = level.getServer();
		if(server == null)
			throw new IllegalArgumentException("Can't get player data instance on client side! (Got null server from level)");
		return get(server);
	}
	
	public static PlayerSavedData get(MinecraftServer mcServer)
	{
		ServerLevel level = mcServer.overworld();
		
		DimensionDataStorage storage = level.getDataStorage();
		
		return storage.computeIfAbsent(factory(mcServer), DATA_NAME);
	}
	
	private static Factory<PlayerSavedData> factory(MinecraftServer mcServer)
	{
		return new Factory<>(() -> new PlayerSavedData(mcServer), nbt -> load(mcServer, nbt));
	}
	
	@Override
	public CompoundTag save(CompoundTag compound)
	{
		ListTag list = new ListTag();
		for (PlayerData data : dataMap.values())
			list.add(data.writeToNBT());
		
		compound.put("playerData", list);
		return compound;
	}
	
	private static PlayerSavedData load(MinecraftServer server, CompoundTag nbt)
	{
		PlayerSavedData savedData = new PlayerSavedData(server);
		
		ListTag list = nbt.getList("playerData", Tag.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++)
		{
			CompoundTag dataCompound = list.getCompound(i);
			try
			{
				PlayerData data = new PlayerData(server, dataCompound);
				savedData.dataMap.put(data.identifier, data);
			} catch(Exception e)
			{
				LOGGER.error("Got exception when loading minestuck player data instance:", e);
			}
		}
		return savedData;
	}
	
	@Nullable
	public static PlayerData getData(ServerPlayer player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		if(identifier == null)
			return null;
		return get(player.server).getData(identifier);
	}
	
	public static PlayerData getData(PlayerIdentifier player, Level level)
	{
		return get(level).getData(player);
	}
	
	public static PlayerData getData(PlayerIdentifier player, MinecraftServer server)
	{
		return get(server).getData(player);
	}
	
	public PlayerData getData(PlayerIdentifier player)
	{
		Objects.requireNonNull(player);
		if(!dataMap.containsKey(player))
		{
			PlayerData data = new PlayerData(this.mcServer, player);
			dataMap.put(player, data);
		}
		return dataMap.get(player);
	}
	
	@Override
	public boolean isDirty()
	{
		// Always save player data. The number of subcomponents (such as the modus, echeladder, grist cache etc)
		// makes this preferable to marking the data dirty every time something changes in any of these subcomponents.
		return true;
	}
}