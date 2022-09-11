package com.mraof.minestuck.network;

import com.mraof.minestuck.block.redstone.StructureCoreBlock;
import com.mraof.minestuck.blockentity.redstone.StructureCoreTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

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
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeEnum(actionType);
		buffer.writeInt(shutdownRange);
		buffer.writeBlockPos(tileBlockPos);
	}
	
	public static StructureCorePacket decode(FriendlyByteBuf buffer)
	{
		StructureCoreTileEntity.ActionType actionType = buffer.readEnum(StructureCoreTileEntity.ActionType.class);
		int summonRange = buffer.readInt();
		BlockPos tileBlockPos = buffer.readBlockPos();
		
		return new StructureCorePacket(actionType, summonRange, tileBlockPos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.level.isAreaLoaded(tileBlockPos, 0))
		{
			if(player.level.getBlockEntity(tileBlockPos) instanceof StructureCoreTileEntity structureCore)
			{
				if(Math.sqrt(player.distanceToSqr(tileBlockPos.getX() + 0.5, tileBlockPos.getY() + 0.5, tileBlockPos.getZ() + 0.5)) <= 8)
				{
					structureCore.setActionType(actionType);
					structureCore.setShutdownRange(shutdownRange);
					structureCore.setHasWiped(false);
					//Imitates the structure block to ensure that changes are sent client-side
					structureCore.setChanged();
					player.level.setBlock(tileBlockPos, structureCore.getBlockState().setValue(StructureCoreBlock.POWERED, false), Block.UPDATE_ALL);
					BlockState state = player.level.getBlockState(tileBlockPos);
					player.level.sendBlockUpdated(tileBlockPos, state, state, 3);
				}
			}
		}
	}
}