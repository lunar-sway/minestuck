package com.mraof.minestuck.tracker;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.PlayerDataPacket;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashSet;
import java.util.Set;

public class MinestuckPlayerTracker {
	
	public static MinestuckPlayerTracker instance = new MinestuckPlayerTracker();
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) 
	{
		EntityPlayer player = event.player;
		Debug.debug(player.getName()+" joined the game. Sending packets.");
		MinecraftServer server = player.getServer();
		if(!server.isDedicatedServer() && IdentifierHandler.host == null)
			IdentifierHandler.host = event.player.getName();
		
		IdentifierHandler.playerLoggedIn(player);
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		
		sendConfigPacket((EntityPlayerMP) player, true);
		sendConfigPacket((EntityPlayerMP) player, false);
		
		SkaianetHandler.playerConnected(player);
		boolean firstTime = false;
		if(MinestuckPlayerData.getGristSet(identifier) == null)
		{
			Debug.debugf("Grist set is null for player %s. Handling it as first time in this world.", player.getName());
			MinestuckPlayerData.setGrist(identifier, new GristSet(GristType.Build, 20));
			firstTime = true;
		}
		
		MinestuckPlayerData.getData(identifier).echeladder.updateEcheladderBonuses(player);
		
		if(CaptchaDeckHandler.getModus(player) == null && MinestuckConfig.defaultModusTypes.length > 0 && !MinestuckPlayerData.getData(player).givenModus)
		{
			int index = player.world.rand.nextInt(MinestuckConfig.defaultModusTypes.length);
			Modus modus = CaptchaDeckHandler.createInstance(new ResourceLocation(MinestuckConfig.defaultModusTypes[index]), Side.SERVER);
			if(modus != null)
			{
				modus.player = player;
				modus.initModus(null, MinestuckConfig.initialModusSize);
				CaptchaDeckHandler.setModus(player, modus);
			} else Debug.warnf("Couldn't create a modus by the name %s.", MinestuckConfig.defaultModusTypes[index]);
		}
		
		if(CaptchaDeckHandler.getModus(player) != null)
		{
			Modus modus = CaptchaDeckHandler.getModus(player);
			modus.player = player;
			MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(Type.CAPTCHA, CaptchaDeckPacket.DATA, CaptchaDeckHandler.writeToNBT(modus)), player);
		}
		
		updateGristCache(identifier);
		updateTitle(player);
		updateEcheladder(player, true);
		MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(Type.PLAYER_DATA, PlayerDataPacket.BOONDOLLAR, MinestuckPlayerData.getData(identifier).boondollars), player);
		ServerEditHandler.onPlayerLoggedIn((EntityPlayerMP) player);
		
		if(firstTime)
			MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(Type.PLAYER_DATA, PlayerDataPacket.COLOR), player);
		else
		{
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.PLAYER_DATA, PlayerDataPacket.COLOR, MinestuckPlayerData.getData(player).color);
			MinestuckChannelHandler.sendToPlayer(packet, player);
		}
		
		if(UpdateChecker.outOfDate)
			player.sendMessage(new TextComponentString("New version of Minestuck: " + UpdateChecker.latestVersion + "\nChanges: " + UpdateChecker.updateChanges));
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)	//Editmode players need to be reset before nei handles the event
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
	{
		ServerEditHandler.onPlayerExit(event.player);
		Modus modus = CaptchaDeckHandler.getModus(event.player);
		if(modus != null)
			modus.player = null;
		dataCheckerPermission.remove(event.player.getName());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onPlayerDrops(PlayerDropsEvent event)
	{
		if(!event.getEntityPlayer().world.isRemote)
		{
			CaptchaDeckHandler.dropSylladex(event.getEntityPlayer());
			
		}
		
	}
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.side.isServer() && event.phase == TickEvent.Phase.END && event.player instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.player;
			if(shouldUpdateConfigurations(player))
				sendConfigPacket(player, false);
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) 
	{
		MinestuckPlayerData.getData(event.player).echeladder.updateEcheladderBonuses(event.player);
	}
	
	public static Set<String> dataCheckerPermission = new HashSet<String>();
	
	private static boolean shouldUpdateConfigurations(EntityPlayerMP player)
	{
		//TODO check for changed configs and change setRequiresWorldRestart status for those config options
		boolean permission = MinestuckConfig.getDataCheckerPermissionFor(player);
		if(permission != dataCheckerPermission.contains(player.getName()))
			return true;
		
		return false;
	}
	
	/**
	 * Uses an "encoded" username as parameter.
	 */
	public static void updateGristCache(PlayerIdentifier player)
	{
		GristSet gristSet = MinestuckPlayerData.getGristSet(player);
		
		//The player
		EntityPlayerMP playerMP = player.getPlayer();
		if(playerMP != null)
		{
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.GRISTCACHE, gristSet, false);
			MinestuckChannelHandler.sendToPlayer(packet, playerMP);
		}
		
		//The editing player, if there is any.
		SburbConnection c = SkaianetHandler.getClientConnection(player);
		if(c != null && ServerEditHandler.getData(c) != null)
		{
			EntityPlayerMP editor = ServerEditHandler.getData(c).getEditor();
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.GRISTCACHE, gristSet, true);
			MinestuckChannelHandler.sendToPlayer(packet, editor);
		}
	}
	
	public static void updateTitle(EntityPlayer player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		Title newTitle = MinestuckPlayerData.getTitle(identifier);
		if(newTitle == null)
			return;
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.PLAYER_DATA, PlayerDataPacket.TITLE, newTitle.getHeroClass(), newTitle.getHeroAspect());
		MinestuckChannelHandler.sendToPlayer(packet, player);
	}
	
	public static void updateEcheladder(EntityPlayer player, boolean jump)
	{
		Echeladder echeladder = MinestuckPlayerData.getData(player).echeladder;
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.PLAYER_DATA, PlayerDataPacket.ECHELADDER, echeladder.getRung(), MinestuckConfig.echeladderProgress ? echeladder.getProgress() : 0F, jump);
		MinestuckChannelHandler.sendToPlayer(packet, player);
	}
	
	public static void updateLands(EntityPlayer player)
	{
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.LANDREGISTER);
		Debug.debugf("Sending land packets to %s.", player == null ? "all players" : player.getName());
		if(player == null)
			MinestuckChannelHandler.sendToAllPlayers(packet);
		else
			MinestuckChannelHandler.sendToPlayer(packet, player);
	}
	public static void updateLands()
	{
		updateLands(null);
	}

	public static void sendConfigPacket(EntityPlayerMP player, boolean mode)
	{
		MinestuckPacket packet;
		if(mode)
			packet = MinestuckPacket.makePacket(Type.CONFIG, true);
		else
		{
			boolean permission = MinestuckConfig.getDataCheckerPermissionFor(player);
			packet = MinestuckPacket.makePacket(Type.CONFIG, false, permission);
			if(permission)
				dataCheckerPermission.add(player.getName());
			else dataCheckerPermission.remove(player.getName());
		}
		MinestuckChannelHandler.sendToPlayer(packet, player);
	}
	
	public static void sendLandEntryMessage(EntityPlayer player)
	{
		if(MinestuckDimensionHandler.isLandDimension(player.dimension))
		{
			LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(player.dimension);
			ChunkProviderLands chunkProvider = (ChunkProviderLands) player.world.provider.createChunkGenerator();
			ITextComponent aspect1 = new TextComponentTranslation("land."+aspects.aspectTerrain.getNames()[chunkProvider.nameIndex1]);
			ITextComponent aspect2 = new TextComponentTranslation("land."+aspects.aspectTitle.getNames()[chunkProvider.nameIndex2]);
			ITextComponent toSend;
			if(chunkProvider.nameOrder)
				toSend = new TextComponentTranslation("land.message.entry", aspect1, aspect2);
			else toSend = new TextComponentTranslation("land.message.entry", aspect2, aspect1);
			player.sendMessage(toSend);
		}
	}
	
}
