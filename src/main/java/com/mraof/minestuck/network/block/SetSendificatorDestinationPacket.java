package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.SendificatorBlockEntity;
import com.mraof.minestuck.inventory.SendificatorMenu;
import com.mraof.minestuck.network.MSPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetSendificatorDestinationPacket(BlockPos destinationBlockPos) implements MSPacket.PlayToServer
{
	public static final Type<SetSendificatorDestinationPacket> ID = new Type<>(Minestuck.id("set_sendificator_destination"));
	public static final StreamCodec<ByteBuf, SetSendificatorDestinationPacket> STREAM_CODEC = BlockPos.STREAM_CODEC.map(SetSendificatorDestinationPacket::new, SetSendificatorDestinationPacket::destinationBlockPos);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		AbstractContainerMenu playerContainer = player.containerMenu;
		if(!(playerContainer instanceof SendificatorMenu sendificatorMenu))
			return;
		
		sendificatorMenu.getPosition().execute((level, machinePos) -> {
			SendificatorBlockEntity blockEntity = (SendificatorBlockEntity) level.getBlockEntity(machinePos);
			if(blockEntity != null)
			{
				blockEntity.setDestinationBlockPos(destinationBlockPos);
				//Imitates the structure block to ensure that changes are sent client-side
				blockEntity.setChanged();
				BlockState state = level.getBlockState(machinePos);
				level.sendBlockUpdated(machinePos, state, state, 3);
			}
		});
	}
}
