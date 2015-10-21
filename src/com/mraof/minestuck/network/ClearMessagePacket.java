package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.network.skaianet.ComputerData;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.TileEntityComputer;

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
		MinestuckPacket packet = MinestuckPacket.makePacket(Type.CLEAR, data, program);
		MinestuckChannelHandler.sendToServer(packet);
	}
	
	@Override
	public MinestuckPacket generatePacket(Object... dat) {
		ComputerData cd = (ComputerData)dat[0];
		
		data.writeInt(cd.getX());
		data.writeInt(cd.getY());
		data.writeInt(cd.getZ());
		data.writeInt(cd.getDimension());
		data.writeInt((Integer)dat[1]);
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) {
		
		computer = new ComputerData("",data.readInt(),data.readInt(),data.readInt(),data.readInt());
		program = data.readInt();
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		
		TileEntityComputer te = SkaianetHandler.getComputer(computer);
		
		if(te != null)
		{
			te.latestmessage.put(program, "");
			te.getWorld().markBlockForUpdate(te.getPos());
		}
	}
	
	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.CLIENT);
	}

}
