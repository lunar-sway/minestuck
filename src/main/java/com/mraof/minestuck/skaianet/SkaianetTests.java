package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

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
			
			connections.setPrimaryConnection(client, server2);
			helper.assertTrue(connections.isPrimaryPair(client, server2), "Primary server player did not match");
		});
	}
	@GameTest(timeoutTicks = 0, template = "empty_gametest")
	public static void multiSessionConnect(GameTestHelper helper)
	{
		helper.succeedIf(() -> {
			SkaianetData skaianetData = SkaianetData.newInstanceForGameTest(false, helper);
			SessionHandler sessions = skaianetData.sessionHandler;
			SburbConnections connections = skaianetData.connections;
			
			PlayerIdentifier client = IdentifierHandler.createNewFakeIdentifier(),
					server = IdentifierHandler.createNewFakeIdentifier();
			
			helper.assertFalse(sessions.getOrCreateSession(client) == sessions.getOrCreateSession(server), "Started in the same session");
			
			connections.setPrimaryConnection(client, server);
			
			helper.assertTrue(sessions.getOrCreateSession(client) == sessions.getOrCreateSession(server), "Sessions were not same after connecting");
			helper.assertTrue(sessions.getSessions().size() == 1, "More than one session present after connecting once");
			
			connections.setPrimaryConnection(server, client);
			
			helper.assertTrue(sessions.getOrCreateSession(client) == sessions.getOrCreateSession(server), "Sessions were not same after connecting both ways");
			helper.assertTrue(sessions.getSessions().size() == 1, "More than one session present after connecting both ways");
		});
	}
	
	@GameTest(timeoutTicks = 0, template = "empty_gametest")
	public static void multiSessionDisconnect(GameTestHelper helper)
	{
		helper.succeedIf(() -> {
			SkaianetData skaianetData = SkaianetData.newInstanceForGameTest(false, helper);
			SessionHandler sessions = skaianetData.sessionHandler;
			SburbConnections connections = skaianetData.connections;
			
			PlayerIdentifier client = IdentifierHandler.createNewFakeIdentifier(),
					server = IdentifierHandler.createNewFakeIdentifier();
			
			helper.assertFalse(sessions.getOrCreateSession(client) == sessions.getOrCreateSession(server), "Started in the same session");
			
			connections.setPrimaryConnection(client, server);
			
			helper.assertTrue(sessions.getOrCreateSession(client) == sessions.getOrCreateSession(server), "Sessions were not same after connecting");
			
			connections.unlinkClientPlayer(client);
			
			helper.assertFalse(sessions.getOrCreateSession(client) == sessions.getOrCreateSession(server), "Remained in the same session after unlinking");
		});
	}
	
	@GameTest(timeoutTicks = 0, template = "empty_gametest")
	public static void globalSession(GameTestHelper helper)
	{
		helper.succeedIf(() -> {
			SkaianetData skaianetData = SkaianetData.newInstanceForGameTest(true, helper);
			SessionHandler sessions = skaianetData.sessionHandler;
			SburbConnections connections = skaianetData.connections;
			
			PlayerIdentifier client = IdentifierHandler.createNewFakeIdentifier(),
					server = IdentifierHandler.createNewFakeIdentifier();
			
			Session session = sessions.getOrCreateSession(client);
			
			helper.assertTrue(session == sessions.getOrCreateSession(server), "Started in different sessions");
			
			connections.setPrimaryConnection(client, server);
			
			helper.assertTrue(session == sessions.getOrCreateSession(client)
					&& session == sessions.getOrCreateSession(server), "Session changed after connecting");
			
			connections.unlinkClientPlayer(client);
			
			helper.assertTrue(session == sessions.getOrCreateSession(client)
					&& session == sessions.getOrCreateSession(server), "Session changed after disconnecting");
		});
	}
	
	@GameTest(timeoutTicks = 0, template = "empty_gametest")
	public static void redundantCalls(GameTestHelper helper)
	{
		helper.succeedIf(() -> {
			SkaianetData skaianetData = SkaianetData.newInstanceForGameTest(false, helper);
			SburbConnections connections = skaianetData.connections;
			
			PlayerIdentifier client = IdentifierHandler.createNewFakeIdentifier(),
					server = IdentifierHandler.createNewFakeIdentifier();
			
			connections.unlinkClientPlayer(client);
			connections.unlinkServerPlayer(client);
			
			connections.setPrimaryConnection(client, server);
			connections.setPrimaryConnection(client, server);
		});
	}
}
