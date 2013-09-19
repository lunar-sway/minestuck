package com.mraof.minestuck.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

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
	private int dimensionId;
	private String server;
	
	public SburbServerOpenPacket() 
	{
		super(Type.SBURB_OPEN);
	}

	@Override
	public byte[] generatePacket(Object... data) {
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		dat.write((data[0].toString()+"\n").getBytes());
		dat.writeInt((Integer)data[1]);
		dat.writeInt((Integer)data[2]);
		dat.writeInt((Integer)data[3]);
		dat.writeInt((Integer)data[4]);
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) 
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);

		server = dat.readLine();
		x = dat.readInt();
		y = dat.readInt();
		z = dat.readInt();
		dimensionId = dat.readInt();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler handler, Player player, String userName) {
		if (!((EntityPlayer)player).worldObj.isRemote) {
			World world = null;
			for(WorldServer worldServ : MinecraftServer.getServer().worldServers)
				if(worldServ.provider.dimensionId == dimensionId){
					world = worldServ;
					break;
				}
			if(world == null){
				Debug.print("Can't find world:"+dimensionId);
				return;
			}
			TileEntity entity = world.getBlockTileEntity(x, y, z);
			if(entity == null || !(entity instanceof TileEntityComputer)){
				Debug.print("Cant find computer at given location");
				return;
			}
			TileEntityComputer te = (TileEntityComputer)entity;
			te.openToClients = true;
			world.markBlockForUpdate(x, y, z);
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "Minestuck";
			packet.data = MinestuckPacket.makePacket(Type.SBURB_OPEN, server,x,y,z,dimensionId);
			packet.length = packet.data.length;
			PacketDispatcher.sendPacketToAllPlayers(packet);
		}
		
		SburbConnection.openServer(server,x,y,z,dimensionId);
		Debug.print("Got openserver packet, server:"+server);
		
	}

}
