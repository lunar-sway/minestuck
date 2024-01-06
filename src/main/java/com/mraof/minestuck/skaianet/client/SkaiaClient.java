package com.mraof.minestuck.skaianet.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.computer.SkaianetInfoPacket;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class SkaiaClient
{
	
	//Variables
	private static final Map<Integer, Map<Integer, String>> openServers = new HashMap<>();
	private static final List<ReducedConnection> connections = new ArrayList<>();
	private static final Map<Integer, Boolean> serverWaiting = new HashMap<>();
	private static final Map<Integer, Boolean> resumingClient = new HashMap<>();
	/**
	 * A map used to track chains of lands, to be used by the skybox render
	 */
	private static final Map<ResourceKey<Level>, List<ResourceKey<Level>>> landChainMap = new HashMap<>();
	private static ComputerBlockEntity be = null;
	public static int playerId;	//The id that this player is expected to have.
	private static boolean hasEntered;
	
	@SubscribeEvent
	public static void onLoggedIn(ClientPlayerNetworkEvent.LoggingIn event)
	{
		openServers.clear();
		connections.clear();
		serverWaiting.clear();
		resumingClient.clear();
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
		boolean b = openServers.get(computer.ownerId) != null;
		if(!b)
		{
			MSPacketHandler.sendToServer(new SkaianetInfoPacket.Request(computer.ownerId));
			be = computer;
		}
		return b;
	}
	
	//Getters used by the computer
	public static int getAssociatedPartner(int playerId, boolean isClient)
	{
		for(ReducedConnection c : connections)
			if(c.isMain())
				if(isClient && c.client().id() == playerId)
					return c.server().id();
				else if(!isClient && c.server().id() == playerId)
					return c.client().id();
		return -1;
	}
	
	public static Map<Integer, String> getAvailableServers(Integer playerId)
	{
		return openServers.get(playerId);
	}
	
	public static boolean hasPlayerEntered()
	{
		return hasEntered;
	}
	
	public static List<ResourceKey<Level>> getLandChain(ResourceKey<Level> id)
	{
		return landChainMap.get(id);
	}
	
	public static boolean isActive(int playerId, boolean isClient)
	{
		if(isClient)
			return getClientConnection(playerId) != null || resumingClient.get(playerId);
		else return serverWaiting.get(playerId);
	}
	
	/**
	 * If the color selection gui may be opened.
	 */
	public static boolean canSelect(int playerId)
	{
		if(playerId != SkaiaClient.playerId)
			return false;
		for(ReducedConnection c : connections)
			if(playerId == c.client().id())
				return false;
		return true;
	}
	
	//Methods called from the actionPerformed method in the gui.
	
	public static ReducedConnection getClientConnection(int client)
	{
		for(ReducedConnection c : connections)
			if(c.isActive() && c.client().id() == client)
				return c;
		return null;
	}
	
	public static void handlePacket(SkaianetInfoPacket.Data data)
	{
		if(playerId == -1)
			playerId = data.playerId();	//The first info packet is expected to be regarding the receiving player.
		openServers.put(data.playerId(), data.openServers());
		
		resumingClient.put(data.playerId(), data.isClientResuming());
		serverWaiting.put(data.playerId(), data.isServerResuming());
		
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
	
	public static void handlePacket(SkaianetInfoPacket.LandChains packet)
	{
		landChainMap.clear();
		for(List<ResourceKey<Level>> list : packet.landChains())
		{
			for(ResourceKey<Level> land : list)
			{
				if(land != null)
					landChainMap.put(land, list);
			}
		}
	}
	
	public static void handlePacket(SkaianetInfoPacket.HasEntered packet)
	{
		hasEntered = packet.hasEntered();
	}
}
