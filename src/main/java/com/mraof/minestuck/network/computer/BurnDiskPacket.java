package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ProgramType;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BurnDiskPacket(BlockPos computerPos, ProgramType programType) implements MSPacket.PlayToServer
{
	public static final Type<BurnDiskPacket> ID = new Type<>(Minestuck.id("burn_disk"));
	public static final StreamCodec<FriendlyByteBuf, BurnDiskPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			BurnDiskPacket::computerPos,
			ProgramType.STREAM_CODEC,
			BurnDiskPacket::programType,
			BurnDiskPacket::new
	);
	
	public static BurnDiskPacket create(ComputerBlockEntity be, ProgramType programType)
	{
		return new BurnDiskPacket(be.getBlockPos(), programType);
	}
	
	@Override
	public Type<BurnDiskPacket> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		ComputerBlockEntity.getAccessibleComputer(player, computerPos)
				.ifPresent(computer -> computer.burnDisk(programType));
	}
}
