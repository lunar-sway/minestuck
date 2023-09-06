package com.mraof.minestuck.skaianet;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.command.DebugLandsCommand;
import com.mraof.minestuck.command.SburbConnectionCommand;
import com.mraof.minestuck.entry.EntryProcess;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.world.DynamicDimensions;
import com.mraof.minestuck.world.MSDimensions;
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
		try
		{
			if(forceConnection(skaianet, client, server))
			{
				source.sendSuccess(() -> Component.translatable(SburbConnectionCommand.SUCCESS, client.getUsername(), server.getUsername()), true);
				return 1;
			} else
			{
				throw SburbConnectionCommand.CONNECTED_EXCEPTION.create();
			}
		} catch(MergeResult.SessionMergeException e)
		{
			throw SburbConnectionCommand.MERGE_EXCEPTION.create(e.getResult());
		}
	}
	
	private static boolean forceConnection(SkaianetHandler skaianet, PlayerIdentifier client, PlayerIdentifier server) throws MergeResult.SessionMergeException
	{
		Optional<SburbConnection> cc = skaianet.getPrimaryConnection(client, true), cs = skaianet.getPrimaryConnection(server, false);
		
		if(cc.isPresent() && cc.equals(cs))
		{
			if(!cc.get().isMain())
			{
				cc.get().setIsMain();
				return true;
			}
			return false;
		}
		
		boolean updateLandChain = false;
		if(cs.isPresent())
		{
			SburbConnection serverConnection = cs.get();
			if(serverConnection.isActive())
				skaianet.closeConnection(serverConnection);
			serverConnection.removeServerPlayer();
			updateLandChain = serverConnection.hasEntered();
		}
		
		if(cc.isPresent() && cc.get().isActive())
			skaianet.closeConnection(cc.get());
		
		SburbConnection connection = skaianet.getConnection(client, server);
		if(!cc.isPresent() || !cc.get().isMain())
		{
			if(connection != null)
			{
				connection.setIsMain();
			} else
			{
				Session session = skaianet.sessionHandler.prepareSessionFor(client, server);
				SburbConnection newConnection = new SburbConnection(client, server, skaianet);
				session.connections.add(newConnection);
				SburbHandler.onConnectionCreated(newConnection);
				newConnection.setIsMain();
			}
		} else
		{
			SburbConnection clientConnection = cc.get();
			clientConnection.removeServerPlayer();
			clientConnection.setNewServerPlayer(server);
			if(connection != null && connection.isActive())
			{
				skaianet.sessionHandler.getPlayerSession(client).connections.remove(connection);
				clientConnection.copyComputerReferences(connection);
			}
			updateLandChain |= clientConnection.hasEntered();
		}
		
		if(updateLandChain)
			skaianet.infoTracker.reloadLandChains();
		
		return true;
	}
	
	public static void createDebugLandsChain(ServerPlayer player, List<LandTypePair> landTypes, CommandSourceStack source) throws CommandSyntaxException
	{
		SkaianetHandler skaianet = SkaianetHandler.get(player.server);
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		
		Optional<SburbConnection> cc = skaianet.getPrimaryConnection(identifier, true);
		if(!cc.isPresent()|| !cc.get().hasEntered())
			throw DebugLandsCommand.MUST_ENTER_EXCEPTION.create();
		SburbConnection clientConnection = cc.get();
		
		if(clientConnection.getSession().locked)
			throw SburbConnectionCommand.LOCKED_EXCEPTION.create();
		
		if(clientConnection.isActive())
			skaianet.closeConnection(clientConnection);
		
		Optional<SburbConnection> cs = skaianet.getPrimaryConnection(identifier, false);
		if(cs.isPresent())
		{
			SburbConnection serverConnection = cs.get();
			if(serverConnection.isActive())
				skaianet.closeConnection(clientConnection);
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
		
		// Several new lands may have been created through calls to createDebugLand(). Send potentially new land types to players
		MSDimensions.sendLandTypesToAll(source.getServer());
		skaianet.infoTracker.reloadLandChains();
	}
	
	private static SburbConnection makeConnectionWithLand(SkaianetHandler skaianet, ResourceKey<Level> dimensionName, PlayerIdentifier client, PlayerIdentifier server) throws MergeResult.SessionMergeException
	{
		SburbConnection c = new SburbConnection(client, server, skaianet);
		c.setIsMain();
		c.setLand(dimensionName);
		c.setHasEntered();
		
		Session session = skaianet.sessionHandler.getSessionForConnecting(client, server);
		session.connections.add(c);
		SburbHandler.onConnectionCreated(c);
		
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
