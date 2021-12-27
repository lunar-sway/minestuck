package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.data.DataCheckerPermissionPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.OpEntry;
import net.minecraft.world.GameType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DataCheckerPermission
{
	private static Set<UUID> dataCheckerPermission = new HashSet<>();
	
	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		sendPacket((ServerPlayerEntity) event.getPlayer());
	}
	
	@SubscribeEvent
	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
	{
		dataCheckerPermission.remove(event.getPlayer().getGameProfile().getId());
	}
	
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) event.player;
			if(shouldUpdateConfigurations(player))
				sendPacket(player);
		}
	}
	
	@SubscribeEvent
	public static void serverStopped(FMLServerStoppedEvent event)
	{
		dataCheckerPermission.clear();
	}
	
	private static boolean shouldUpdateConfigurations(ServerPlayerEntity player)
	{
		boolean permission = hasPermission(player);
		return permission != dataCheckerPermission.contains(player.getGameProfile().getId());
	}
	
	private static void sendPacket(ServerPlayerEntity player)
	{
		DataCheckerPermissionPacket packet;
		boolean permission = hasPermission(player);
		if(permission)
			dataCheckerPermission.add(player.getGameProfile().getId());
		else dataCheckerPermission.remove(player.getGameProfile().getId());
		packet = new DataCheckerPermissionPacket(permission);
		MSPacketHandler.sendToPlayer(packet, player);
	}
	
	public static boolean hasPermission(ServerPlayerEntity player)
	{
		switch(MinestuckConfig.SERVER.dataCheckerPermission.get())
		{
			case ANYONE: return true;
			case OPS: return hasOp(player);
			case GAMEMODE: return hasGamemodePermission(player);
			case OPS_OR_GAMEMODE: return hasOp(player) || hasGamemodePermission(player);
			case NONE: default: return false;
		}
	}
	
	private static boolean hasGamemodePermission(ServerPlayerEntity player)
	{
		GameType gameType = player.gameMode.getGameModeForPlayer();
		
		EditData data = ServerEditHandler.getData(player);
		if(data != null)
			gameType = data.getDecoy().gameType;
		
		return !gameType.isSurvival();
	}
	
	private static boolean hasOp(ServerPlayerEntity player)
	{
		MinecraftServer server = player.getServer();
		if(server != null && server.getPlayerList().isOp(player.getGameProfile()))
		{
			OpEntry entry = server.getPlayerList().getOps().get(player.getGameProfile());
			return (entry != null ? entry.getLevel() : server.getOperatorUserPermissionLevel()) >= 2;
		}
		return false;
	}
}