package com.mraof.minestuck.network;

import com.mraof.minestuck.block.redstone.StructureCoreBlock;
import com.mraof.minestuck.blockentity.redstone.StructureCoreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class StructureCorePacket implements MSPacket.PlayToServer
{
	private final StructureCoreBlockEntity.ActionType actionType;
	private final int shutdownRange;
	private final BlockPos beBlockPos;
	
	public StructureCorePacket(StructureCoreBlockEntity.ActionType actionType, int shutdownRange, BlockPos beBlockPos)
	{
		this.actionType = actionType;
		this.shutdownRange = shutdownRange;
		this.beBlockPos = beBlockPos;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeEnum(actionType);
		buffer.writeInt(shutdownRange);
		buffer.writeBlockPos(beBlockPos);
	}
	
	public static StructureCorePacket decode(FriendlyByteBuf buffer)
	{
		StructureCoreBlockEntity.ActionType actionType = buffer.readEnum(StructureCoreBlockEntity.ActionType.class);
		int summonRange = buffer.readInt();
		BlockPos beBlockPos = buffer.readBlockPos();
		
		return new StructureCorePacket(actionType, summonRange, beBlockPos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.level().isAreaLoaded(beBlockPos, 0))
		{
			if(player.level().getBlockEntity(beBlockPos) instanceof StructureCoreBlockEntity structureCore)
			{
				if(Math.sqrt(player.distanceToSqr(beBlockPos.getX() + 0.5, beBlockPos.getY() + 0.5, beBlockPos.getZ() + 0.5)) <= 8)
				{
					structureCore.setActionType(actionType);
					structureCore.setShutdownRange(shutdownRange);
					structureCore.setHasWiped(false);
					//Imitates the structure block to ensure that changes are sent client-side
					structureCore.setChanged();
					player.level().setBlock(beBlockPos, structureCore.getBlockState().setValue(StructureCoreBlock.POWERED, false), Block.UPDATE_ALL);
					BlockState state = player.level().getBlockState(beBlockPos);
					player.level().sendBlockUpdated(beBlockPos, state, state, 3);
				}
			}
		}
	}
}