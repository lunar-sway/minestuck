package com.mraof.minestuck.skaianet;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.command.DebugLandsCommand;
import com.mraof.minestuck.command.SburbConnectionCommand;
import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.world.DynamicDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.List;

public final class CommandActionHandler
{
	
	public static int connectByCommand(CommandSourceStack source, PlayerIdentifier client, PlayerIdentifier server) throws CommandSyntaxException
	{
		SkaianetHandler skaianet = SkaianetHandler.get(source.getServer());
		if(forceConnection(skaianet, client, server))
		{
			source.sendSuccess(() -> Component.translatable(SburbConnectionCommand.SUCCESS, client.getUsername(), server.getUsername()), true);
			return 1;
		} else
		{
			throw SburbConnectionCommand.CONNECTED_EXCEPTION.create();
		}
	}
	
	private static boolean forceConnection(SkaianetHandler skaianet, PlayerIdentifier client, PlayerIdentifier server)
	{
		SburbPlayerData clientData = skaianet.getOrCreateData(client);
		if(clientData.isPrimaryServerPlayer(server))
			return false;
		
		if(!clientData.hasPrimaryConnection() && skaianet.getActiveConnection(client).filter(connection -> connection.server().equals(server)).isPresent())
		{
			SkaianetConnectionInteractions.trySetPrimaryConnection(client, server, skaianet);
			return true;
		}
		
		SkaianetConnectionInteractions.unlinkClientPlayer(client, skaianet);
		SkaianetConnectionInteractions.unlinkServerPlayer(server, skaianet);
		
		if(clientData.hasPrimaryConnection())
			SkaianetConnectionInteractions.newServerForClient(client, server, skaianet);
		else
			SkaianetConnectionInteractions.trySetPrimaryConnection(client, server, skaianet);
		
		return true;
	}
	
	public static void createDebugLandsChain(ServerPlayer player, List<LandTypePair> landTypes) throws CommandSyntaxException
	{
		SkaianetHandler skaianet = SkaianetHandler.get(player.server);
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		
		if(!skaianet.getOrCreateData(identifier).hasEntered())
			throw DebugLandsCommand.MUST_ENTER_EXCEPTION.create();
		
		SkaianetConnectionInteractions.unlinkClientPlayer(identifier, skaianet);
		SkaianetConnectionInteractions.unlinkServerPlayer(identifier, skaianet);
		
		PlayerIdentifier lastPlayer = identifier;
		int i = 0;
		for(; i < landTypes.size(); i++)
		{
			LandTypePair land = landTypes.get(i);
			if(land == null)
				break;
			
			PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
			SkaianetConnectionInteractions.newServerForClient(lastPlayer, fakePlayer, skaianet);
			
			makeConnectionWithLand(skaianet, createDebugLand(player.server, land), fakePlayer, IdentifierHandler.NULL_IDENTIFIER);
			lastPlayer = fakePlayer;
		}
		
		if(i == landTypes.size())
			SkaianetConnectionInteractions.newServerForClient(lastPlayer, identifier, skaianet);
		else
		{
			PlayerIdentifier lastIdentifier = identifier;
			for(i = landTypes.size() - 1; i >= 0; i--)
			{
				LandTypePair land = landTypes.get(i);
				if(land == null)
					break;
				
				PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
				makeConnectionWithLand(skaianet, createDebugLand(player.server, land), fakePlayer, lastIdentifier);
				
				lastIdentifier = fakePlayer;
			}
		}
	}
	
	private static void makeConnectionWithLand(SkaianetHandler skaianet, ResourceKey<Level> dimensionName, PlayerIdentifier client, PlayerIdentifier server)
	{
		SkaianetConnectionInteractions.trySetPrimaryConnection(client, server, skaianet);
		
		SburbPlayerData data = skaianet.getOrCreateData(client);
		data.setLand(dimensionName);
		data.setHasEntered();
		
		EntryProcess.placeGates(skaianet.mcServer.getLevel(dimensionName));
	}
	
	private static ResourceKey<Level> createDebugLand(MinecraftServer server, LandTypePair landTypes)
	{
		return DynamicDimensions.createLand(server, new ResourceLocation("minestuck:debug_land"), landTypes);
	}
}
