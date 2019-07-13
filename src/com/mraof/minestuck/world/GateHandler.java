package com.mraof.minestuck.world;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Location;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GateHandler
{
	
	public static final int gateHeight1 = 144, gateHeight2 = 192;
	
	static Map<DimensionType, BlockPos> gateData = new HashMap<>();
	
	public static void teleport(int gateId, DimensionType dim, ServerPlayerEntity player)
	{
		Location location = null;
		player.timeUntilPortal = player.getPortalCooldown();	//Basically to avoid message spam when something goes wrong
		
		if(gateId == 1)
		{
			BlockPos pos = getGatePos(player.server, -1, dim);
			Random rand = player.world.rand;
			BlockPos spawn = player.world.getDimension().getSpawnPoint();
			if(pos != null)
				do
				{
					int radius = 160 + rand.nextInt(60);
					double d = rand.nextDouble();
					int i = radius*radius;
					int x = (int) Math.sqrt(i*d);
					int z = (int) Math.sqrt(i*(1-d));
					if(rand.nextBoolean()) x = -x;
					if(rand.nextBoolean()) z = -z;
					
					BlockPos placement = pos.add(x, 0, z);
					
					if(player.world.getBiomeBody(placement) == BiomeMinestuck.mediumNormal)
						location = new Location(player.world.getHeight(Heightmap.Type.MOTION_BLOCKING, placement), dim);
					
				} while(location == null);	//TODO replace with a more friendly version without a chance of freezing the game
			else Debug.errorf("Unexpected error: Couldn't find position for land gate for dimension %d.", dim);
			
		} else if(gateId == 2)
		{
			SburbConnection landConnection = SburbHandler.getConnectionForDimension(player.server, dim);
			if(landConnection != null)
			{
				SburbConnection clientConnection = SkaianetHandler.get(player.world).getMainConnection(landConnection.getClientIdentifier(), false);
				
				if(clientConnection != null && clientConnection.hasEntered() && MinestuckDimensionHandler.isLandDimension(clientConnection.getClientDimension()))
				{
					DimensionType clientDim = clientConnection.getClientDimension();
					BlockPos gatePos = getGatePos(player.server, -1, clientDim);
					WorldServer world = DimensionManager.getWorld(player.server, clientDim, false, true);
					
					if(gatePos == null)
					{
						findGatePlacement(world);
						gatePos = getGatePos(player.server, -1, clientDim);
						if(gatePos == null) {Debug.errorf("Unexpected error: Can't initiaize land gate placement for dimension %d!", clientDim); return;}
					}
					
					if(gatePos.getY() == -1)
					{
						world.getChunkProvider().getChunk(gatePos.getX() - 8 >> 4, gatePos.getZ() - 8 >> 4, true, true);
						world.getChunkProvider().getChunk(gatePos.getX() + 8 >> 4, gatePos.getZ() - 8 >> 4, true, true);
						world.getChunkProvider().getChunk(gatePos.getX() - 8 >> 4, gatePos.getZ() + 8 >> 4, true, true);
						world.getChunkProvider().getChunk(gatePos.getX() + 8 >> 4, gatePos.getZ() + 8 >> 4, true, true);
						gatePos = getGatePos(player.server, -1, clientDim);
						if(gatePos.getY() == -1) {Debug.errorf("Unexpected error: Gate didn't generate after loading chunks! Dim: %d, pos: %s", clientDim, gatePos); return;}
					}
					
					location = new Location(gatePos, clientDim);
				}
				else player.sendMessage(new TextComponentTranslation("message.gateMissingLand"));
			} else Debug.errorf("Unexpected error: Can't find connection for dimension %d!", dim);
		} else if(gateId == -1)
		{
			SburbConnection landConnection = SburbHandler.getConnectionForDimension(player.server, dim);
			if(landConnection != null)
			{
				SburbConnection serverConnection = SkaianetHandler.get(player.world).getMainConnection(landConnection.getServerIdentifier(), true);
				
				if(serverConnection != null && serverConnection.hasEntered() && MinestuckDimensionHandler.isLandDimension(serverConnection.getClientDimension()))	//Last shouldn't be necessary, but just in case something goes wrong elsewhere...
				{
					DimensionType serverDim = serverConnection.getClientDimension();
					location = new Location(getGatePos(player.server, 2, serverDim), serverDim);
					
				} else player.sendMessage(new TextComponentTranslation("message.gateMissingLand"));
				
			} else Debug.errorf("Unexpected error: Can't find connection for dimension %d!", dim);
		} else Debug.errorf("Unexpected error: Gate id %d is out of bounds!", gateId);
		
		if(location != null)
		{
			if(gateId != 1)
			{
				WorldServer world = DimensionManager.getWorld(player.server, location.dim, false, true);
				
				IBlockState block = world.getBlockState(location.pos);
				
				if(block.getBlock() != MinestuckBlocks.GATE)
				{
					Debug.debugf("Can't find destination gate at %s. Probably broken.", location);
					player.sendMessage(new TextComponentTranslation("message.gateDestroyed"));
					return;
				}
			}
			
			Teleport.teleportEntity(player, location.dim, null, location.pos);
		}
	}
	
	public static void findGatePlacement(World world)
	{
		DimensionType dim = world.getDimension().getType();
		if(MinestuckDimensionHandler.isLandDimension(dim) && !gateData.containsKey(dim))
		{
			BlockPos spawn = new BlockPos(0, -1, 0);
			Random rand = new Random(world.getSeed()^43839551L^world.getDimension().getType().getId());
			
			BlockPos gatePos = null;
			int tries = 0;
			do
			{
				int distance = (500 + rand.nextInt(200 + tries));	//The longer time it takes, the larger the area searched
				distance *= distance;
				double d = rand.nextDouble();
				int x = (int) Math.sqrt(distance*d);
				int z = (int) Math.sqrt(distance*(1-d));
				
				BlockPos pos = new BlockPos(spawn.getX() + x, -1, spawn.getZ() + z);
				//TODO When we have biomes again
				//if(/*!world.getChunkProvider().chunkExists(pos.getX() >> 4, pos.getZ() >> 4) &&*/ Lists.newArrayList(BiomeMinestuck.mediumNormal).containsAll(world.getChunkProvider().getChunkGenerator().getBiomeProvider().getBiomesInSquare(pos.getX(), pos.getZ(), Math.max(20, 50 - tries))))
					gatePos = pos;
				
				tries++;
			} while(gatePos == null);	//TODO replace with a more friendly version without a chance of freezing the game
			
			Debug.infof("Land gate will generate at %d %d in dimension %s.", gatePos.getX(), gatePos.getZ(), dim.getRegistryName());
			gateData.put(dim, gatePos);
		}
	}
	
	public static BlockPos getGatePos(MinecraftServer server, int gateId, DimensionType dim)
	{
		if(!MinestuckDimensionHandler.isLandDimension(dim))
			return null;
		
		if(gateId == -1)
			return gateData.get(dim);
		else if(gateId == 1 || gateId == 2)
		{
			World world = DimensionManager.getWorld(server, dim, false, true);
			
			BlockPos spawn = world.getDimension().getSpawnPoint();
			int y;
			if(gateId == 1)
				y = gateHeight1;
			else y = gateHeight2;
			return new BlockPos(spawn.getX(), y, spawn.getZ());
		}
		
		return null;
	}
	
	public static void setDefiniteGatePos(int gateId, DimensionType dim, BlockPos newPos)
	{
		if(gateId == -1)
		{
			BlockPos oldPos = gateData.get(dim);
			if(oldPos.getY() != -1)
			{
				Debug.error("Trying to set position for a gate that should already be generated!");
				return;
			}
			
			gateData.put(dim, newPos);
		}
		else Debug.error("Trying to set position for a gate that should already be generated/doesn't exist!");
	}
	
	static void saveData(NBTTagList nbtList)
	{
		for(int i = 0; i < nbtList.size(); i++)
		{
			NBTTagCompound nbt = nbtList.getCompound(i);
			if(nbt.getString("type").equals("land"))
			{
				int dim = nbt.getInt("dimID");
				if(gateData.containsKey(dim))
				{
					BlockPos gatePos = gateData.get(dim);
					nbt.putInt("gateX", gatePos.getX());
					nbt.putInt("gateY", gatePos.getY());
					nbt.putInt("gateZ", gatePos.getZ());
				}
			}
		}
	}
	
	static void loadData(NBTTagList nbtList)
	{
		for(int i = 0; i < nbtList.size(); i++)
		{
			NBTTagCompound nbt = nbtList.getCompound(i);
			if(nbt.getString("type").equals("land") && nbt.contains("gateX"))
			{
				DimensionType dim = DimensionType.byName(ResourceLocation.tryCreate(nbt.getString("dim")));
				if(dim != null)
				{
					BlockPos pos = new BlockPos(nbt.getInt("gateX"), nbt.getInt("gateY"), nbt.getInt("gateZ"));
					gateData.put(dim, pos);
				} else Debug.warnf("Unable to load gate position for dimension %s. Could not find dimension by that name!", nbt.getString("dim"));
			}
		}
	}
	
}