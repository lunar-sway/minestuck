package com.mraof.minestuck.tileentity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.ITeleporter;
import com.mraof.minestuck.util.Location;
import com.mraof.minestuck.util.Teleport;

public class TileEntityTransportalizer extends TileEntity implements ITeleporter
{
	public static HashMap<String, Location> transportalizers = new HashMap<String, Location>();
	private static Random rand = new Random();
	String id = "";
	private String destId = "";
	Location currentLocation = new Location();

	@Override
	public void setWorldObj(World world)
	{
		super.setWorldObj(world);
		if(!world.isRemote && id.isEmpty())
		{
			id = getUnusedId();
		}
		put(id, new Location(this.xCoord, this.yCoord, this.zCoord, world.provider.dimensionId));
	}

	public static String getUnusedId()
	{
		String unusedId = "";
		do
		{
			for(int i = 0; i < 4; i++)
			{
				unusedId += (char) (rand.nextInt(26) + 'A');
			}
		} 
		while(transportalizers.containsKey(unusedId));

		return unusedId;
	}

	public static void put(String key, Location location)
	{
		//Debug.print("Adding transportalizer with key " + key + " at " + location);
		transportalizers.put(key, location);
	}

	public void updateLocation(Location location)
	{
		this.currentLocation = location;
		if(worldObj != null || !worldObj.isRemote)
			put(id, location);
	}

	public static void teleportTo(Entity entity, Location location)
	{
		entity.timeUntilPortal = 60;
		double x = location.x + entity.posX % 1;
		double y = location.y + entity.posY % 1;
		double z = location.z + entity.posZ % 1;
		if(entity instanceof EntityPlayerMP)
		{
			((EntityPlayerMP) entity).setPositionAndUpdate(x, y, z);
		}
		else
		{
			entity.setPosition(x, y, z);
		}
		//Debug.print("Teleported to " + location);
	}

	public void teleport(Entity entity)
	{
		Location location = transportalizers.get(this.destId);
		if(location != null && location.y != -1)
		{
			TileEntityTransportalizer destTransportalizer = (TileEntityTransportalizer) MinecraftServer.getServer().worldServerForDimension(location.dim).getTileEntity(location.x, location.y, location.z);
			if(destTransportalizer == null)
			{
				Debug.print("Invalid transportalizer in map: " + this.destId + " at " + location);
				transportalizers.remove(this.destId);
				this.destId = "";
				return;
			}
			if(location.dim != entity.dimension)
				Teleport.teleportEntity(entity, location.dim, destTransportalizer);
			else
				teleportTo(entity, transportalizers.get(this.destId));
		}
		else
		{
			//Debug.print(this.destId + " " + transportalizers);
		}
	}

	public static void saveTransportalizers(NBTTagCompound tagCompound)
	{
		NBTTagCompound transportalizerTagCompound = new NBTTagCompound();
		Iterator<Map.Entry<String, Location>> it = transportalizers.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String, Location> entry = it.next();
			Location location = entry.getValue();
			NBTTagCompound locationTag = new NBTTagCompound();
			locationTag.setInteger("x", location.x);
			locationTag.setInteger("y", location.y);
			locationTag.setInteger("z", location.z);
			locationTag.setInteger("dim", location.dim);
			transportalizerTagCompound.setTag(entry.getKey(), locationTag);
		}
		tagCompound.setTag("transportalizers", transportalizerTagCompound);
		Debug.print("Transportalizer data: "+transportalizerTagCompound);
	}

	public static void loadTransportalizers(NBTTagCompound tagCompound)
	{
		System.out.println(tagCompound);
		for(Object id : tagCompound.func_150296_c())
		{
			NBTTagCompound locationTag = tagCompound.getCompoundTag((String)id);
			Debug.printf("Loaded transportalizer %s: %d, %d, %d, %d", (String)id, locationTag.getInteger("x"), locationTag.getInteger("y"), locationTag.getInteger("z"), locationTag.getInteger("dim"));
			put((String)id, new Location(locationTag.getInteger("x"), locationTag.getInteger("y"), locationTag.getInteger("z"), locationTag.getInteger("dim")));
		}
	}

	public String getId()
	{
		return id;
	}

	public String getDestId()
	{
		return destId;
	}

	public void setDestId(String destId)
	{
		this.destId = destId;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		this.destId = tagCompound.getString("destId");
		String readId = tagCompound.getString("idString");
		Debug.print("Id read is " + readId);
		if(!readId.isEmpty())
		{
			transportalizers.remove(id);
			id = readId;
		}

		int dimensionId = 0;

		if(this.worldObj != null)
		{
			dimensionId = this.worldObj.provider.dimensionId;
		}
		else if(transportalizers.get(this.id) != null)
		{
			dimensionId = transportalizers.get(this.id).dim;
		}
		Debug.printf("%d, %d, %d, %d", this.xCoord, this.yCoord, this.zCoord, dimensionId);

		put(this.id, new Location(this.xCoord, this.yCoord, this.zCoord, dimensionId));
		destId = tagCompound.getString("destId");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		if(id.isEmpty())
		{
			id = getUnusedId();
			put(id, new Location());
		}
		tagCompound.setString("idString", id);
		Debug.print("Saved id as " + id);
		tagCompound.setString("destId", destId);
	}

	@Override
	public Packet getDescriptionPacket() 
	{
		NBTTagCompound tagCompound = new NBTTagCompound();
		this.writeToNBT(tagCompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 2, tagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) 
	{
		this.readFromNBT(pkt.func_148857_g());
	}

	@Override
	public void makeDestination(Entity entity, WorldServer worldserver, WorldServer worldserver1) 
	{
		entity.setLocationAndAngles(this.xCoord, this.yCoord, this.zCoord, entity.rotationYaw, entity.rotationPitch);
		entity.timeUntilPortal = 60;
	}

	@Override
	public boolean canUpdate()
	{
		return false;
	}
}

