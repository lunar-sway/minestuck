package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.Optional;

@PrefixGameTestTemplate(false)
@GameTestHolder(Minestuck.MOD_ID)
public final class SkaianetTests
{
	@GameTest(timeoutTicks = 0, template = "empty_gametest")
	public static void primaryConnection(GameTestHelper helper)
	{
		helper.succeedIf(() -> {
			SkaianetData skaianetData = SkaianetData.newInstanceForGameTest(helper);
			
			PlayerIdentifier client = IdentifierHandler.createNewFakeIdentifier(),
					server = IdentifierHandler.createNewFakeIdentifier();
			helper.assertFalse(skaianetData.connections.hasPrimaryConnectionForClient(client), "Client player has primary connection on initialization");
			helper.assertFalse(skaianetData.connections.hasPrimaryConnectionForServer(server), "Server player has primary connection on initialization");
			
			skaianetData.connections.trySetPrimaryConnection(client, server);
			helper.assertTrue(skaianetData.connections.hasPrimaryConnectionForClient(client), "Client player is missing primary connection");
			helper.assertTrue(skaianetData.connections.hasPrimaryConnectionForServer(server), "Server player is missing primary connection");
			
			helper.assertTrue(skaianetData.connections.primaryPartnerForClient(client).equals(Optional.of(server)), "Primary partner does not match for client player");
			helper.assertTrue(skaianetData.connections.primaryPartnerForServer(server).equals(Optional.of(client)), "Primary partner does not match for server player");
		});
	}
	
	@GameTest(timeoutTicks = 0, template = "empty_gametest")
	public static void changePrimaryServerPlayer(GameTestHelper helper)
	{
		helper.succeedIf(() -> {
			SkaianetData skaianetData = SkaianetData.newInstanceForGameTest(helper);
			
			PlayerIdentifier client = IdentifierHandler.createNewFakeIdentifier(),
					server1 = IdentifierHandler.createNewFakeIdentifier(),
					server2 = IdentifierHandler.createNewFakeIdentifier();
			
			skaianetData.connections.trySetPrimaryConnection(client, server1);
			helper.assertTrue(skaianetData.connections.primaryPartnerForClient(client).equals(Optional.of(server1)), "Primary server player did not match");
			
			skaianetData.connections.unlinkClientPlayer(client);
			helper.assertTrue(skaianetData.connections.primaryPartnerForClient(client).isEmpty(), "Client player still has partner");
			helper.assertTrue(skaianetData.connections.hasPrimaryConnectionForClient(client), "Client lost primary connection entirely");
			
			skaianetData.connections.newServerForClient(client, server2);
			helper.assertTrue(skaianetData.connections.primaryPartnerForClient(client).equals(Optional.of(server2)), "Primary server player did not match");
		});
	}
}
