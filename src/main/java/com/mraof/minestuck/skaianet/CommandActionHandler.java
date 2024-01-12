package com.mraof.minestuck.skaianet;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.command.DebugLandsCommand;
import com.mraof.minestuck.command.SburbConnectionCommand;
import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.world.DynamicDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

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
		Optional<SburbConnection> cc = skaianet.getPrimaryOrCandidateConnection(client, true), cs = skaianet.getPrimaryOrCandidateConnection(server, false);
		
		if(cc.isPresent() && cc.equals(cs))
		{
			if(!cc.get().isMain())
			{
				skaianet.trySetPrimaryConnection(cc.get());
				return true;
			}
			return false;
		}
		
		if(cs.isPresent())
		{
			SburbConnection serverConnection = cs.get();
			serverConnection.closeIfActive();
			serverConnection.removeServerPlayer();
		}
		
		cc.ifPresent(SburbConnection::closeIfActive);
		
		SburbConnection connection = skaianet.getConnection(client, server);
		if(cc.isEmpty() || !cc.get().isMain())
		{
			if(connection != null)
				skaianet.trySetPrimaryConnection(connection);
			else
			{
				Session session = skaianet.sessionHandler.prepareSessionFor(client, server);
				SburbConnection newConnection = new SburbConnection(client, server, skaianet);
				session.connections.add(newConnection);
				skaianet.trySetPrimaryConnection(newConnection);
			}
		} else
		{
			SburbConnection clientConnection = cc.get();
			clientConnection.removeServerPlayer();
			clientConnection.setNewServerPlayer(server);
			if(connection != null)
				skaianet.sessionHandler.getPlayerSession(client).connections.remove(connection);
		}
		
		return true;
	}
	
	public static void createDebugLandsChain(ServerPlayer player, List<LandTypePair> landTypes, CommandSourceStack source) throws CommandSyntaxException
	{
		SkaianetHandler skaianet = SkaianetHandler.get(player.server);
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		
		Optional<SburbConnection> cc = skaianet.getPrimaryOrCandidateConnection(identifier, true);
		if(cc.isEmpty() || !skaianet.getOrCreateData(identifier).hasEntered())
			throw DebugLandsCommand.MUST_ENTER_EXCEPTION.create();
		SburbConnection clientConnection = cc.get();
		
		clientConnection.closeIfActive();
		
		Optional<SburbConnection> cs = skaianet.getPrimaryOrCandidateConnection(identifier, false);
		if(cs.isPresent())
		{
			SburbConnection serverConnection = cs.get();
			serverConnection.closeIfActive();
			serverConnection.removeServerPlayer();
			source.sendSuccess(() -> Component.literal(identifier.getUsername()+"'s old client player "+serverConnection.getClientIdentifier().getUsername()+" is now without a server player.").withStyle(ChatFormatting.YELLOW), true);
		}
		
		try
		{
			clientConnection.removeServerPlayer();
			SburbConnection c = clientConnection;
			int i = 0;
			for(; i < landTypes.size(); i++)
			{
				LandTypePair land = landTypes.get(i);
				if(land == null)
					break;
				PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
				c.setNewServerPlayer(fakePlayer);
				
				c = makeConnectionWithLand(skaianet, createDebugLand(player.server, land), fakePlayer, IdentifierHandler.NULL_IDENTIFIER);
			}
			
			if(i == landTypes.size())
				c.setNewServerPlayer(identifier);
			else
			{
				PlayerIdentifier lastIdentifier = identifier;
				for(i = landTypes.size() - 1; i >= 0; i--)
				{
					LandTypePair land = landTypes.get(i);
					if(land == null)
						break;
					PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
					
					c = makeConnectionWithLand(skaianet, createDebugLand(player.server, land), fakePlayer, lastIdentifier);
					
					lastIdentifier = fakePlayer;
				}
			}
		} catch(MergeResult.SessionMergeException e)
		{
			//TODO give proper feedback to user. The operation will most likely have partially executed
		}
	}
	
	private static SburbConnection makeConnectionWithLand(SkaianetHandler skaianet, ResourceKey<Level> dimensionName, PlayerIdentifier client, PlayerIdentifier server) throws MergeResult.SessionMergeException
	{
		SburbConnection c = new SburbConnection(client, server, skaianet);
		skaianet.trySetPrimaryConnection(c);
		SburbPlayerData data = skaianet.getOrCreateData(client);
		data.setLand(dimensionName);
		data.setHasEntered();
		
		Session session = skaianet.sessionHandler.getSessionForConnecting(client, server);
		session.connections.add(c);
		
		//The land types used by generation is set during connection init above, so placing gates currently has to go after that
		ServerLevel level = skaianet.mcServer.getLevel(dimensionName);
		EntryProcess.placeGates(level);
		
		return c;
	}
	
	
	private static ResourceKey<Level> createDebugLand(MinecraftServer server, LandTypePair landTypes)
	{
		return DynamicDimensions.createLand(server, new ResourceLocation("minestuck:debug_land"), landTypes);
	}
}
