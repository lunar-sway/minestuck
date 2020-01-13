package com.mraof.minestuck.tracker;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.DataCheckerPermissionPacket;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerTracker
{
	public static final String LAND_ENTRY = "minestuck.land_entry";
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		Debug.debug(player.getGameProfile().getName()+" joined the game. Sending packets.");
		
		sendConfigPacket(player);
		
		SkaianetHandler.get(player.server).playerConnected(player);
		
		PlayerSavedData.getData(player).onPlayerLoggedIn(player);
		
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)	//Editmode players need to be reset before nei handles the event
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
	{
		ServerEditHandler.onPlayerExit(event.getPlayer());
		dataCheckerPermission.remove(event.getPlayer().getGameProfile().getId());
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public static void onPlayerDrops(LivingDropsEvent event)
	{
		if(!event.getEntity().world.isRemote && event.getEntity() instanceof ServerPlayerEntity)
		{
			CaptchaDeckHandler.dropSylladex((ServerPlayerEntity) event.getEntity());
		}
	}
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) event.player;
			if(shouldUpdateConfigurations(player))
				sendConfigPacket(player);
		}
	}
	
	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
	{
		PlayerSavedData.getData((ServerPlayerEntity) event.getPlayer()).getEcheladder().updateEcheladderBonuses((ServerPlayerEntity) event.getPlayer());
	}
	
	@SubscribeEvent
	public static void serverStopped(FMLServerStoppedEvent event)
	{
		dataCheckerPermission.clear();
	}
	
	public static Set<UUID> dataCheckerPermission = new HashSet<>();
	
	private static boolean shouldUpdateConfigurations(ServerPlayerEntity player)
	{
		boolean permission = MinestuckConfig.getDataCheckerPermissionFor(player);
		return permission != dataCheckerPermission.contains(player.getGameProfile().getId());
	}
	
	public static void sendConfigPacket(ServerPlayerEntity player)
	{
		DataCheckerPermissionPacket packet;
		boolean permission = MinestuckConfig.getDataCheckerPermissionFor(player);
		if(permission)
			dataCheckerPermission.add(player.getGameProfile().getId());
		else dataCheckerPermission.remove(player.getGameProfile().getId());
		packet = new DataCheckerPermissionPacket(permission);
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