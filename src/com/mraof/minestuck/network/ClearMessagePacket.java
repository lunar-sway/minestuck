package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * This packet tells the server to clear the message for the
 * given computer's program. Parameter 1 is a <code>ComputerData</code>
 * that represents the computer. Parameter 2 is a <code>Integer</code>
 * that represents the program to clear.
 * @author kirderf1
 *
 */
public class ClearMessagePacket implements PlayToServerPacket
{
	BlockPos computer;
	int program;
	
	public ClearMessagePacket(BlockPos computer, int program)
	{
		this.computer = computer;
		this.program = program;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(computer);
		buffer.writeInt(program);
	}
	
	public static ClearMessagePacket decode(PacketBuffer buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		int program = buffer.readInt();
		
		return new ClearMessagePacket(computer, program);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
	
		if(player.getEntityWorld().isAreaLoaded(computer, 0))
		{
			TileEntity te = player.getEntityWorld().getTileEntity(computer);
			if(te instanceof ComputerTileEntity)
			{
				ComputerTileEntity computerTE = (ComputerTileEntity) te;
				
				computerTE.latestmessage.put(program, "");
				computerTE.markBlockForUpdate();
			}
		}
	}
}