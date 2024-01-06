package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.Minestuck;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
		for(SburbPlayerData playerData : SkaianetHandler.get(event.getServer()).playerDataMap.values())
			validate(event.getServer(), playerData);
	}
	
	private static void validate(MinecraftServer mcServer, SburbPlayerData playerData)
	{
		if(playerData.getClientDimension() != null && mcServer.getLevel(playerData.getClientDimension()) == null)
		{
			LOGGER.error("Found missing land dimension \"{}\" in the connection for player {}. Resetting entry status.", playerData.getClientDimension(), playerData.getPlayerId().getUsername());
			playerData.resetEntryState();
		}
	}
}
