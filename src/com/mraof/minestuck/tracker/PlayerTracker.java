package com.mraof.minestuck.tracker;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.ModConfigPacket;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.UpdateChecker;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerTracker
{
	public static final String LAND_ENTRY = "minestuck.land_entry";
	
	public static PlayerTracker instance = new PlayerTracker();
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		Debug.debug(player.getGameProfile().getName()+" joined the game. Sending packets.");
		MinecraftServer server = player.getServer();
		if(!server.isDedicatedServer() && IdentifierHandler.host == null)
			IdentifierHandler.host = event.getPlayer().getName().getUnformattedComponentText();
		
		IdentifierHandler.playerLoggedIn(player);
		
		sendConfigPacket(player, true);
		sendConfigPacket(player, false);
		
		SkaianetHandler.get(player.server).playerConnected(player);
		
		PlayerSavedData.getData(player).onPlayerLoggedIn(player);
		
		ServerEditHandler.onPlayerLoggedIn(player);
		
		if(UpdateChecker.outOfDate)
			player.sendMessage(new StringTextComponent("New version of Minestuck: " + UpdateChecker.latestVersion + "\nChanges: " + UpdateChecker.updateChanges));
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)	//Editmode players need to be reset before nei handles the event
	public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
	{
		ServerEditHandler.onPlayerExit(event.getPlayer());
		dataCheckerPermission.remove(event.getPlayer().getGameProfile().getId());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onPlayerDrops(LivingDropsEvent event)
	{
		if(!event.getEntity().world.isRemote && event.getEntity() instanceof ServerPlayerEntity)
		{
			CaptchaDeckHandler.dropSylladex((ServerPlayerEntity) event.getEntity());
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) event.player;
			if(shouldUpdateConfigurations(player))
				sendConfigPacket(player, false);
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) 
	{
		PlayerSavedData.getData((ServerPlayerEntity) event.getPlayer()).getEcheladder().updateEcheladderBonuses((ServerPlayerEntity) event.getPlayer());
	}
	
	public static Set<UUID> dataCheckerPermission = new HashSet<>();
	
	private static boolean shouldUpdateConfigurations(ServerPlayerEntity player)
	{
		//TODO check for changed configs and change setRequiresWorldRestart status for those config options
		boolean permission = MinestuckConfig.getDataCheckerPermissionFor(player);
		if(permission != dataCheckerPermission.contains(player.getGameProfile().getId()))
			return true;
		
		return false;
	}
	
	public static void sendConfigPacket(ServerPlayerEntity player, boolean mode)
	{
		ModConfigPacket packet;
		if(mode)
			packet = new ModConfigPacket();
		else
		{
			boolean permission = MinestuckConfig.getDataCheckerPermissionFor(player);
			packet = new ModConfigPacket(permission);
			if(permission)
				dataCheckerPermission.add(player.getGameProfile().getId());
			else dataCheckerPermission.remove(player.getGameProfile().getId());
		}
		MSPacketHandler.sendToPlayer(packet, player);
	}
	
	public static void sendLandEntryMessage(PlayerEntity player)
	{
		if(MSDimensions.isLandDimension(player.dimension))
		{
			LandInfo info = MSDimensions.getLandInfo(player.getServer(), player.dimension);
			ITextComponent toSend;
			toSend = new TranslationTextComponent(LAND_ENTRY, info.landAsTextComponent());
			player.sendMessage(toSend);
		}
	}
}