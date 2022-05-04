package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Teleport;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public class LandRespawnHandler
{
	@SubscribeEvent
	public static void onRespawn(PlayerEvent.PlayerRespawnEvent event)
	{
		if (!event.isEndConquered() && event.getPlayer() instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
			if (player.getRespawnPosition() == null)
			{
				Optional<SburbConnection> c = SkaianetHandler.get(player.server).getPrimaryConnection(IdentifierHandler.encode(player), true);
				if(c.isPresent() && c.get().getLandInfo() != null)
				{
					LandInfo info = c.get().getLandInfo();
					RegistryKey<World> land = info.getDimensionType();
					ServerWorld destination = player.server.getLevel(land);
					BlockPos spawn = info.getSpawn();
					
					int spawnFuzz = 12;	//TODO spawn explicitly within the entry area
					int spawnFuzzHalf = spawnFuzz / 2;
					
					spawn = spawn.offset(player.getRandom().nextInt(spawnFuzz) - spawnFuzzHalf,
							0, player.getRandom().nextInt(spawnFuzz) - spawnFuzzHalf);
					
					int y = destination.getChunk(spawn).getHeight(Heightmap.Type.MOTION_BLOCKING, spawn.getX() & 15, spawn.getZ() & 15) + 1;
					if(y >= 0)
					{
						spawn = new BlockPos(spawn.getX(), y, spawn.getZ());
					}
					
					Teleport.teleportEntity(player, destination, spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5);
				}
			}
		}
	}
}