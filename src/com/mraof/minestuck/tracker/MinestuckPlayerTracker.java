package com.mraof.minestuck.tracker;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.network.*;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspects;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Set;

public class MinestuckPlayerTracker
{
	
	public static MinestuckPlayerTracker instance = new MinestuckPlayerTracker();
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) 
	{
		EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
		Debug.debug(player.getName()+" joined the game. Sending packets.");
		MinecraftServer server = player.getServer();
		if(!server.isDedicatedServer() && IdentifierHandler.host == null)
			IdentifierHandler.host = event.getPlayer().getName().getUnformattedComponentText();
		
		IdentifierHandler.playerLoggedIn(player);
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		
		sendConfigPacket(player, true);
		sendConfigPacket(player, false);
		
		SkaianetHandler.get(player.world).playerConnected(player);
		boolean firstTime = false;
		if(MinestuckPlayerData.getGristSet(identifier) == null)
		{
			Debug.debugf("Grist set is null for player %s. Handling it as first time in this world.", player.getName());
			MinestuckPlayerData.setGrist(identifier, new GristSet(GristType.BUILD, 20));
			firstTime = true;
		}
		
		MinestuckPlayerData.getData(identifier).echeladder.updateEcheladderBonuses(player);
		
		if(CaptchaDeckHandler.getModus(player) == null && MinestuckConfig.defaultModusTypes.length > 0 && !MinestuckPlayerData.getData(player).givenModus)
		{
			int index = player.world.rand.nextInt(MinestuckConfig.defaultModusTypes.length);
			Modus modus = CaptchaDeckHandler.createInstance(new ResourceLocation(MinestuckConfig.defaultModusTypes[index]), LogicalSide.SERVER);
			if(modus != null)
			{
				modus.initModus(player, null, MinestuckConfig.initialModusSize);
				CaptchaDeckHandler.setModus(player, modus);
			} else Debug.warnf("Couldn't create a modus by the name %s.", MinestuckConfig.defaultModusTypes[index]);
		}
		
		if(CaptchaDeckHandler.getModus(player) != null)
		{
			Modus modus = CaptchaDeckHandler.getModus(player);
			MinestuckPacketHandler.sendToPlayer(CaptchaDeckPacket.data(CaptchaDeckHandler.writeToNBT(modus)), player);
		}
		
		updateGristCache(player.getServer(), identifier);
		updateTitle(player);
		updateEcheladder(player, true);
		MinestuckPacketHandler.sendToPlayer(PlayerDataPacket.boondollars(MinestuckPlayerData.getData(identifier).boondollars), player);
		ServerEditHandler.onPlayerLoggedIn(player);
		
		if(firstTime && !player.isSpectator())
			MinestuckPacketHandler.sendToPlayer(PlayerDataPacket.color(), player);
		else
		{
			PlayerDataPacket packet = PlayerDataPacket.color(MinestuckPlayerData.getData(player).color);
			MinestuckPacketHandler.sendToPlayer(packet, player);
		}
		
		if(UpdateChecker.outOfDate)
			player.sendMessage(new TextComponentString("New version of Minestuck: " + UpdateChecker.latestVersion + "\nChanges: " + UpdateChecker.updateChanges));
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)	//Editmode players need to be reset before nei handles the event
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
	{
		ServerEditHandler.onPlayerExit(event.getPlayer());
		dataCheckerPermission.remove(event.getPlayer().getName().getUnformattedComponentText());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onPlayerDrops(PlayerDropsEvent event)
	{
		if(!event.getEntityPlayer().world.isRemote && event.getEntityPlayer() instanceof EntityPlayerMP)
		{
			CaptchaDeckHandler.dropSylladex((EntityPlayerMP) event.getEntityPlayer());
			
		}
		
	}
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.player instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.player;
			if(shouldUpdateConfigurations(player))
				sendConfigPacket(player, false);
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) 
	{
		MinestuckPlayerData.getData(event.getPlayer()).echeladder.updateEcheladderBonuses(event.getPlayer());
	}
	
	public static Set<String> dataCheckerPermission = new HashSet<>();
	
	private static boolean shouldUpdateConfigurations(EntityPlayerMP player)
	{
		//TODO check for changed configs and change setRequiresWorldRestart status for those config options
		boolean permission = MinestuckConfig.getDataCheckerPermissionFor(player);
		if(permission != dataCheckerPermission.contains(player.getName().getUnformattedComponentText()))
			return true;
		
		return false;
	}
	
	/**
	 * Uses an "encoded" username as parameter.
	 */
	public static void updateGristCache(MinecraftServer server, PlayerIdentifier player)
	{
		GristSet gristSet = MinestuckPlayerData.getGristSet(player);
		
		//The player
		EntityPlayerMP playerMP = player.getPlayer(server);
		if(playerMP != null)
		{
			GristCachePacket packet = new GristCachePacket(gristSet, false);
			MinestuckPacketHandler.sendToPlayer(packet, playerMP);
		}
		
		//The editing player, if there is any.
		SburbConnection c = SkaianetHandler.get(server).getClientConnection(player);
		if(c != null && ServerEditHandler.getData(c) != null)
		{
			EntityPlayerMP editor = ServerEditHandler.getData(c).getEditor();
			GristCachePacket packet = new GristCachePacket(gristSet, true);
			MinestuckPacketHandler.sendToPlayer(packet, editor);
		}
	}
	
	public static void updateTitle(EntityPlayerMP player)
	{
		PlayerIdentifier identifier = IdentifierHandler.encode(player);
		Title newTitle = MinestuckPlayerData.getTitle(identifier);
		if(newTitle == null)
			return;
		PlayerDataPacket packet = PlayerDataPacket.title(newTitle.getHeroClass(), newTitle.getHeroAspect());
		MinestuckPacketHandler.sendToPlayer(packet, player);
	}
	
	public static void updateEcheladder(EntityPlayerMP player, boolean jump)
	{
		Echeladder echeladder = MinestuckPlayerData.getData(player).echeladder;
		PlayerDataPacket packet = PlayerDataPacket.echeladder(echeladder.getRung(), MinestuckConfig.echeladderProgress ? echeladder.getProgress() : 0F, jump);
		MinestuckPacketHandler.sendToPlayer(packet, player);
	}
	
	/*public static void updateLands(EntityPlayer player)
	{
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.LANDREGISTER);
		Debug.debugf("Sending land packets to %s.", player == null ? "all players" : player.getName());
		if(player == null)
			MinestuckPacketHandler.sendToAllPlayers(packet);
		else
			MinestuckPacketHandler.sendToPlayer(packet, player);
	}
	
	public static void updateLands()
	{
		updateLands(null);
	}*/

	public static void sendConfigPacket(EntityPlayerMP player, boolean mode)
	{
		ModConfigPacket packet;
		if(mode)
			packet = new ModConfigPacket();
		else
		{
			boolean permission = MinestuckConfig.getDataCheckerPermissionFor(player);
			packet = new ModConfigPacket(permission);
			if(permission)
				dataCheckerPermission.add(player.getName().getUnformattedComponentText());
			else dataCheckerPermission.remove(player.getName().getUnformattedComponentText());
		}
		MinestuckPacketHandler.sendToPlayer(packet, player);
	}
	
	public static void sendLandEntryMessage(EntityPlayer player)
	{
		if(MinestuckDimensionHandler.isLandDimension(player.dimension))
		{
			LandAspects aspects = MinestuckDimensionHandler.getAspects(player.dimension);
			ChunkProviderLands chunkProvider = (ChunkProviderLands) player.world.getDimension().createChunkGenerator(); //TODO Check out deprecation
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
