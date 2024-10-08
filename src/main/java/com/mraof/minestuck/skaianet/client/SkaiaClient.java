package com.mraof.minestuck.skaianet.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.network.computer.SkaianetInfoPackets;
import com.mraof.minestuck.skaianet.LandChain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class SkaiaClient
{
	private static final Map<Integer, ReducedPlayerState> playerStateMap = new HashMap<>();
	private static final List<ReducedConnection> connections = new ArrayList<>();
	/**
	 * A map used to track chains of lands, to be used by the skybox render
	 */
	private static final Map<ResourceKey<Level>, LandChain> landChainMap = new HashMap<>();
	private static ComputerBlockEntity be = null;
	public static int playerId;	//The id that this player is expected to have.
	private static boolean hasEntered;
	
	@SubscribeEvent
	public static void onLoggedIn(ClientPlayerNetworkEvent.LoggingIn event)
	{
		playerStateMap.clear();
		connections.clear();
		landChainMap.clear();
		playerId = -1;
		hasEntered = false;
	}
	
	/**
	 * Called by a computer on interact. If it doesn't have the sufficient information,
	 * returns false and sends a request about that information.
	 * @param computer The computer. Will save this variable for later if it sends a request.
	 * @return If it currently has the necessary information.
	 */
	public static boolean requestData(ComputerBlockEntity computer)
	{
		boolean b = playerStateMap.get(computer.ownerId) != null;
		if(!b)
		{
			PacketDistributor.SERVER.noArg().send(new SkaianetInfoPackets.Request(computer.ownerId));
			be = computer;
		}
		return b;
	}
	
	//Getters used by the computer
	
	public static boolean hasPrimaryConnectionAsClient(int playerId)
	{
		return playerStateMap.get(playerId).hasPrimaryConnectionAsClient();
	}
	
	public static boolean hasPrimaryConnectionAsServer(int playerId)
	{
		return playerStateMap.get(playerId).hasPrimaryConnectionAsServer();
	}
	
	public static Map<Integer, String> getAvailableServers(Integer playerId)
	{
		return playerStateMap.get(playerId).openServers();
	}
	
	public static boolean hasPlayerEntered()
	{
		return hasEntered;
	}
	
	@Nullable
	public static LandChain getLandChain(ResourceKey<Level> id)
	{
		return landChainMap.get(id);
	}
	
	public static boolean isActive(int playerId, boolean isClient)
	{
		if(isClient)
			return getClientConnection(playerId) != null || playerStateMap.get(playerId).isClientResuming();
		else return playerStateMap.get(playerId).isServerResuming();
	}
	
	/**
	 * If the color selection gui may be opened.
	 */
	public static boolean canSelect(int playerId)
	{
		if(playerId != SkaiaClient.playerId)
			return false;
		if(playerStateMap.get(playerId).hasPrimaryConnectionAsClient())
			return false;
		
		return connections.stream().anyMatch(c -> c.client().id() == playerId);
	}
	
	//Methods called from the actionPerformed method in the gui.
	
	public static ReducedConnection getClientConnection(int client)
	{
		for(ReducedConnection c : connections)
			if(c.client().id() == client)
				return c;
		return null;
	}
	
	public static void handlePacket(SkaianetInfoPackets.Data data)
	{
		if(playerId == -1)
			playerId = data.playerId();	//The first info packet is expected to be regarding the receiving player.
		
		playerStateMap.put(data.playerId(), data.playerState());
		
		connections.removeIf(c -> c.client().id() == data.playerId() || c.server().id() == data.playerId());
		connections.addAll(data.connections());
		
		Screen gui = Minecraft.getInstance().screen;
		if(gui instanceof ComputerScreen computerScreen)
			computerScreen.updateGui();
		else if(be != null && be.ownerId == data.playerId())
		{
			if(!Minecraft.getInstance().player.isShiftKeyDown())
				MSScreenFactories.displayComputerScreen(be);
			be = null;
		}
	}
	
	public static void handlePacket(SkaianetInfoPackets.LandChains packet)
	{
		landChainMap.clear();
		for(LandChain landChain : packet.landChains())
		{
			landChain.lands().forEach(land -> landChainMap.put(land, landChain));
		}
	}
	
	public static void handlePacket(SkaianetInfoPackets.HasEntered packet)
	{
		hasEntered = packet.hasEntered();
	}
}
