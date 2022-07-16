package com.mraof.minestuck.network;

import com.mraof.minestuck.block.redstone.StructureCoreBlock;
import com.mraof.minestuck.tileentity.redstone.StructureCoreTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public class StructureCorePacket implements PlayToServerPacket
{
	private final StructureCoreTileEntity.ActionType actionType;
	private final int shutdownRange;
	private final BlockPos tileBlockPos;
	
	public StructureCorePacket(StructureCoreTileEntity.ActionType actionType, int shutdownRange, BlockPos tileBlockPos)
	{
		this.actionType = actionType;
		this.shutdownRange = shutdownRange;
		this.tileBlockPos = tileBlockPos;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeEnum(actionType);
		buffer.writeInt(shutdownRange);
		buffer.writeBlockPos(tileBlockPos);
	}
	
	public static StructureCorePacket decode(PacketBuffer buffer)
	{
		StructureCoreTileEntity.ActionType actionType = buffer.readEnum(StructureCoreTileEntity.ActionType.class);
		int summonRange = buffer.readInt();
		BlockPos tileBlockPos = buffer.readBlockPos();
		
		return new StructureCorePacket(actionType, summonRange, tileBlockPos);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(player.level.isAreaLoaded(tileBlockPos, 0))
		{
			TileEntity te = player.level.getBlockEntity(tileBlockPos);
			if(te instanceof StructureCoreTileEntity)
			{
				if(Math.sqrt(player.distanceToSqr(tileBlockPos.getX() + 0.5, tileBlockPos.getY() + 0.5, tileBlockPos.getZ() + 0.5)) <= 8)
				{
					((StructureCoreTileEntity) te).setActionType(actionType);
					((StructureCoreTileEntity) te).setShutdownRange(shutdownRange);
					((StructureCoreTileEntity) te).setHasWiped(false);
					//Imitates the structure block to ensure that changes are sent client-side
					te.setChanged();
					player.level.setBlock(tileBlockPos, te.getBlockState().setValue(StructureCoreBlock.POWERED, false), Constants.BlockFlags.DEFAULT);
					BlockState state = player.level.getBlockState(tileBlockPos);
					player.level.sendBlockUpdated(tileBlockPos, state, state, 3);
				}
			}
		}
	}
}