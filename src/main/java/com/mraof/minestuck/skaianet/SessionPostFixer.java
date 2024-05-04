package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.Minestuck;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class responsible for fixing invalid states in connection or session data,
 * which might happen if a world is saved incorrectly after a crash.
 */
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID)
public final class SessionPostFixer
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@SubscribeEvent
	public static void onServerStarted(ServerStartedEvent event)
	{
		for(SburbPlayerData playerData : SkaianetData.get(event.getServer()).allPlayerData())
			validate(event.getServer(), playerData);
	}
	
	private static void validate(MinecraftServer mcServer, SburbPlayerData playerData)
	{
		if(playerData.getLandDimension() != null && mcServer.getLevel(playerData.getLandDimension()) == null)
		{
			LOGGER.error("Found missing land dimension \"{}\" in the connection for player {}. Resetting entry status.", playerData.getLandDimension(), playerData.playerId().getUsername());
			playerData.resetEntryState();
		}
	}
}
