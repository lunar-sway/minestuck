package com.mraof.minestuck.skaianet;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.SburbConnectClosedPacket;
import com.mraof.minestuck.network.SburbConnectPacket;
import com.mraof.minestuck.network.SkaianetInfoPacket;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
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
	private static final Map<ResourceLocation, List<ResourceLocation>> landChainMap = new HashMap<>();
	private static ComputerTileEntity te = null;
	public static int playerId;	//The id that this player is expected to have.
	
	@SubscribeEvent
	public static void onLoggedIn(ClientPlayerNetworkEvent.LoggedInEvent event)
	{
		openServers.clear();
		connections.clear();
		serverWaiting.clear();
		resumingClient.clear();
		landChainMap.clear();
		playerId = -1;
	}
	
	/**
	 * Called by a computer on interact. If it doesn't have the sufficient information,
	 * returns false and sends a request about that information.
	 * @param computer The computer. Will save this variable for later if it sends a request.
	 * @return If it currently has the necessary information.
	 */
	public static boolean requestData(ComputerTileEntity computer)
	{
		boolean b = openServers.get(computer.ownerId) != null;
		if(!b)
		{
			SkaianetInfoPacket packet = SkaianetInfoPacket.request(computer.ownerId);
			MSPacketHandler.sendToServer(packet);
			te = computer;
		}
		return b;
	}
	
	//Getters used by the computer
	public static int getAssociatedPartner(int playerId, boolean isClient)
	{
		for(ReducedConnection c : connections)
			if(c.isMain())
				if(isClient && c.getClientId() == playerId)
					return c.getServerId();
				else if(!isClient && c.getServerId() == playerId)
					return c.getClientId();
		return -1;
	}
	
	public static Map<Integer, String> getAvailableServers(Integer playerId)
	{
		return openServers.get(playerId);
	}
	
	public static boolean enteredMedium(int player)
	{
		for(ReducedConnection c : connections)
			if(c.isMain() && c.getClientId() == player)
				return c.hasEntered();
		return false;
	}
	
	public static List<ResourceLocation> getLandChain(DimensionType id)
	{
		return landChainMap.get(id.getRegistryName());
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
			if(playerId == c.getClientId())
				return false;
		return true;
	}
	
	//Methods called from the actionPerformed method in the gui.
	
	public static ReducedConnection getClientConnection(int client)
	{
		for(ReducedConnection c : connections)
			if(c.isActive() && c.getClientId() == client)
				return c;
		return null;
	}
	
	public static void sendConnectRequest(ComputerTileEntity te, int otherPlayer, boolean isClient)	//Used for both connect, open server and resume
	{
		SburbConnectPacket packet = new SburbConnectPacket(te.getPos(), otherPlayer, isClient);
		MSPacketHandler.sendToServer(packet);
	}
	
	public static void sendCloseRequest(ComputerTileEntity te, int otherPlayer, boolean isClient)
	{
		SburbConnectClosedPacket packet = new SburbConnectClosedPacket(te.ownerId, otherPlayer, isClient);
		MSPacketHandler.sendToServer(packet);
	}
	
	public static void consumePacket(SkaianetInfoPacket data)
	{
		if(data.landChains != null)
		{
			landChainMap.clear();
			for(List<ResourceLocation> list : data.landChains)
			{
				for(ResourceLocation land : list)
				{
					landChainMap.put(land, list);
				}
			}
			return;
		}
		
		if(playerId == -1)
			playerId = data.playerId;	//The first info packet is expected to be regarding the receiving player.
		openServers.put(data.playerId, data.openServers);
		
		resumingClient.put(data.playerId, data.isClientResuming);
		serverWaiting.put(data.playerId, data.isServerResuming);
		
		connections.removeIf(c -> c.getClientId() == data.playerId || c.getServerId() == data.playerId);
		connections.addAll(data.connectionsTo);
		
		Screen gui = Minecraft.getInstance().currentScreen;
		if(gui instanceof ComputerScreen)
			((ComputerScreen)gui).updateGui();
		else if(te != null && te.ownerId == data.playerId)
		{
			if(!Minecraft.getInstance().player.isSneaking())
				MSScreenFactories.displayComputerScreen(te);
			te = null;
		}
	}
}
