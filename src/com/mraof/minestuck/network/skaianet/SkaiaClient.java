package com.mraof.minestuck.network.skaianet;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.SkaianetInfoPacket;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

@SideOnly(Side.CLIENT)
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
	private static Map<Integer, List<Integer>> landChainMap = new HashMap<>();
	private static TileEntityComputer te = null;
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
	public static boolean requestData(TileEntityComputer computer)
	{
		boolean b = openServers.get(computer.ownerId) != null;
		if(!b)
		{
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.SBURB_INFO, computer.ownerId);
			MinestuckChannelHandler.sendToServer(packet);
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
				return c.enteredGame;
		return false;
	}
	
	public static List<Integer> getLandChain(int id)
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
	
	public static void sendConnectRequest(TileEntityComputer te, int otherPlayer, boolean isClient)	//Used for both connect, open server and resume
	{
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.SBURB_CONNECT, ComputerData.createData(te), otherPlayer, isClient);
		MinestuckChannelHandler.sendToServer(packet);
	}
	
	public static void sendCloseRequest(TileEntityComputer te, int otherPlayer, boolean isClient)
	{
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.SBURB_CLOSE, te.ownerId, otherPlayer, isClient);
		MinestuckChannelHandler.sendToServer(packet);
	}
	
	//Methods used by the SkaianetInfoPacket.
	public static SburbConnection getConnection(ByteBuf data)
	{
		SburbConnection c = new SburbConnection();
		
		c.isMain = data.readBoolean();
		if(c.isMain)
		{
			c.isActive = data.readBoolean();
			c.enteredGame = data.readBoolean();
		}
		c.clientId = data.readInt();
		c.clientName = MinestuckPacket.readLine(data);
		c.serverId = data.readInt();
		c.serverName = MinestuckPacket.readLine(data);
		
		return c;
	}
	
	public static void consumePacket(SkaianetInfoPacket data)
	{
		if(data.landChains != null)
		{
			landChainMap.clear();
			for(List<Integer> list : data.landChains)
				for(int i : list)
					landChainMap.put(i, list);
			return;
		}
		
		if(playerId == -1)
			playerId = data.playerId;	//The first info packet is expected to be regarding the receiving player.
		openServers.put(data.playerId, data.openServers);
		
		resumingClient.put(data.playerId, data.isClientResuming);
		serverWaiting.put(data.playerId, data.isServerResuming);
		
		Iterator<SburbConnection> i = connections.iterator();
		while(i.hasNext())
		{
			SburbConnection c = i.next();
			if(c.clientId == data.playerId || c.serverId == data.playerId)
				i.remove();
		}
		connections.addAll(data.connections);
		
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if(gui instanceof GuiComputer)
			((GuiComputer)gui).updateGui();
		else if(te != null && te.ownerId == data.playerId)
		{
			if(!Minecraft.getMinecraft().player.isSneaking())
				Minecraft.getMinecraft().player.openGui(Minestuck.instance, GuiHandler.GuiId.COMPUTER.ordinal(), te.getWorld(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
			te = null;
		}
	}
}
