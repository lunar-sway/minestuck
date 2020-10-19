package com.mraof.minestuck.skaianet;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mraof.minestuck.command.DebugLandsCommand;
import com.mraof.minestuck.command.SburbConnectionCommand;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.world.MSDimensionTypes;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;

import java.util.List;
import java.util.Optional;

public final class CommandActionHandler
{
	
	public static int connectByCommand(CommandSource source, PlayerIdentifier client, PlayerIdentifier server) throws CommandSyntaxException
	{
		SkaianetHandler skaianet = SkaianetHandler.get(source.getServer());
		try
		{
			Session target = skaianet.sessionHandler.tryGetSessionToAdd(client, server);
			if(target.locked)
			{
				throw SburbConnectionCommand.LOCKED_EXCEPTION.create();
			}
			
			if(forceConnection(skaianet, target, client, server))
			{
				source.sendFeedback(new TranslationTextComponent(SburbConnectionCommand.SUCCESS, client.getUsername(), server.getUsername()), true);
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
	
	private static boolean forceConnection(SkaianetHandler skaianet, Session session, PlayerIdentifier client, PlayerIdentifier server)
	{
		Optional<SburbConnection> cc = skaianet.getMainConnection(client, true), cs = skaianet.getMainConnection(server, false);
		
		if(cc.isPresent() && cc.get() == cs.orElse(null) || session.locked)
			return false;
		
		boolean updateLandChain = false;
		if(cs.isPresent())
		{
			SburbConnection serverConnection = cs.get();
			if(serverConnection.isActive())
				skaianet.closeConnection(serverConnection);
			skaianet.infoTracker.markDirty(serverConnection);
			serverConnection.removeServerPlayer();
			updateLandChain = serverConnection.hasEntered();
		}
		
		if(cc.isPresent() && cc.get().isActive())
			skaianet.closeConnection(cc.get());
		
		SburbConnection connection = skaianet.getConnection(client, server);
		if(!cc.isPresent())
		{
			if(connection != null)
			{
				connection.setIsMain();
				skaianet.infoTracker.markDirty(connection);
			} else
			{
				SburbConnection newConnection = new SburbConnection(client, server, skaianet);
				session.connections.add(newConnection);
				SburbHandler.onConnectionCreated(newConnection);
				newConnection.setIsMain();
				skaianet.infoTracker.markDirty(newConnection);
			}
		} else
		{
			SburbConnection clientConnection = cc.get();
			skaianet.infoTracker.markDirty(clientConnection);
			clientConnection.removeServerPlayer();
			clientConnection.setNewServerPlayer(server);
			if(connection != null && connection.isActive())
			{
				session.connections.remove(connection);
				clientConnection.copyComputerReferences(connection);
			}
			skaianet.infoTracker.markDirty(clientConnection);
			updateLandChain |= clientConnection.hasEntered();
		}
		
		skaianet.sessionHandler.onConnectionChainBroken(session);
		
		skaianet.checkAndUpdate();
		if(updateLandChain)
			skaianet.infoTracker.reloadLandChains();
		
		return true;
	}
	
	public static void createDebugLandsChain(ServerPlayerEntity player, List<LandTypePair> landTypes, CommandSource source) throws CommandSyntaxException
	{
		SessionHandler sessions = SessionHandler.get(player.server);
		SkaianetHandler skaianet = SkaianetHandler.get(player.server);
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		Session s = sessions.getPlayerSession(identifier);
		if(s != null && s.locked)
			throw SburbConnectionCommand.LOCKED_EXCEPTION.create();
		
		Optional<SburbConnection> cc = skaianet.getMainConnection(identifier, true);
		if(s == null || !cc.isPresent()|| !cc.get().hasEntered())
			throw DebugLandsCommand.MUST_ENTER_EXCEPTION.create();
		SburbConnection clientConnection = cc.get();
		if(clientConnection.isActive())
			skaianet.closeConnection(clientConnection);
		
		Optional<SburbConnection> cs = skaianet.getMainConnection(identifier, false);
		if(cs.isPresent())
		{
			SburbConnection serverConnection = cs.get();
			if(serverConnection.isActive())
				skaianet.closeConnection(clientConnection);
			skaianet.infoTracker.markDirty(serverConnection);
			serverConnection.removeServerPlayer();
			source.sendFeedback(new StringTextComponent(identifier.getUsername()+"'s old client player "+serverConnection.getClientIdentifier().getUsername()+" is now without a server player.").setStyle(new Style().setColor(TextFormatting.YELLOW)), true);
		}
		
		skaianet.infoTracker.markDirty(clientConnection);
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
			
			c = makeConnectionWithLand(skaianet, land, createDebugLand(land), fakePlayer, IdentifierHandler.NULL_IDENTIFIER, s);
		}
		
		if(i == landTypes.size())
			c.setNewServerPlayer(identifier);
		else
		{
			PlayerIdentifier lastIdentifier = identifier;
			for(i = landTypes.size() - 1; i >= 0; i++)
			{
				LandTypePair land = landTypes.get(i);
				if(land == null)
					break;
				PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
				
				c = makeConnectionWithLand(skaianet, land, createDebugLand(land), fakePlayer, lastIdentifier, s);
				
				lastIdentifier = fakePlayer;
			}
		}
		
		skaianet.checkAndUpdate();
		skaianet.infoTracker.reloadLandChains();
	}
	
	private static SburbConnection makeConnectionWithLand(SkaianetHandler skaianet, LandTypePair landTypes, DimensionType dimensionName, PlayerIdentifier client, PlayerIdentifier server, Session session)
	{
		SburbConnection c = new SburbConnection(client, server, skaianet);
		c.setIsMain();
		c.setLand(landTypes, dimensionName);
		c.setHasEntered();
		
		session.connections.add(c);
		SburbHandler.onConnectionCreated(c);
		
		return c;
	}
	
	
	private static DimensionType createDebugLand(LandTypePair landTypes) throws CommandSyntaxException
	{
		String base = "minestuck:debug_land";
		
		ResourceLocation landName = new ResourceLocation(base);
		
		for(int i = 0; DimensionType.byName(landName) != null; i++)
		{
			landName = new ResourceLocation(base+"_"+i);
		}
		
		return DimensionManager.registerDimension(landName, MSDimensionTypes.LANDS, null, true);
	}
}
