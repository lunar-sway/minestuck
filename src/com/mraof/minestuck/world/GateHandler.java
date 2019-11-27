package com.mraof.minestuck.world;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.lands.LandInfoContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;

import java.util.Random;

public class GateHandler
{
	public static final String DESTROYED = "minestuck.gate_destroyed";
	public static final String MISSING_LAND = "minestuck.gate_missing_land";
	
	public static final int gateHeight1 = 144, gateHeight2 = 192;
	
	public static void teleport(Type gateType, DimensionType dim, ServerPlayerEntity player)
	{
		GlobalPos location = null;
		player.timeUntilPortal = player.getPortalCooldown();	//Basically to avoid message spam when something goes wrong
		
		if(gateType == Type.GATE_1)
		{
			BlockPos pos = getGatePos(player.server, Type.LAND_GATE, dim);
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
					
					if(player.world.getBiomeBody(placement) == MSBiomes.LAND_NORMAL)
						location = GlobalPos.of(dim, player.world.getHeight(Heightmap.Type.MOTION_BLOCKING, placement));
					
				} while(location == null);	//TODO replace with a more friendly version without a chance of freezing the game
			else Debug.errorf("Unexpected error: Couldn't find position for land gate for dimension %d.", dim);
			
		} else if(gateType == Type.GATE_2)
		{
			SburbConnection landConnection = SburbHandler.getConnectionForDimension(player.server, dim);
			if(landConnection != null)
			{
				SburbConnection clientConnection = SkaianetHandler.get(player.getServer()).getMainConnection(landConnection.getClientIdentifier(), false);
				
				if(clientConnection != null && clientConnection.hasEntered() && MSDimensions.isLandDimension(clientConnection.getClientDimension()))
				{
					DimensionType clientDim = clientConnection.getClientDimension();
					BlockPos gatePos = getGatePos(player.server, Type.LAND_GATE, clientDim);
					ServerWorld world = DimensionManager.getWorld(player.server, clientDim, false, true);
					//TODO Consider only storing the land gate pos through GateStructure and GatePiece
					if(gatePos == null)
					{
						findGatePlacement(world);
						gatePos = getGatePos(player.server, Type.LAND_GATE, clientDim);
						if(gatePos == null) {Debug.errorf("Unexpected error: Can't initiaize land gate placement for dimension %d!", clientDim); return;}
					}
					
					if(gatePos.getY() == -1)
					{
						//TODO Is this good enough to generate the gate structure? Is there a better way of doing it?
						world.getChunkProvider().getChunk(gatePos.getX() - 8 >> 4, gatePos.getZ() - 8 >> 4, true);
						world.getChunkProvider().getChunk(gatePos.getX() + 8 >> 4, gatePos.getZ() - 8 >> 4, true);
						world.getChunkProvider().getChunk(gatePos.getX() - 8 >> 4, gatePos.getZ() + 8 >> 4, true);
						world.getChunkProvider().getChunk(gatePos.getX() + 8 >> 4, gatePos.getZ() + 8 >> 4, true);
						gatePos = getGatePos(player.server, Type.LAND_GATE, clientDim);
						if(gatePos.getY() == -1) {Debug.errorf("Unexpected error: Gate didn't generate after loading chunks! Dim: %d, pos: %s", clientDim, gatePos); return;}
					}
					
					location = GlobalPos.of(clientDim, gatePos);
				}
				else player.sendMessage(new TranslationTextComponent(MISSING_LAND));
			} else Debug.errorf("Unexpected error: Can't find connection for dimension %d!", dim);
		} else if(gateType == Type.LAND_GATE)
		{
			SburbConnection landConnection = SburbHandler.getConnectionForDimension(player.server, dim);
			if(landConnection != null)
			{
				SburbConnection serverConnection = SkaianetHandler.get(player.getServer()).getMainConnection(landConnection.getServerIdentifier(), true);
				
				if(serverConnection != null && serverConnection.hasEntered() && MSDimensions.isLandDimension(serverConnection.getClientDimension()))	//Last shouldn't be necessary, but just in case something goes wrong elsewhere...
				{
					DimensionType serverDim = serverConnection.getClientDimension();
					location = GlobalPos.of(serverDim, getGatePos(player.server, Type.GATE_2, serverDim));
					
				} else player.sendMessage(new TranslationTextComponent(MISSING_LAND));
				
			} else Debug.errorf("Unexpected error: Can't find connection for dimension %d!", dim);
		} else Debug.errorf("Unexpected error: Gate id %d is out of bounds!", gateType);
		
