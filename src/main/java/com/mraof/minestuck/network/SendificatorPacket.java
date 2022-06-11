package com.mraof.minestuck.network;

import com.mraof.minestuck.inventory.SendificatorContainer;
import com.mraof.minestuck.tileentity.machine.SendificatorTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public class SendificatorPacket implements PlayToServerPacket
{
	private final BlockPos destinationBlockPos;
	
	public SendificatorPacket(BlockPos pos)
	{
		this.destinationBlockPos = pos;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(destinationBlockPos);
	}
	
	public static SendificatorPacket decode(PacketBuffer buffer)
	{
		BlockPos destinationBlockPos = buffer.readBlockPos();
		
		return new SendificatorPacket(destinationBlockPos);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		Container playerContainer = player.containerMenu;
		if(playerContainer instanceof SendificatorContainer)
		{
			BlockPos machinePos = ((SendificatorContainer) playerContainer).machinePos;
			SendificatorTileEntity tileEntity = (SendificatorTileEntity) player.level.getBlockEntity(machinePos);
			if(tileEntity != null)
			{
				tileEntity.setDestinationBlockPos(destinationBlockPos);
				//Imitates the structure block to ensure that changes are sent client-side
				tileEntity.setChanged();
				BlockState state = player.level.getBlockState(machinePos);
				player.level.sendBlockUpdated(machinePos, state, state, 3);
			}
		}
	}
}