package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ProgramTypes;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EjectDiskPacket(BlockPos computerPos, ItemStack itemStack) implements MSPacket.PlayToServer
{
	public static final Type<EjectDiskPacket> ID = new Type<>(Minestuck.id("eject_disk"));
	public static final StreamCodec<RegistryFriendlyByteBuf, EjectDiskPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			EjectDiskPacket::computerPos,
			ItemStack.STREAM_CODEC,
			EjectDiskPacket::itemStack,
			EjectDiskPacket::new
	);
	
	public static EjectDiskPacket create(ComputerBlockEntity be, ItemStack itemStack)
	{
		return new EjectDiskPacket(be.getBlockPos(), itemStack);
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
		Level level = computer.getLevel();
		if(level == null)
			return;
		
		if(computer.getProgramData(ProgramTypes.SETTINGS).isEmpty())
			return;
		
		computer.tryDropDisk(itemStack);
	}
}
