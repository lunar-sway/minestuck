package com.mraof.minestuck.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.IConnectionListener;
import com.mraof.minestuck.util.SburbConnection;
import com.mraof.minestuck.util.SburbConnector;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class SburbServerOpenPacket extends MinestuckPacket implements IConnectionListener {

	public String connectedTo;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int gristTotal;
	private Player player;
	private TileEntityComputer te;
	
	public SburbServerOpenPacket() 
	{
		super(Type.SBURB_OPEN);
	}

	@Override
	public byte[] generatePacket(Object... data) 
	{
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeInt((Integer)data[0]);
		dat.writeInt((Integer)data[1]);
		dat.writeInt((Integer)data[2]);
		dat.writeChars((String) data[3]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		
		xCoord = dat.readInt();
		yCoord = dat.readInt();
		zCoord = dat.readInt();
		connectedTo = dat.readLine();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName)
	{
		
		this.te = (TileEntityComputer)Minecraft.getMinecraft().theWorld.getBlockTileEntity(xCoord,yCoord,zCoord);
		
		if (te == null) {
			System.out.println("[MINESTUCK] Packet recieved, but TE isn't there!");
			return;
		}
		this.player = player;
		SburbConnector.addServer(connectedTo);
		SburbConnector.addListener(this);
	}

	@Override
	public void onConnected(SburbConnection conn) {
		if (!te.connected && conn.getServerPlayer() == ((EntityPlayer)player).username) {
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_CONNECT,te.xCoord,te.yCoord,te.zCoord,connectedTo);
			packet.length = packet.data.length;
			PacketDispatcher.sendPacketToPlayer(packet, player);
			
			te.connectedTo = connectedTo;
			te.connected = true;
		}
	}

}
