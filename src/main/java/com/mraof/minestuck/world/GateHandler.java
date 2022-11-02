package com.mraof.minestuck.world;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.biome.RegistryBackedBiomeSet;
import com.mraof.minestuck.world.gen.structure.gate.LandGatePlacement;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

public class GateHandler
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String DESTROYED = "minestuck.gate_destroyed";
	public static final String MISSING_LAND = "minestuck.gate_missing_land";
	
	public static final int GATE_HEIGHT_1 = 124, GATE_HEIGHT_2 = 154; //intervals of 30 blocks: 124/154/184/214/244/274/304
	
	public static void teleport(Type gateType, ServerLevel level, ServerPlayer player)
	{
		player.setPortalCooldown();	//Basically to avoid message spam when something goes wrong
		
		GlobalPos destination = gateType.getDestination(level);
		
		if(destination != null)
		{
			ServerLevel destinationWorld = player.server.getLevel(destination.dimension());
			
			if(gateType.isDestinationGate)
			{
				BlockState block = destinationWorld.getBlockState(destination.pos());
				
				if(!block.is(MSBlocks.GATE_MAIN.get()))
				{
					LOGGER.debug("Can't find destination gate at {}. Probably broken.", destination);
					player.sendMessage(new TranslatableComponent(DESTROYED), Util.NIL_UUID);
					return;
				}
			}
			
			Teleport.teleportEntity(player, destinationWorld, destination.pos().getX() + 0.5, destination.pos().getY(), destination.pos().getZ() + 0.5);
		}
	}
	
	private static GlobalPos findPosNearLandGate(ServerLevel level)
	{
		BlockPos pos = Type.LAND_GATE.getPosition(level);
		Optional<RegistryBackedBiomeSet> optional = LandBiomeSetType.getSet(level.getChunkSource().getGenerator());
		if(pos != null && optional.isPresent())
		{
			Random rand = level.random;
			RegistryBackedBiomeSet biomes = optional.get();
			while(true)    //TODO replace with a more friendly version without a chance of freezing the game
			{
				int radius = 160 + rand.nextInt(60);
				double d = rand.nextDouble();
				int i = radius * radius;
				int x = (int) Math.sqrt(i * d);
				int z = (int) Math.sqrt(i * (1 - d));
				if(rand.nextBoolean()) x = -x;
				if(rand.nextBoolean()) z = -z;
				
				BlockPos placement = pos.offset(x, 0, z);
				
				if(biomes.NORMAL == level.getBiome(placement))
				{
					//TODO Can and has placed the player into a lava ocean. Fix this (Also for other hazards)
					int y = level.getChunk(placement).getHeight(Heightmap.Types.MOTION_BLOCKING, placement.getX(), placement.getZ());
					return GlobalPos.of(level.dimension(), new BlockPos(placement.getX(), y + 1, placement.getZ()));
				}
			}
		} else
			LOGGER.error("Unexpected error: Couldn't find position for land gate for dimension {}.", level.dimension());
		return null;
	}
	
	private static GlobalPos findClientLandGate(ServerLevel level)
	{
		SburbConnection landConnection = SburbHandler.getConnectionForDimension(level.getServer(), level.dimension());
		if(landConnection != null)
		{
			SburbConnection clientConnection = SkaianetHandler.get(level.getServer()).getPrimaryConnection(landConnection.getClientIdentifier(), false).orElse(null);
			
			if(clientConnection != null && clientConnection.hasEntered() && MSDimensions.isLandDimension(level.getServer(), clientConnection.getClientDimension()))
			{
				ResourceKey<Level> clientDim = clientConnection.getClientDimension();
				ServerLevel clientLevel = level.getServer().getLevel(clientDim);
				BlockPos gatePos = Type.LAND_GATE.getPosition(clientLevel);
				if(gatePos == null)
				{LOGGER.error("Unexpected error: Can't initialize land gate placement for dimension {}!", clientDim); return null;}
				
				return GlobalPos.of(clientDim, gatePos);
			}
			//else player.sendMessage(new TranslationTextComponent(MISSING_LAND));
		} else
			LOGGER.error("Unexpected error: Can't find connection for dimension {}!", level.dimension());
		return null;
	}
	
	private static GlobalPos findServerSecondGate(ServerLevel level)
	{
		SburbConnection landConnection = SburbHandler.getConnectionForDimension(level.getServer(), level.dimension());
		if(landConnection != null)
		{
			SburbConnection serverConnection = SkaianetHandler.get(level.getServer()).getPrimaryConnection(landConnection.getServerIdentifier(), true).orElse(null);
			
			if(serverConnection != null && serverConnection.hasEntered() && MSDimensions.isLandDimension(level.getServer(), serverConnection.getClientDimension()))	//Last shouldn't be necessary, but just in case something goes wrong elsewhere...
			{
				ResourceKey<Level> serverDim = serverConnection.getClientDimension();
				return GlobalPos.of(serverDim, Type.GATE_2.getPosition(level.getServer().getLevel(serverDim)));
				
			}// else player.sendMessage(new TranslationTextComponent(MISSING_LAND));
			
		} else
			LOGGER.error("Unexpected error: Can't find connection for dimension {}!", level.dimension());
		return null;
	}
	
	public enum Type
	{
		GATE_1(false, world -> new BlockPos(0, GATE_HEIGHT_1, 0), GateHandler::findPosNearLandGate),
		GATE_2(true, world -> new BlockPos(0, GATE_HEIGHT_2, 0), GateHandler::findClientLandGate),
		LAND_GATE(true, LandGatePlacement::findLandGatePos, GateHandler::findServerSecondGate);
		
		private final boolean isDestinationGate;
		private final Function<ServerLevel, BlockPos> locationFinder;
		private final Function<ServerLevel, GlobalPos> destinationFinder;
		
		Type(boolean isDestinationGate, Function<ServerLevel, BlockPos> locationFinder, Function<ServerLevel, GlobalPos> destinationFinder)
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
		
		public GlobalPos getDestination(ServerLevel level)
		{
			return destinationFinder.apply(level);
		}
		
		public BlockPos getPosition(ServerLevel level)
		{
			return locationFinder.apply(level);
		}
	}
}