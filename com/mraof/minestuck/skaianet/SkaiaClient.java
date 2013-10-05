package com.mraof.minestuck.skaianet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

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
	
	static HashMap<String,List<String>> map = new HashMap();
	static List<SburbConnection> connections = new ArrayList();
	
	public static List<String> getAvailableServers(String player){
		return map.get(player);
	}
	
	public static boolean enteredMedium(String player){
		for(SburbConnection c : connections)
			if(c.isMain && c.getClientName().equals(player))
				return c.enteredGame;
		return false;
	}
	
	public static SburbConnection getClientConnection(String client){
		for(SburbConnection c : connections)
			if(c.client != null && c.getClientName().equals(client))
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
		packet.data = MinestuckPacket.makePacket(Type.SBURB_CLOSE, ComputerData.createData(te), otherPlayer, isClient);
		packet.length = packet.data.length;
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	public static void consumePacket(SkaianetInfoPacket data){
		//TODO Consume data
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if(gui != null && gui instanceof GuiComputer)
			((GuiComputer)gui).updateGui();
	}
	
}
