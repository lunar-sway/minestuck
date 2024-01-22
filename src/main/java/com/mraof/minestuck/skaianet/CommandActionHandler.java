package com.mraof.minestuck.skaianet;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.command.DebugLandsCommand;
import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.world.DynamicDimensions;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.List;

public final class CommandActionHandler
{
	private static final ResourceLocation DEBUG_LAND_BASE_ID = new ResourceLocation(Minestuck.MOD_ID, "debug_land");
	
	public static void createDebugLandsChain(ServerPlayer player, List<LandTypePair> landTypes) throws CommandSyntaxException
	{
		var connections = SkaianetConnectionInteractions.get(player.server);
		PlayerIdentifier playerId = IdentifierHandler.encode(player);
		
		if(!SburbPlayerData.get(playerId, player.server).hasEntered())
			throw DebugLandsCommand.MUST_ENTER_EXCEPTION.create();
		
		connections.unlinkClientPlayer(playerId);
		connections.unlinkServerPlayer(playerId);
		
		PlayerIdentifier lastPlayer = playerId;
		int i = 0;
		for(; i < landTypes.size(); i++)
		{
			LandTypePair land = landTypes.get(i);
			if(land == null)
				break;
			
			PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
			connections.newServerForClient(lastPlayer, fakePlayer);
			
			makeConnectionWithLand(land, fakePlayer, IdentifierHandler.NULL_IDENTIFIER, player.server);
			lastPlayer = fakePlayer;
		}
		
		if(i == landTypes.size())
			connections.newServerForClient(lastPlayer, playerId);
		else
		{
			PlayerIdentifier lastIdentifier = playerId;
			for(i = landTypes.size() - 1; i >= 0; i--)
			{
				LandTypePair land = landTypes.get(i);
				if(land == null)
					break;
				
				PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
				makeConnectionWithLand(land, fakePlayer, lastIdentifier, player.server);
				
				lastIdentifier = fakePlayer;
			}
		}
		
		MSDimensions.sendLandTypesToAll(player.server);
	}
	
	private static void makeConnectionWithLand(LandTypePair landTypes, PlayerIdentifier client, PlayerIdentifier server, MinecraftServer mcServer)
	{
		SkaianetConnectionInteractions.get(mcServer).trySetPrimaryConnection(client, server);
		
		SburbPlayerData playerData = SburbPlayerData.get(client, mcServer);
		ResourceKey<Level> dimensionName = DynamicDimensions.createLand(mcServer, DEBUG_LAND_BASE_ID, landTypes);
		playerData.setLand(dimensionName);
		playerData.setHasEntered();
		
		EntryProcess.placeGates(mcServer.getLevel(dimensionName));
	}
}
