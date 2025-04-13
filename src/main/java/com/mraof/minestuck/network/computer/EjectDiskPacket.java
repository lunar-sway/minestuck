package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ProgramTypes;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EjectDiskPacket(BlockPos computerPos, int index) implements MSPacket.PlayToServer
{
	public static final Type<EjectDiskPacket> ID = new Type<>(Minestuck.id("eject_disk"));
	public static final StreamCodec<RegistryFriendlyByteBuf, EjectDiskPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			EjectDiskPacket::computerPos,
			ByteBufCodecs.INT,
			EjectDiskPacket::index,
			EjectDiskPacket::new
	);
	
	public static EjectDiskPacket create(ComputerBlockEntity be, int index)
	{
		return new EjectDiskPacket(be.getBlockPos(), index);
	}
	
	@Override
	public Type<EjectDiskPacket> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		ComputerBlockEntity.getAccessibleComputer(player, this.computerPos)
				.ifPresent(this::tryEjectDisk);
	}
	
	private void tryEjectDisk(ComputerBlockEntity computer)
	{
		if(computer.getProgramData(ProgramTypes.SETTINGS).isEmpty())
			return;
		
		computer.tryEjectDisk(this.index);
	}
}
