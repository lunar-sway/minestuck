package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Location;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TransportalizerSavedData extends WorldSavedData
{
	private static final String DATA_NAME = Minestuck.MOD_ID+"_transportalizers";
	
	private HashMap<String, Location> locations;
	
	private TransportalizerSavedData()
	{
		super(DATA_NAME);
		locations = new HashMap<>();
	}
	
	@Override
	public void read(CompoundNBT nbt)
	{
		locations = new HashMap<>();
		for(String id : nbt.keySet())
		{
			CompoundNBT locationTag = nbt.getCompound(id);
			Location location = Location.fromNBT(locationTag);
			
			if(location != null)
			{
				locations.put(id, location);
			} else Debug.warnf("Could not load transportalizer %s due to unreadable location. This transportalizer will be ignored.", id);
		}
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		for(Map.Entry<String, Location> entry : locations.entrySet())
		{
			Location location = entry.getValue();
			
			CompoundNBT locationTag = location.toNBT(new CompoundNBT());
			
			if(locationTag != null)
				compound.put(entry.getKey(), locationTag);
			else Debug.warnf("Couldn't save the location of transportalizer %s!", entry.getKey());
		}
		
		return compound;
	}
	
	public Location get(String id)
	{
		return locations.get(id);
	}
	
	public boolean set(String id, Location location)
	{
		if(!locations.containsKey(id))
		{
			locations.put(id, location);
			this.markDirty();
			return true;
		} else return locations.get(id).equals(location);
	}
	
	public boolean remove(String id, Location location)
	{
		boolean removed = locations.remove(id, location);
		if(removed)
			this.markDirty();
		return removed;
	}
	
	public String findNewId(Random random, Location location)
	{
		String unusedId = "";
		do
		{
			for(int i = 0; i < 4; i++)
			{
				unusedId += (char) (random.nextInt(26) + 'A');
			}
		}
		while(locations.containsKey(unusedId));
		
		locations.put(unusedId, location);
		this.markDirty();
		return unusedId;
	}
	
	
	public static TransportalizerSavedData get(World world)
	{
		MinecraftServer server = world.getServer();
		if(server == null)
			throw new IllegalArgumentException("Can't get transportalizer data instance on client side! (Got null server from world)");
		return get(server);
	}
	
	public static TransportalizerSavedData get(MinecraftServer mcServer)
	{
		ServerWorld world = mcServer.getWorld(DimensionType.OVERWORLD);
		
		DimensionSavedDataManager storage = world.getSavedData();
		TransportalizerSavedData instance = storage.get(TransportalizerSavedData::new, DATA_NAME);
		
		if(instance == null)	//There is no save data
		{
			instance = new TransportalizerSavedData();
			storage.set(instance);
		}
		
		return instance;
	}
}