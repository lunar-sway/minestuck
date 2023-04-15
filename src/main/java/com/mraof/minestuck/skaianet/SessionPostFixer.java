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
		for(Session session : SessionHandler.get(event.getServer()).getSessions())
		{
			for(SburbConnection connection : session.connections)
				validate(event.getServer(), connection);
		}
	}
	
	private static void validate(MinecraftServer mcServer, SburbConnection connection)
	{
		if(connection.getClientDimension() != null && mcServer.getLevel(connection.getClientDimension()) == null)
		{
			LOGGER.error("Found missing land dimension \"{}\" in the connection for player {}. Resetting entry status.", connection.getClientDimension(), connection.getClientIdentifier().getUsername());
			connection.resetEntryState();
		}
	}
}
