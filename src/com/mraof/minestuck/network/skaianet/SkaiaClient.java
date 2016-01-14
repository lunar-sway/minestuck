package com.mraof.minestuck.network.skaianet;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientProxy;
import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.SkaianetInfoPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.UsernameHandler;

@SideOnly(Side.CLIENT)
public class SkaiaClient
{
	
	//Variables
	static Map<String,List<String>> openServers = new HashMap<String, List<String>>();
	static List<SburbConnection> connections = new ArrayList<SburbConnection>();
	static Map<String, Boolean> resumingServer = new HashMap<String, Boolean>();
	static Map<String, Boolean> resumingClient = new HashMap<String, Boolean>();
	static TileEntityComputer te = null;
	
	/**
	 * Called by a computer on interact. If it doesn't have the sufficient information,
	 * returns false and sends a request about that information.
	 * @param computer The computer. Will save this variable for later if it sends a request.
	 * @return If it currently has the necessary information.
	 */
	public static boolean requestData(TileEntityComputer computer){
		boolean b = openServers.get(computer.owner) != null;
		if(!b) {
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.SBURB_INFO, computer.owner);
			MinestuckChannelHandler.sendToServer(packet);
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
	
	public static boolean canSelect(String player)
	{
		if(!player.equals(UsernameHandler.encode(ClientProxy.getClientPlayer().getName())))
			return false;
		for(SburbConnection c : connections)
			if(player.equals(c.getClientName()))
				return false;
		return true;
	}
	
	//Methods called from the actionPerformed method in the gui.
	
	public static SburbConnection getClientConnection(String client){
		for(SburbConnection c : connections)
			if(c.isActive && c.getClientName().equals(client))
				return c;
		return null;
	}
	
	public static void sendConnectRequest(TileEntityComputer te, String otherPlayer, boolean isClient){	//Used for both connect, open server and resume
//		Debug.print("Sending connect packet to server");
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.SBURB_CONNECT, ComputerData.createData(te), otherPlayer, isClient);
		MinestuckChannelHandler.sendToServer(packet);
	}
	
	public static void sendCloseRequest(TileEntityComputer te, String otherPlayer, boolean isClient){
//		Debug.print("Sending close packet to server");
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.SBURB_CLOSE, te.owner, otherPlayer, isClient);
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
		c.clientName = MinestuckPacket.readLine(data);
		c.serverName = MinestuckPacket.readLine(data);
		
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
				Minecraft.getMinecraft().thePlayer.openGui(Minestuck.instance, GuiHandler.GuiId.COMPUTER.ordinal(), te.getWorld(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
			te = null;
		}
	}
	
}
