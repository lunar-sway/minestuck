package com.mraof.minestuck.network;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mraof.minestuck.network.skaianet.ComputerData;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.util.Debug;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/**
 * This packet tells the server to clear the message for the
 * given computer's program. Parameter 1 is a <code>ComputerData</code>
 * that represents the computer. Parameter 2 is a <code>Integer</code>
 * that represents the program to clear.
 * @author kirderf1
 *
 */
public class ClearMessagePacket extends MinestuckPacket {
	
	ComputerData computer;
	int program;
	
	public static void send(ComputerData data, int program){
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "Minestuck";
		packet.data = MinestuckPacket.makePacket(Type.CLEAR, data, program);
		packet.length = packet.data.length;
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	public ClearMessagePacket() {
		super(Type.SBURB_CLOSE);
	}
	
	@Override
	public byte[] generatePacket(Object... data) {
		ByteArrayDataOutput dat = ByteStreams.newDataOutput();
		ComputerData cd = (ComputerData)data[0];
		
		dat.writeInt(cd.getX());
		dat.writeInt(cd.getY());
		dat.writeInt(cd.getZ());
		dat.writeInt(cd.getDimension());
		dat.writeInt((Integer)data[1]);
		
		return dat.toByteArray();
	}

	@Override
	public MinestuckPacket consumePacket(byte[] data) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(data);
		for(byte b : data)
			Debug.print(b);
		computer = new ComputerData("",dat.readInt(),dat.readInt(),dat.readInt(),dat.readInt());
		program = dat.readInt();
		
		return this;
	}

	@Override
	public void execute(INetworkManager network, MinestuckPacketHandler minestuckPacketHandler, Player player, String userName) {
		
		TileEntityComputer te = SkaianetHandler.getComputer(computer);
		
		if(te != null){
			te.latestmessage.put(program, "");
			te.worldObj.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
		}
	}

}
