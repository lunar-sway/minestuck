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
		SburbConnection cc = skaianet.getMainConnection(client, true), cs = skaianet.getMainConnection(server, false);
		
		if(cc != null && cc == cs || session.locked)
			return false;
		
		boolean updateLandChain = false;
		if(cs != null)
		{
			if(cs.isActive())
				skaianet.closeConnection(server, cs.getClientIdentifier(), false);
			cs.removeServerPlayer();
			updateLandChain = cs.hasEntered();
		}
		
		if(cc != null && cc.isActive())
			skaianet.closeConnection(client, cc.getServerIdentifier(), true);
		
		SburbConnection connection = skaianet.getConnection(client, server);
		if(cc == null)
		{
			if(connection != null)
				cc = connection;
			else
			{
				cc = new SburbConnection(client, server, skaianet);
				skaianet.connections.add(cc);
				session.connections.add(cc);
				SburbHandler.onConnectionCreated(cc);
			}
			cc.setIsMain();
		} else
		{
			cc.removeServerPlayer();
			cc.setNewServerPlayer(server);
			if(connection != null && connection.isActive())
			{
				skaianet.connections.remove(connection);
				session.connections.remove(connection);
				cc.setActive(connection.getClientComputer(), connection.getServerComputer());
			}
			updateLandChain |= cc.hasEntered();
		}
		
		skaianet.sessionHandler.onConnectionChainBroken(session);
		
		skaianet.updateAll();
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
		
		SburbConnection cc = skaianet.getMainConnection(identifier, true);
		if(s == null || cc == null || !cc.hasEntered())
			throw DebugLandsCommand.MUST_ENTER_EXCEPTION.create();
		if(cc.isActive())
			skaianet.closeConnection(identifier, cc.getServerIdentifier(), true);
		
		SburbConnection cs = skaianet.getMainConnection(identifier, false);
		if(cs != null) {
			if(cs.isActive())
				skaianet.closeConnection(identifier, cs.getClientIdentifier(), false);
			cs.removeServerPlayer();
			source.sendFeedback(new StringTextComponent(identifier.getUsername()+"'s old client player "+cs.getClientIdentifier().getUsername()+" is now without a server player.").setStyle(new Style().setColor(TextFormatting.YELLOW)), true);
		}
		
		cc.removeServerPlayer();
		SburbConnection c = cc;
		int i = 0;
		for(; i < landTypes.size(); i++)
		{
			LandTypePair land = landTypes.get(i);
			if(land == null)
				break;
			PlayerIdentifier fakePlayer = IdentifierHandler.createNewFakeIdentifier();
			c.setNewServerPlayer(fakePlayer);
			
			c = skaianet.makeConnectionWithLand(land, createDebugLand(land), fakePlayer, IdentifierHandler.NULL_IDENTIFIER, s);
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
				
				c = skaianet.makeConnectionWithLand(land, createDebugLand(land), fakePlayer, lastIdentifier, s);
				
				lastIdentifier = fakePlayer;
			}
		}
		
		skaianet.updateAll();
		skaianet.infoTracker.reloadLandChains();
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
