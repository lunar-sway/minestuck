package com.mraof.minestuck.world.storage;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.WorldSavedDataStorage;

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
	
	private TransportalizerSavedData(String name)
	{
		super(name);
	}
	
	@Override
	public void read(NBTTagCompound nbt)
	{
		locations = new HashMap<>();
		for(String id : nbt.keySet())
		{
			NBTTagCompound locationTag = nbt.getCompound(id);
			Location location = Location.fromNBT(locationTag);
			
			if(location != null)
			{
				locations.put(id, location);
			} else Debug.warnf("Could not load transportalizer %s due to unreadable location. This transportalizer will be ignored.", id);
		}
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		for(Map.Entry<String, Location> entry : locations.entrySet())
		{
			Location location = entry.getValue();
			
			NBTTagCompound locationTag = location.toNBT(new NBTTagCompound());
			
			if(locationTag != null)
				compound.setTag(entry.getKey(), locationTag);
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
		if(world.isRemote)
			throw new IllegalStateException("Should not attempt to get saved data on the client side!");
		
		WorldSavedDataStorage storage = world.getMapStorage();
		TransportalizerSavedData instance = storage.func_212426_a(DimensionType.OVERWORLD, TransportalizerSavedData::new, DATA_NAME);
		
		if(instance == null)	//There is no save data
		{
			instance = new TransportalizerSavedData();
			storage.func_212424_a(DimensionType.OVERWORLD, DATA_NAME, instance);
		}
		
		return instance;
	}
}