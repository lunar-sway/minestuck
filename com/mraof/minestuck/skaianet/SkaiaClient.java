package com.mraof.minestuck.skaianet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.SkaianetInfoPacket;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SkaiaClient {
	
	//Variables
	static Map<String,List<String>> openServers = new HashMap();
	static List<SburbConnection> connections = new ArrayList();
	static Map<String,Boolean> resumingServer = new HashMap();
	static Map<String, Boolean> resumingClient = new HashMap();
	static TileEntityComputer te = null;
	
	/**
	 * Called by a computer on interact. If it doesn't have the sufficient information,
	 * returns false and sends a request about that information.
	 * @param computer The computer. Will save this variable for later if it sends a request.
	 * @return If it currently has the necessary information.
	 */
	public static boolean requestData(TileEntityComputer computer){
		boolean b = openServers.get(computer.owner) != null;
		if(!b){
			Debug.print("Sending data request about the player \""+computer.owner+"\".");
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_INFO, computer.owner);
			packet.length = packet.data.length;
			PacketDispatcher.sendPacketToServer(packet);
			te = computer;
		}
		return b;
	}
	
	//Getters used by the gui
	public static String getAssociatedPartner(String player, boolean isClient){
		for(SburbConnection c : connections)
			if(c.isMain)
				if(isClient && c.getClientName().equals(player))
					return c.getServerName();
				else if(!isClient && c.getServerName().equals(player))
				return c.getClientName();
		return "";
	}
	
	public static List<String> getAvailableServers(String player){
		return openServers.get(player);
	}
	
	public static boolean enteredMedium(String player){
		for(SburbConnection c : connections)
			if(c.isMain && c.getClientName().equals(player))
				return c.enteredGame;
		return false;
	}
	
	public static boolean isResuming(String player, boolean isClient){
		if(isClient){
			return resumingClient.get(player);
		}
		else {
			return resumingServer.get(player);
		}
	}
	
	public static boolean isActive(String player, boolean isClient){
		if(isClient)
			return getClientConnection(player) != null || resumingClient.get(player);
		else return openServers.get(player).contains(player) || resumingServer.get(player);
	}
	
	//Methods called from the actionPerformed method in the gui.
	
	public static void giveItems(String player){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.SBURB_GIVE, player);
		packet.length = packet.data.length;
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	public static SburbConnection getClientConnection(String client){
		for(SburbConnection c : connections)
			if(c.isActive && c.getClientName().equals(client))
				return c;
		return null;
	}
	
	public static void sendConnectRequest(TileEntityComputer te, String otherPlayer, boolean isClient){	//Used for both connect, open server and resume
		Debug.print("Sending connect packet to server");
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.SBURB_CONNECT, ComputerData.createData(te), otherPlayer, isClient);
		packet.length = packet.data.length;
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	public static void sendCloseRequest(TileEntityComputer te, String otherPlayer, boolean isClient){
		Debug.print("Sending close packet to server");
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.SBURB_CLOSE, te.owner, otherPlayer, isClient);
		packet.length = packet.data.length;
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	//Methods used by the SkaianetInfoPacket.
	public static SburbConnection getConnection(ByteArrayDataInput data){
		SburbConnection c = new SburbConnection();
		
		c.isMain = data.readBoolean();
		if(c.isMain){
			c.isActive = data.readBoolean();
			c.enteredGame = data.readBoolean();
		}
		c.clientName = data.readLine();
		c.serverName = data.readLine();
		
		return c;
	}
	
	public static void consumePacket(SkaianetInfoPacket data){
		
		openServers.put(data.player, data.openServers);
		
		resumingClient.put(data.player, data.isClientResuming);
		resumingServer.put(data.player, data.isServerResuming);
		
		Iterator<SburbConnection> i = connections.iterator();
		while(i.hasNext()){
			SburbConnection c = i.next();
			if(c.clientName.equals(data.player) || c.serverName.equals(data.player))
				i.remove();
		}
		
		connections.addAll(data.connections);
		
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if(gui != null && gui instanceof GuiComputer)
			((GuiComputer)gui).updateGui();
		else if(te != null && te.owner.equals(data.player)){
			if(!Minecraft.getMinecraft().thePlayer.isSneaking())
				Minecraft.getMinecraft().thePlayer.openGui(Minestuck.instance, 1, te.worldObj, te.xCoord, te.yCoord, te.zCoord);
			te = null;
		}
	}
	
}
