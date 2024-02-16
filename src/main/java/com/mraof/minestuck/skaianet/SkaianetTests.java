package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.Optional;

/**
 * A collection of {@link GameTest}s which focus on testing the core systems in the skaianet package.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(Minestuck.MOD_ID)
public final class SkaianetTests
{
	@GameTest(timeoutTicks = 0, template = "empty_gametest")
	public static void primaryConnection(GameTestHelper helper)
	{
		helper.succeedIf(() -> {
			SkaianetData skaianetData = SkaianetData.newInstanceForGameTest(false, helper);
			SburbConnections connections = skaianetData.connections;
			
			PlayerIdentifier client = IdentifierHandler.createNewFakeIdentifier(),
					server = IdentifierHandler.createNewFakeIdentifier();
			helper.assertFalse(connections.hasPrimaryConnectionForClient(client), "Client player has primary connection on initialization");
			helper.assertFalse(connections.hasPrimaryConnectionForServer(server), "Server player has primary connection on initialization");
			
			connections.setPrimaryConnection(client, server);
			helper.assertTrue(connections.hasPrimaryConnectionForClient(client), "Client player is missing primary connection");
			helper.assertTrue(connections.hasPrimaryConnectionForServer(server), "Server player is missing primary connection");
			
			helper.assertTrue(connections.primaryPartnerForClient(client).equals(Optional.of(server)), "Primary partner does not match for client player");
			helper.assertTrue(connections.primaryPartnerForServer(server).equals(Optional.of(client)), "Primary partner does not match for server player");
		});
	}
	
	@GameTest(timeoutTicks = 0, template = "empty_gametest")
	public static void changePrimaryServerPlayer(GameTestHelper helper)
	{
		helper.succeedIf(() -> {
			SkaianetData skaianetData = SkaianetData.newInstanceForGameTest(false, helper);
			SburbConnections connections = skaianetData.connections;
			
			PlayerIdentifier client = IdentifierHandler.createNewFakeIdentifier(),
					server1 = IdentifierHandler.createNewFakeIdentifier(),
					server2 = IdentifierHandler.createNewFakeIdentifier();
			
			connections.setPrimaryConnection(client, server1);
			helper.assertTrue(connections.isPrimaryPair(client, server1), "Primary server player did not match");
			
			connections.unlinkClientPlayer(client);
			helper.assertTrue(connections.primaryPartnerForClient(client).isEmpty(), "Client player still has partner");
			helper.assertTrue(connections.hasPrimaryConnectionForClient(client), "Client lost primary connection entirely");
			
			connections.newServerForClient(client, server2);
			helper.assertTrue(connections.isPrimaryPair(client, server2), "Primary server player did not match");
		});
	}
}
