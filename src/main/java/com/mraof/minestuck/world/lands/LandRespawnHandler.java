package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.util.Teleport;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class LandRespawnHandler
{
	@SubscribeEvent
	public static void onRespawn(PlayerEvent.PlayerRespawnEvent event)
	{
		if(event.isEndConquered() || !(event.getEntity() instanceof ServerPlayer player))
			return;
		if(player.getRespawnPosition() != null)
			return;
		
		ResourceKey<Level> land = SburbPlayerData.get(IdentifierHandler.encode(player), player.server).getLandDimensionIfEntered();
		
		if(land == null)
			return;
		
		ServerLevel landLevel = player.server.getLevel(land);
		
		if (landLevel == null)
			return;
		
		int spawnFuzz = 12;	//TODO spawn explicitly within the entry area
		int spawnFuzzHalf = spawnFuzz / 2;
		
		BlockPos spawn = new BlockPos(player.getRandom().nextInt(spawnFuzz) - spawnFuzzHalf,
				0, player.getRandom().nextInt(spawnFuzz) - spawnFuzzHalf);
		
		int y = landLevel.getChunk(spawn).getHeight(Heightmap.Types.MOTION_BLOCKING, spawn.getX() & 15, spawn.getZ() & 15) + 1;
		if(y >= 0)
		{
			spawn = new BlockPos(spawn.getX(), y, spawn.getZ());
		}
		
		Teleport.teleportEntity(player, landLevel, spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5);
	}
}