package com.mraof.minestuck.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.SburbConnection;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class SburbServerOpenPacket extends MinestuckPacket {

	private int x,y,z;
	
	public SburbServerOpenPacket() 
	{
		super(Type.SBURB_OPEN);
	}

	@Override
	public byte[] generatePacket(Object... data) {
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.writeInt((Integer)data[0]);
		dat.writeInt((Integer)data[1]);
		dat.writeInt((Integer)data[2]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);

		x = dat.readInt();
		y = dat.readInt();
		z = dat.readInt();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName) {
		TileEntity entity = ((EntityPlayer)player).worldObj.getBlockTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileEntityComputer)){
			Debug.print("Cant find coputer at given location");
			return;
		}
		TileEntityComputer te = (TileEntityComputer) entity;
		te.openToClients = true;
		if(te.gui != null)
			te.gui.updateGui();
		
		SburbConnection.openServer(te.owner);
		Debug.print("Got openserver packet, server:"+te.owner+", id:"+te.id+","+((EntityPlayer)player).worldObj.isRemote);
		
		if (!((EntityPlayer)player).worldObj.isRemote) {
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_OPEN,te.xCoord,te.yCoord,te.zCoord);
			packet.length = packet.data.length;
			PacketDispatcher.sendPacketToAllPlayers(packet);
		}
	}

}
