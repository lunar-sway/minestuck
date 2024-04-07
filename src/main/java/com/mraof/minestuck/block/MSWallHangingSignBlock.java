package com.mraof.minestuck.block;

import com.mraof.minestuck.blockentity.MSHangingSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class MSWallHangingSignBlock extends WallHangingSignBlock
{
	public MSWallHangingSignBlock(Properties pProperties, WoodType pType)
	{
		super(pProperties, pType);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new MSHangingSignBlockEntity(pPos, pState);
	}
}
