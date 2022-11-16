package com.mraof.minestuck.network;

import com.mraof.minestuck.blockentity.machine.SendificatorBlockEntity;
import com.mraof.minestuck.inventory.SendificatorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class SendificatorPacket implements PlayToServerPacket
{
	private final BlockPos destinationBlockPos;
	
	public SendificatorPacket(BlockPos pos)
	{
		this.destinationBlockPos = pos;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(destinationBlockPos);
	}
	
	public static SendificatorPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos destinationBlockPos = buffer.readBlockPos();
		
		return new SendificatorPacket(destinationBlockPos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		AbstractContainerMenu playerContainer = player.containerMenu;
		if(playerContainer instanceof SendificatorMenu sendificatorMenu)
		{
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
}