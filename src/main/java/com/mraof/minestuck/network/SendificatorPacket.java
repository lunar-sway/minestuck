package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.machine.SendificatorTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class SendificatorPacket implements PlayToServerPacket
{
	private final BlockPos destinationBlockPos;
	private final BlockPos tileBlockPos;
	
	public SendificatorPacket(BlockPos pos, BlockPos tileBlockPos)
	{
		this.destinationBlockPos = pos;
		this.tileBlockPos = tileBlockPos;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(destinationBlockPos);
		buffer.writeBlockPos(tileBlockPos);
	}
	
	public static SendificatorPacket decode(PacketBuffer buffer)
	{
		BlockPos destinationBlockPos = buffer.readBlockPos();
		BlockPos tileBlockPos = buffer.readBlockPos();
		
		return new SendificatorPacket(destinationBlockPos, tileBlockPos);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(player.level.isAreaLoaded(tileBlockPos, 0))
		{
			TileEntity te = player.level.getBlockEntity(tileBlockPos);
			if(te instanceof SendificatorTileEntity)
			{
				BlockPos tePos = te.getBlockPos();
				if(Math.sqrt(player.distanceToSqr(tePos.getX() + 0.5, tePos.getY() + 0.5, tePos.getZ() + 0.5)) <= 8)
				{
					((SendificatorTileEntity) te).setDestinationBlockPos(destinationBlockPos);
					//Imitates the structure block to ensure that changes are sent client-side
					te.setChanged();
					BlockState state = player.level.getBlockState(tileBlockPos);
					player.level.sendBlockUpdated(tileBlockPos, state, state, 3);
				}
			}
		}
	}
}