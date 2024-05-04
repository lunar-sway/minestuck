package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of all transportalizer codes and the locations that they are linked to.
 */
public class TransportalizerSavedData extends SavedData
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String DATA_NAME = Minestuck.MOD_ID+"_transportalizers";
	
	private final Map<String, GlobalPos> locations;
	
	private TransportalizerSavedData()
	{
		locations = new HashMap<>();
	}
	
	public static TransportalizerSavedData load(CompoundTag nbt)
	{
		TransportalizerSavedData data = new TransportalizerSavedData();
		data.locations.clear();
		for(String id : nbt.getAllKeys())
		{
			Tag locationTag = nbt.get(id);
			GlobalPos.CODEC.parse(NbtOps.INSTANCE, locationTag).resultOrPartial(LOGGER::error).ifPresent(location -> data.locations.put(id, location));
		}
		return data;
	}
	
	@Override
	public CompoundTag save(CompoundTag compound)
	{
		for(Map.Entry<String, GlobalPos> entry : locations.entrySet())
		{
			GlobalPos location = entry.getValue();
			
			GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, location).resultOrPartial(LOGGER::error).ifPresent(locationTag -> compound.put(entry.getKey(), locationTag));
		}
		
		return compound;
	}
	
	public GlobalPos get(String id)
	{
		return locations.get(id);
	}
	
	public boolean set(String id, GlobalPos location)
	{
		if(!locations.containsKey(id))
		{
			locations.put(id, location);
			this.setDirty();
			return true;
		} else return locations.get(id).equals(location);
	}
	
	public boolean remove(String id, GlobalPos location)
	{
		boolean removed = locations.remove(id, location);
		if(removed)
			this.setDirty();
		return removed;
	}
	
	public void replace(String id, GlobalPos oldPos, GlobalPos newPos)
	{
		if(locations.replace(id, oldPos, newPos))
			setDirty();
	}
	
	public static TransportalizerSavedData get(Level level)
	{
		MinecraftServer server = level.getServer();
		if(server == null)
			throw new IllegalArgumentException("Can't get transportalizer data instance on client side! (Got null server from level)");
		return get(server);
	}
	
	public static TransportalizerSavedData get(MinecraftServer mcServer)
	{
		ServerLevel level = mcServer.getLevel(Level.OVERWORLD);
		
		DimensionDataStorage storage = level.getDataStorage();
		
		return storage.computeIfAbsent(new Factory<>(TransportalizerSavedData::new, TransportalizerSavedData::load), DATA_NAME);
	}
}