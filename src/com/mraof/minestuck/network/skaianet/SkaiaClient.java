package com.mraof.minestuck.network.skaianet;

import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.client.gui.ModScreenFactories;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.network.SburbConnectClosedPacket;
import com.mraof.minestuck.network.SburbConnectPacket;
import com.mraof.minestuck.network.SkaianetInfoPacket;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.dimension.DimensionType;

import java.util.*;

public class SkaiaClient
{
	
	//Variables
	private static Map<Integer, Map<Integer, String>> openServers = new HashMap<>();
	private static List<SburbConnection> connections = new ArrayList<>();
	private static Map<Integer, Boolean> serverWaiting = new HashMap<>();
	private static Map<Integer, Boolean> resumingClient = new HashMap<>();
	/**
	 * A map used to track chains of lands, to be used by the skybox render
	 */
	private static Map<DimensionType, List<DimensionType>> landChainMap = new HashMap<>();
	private static ComputerTileEntity te = null;
	public static int playerId;	//The id that this player is expected to have.
	
	public static void clear()
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
			MinestuckPacketHandler.sendToServer(packet);
			te = computer;
		}
		return b;
	}
	
	//Getters used by the computer
	public static int getAssociatedPartner(int playerId, boolean isClient)
	{
		for(SburbConnection c : connections)
			if(c.isMain)
				if(isClient && c.clientId == playerId)
					return c.serverId;
				else if(!isClient && c.serverId == playerId)
					return c.clientId;
		return -1;
	}
	
	public static Map<Integer, String> getAvailableServers(Integer playerId)
	{
		return openServers.get(playerId);
	}
	
	public static boolean enteredMedium(int player)
	{
		for(SburbConnection c : connections)
			if(c.isMain && c.clientId == player)
				return c.hasEntered();
		return false;
	}
	
	public static List<DimensionType> getLandChain(DimensionType id)
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
		for(SburbConnection c : connections)
			if(playerId == c.clientId)
				return false;
		return true;
	}
	
	//Methods called from the actionPerformed method in the gui.
	
	public static SburbConnection getClientConnection(int client)
	{
		for(SburbConnection c : connections)
			if(c.isActive && c.clientId == client)
				return c;
		return null;
	}
	
	public static void sendConnectRequest(ComputerTileEntity te, int otherPlayer, boolean isClient)	//Used for both connect, open server and resume
	{
		SburbConnectPacket packet = new SburbConnectPacket(ComputerData.createData(te), otherPlayer, isClient);
		MinestuckPacketHandler.sendToServer(packet);
	}
	
	public static void sendCloseRequest(ComputerTileEntity te, int otherPlayer, boolean isClient)
	{
		SburbConnectClosedPacket packet = new SburbConnectClosedPacket(te.ownerId, otherPlayer, isClient);
		MinestuckPacketHandler.sendToServer(packet);
	}
	
	//Methods used by the SkaianetInfoPacket.
	public static SburbConnection getConnectionFromBuffer(PacketBuffer buffer)
	{
		SburbConnection c = new SburbConnection();
		
		c.isMain = buffer.readBoolean();
		if(c.isMain)
		{
			c.isActive = buffer.readBoolean();
			c.hasEntered = buffer.readBoolean();
		}
		c.clientId = buffer.readInt();
		c.clientName = buffer.readString(16);
		c.serverId = buffer.readInt();
		c.serverName = buffer.readString(16);
		
		return c;
	}
	
	public static void consumePacket(SkaianetInfoPacket data)
	{
		if(data.landChains != null)
		{
			landChainMap.clear();
			for(List<Integer> list : data.landChains)
			{
				List<DimensionType> dimList = new ArrayList<>();
				for(int i : list)
				{
					DimensionType type = DimensionType.getById(i);
					dimList.add(type);
					landChainMap.put(type, dimList);
				}
			}
			return;
		}
		
		if(playerId == -1)
			playerId = data.playerId;	//The first info packet is expected to be regarding the receiving player.
		openServers.put(data.playerId, data.openServers);
		
		resumingClient.put(data.playerId, data.isClientResuming);
		serverWaiting.put(data.playerId, data.isServerResuming);
		
		connections.removeIf(c -> c.clientId == data.playerId || c.serverId == data.playerId);
		connections.addAll(data.connections);
		
		Screen gui = Minecraft.getInstance().currentScreen;
		if(gui instanceof ComputerScreen)
			((ComputerScreen)gui).updateGui();
		else if(te != null && te.ownerId == data.playerId)
		{
			if(!Minecraft.getInstance().player.isSneaking())
				ModScreenFactories.displayComputerScreen(te);
			te = null;
		}
	}
}
