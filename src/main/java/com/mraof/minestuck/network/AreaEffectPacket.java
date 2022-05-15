package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.redstone.AreaEffectTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class AreaEffectPacket implements PlayToServerPacket
{
	private final BlockPos minEffectPos;
	private final BlockPos maxEffectPos;
	private final BlockPos tileBlockPos;
	
	public AreaEffectPacket(BlockPos minPos, BlockPos maxPos, BlockPos tileBlockPos)
	{
		this.minEffectPos = minPos;
		this.maxEffectPos = maxPos;
		this.tileBlockPos = tileBlockPos;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(minEffectPos);
		buffer.writeBlockPos(maxEffectPos);
		buffer.writeBlockPos(tileBlockPos);
	}
	
	public static AreaEffectPacket decode(PacketBuffer buffer)
	{
		BlockPos minEffectPos = buffer.readBlockPos();
		BlockPos maxEffectPos = buffer.readBlockPos();
		BlockPos tileBlockPos = buffer.readBlockPos();
		
		return new AreaEffectPacket(minEffectPos, maxEffectPos, tileBlockPos);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(player.level.isAreaLoaded(tileBlockPos, 0))
		{
			TileEntity te = player.level.getBlockEntity(tileBlockPos);
			if(te instanceof AreaEffectTileEntity)
			{
				if(Math.sqrt(player.distanceToSqr(tileBlockPos.getX() + 0.5, tileBlockPos.getY() + 0.5, tileBlockPos.getZ() + 0.5)) <= 8)
				{
					((AreaEffectTileEntity) te).setMinAndMaxEffectPos(minEffectPos, maxEffectPos);
					//Imitates the structure block to ensure that changes are sent client-side
					te.setChanged();
					BlockState state = player.level.getBlockState(tileBlockPos);
					player.level.sendBlockUpdated(tileBlockPos, state, state, 3);
				}
			}
		}
	}
}