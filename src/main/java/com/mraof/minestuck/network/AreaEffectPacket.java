package com.mraof.minestuck.network;

import com.mraof.minestuck.tileentity.redstone.AreaEffectTileEntity;
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
	public void execute(ServerPlayerEntity player) //TODO in an unknown set of conditions, the position changes seem to only take effect(from the perspective of being applied to entities) upon reloading after the change is made
	{
		TileEntity te = player.level.getBlockEntity(tileBlockPos);
		if(te instanceof AreaEffectTileEntity)
		{
			((AreaEffectTileEntity) te).setMinEffectPos(minEffectPos);
			((AreaEffectTileEntity) te).setMaxEffectPos(maxEffectPos);
		}
	}
}