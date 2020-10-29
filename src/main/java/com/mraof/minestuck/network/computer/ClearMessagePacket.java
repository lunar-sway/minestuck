package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.network.PlayToServerPacket;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
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
	private final BlockPos pos;
	private final int program;
	
	public ClearMessagePacket(BlockPos pos, int program)
	{
		this.pos = pos;
		this.program = program;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(pos);
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
		ComputerTileEntity.forNetworkIfPresent(player, pos, computer -> {
			computer.latestmessage.put(program, "");
			computer.markBlockForUpdate();
		});
	}
}