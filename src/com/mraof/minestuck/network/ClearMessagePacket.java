package com.mraof.minestuck.network;

import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import com.mraof.minestuck.util.Location;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * This packet tells the server to clear the message for the
 * given computer's program. Parameter 1 is a <code>ComputerData</code>
 * that represents the computer. Parameter 2 is a <code>Integer</code>
 * that represents the program to clear.
 * @author kirderf1
 *
 */
public class ClearMessagePacket
{
	Location computer;
	int program;
	
	public ClearMessagePacket(Location computer, int program)
	{
		this.computer = computer;
		this.program = program;
	}
	
	public void encode(PacketBuffer buffer)
	{
		computer.toBuffer(buffer);
		buffer.writeInt(program);
	}
	
	public static ClearMessagePacket decode(PacketBuffer buffer)
	{
		Location computer = Location.fromBuffer(buffer);
		int program = buffer.readInt();
		
		return new ClearMessagePacket(computer, program);
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute(EntityPlayerMP player)
	{
	
		if(player.getEntityWorld().dimension.getType() == computer.dim && player.getEntityWorld().isBlockLoaded(computer.pos))
		{
			ComputerTileEntity te = SkaianetHandler.getComputer(player.getServer(), computer);
			
			if(te != null)
			{
				te.latestmessage.put(program, "");
				te.markBlockForUpdate();
			}
		}
	}
}