package com.mraof.minestuck.world;

import java.util.Map;
import java.util.Random;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Location;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.biome.BiomeGenMinestuck;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class GateHandler
{
	
	public static final int gateHeight1 = 144, gateHeight2 = 192;
	
	static Map<Integer, BlockPos> gateData;
	
	public static void teleport(int gateId, int dim, EntityPlayerMP player)
	{
		Location location = null;
		if(gateId == 1)
		{
			BlockPos pos = getGatePos(-1, dim);
			Random rand = player.worldObj.rand;
			BlockPos spawn = player.worldObj.provider.getSpawnPoint();
			if(pos != null)
				do
				{
					int radius = 150 + rand.nextInt(100);
					double d = rand.nextDouble();
					int i = radius*radius;
					int x = (int) Math.sqrt(i*d);
					int z = (int) Math.sqrt((1-i)*d);
					if(rand.nextBoolean()) x = -x;
					if(rand.nextBoolean()) z = -z;
					
					BlockPos placement = pos.add(x, 0, z);
					double distance = placement.distanceSq(spawn.getX(), placement.getY(), spawn.getZ());
					
					if(player.worldObj.getBiomeGenForCoordsBody(placement) != BiomeGenMinestuck.mediumOcean && distance > 10000)
						location = new Location(player.worldObj.getTopSolidOrLiquidBlock(placement), dim);
					
				} while(location == null);
			else Debug.printf("Unexpected error: Couldn't find position for land gate for dimension %d.", dim);
			
		} else if(gateId == 2)
		{
			//TODO
		} else if(gateId == -1)
		{
			SburbConnection landConnection = SessionHandler.getConnectionForDimension(dim);
			if(landConnection != null)
			{
				SburbConnection serverConnection = SkaianetHandler.getConnection(landConnection.getServerName(), SkaianetHandler.getAssociatedPartner(landConnection.getServerName(), true));
				
				if(serverConnection != null && serverConnection.enteredGame() && MinestuckDimensionHandler.isLandDimension(serverConnection.getClientDimension()))	//Last shouldn't be necessary, but just in case something goes wrong elsewhere...
				{
					int serverDim = serverConnection.getClientDimension();
					location = new Location(getGatePos(gateId, serverDim), serverDim);
					
				} Debug.printf("Player %s tried to teleport through gate before their server player entered the game.", player.getName());
				
			} else Debug.printf("Unexpected error: Can't find connection for dimension %d!", dim);
		} else Debug.printf("Unexpected error: Gate id %d is out of bounds!", gateId);
		
		if(location != null)
		{
			player.timeUntilPortal = 60;
			if(location.dim != dim)
				Teleport.teleportEntity(player, location.dim, null);
			player.playerNetServerHandler.setPlayerLocation(location.pos.getX() + 0.5, location.pos.getY(), location.pos.getZ() + 0.5, player.rotationYaw, player.rotationPitch);
		}
	}
	
	public static void findGatePlacement(World world)
	{
		int dim = world.provider.getDimensionId();
		if(MinestuckDimensionHandler.isLandDimension(dim) && !gateData.containsKey(dim))
		{
			BlockPos spawn = MinestuckDimensionHandler.getSpawn(dim);
			
			//TODO
			
		}
	}
	
	public static BlockPos getGatePos(int gateId, int dim)
	{
		if(!MinestuckDimensionHandler.isLandDimension(dim))
			return null;
		
		if(gateId == -1)
			return gateData.get(dim);
		else if(gateId == 1 || gateId == 2)
		{
			World world = DimensionManager.getWorld(dim);
			if(world == null) {
				DimensionManager.initDimension(dim);
				world = DimensionManager.getWorld(dim);
			}
			
			BlockPos spawn = world.provider.getSpawnPoint();
			int y;
			if(gateId == 1)
				y = gateHeight1;
			else y = gateHeight2;
			return new BlockPos(spawn.getX(), y, spawn.getZ());
		}
		
		return null;
	}
	
	public static void setDefiniteGatePos(int gateId, int dim, BlockPos newPos)
	{
		if(gateId == -1)
		{
			BlockPos oldPos = gateData.get(dim);
			if(oldPos.getY() != -1)
			{
				Debug.print("Trying to set position for a gate that should already be generated!");
				return;
			}
			
			gateData.put(dim, newPos);
		}
		else Debug.print("Trying to set position for a gate that should already be generated/doesn't exist!");
	}
	
	static void saveData(NBTTagList nbtList)
	{
		for(int i = 0; i < nbtList.tagCount(); i++)
		{
			NBTTagCompound nbt = nbtList.getCompoundTagAt(i);
			if(nbt.getString("type").equals("land"))
			{
				int dim = nbt.getInteger("dimID");
				if(gateData.containsKey(dim))
				{
					BlockPos gatePos = gateData.get(dim);
					nbt.setInteger("gateX", gatePos.getX());
					nbt.setInteger("gateY", gatePos.getY());
					nbt.setInteger("gateZ", gatePos.getZ());
				}
			}
		}
	}
	
	static void loadData(NBTTagList nbtList)
	{
		for(int i = 0; i < nbtList.tagCount(); i++)
		{
			NBTTagCompound nbt = nbtList.getCompoundTagAt(i);
			if(nbt.getString("type").equals("land") && nbt.hasKey("gateX"))
			{
				int dim = nbt.getInteger("dimID");
				BlockPos pos = new BlockPos(nbt.getInteger("gateX"), nbt.getInteger("gateY"), nbt.getInteger("gateZ"));
				gateData.put(dim, pos);
			}
		}
	}
	
}