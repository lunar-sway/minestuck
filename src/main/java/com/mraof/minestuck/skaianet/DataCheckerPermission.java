package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.network.DataCheckerPackets;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class DataCheckerPermission
{
	private static final Set<UUID> dataCheckerPermission = new HashSet<>();
	
	@SubscribeEvent
	private static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		sendPacket((ServerPlayer) event.getEntity());
	}
	
	@SubscribeEvent
	private static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
	{
		dataCheckerPermission.remove(event.getEntity().getGameProfile().getId());
	}
	
	@SubscribeEvent
	private static void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayer player)
		{
			if(shouldUpdateConfigurations(player))
				sendPacket(player);
		}
	}
	
	@SubscribeEvent
	private static void serverStopped(ServerStoppedEvent event)
	{
		dataCheckerPermission.clear();
	}
	
	private static boolean shouldUpdateConfigurations(ServerPlayer player)
	{
		boolean permission = hasPermission(player);
		return permission != dataCheckerPermission.contains(player.getGameProfile().getId());
	}
	
	private static void sendPacket(ServerPlayer player)
	{
		DataCheckerPackets.Permission packet;
		boolean permission = hasPermission(player);
		if(permission)
			dataCheckerPermission.add(player.getGameProfile().getId());
		else dataCheckerPermission.remove(player.getGameProfile().getId());
		packet = new DataCheckerPackets.Permission(permission);
		PacketDistributor.PLAYER.with(player).send(packet);
	}
	
	public static boolean hasPermission(ServerPlayer player)
	{
		return switch(MinestuckConfig.SERVER.dataCheckerPermission.get())
		{
			case ANYONE -> true;
			case OPS -> hasOp(player);
			case GAMEMODE -> hasGamemodePermission(player);
			case OPS_OR_GAMEMODE -> hasOp(player) || hasGamemodePermission(player);
			case NONE -> false;
		};
	}
	
	private static boolean hasGamemodePermission(ServerPlayer player)
	{
		GameType gameType = player.gameMode.getGameModeForPlayer();
		
		EditData data = ServerEditHandler.getData(player);
		if(data != null)
			gameType = data.getDecoy().gameType;
		
		return !gameType.isSurvival();
	}
	
	private static boolean hasOp(ServerPlayer player)
	{
		MinecraftServer server = player.getServer();
		if(server != null && server.getPlayerList().isOp(player.getGameProfile()))
			return player.hasPermissions(Commands.LEVEL_GAMEMASTERS);
		return false;
	}
}
