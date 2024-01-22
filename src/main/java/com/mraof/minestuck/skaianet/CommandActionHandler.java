package com.mraof.minestuck.skaianet;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.command.DebugLandsCommand;
import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.world.DynamicDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.List;

public final class CommandActionHandler
{
	public static void createDebugLandsChain(ServerPlayer player, List<LandTypePair> landTypes) throws CommandSyntaxException
	{
		SkaianetData skaianetData = SkaianetData.get(player.server);
		var connectionInteractions = skaianetData.connectionInteractions;
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		
		if(!skaianetData.getOrCreateData(identifier).hasEntered())
			throw DebugLandsCommand.MUST_ENTER_EXCEPTION.create();
		
		connectionInteractions.unlinkClientPlayer(identifier);
		connectionInteractions.unlinkServerPlayer(identifier);
		
		PlayerIdentifier lastPlayer = identifier;
		int i = 0;
		for(; i < landTypes.size(); i++)
		{
			LandTypePair land = landTypes.get(i);
			if(land == null)
				break;
			
			PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
			connectionInteractions.newServerForClient(lastPlayer, fakePlayer);
			
			makeConnectionWithLand(skaianetData, createDebugLand(player.server, land), fakePlayer, IdentifierHandler.NULL_IDENTIFIER);
			lastPlayer = fakePlayer;
		}
		
		if(i == landTypes.size())
			connectionInteractions.newServerForClient(lastPlayer, identifier);
		else
		{
			PlayerIdentifier lastIdentifier = identifier;
			for(i = landTypes.size() - 1; i >= 0; i--)
			{
				LandTypePair land = landTypes.get(i);
				if(land == null)
					break;
				
				PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
				makeConnectionWithLand(skaianetData, createDebugLand(player.server, land), fakePlayer, lastIdentifier);
				
				lastIdentifier = fakePlayer;
			}
		}
	}
	
	private static void makeConnectionWithLand(SkaianetData skaianetData, ResourceKey<Level> dimensionName, PlayerIdentifier client, PlayerIdentifier server)
	{
		skaianetData.connectionInteractions.trySetPrimaryConnection(client, server);
		
		SburbPlayerData playerData = skaianetData.getOrCreateData(client);
		playerData.setLand(dimensionName);
		playerData.setHasEntered();
		
		EntryProcess.placeGates(skaianetData.mcServer.getLevel(dimensionName));
	}
	
	private static ResourceKey<Level> createDebugLand(MinecraftServer server, LandTypePair landTypes)
	{
		return DynamicDimensions.createLand(server, new ResourceLocation("minestuck:debug_land"), landTypes);
	}
}
