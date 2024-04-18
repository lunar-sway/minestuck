package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * This packet tells the server to clear the message for the
 * given computer's program. Parameter 1 is a <code>ComputerData</code>
 * that represents the computer. Parameter 2 is a <code>Integer</code>
 * that represents the program to clear.
 * @author kirderf1
 *
 */
public class ClearMessagePacket implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("clear_message");
	
	private final BlockPos pos;
	private final int program;
	
	public ClearMessagePacket(BlockPos pos, int program)
	{
		this.pos = pos;
		this.program = program;
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeInt(program);
	}
	
	public static ClearMessagePacket read(FriendlyByteBuf buffer)
	{
		BlockPos computer = buffer.readBlockPos();
		int program = buffer.readInt();
		
		return new ClearMessagePacket(computer, program);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		ComputerBlockEntity.forNetworkIfPresent(player, pos, computer -> {
			computer.latestmessage.put(program, "");
			computer.markBlockForUpdate();
		});
	}
}