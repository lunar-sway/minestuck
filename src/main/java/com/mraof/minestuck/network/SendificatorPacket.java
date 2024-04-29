package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.SendificatorBlockEntity;
import com.mraof.minestuck.inventory.SendificatorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public record SendificatorPacket(BlockPos destinationBlockPos) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("sendificator");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(destinationBlockPos);
	}
	
	public static SendificatorPacket read(FriendlyByteBuf buffer)
	{
		BlockPos destinationBlockPos = buffer.readBlockPos();
		
		return new SendificatorPacket(destinationBlockPos);
	}
	
	@Override
	public void execute(ServerPlayer player)
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
