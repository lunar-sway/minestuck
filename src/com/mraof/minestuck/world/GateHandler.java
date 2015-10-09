package com.mraof.minestuck.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Location;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.biome.BiomeGenMinestuck;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class GateHandler
{
	
	public static final int gateHeight1 = 144, gateHeight2 = 192;
	
	static Map<Integer, BlockPos> gateData = new HashMap<Integer, BlockPos>();
	
	public static void teleport(int gateId, int dim, EntityPlayerMP player)
	{
		Location location = null;
		player.timeUntilPortal = 10;	//Basically to avoid message spam when something goes wrong
		
		if(gateId == 1)
		{
			BlockPos pos = getGatePos(-1, dim);
			Random rand = player.worldObj.rand;
			BlockPos spawn = player.worldObj.provider.getSpawnPoint();
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
					
					if(player.worldObj.getBiomeGenForCoordsBody(placement) != BiomeGenMinestuck.mediumOcean)
						location = new Location(player.worldObj.getTopSolidOrLiquidBlock(placement), dim);
					
				} while(location == null);	//TODO replace with a more friendly version without a chance of freezing the game
			else Debug.printf("Unexpected error: Couldn't find position for land gate for dimension %d.", dim);
			
		} else if(gateId == 2)
		{
			SburbConnection landConnection = SessionHandler.getConnectionForDimension(dim);
			if(landConnection != null)
			{
				SburbConnection clientConnection = SkaianetHandler.getMainConnection(landConnection.getClientName(), false);
				
				if(clientConnection != null && clientConnection.enteredGame() && MinestuckDimensionHandler.isLandDimension(clientConnection.getClientDimension()))
				{
					int clientDim = clientConnection.getClientDimension();
					BlockPos gatePos = getGatePos(-1, clientDim);
					WorldServer world = player.mcServer.worldServerForDimension(clientDim);
					
					if(gatePos == null)
					{
						findGatePlacement(world);
						gatePos = getGatePos(-1, clientDim);
						if(gatePos == null) {Debug.printf("Unexpected error: Can't initiaize land gate placement for dimension %d!", clientDim); return;}
					}
					
					if(gatePos.getY() == -1)
					{
						world.theChunkProviderServer.loadChunk(gatePos.getX() - 8 >> 4, gatePos.getZ() - 8 >> 4);
						world.theChunkProviderServer.loadChunk(gatePos.getX() + 8 >> 4, gatePos.getZ() - 8 >> 4);
						world.theChunkProviderServer.loadChunk(gatePos.getX() - 8 >> 4, gatePos.getZ() + 8 >> 4);
						world.theChunkProviderServer.loadChunk(gatePos.getX() + 8 >> 4, gatePos.getZ() + 8 >> 4);
						gatePos = getGatePos(-1, clientDim);
						if(gatePos.getY() == -1) {Debug.printf("Unexpected error: Gate didn't generate after loading chunks! Dim: %d, pos: %s", clientDim, gatePos); return;}
					}
					
					location = new Location(gatePos, clientDim);
				}
				
			} else Debug.printf("Unexpected error: Can't find connection for dimension %d!", dim);
		} else if(gateId == -1)
		{
			SburbConnection landConnection = SessionHandler.getConnectionForDimension(dim);
			if(landConnection != null)
			{
				SburbConnection serverConnection = SkaianetHandler.getMainConnection(landConnection.getServerName(), true);
				
				if(serverConnection != null && serverConnection.enteredGame() && MinestuckDimensionHandler.isLandDimension(serverConnection.getClientDimension()))	//Last shouldn't be necessary, but just in case something goes wrong elsewhere...
				{
					int serverDim = serverConnection.getClientDimension();
					location = new Location(getGatePos(2, serverDim), serverDim);
					
				} else Debug.printf("Player %s tried to teleport through gate before their server player entered the game.", player.getCommandSenderName());
				
			} else Debug.printf("Unexpected error: Can't find connection for dimension %d!", dim);
		} else Debug.printf("Unexpected error: Gate id %d is out of bounds!", gateId);
		
		if(location != null)
		{
			if(gateId != 1)
			{
				WorldServer world = player.mcServer.worldServerForDimension(location.dim);
				
				IBlockState block = world.getBlockState(location.pos);
				
				if(block.getBlock() != MinestuckBlocks.gate)
				{
					Debug.print("Can't find destination gate. Probably destroyed.");
					player.addChatMessage(new ChatComponentTranslation("message.gateDestroyed"));
					return;
				}
			}
			
			player.timeUntilPortal = 60;
			if(location.dim != dim)
				Teleport.teleportEntity(player, location.dim, null, false);
			player.playerNetServerHandler.setPlayerLocation(location.pos.getX() + 0.5, location.pos.getY(), location.pos.getZ() + 0.5, player.rotationYaw, player.rotationPitch);
		}
	}
	
	public static void findGatePlacement(World world)
	{
		int dim = world.provider.getDimensionId();
		if(MinestuckDimensionHandler.isLandDimension(dim) && !gateData.containsKey(dim))
		{
			BlockPos spawn = MinestuckDimensionHandler.getSpawn(dim);
			Random rand = world.setRandomSeed(0, 0, 43839551^world.provider.getDimensionId());
			
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
				
				if(!world.getChunkProvider().chunkExists(pos.getX() >> 4, pos.getZ() >> 4) && world.provider.getWorldChunkManager().areBiomesViable(pos.getX(), pos.getZ(), Math.max(20, 50 - tries), Lists.newArrayList(BiomeGenMinestuck.mediumNormal)))
					gatePos = pos;
				
				tries++;
			} while(gatePos == null);	//TODO replace with a more friendly version without a chance of freezing the game
			
			Debug.printf("Land gate will generate at %d %d.", gatePos.getX(), gatePos.getZ());
			gateData.put(dim, gatePos);
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