package com.mraof.minestuck.skaianet;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SkaiaClient {
	
	static List<String> list = new ArrayList();
	
	public static List<String> getAvailableServers(){
		return list;
	}
	
	public static void sendOpenServerRequest(TileEntityComputer te){
		if(MinecraftServer.getServer() != null){	//If this is the same minecraft program as the server
			Debug.print("Sending open server request directly to SkaianetHandler");
			SkaianetHandler.requestConnection(new ComputerData(te.owner, te.xCoord, te.yCoord, te.zCoord, te.worldObj.provider.dimensionId), "", false);
		} else {
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_OPEN,te.owner,te.xCoord,te.yCoord,te.zCoord,te.worldObj.provider.dimensionId);
			packet.length = packet.data.length;
			Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
			Debug.print("Sent an open server request to server");
		}
	}
	
}