		if(location != null)
		{
			if(gateType != Type.GATE_1)
			{
				ServerWorld world = DimensionManager.getWorld(player.server, location.getDimension(), false, true);
				
				BlockState block = world.getBlockState(location.getPos());
				
				if(block.getBlock() != MSBlocks.GATE)
				{
					Debug.debugf("Can't find destination gate at %s. Probably broken.", location);
					player.sendMessage(new TranslationTextComponent(DESTROYED));
					return;
				}
			}
			
			//Teleport.teleportEntity(player, location.dim, null, location.pos);	//TODO
		}
	}
	
	public static void findGatePlacement(World world)	//TODO The position is now determined by GateStructure. Use that one instead to determine the gate position.
	{
		DimensionType dim = world.getDimension().getType();
		LandInfoContainer info = MSDimensions.getLandInfo(world.getServer(), dim);
		if(MSDimensions.isLandDimension(dim) && info != null && info.getGatePos() == null)
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
				
				if(!world.getChunkProvider().chunkExists(pos.getX() >> 4, pos.getZ() >> 4) && Lists.newArrayList(MSBiomes.LAND_NORMAL).containsAll(world.getChunkProvider().getChunkGenerator().getBiomeProvider().getBiomesInSquare(pos.getX(), pos.getZ(), Math.max(20, 50 - tries))))
					gatePos = pos;
				
				tries++;
			} while(gatePos == null);
			
			Debug.infof("Land gate will generate at %d %d in dimension %s.", gatePos.getX(), gatePos.getZ(), dim.getRegistryName());
			info.setGatePos(gatePos);
		}
	}
	
	public static BlockPos getGatePos(MinecraftServer server, Type type, DimensionType dim)
	{
		if(!MSDimensions.isLandDimension(dim))
			return null;
		
		if(type == Type.LAND_GATE)
		{
			LandInfoContainer info = MSDimensions.getLandInfo(server, dim);
			if(info != null)
				return info.getGatePos();
			else
			{
				Debug.warn("Tried to find gate pos for dimension without land info!");
				return null;
			}
		}
		else if(type == Type.GATE_1 || type == Type.GATE_2)
		{
			BlockPos spawn = new BlockPos(0, 0, 0);
			int y;
			if(type == Type.GATE_1)
				y = gateHeight1;
			else y = gateHeight2;
			return new BlockPos(spawn.getX(), y, spawn.getZ());
		}
		
		return null;
	}
	
	public static void setDefiniteGatePos(MinecraftServer server, Type type, DimensionType dim, BlockPos newPos)
	{
		if(type == Type.LAND_GATE)
		{
			LandInfoContainer info = MSDimensions.getLandInfo(server, dim);
			if(info == null)
			{
				Debug.errorf("Tried to set gate position for dimension %s but did not get any land info", dim);
				return;
			}
			BlockPos oldPos = info.getGatePos();
			if(oldPos != null && oldPos.getY() != -1)
			{
				Debug.error("Trying to set position for a gate that should already be generated!");
				return;
			}
			
			info.setGatePos(newPos);
		}
		else Debug.error("Trying to set position for a gate that should already be generated/doesn't exist!");
	}
	
	public enum Type
	{
		GATE_1,
		GATE_2,
		LAND_GATE;
		
		@Override
		public String toString()
		{
			return this.name().toLowerCase();
		}
		
		public static Type fromString(String str)
		{
			for(Type type : values())
			{
				if(type.toString().equals(str))
					return type;
			}
			return null;
		}
	}
}