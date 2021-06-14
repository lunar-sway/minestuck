package com.mraof.minestuck.world;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.biome.BiomeType;
import com.mraof.minestuck.world.lands.LandInfo;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Function;

public class GateHandler
{
	public static final String DESTROYED = "minestuck.gate_destroyed";
	public static final String MISSING_LAND = "minestuck.gate_missing_land";
	
	public static final int gateHeight1 = 144, gateHeight2 = 192;
	
	public static void teleport(Type gateType, ServerWorld world, ServerPlayerEntity player)
	{
		player.setPortalCooldown();	//Basically to avoid message spam when something goes wrong
		
		GlobalPos destination = gateType.getDestination(world);
		
		if(destination != null)
		{
			ServerWorld destinationWorld = player.server.getLevel(destination.dimension());
			
			if(gateType.isDestinationGate)
			{
				BlockState block = destinationWorld.getBlockState(destination.pos());
				
				if(block.getBlock() != MSBlocks.GATE)
				{
					Debug.debugf("Can't find destination gate at %s. Probably broken.", destination);
					player.sendMessage(new TranslationTextComponent(DESTROYED), Util.NIL_UUID);
					return;
				}
			}
			
			Teleport.teleportEntity(player, destinationWorld, destination.pos().getX() + 0.5, destination.pos().getY(), destination.pos().getZ() + 0.5);
		}
	}
	
	private static BlockPos getSavedLandGate(ServerWorld world)
	{
		LandInfo info = MSDimensions.getLandInfo(world);
		if(info != null)
		{
			if(info.getGatePos() != null)
				return info.getGatePos();
		}
		
		BlockPos gatePos = null;//TODO MSFeatures.LAND_GATE.findLandGatePos(world);
		
		if(info != null)
			info.setGatePos(gatePos);
		
		return gatePos;
	}
	
	private static GlobalPos findPosNearLandGate(ServerWorld world)
	{
		BlockPos pos = Type.LAND_GATE.getPosition(world);
		Random rand = world.random;
		if(pos != null)
			while(true)	//TODO replace with a more friendly version without a chance of freezing the game
			{
				int radius = 160 + rand.nextInt(60);
				double d = rand.nextDouble();
				int i = radius*radius;
				int x = (int) Math.sqrt(i*d);
				int z = (int) Math.sqrt(i*(1-d));
				if(rand.nextBoolean()) x = -x;
				if(rand.nextBoolean()) z = -z;
				
				BlockPos placement = pos.offset(x, 0, z);
				
				if(BiomeType.NORMAL.isBiomeOfType(world.getBiome(placement)))
				{
					//TODO Can and has placed the player into a lava ocean. Fix this (Also for other hazards)
					int y = world.getChunk(placement).getHeight(Heightmap.Type.MOTION_BLOCKING, placement.getX(), placement.getZ());
					return GlobalPos.of(world.dimension(), new BlockPos(placement.getX(), y + 1, placement.getZ()));
				}
			}
		else
			Debug.errorf("Unexpected error: Couldn't find position for land gate for dimension %d.", world.dimension());
		return null;
	}
	
	private static GlobalPos findClientLandGate(ServerWorld world)
	{
		SburbConnection landConnection = SburbHandler.getConnectionForDimension(world.getServer(), world.dimension());
		if(landConnection != null)
		{
			SburbConnection clientConnection = SkaianetHandler.get(world.getServer()).getPrimaryConnection(landConnection.getClientIdentifier(), false).orElse(null);
			
			if(clientConnection != null && clientConnection.hasEntered() && MSDimensions.isLandDimension(clientConnection.getClientDimension()))
			{
				RegistryKey<World> clientDim = clientConnection.getClientDimension();
				ServerWorld clientWorld = world.getServer().getLevel(clientDim);
				BlockPos gatePos = Type.LAND_GATE.getPosition(clientWorld);
				if(gatePos == null)
				{Debug.errorf("Unexpected error: Can't initialize land gate placement for dimension %d!", clientDim); return null;}
				
				return GlobalPos.of(clientDim, gatePos);
			}
			//else player.sendMessage(new TranslationTextComponent(MISSING_LAND));
		} else
			Debug.errorf("Unexpected error: Can't find connection for dimension %d!", world.dimension());
		return null;
	}
	
	private static GlobalPos findServerSecondGate(ServerWorld world)
	{
		SburbConnection landConnection = SburbHandler.getConnectionForDimension(world.getServer(), world.dimension());
		if(landConnection != null)
		{
			SburbConnection serverConnection = SkaianetHandler.get(world.getServer()).getPrimaryConnection(landConnection.getServerIdentifier(), true).orElse(null);
			
			if(serverConnection != null && serverConnection.hasEntered() && MSDimensions.isLandDimension(serverConnection.getClientDimension()))	//Last shouldn't be necessary, but just in case something goes wrong elsewhere...
			{
				RegistryKey<World> serverDim = serverConnection.getClientDimension();
				return GlobalPos.of(serverDim, Type.GATE_2.getPosition(world.getServer().getLevel(serverDim)));
				
			}// else player.sendMessage(new TranslationTextComponent(MISSING_LAND));
			
		} else Debug.errorf("Unexpected error: Can't find connection for dimension %d!", world.dimension());
		return null;
	}
	
	public enum Type
	{
		GATE_1(false, world -> new BlockPos(0, gateHeight1, 0), GateHandler::findPosNearLandGate),
		GATE_2(true, world -> new BlockPos(0, gateHeight2, 0), GateHandler::findClientLandGate),
		LAND_GATE(true, GateHandler::getSavedLandGate, GateHandler::findServerSecondGate);
		
		private final boolean isDestinationGate;
		private final Function<ServerWorld, BlockPos> locationFinder;
		private final Function<ServerWorld, GlobalPos> destinationFinder;
		
		Type(boolean isDestinationGate, Function<ServerWorld, BlockPos> locationFinder, Function<ServerWorld, GlobalPos> destinationFinder)
		{
			this.isDestinationGate = isDestinationGate;
			this.locationFinder = locationFinder;
			this.destinationFinder = destinationFinder;
		}
		
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
		
		public GlobalPos getDestination(ServerWorld world)
		{
			return destinationFinder.apply(world);
		}
		
		public BlockPos getPosition(ServerWorld world)
		{
			return locationFinder.apply(world);
		}
	}
}