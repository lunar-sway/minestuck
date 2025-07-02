package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.SburbClientData;
import com.mraof.minestuck.computer.SburbServerData;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * This packet tells the server to clear the message for the
 * given computer's program. Parameter 1 is a <code>ComputerData</code>
 * that represents the computer. Parameter 2 is a <code>Integer</code>
 * that represents the program to clear.
 * @author kirderf1
 *
 */
public record ClearMessagePacket(BlockPos computerPos, int program) implements MSPacket.PlayToServer
{
	public static final Type<ClearMessagePacket> ID = new Type<>(Minestuck.id("clear_message"));
	public static final StreamCodec<FriendlyByteBuf, ClearMessagePacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			ClearMessagePacket::computerPos,
			ByteBufCodecs.INT,
			ClearMessagePacket::program,
			ClearMessagePacket::new
	);
	
	
	@Override
	public Type<ClearMessagePacket> type()
	{
		return ID;
	}
	
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		ComputerBlockEntity.getAccessibleComputer(player, computerPos).ifPresent(computer -> {
			if(program == 0)
				computer.getSburbClientData().ifPresent(SburbClientData::clearEventMessage);
			if(program == 1)
				computer.getSburbServerData().ifPresent(SburbServerData::clearEventMessage);
		});
	}
}
