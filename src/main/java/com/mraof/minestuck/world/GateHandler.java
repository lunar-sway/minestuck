package com.mraof.minestuck.world;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnections;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.gen.structure.gate.LandGatePlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Function;

public class GateHandler
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String DESTROYED = "minestuck.gate_destroyed";
	
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
					player.sendSystemMessage(Component.translatable(DESTROYED));
					return;
				}
			}
			
			Teleport.teleportEntity(player, destinationWorld, destination.pos().getX() + 0.5, destination.pos().getY(), destination.pos().getZ() + 0.5);
		}
	}
	
	private static GlobalPos findPosNearLandGate(ServerLevel level)
	{
		BlockPos pos = Type.LAND_GATE.getPosition(level);
		if(pos != null)
		{
			RandomSource rand = level.random;
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
				
				if(level.getBiome(placement).is(MSTags.Biomes.LAND_NORMAL))
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
		Optional<PlayerIdentifier> landPlayer = SburbPlayerData.getForLand(level).map(SburbPlayerData::playerId);
		if(landPlayer.isEmpty())
		{
			LOGGER.error("Unexpected error: Can't find player for land {}!", level.dimension());
			return null;
		}
		
		Optional<ResourceKey<Level>> clientLandOptional = SburbConnections.get(level.getServer())
				.primaryPartnerForServer(landPlayer.get())
				.flatMap(clientPlayer -> {
					SburbPlayerData clientPlayerData = SburbPlayerData.get(clientPlayer, level.getServer());
					return Optional.ofNullable(clientPlayerData.getLandDimensionIfEntered());
				});
		
		if(clientLandOptional.isEmpty())
		{
			return null;
		}
		
		ResourceKey<Level> clientLand = clientLandOptional.get();
		ServerLevel clientLevel = level.getServer().getLevel(clientLand);
		BlockPos gatePos = Type.LAND_GATE.getPosition(clientLevel);
		if(gatePos == null)
		{
			LOGGER.error("Unexpected error: Can't initialize land gate placement for dimension {}!", clientLand);
			return null;
		}
		
		return GlobalPos.of(clientLand, gatePos);
	}
	
	private static GlobalPos findServerSecondGate(ServerLevel level)
	{
		Optional<PlayerIdentifier> landPlayer = SburbPlayerData.getForLand(level).map(SburbPlayerData::playerId);
		if(landPlayer.isEmpty())
		{
			LOGGER.error("Unexpected error: Can't find player for land {}!", level.dimension());
			return null;
		}
		
		Optional<ResourceKey<Level>> serverLandOptional = SburbConnections.get(level.getServer()).primaryPartnerForClient(landPlayer.get())
				.flatMap(serverPlayer -> {
					SburbPlayerData serverPlayerData = SburbPlayerData.get(serverPlayer, level.getServer());
					return Optional.ofNullable(serverPlayerData.getLandDimensionIfEntered());
				});
		if(serverLandOptional.isEmpty())
		{
			return null;
		}
		
		var serverLand = serverLandOptional.get();
		return GlobalPos.of(serverLand, Type.GATE_2.getPosition(level.getServer().getLevel(serverLand)));
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