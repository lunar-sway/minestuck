package com.mraof.minestuck.block;

import com.mraof.minestuck.blockentity.MSHangingSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * Used to create modded sign block entities with custom textures.
 *
 * MSHangingSignBlock represents modded hanging signs, placed on the bottom of a block.
 */

public class MSHangingSignBlock extends CeilingHangingSignBlock
{
	public MSHangingSignBlock(Properties pProperties, WoodType pType)
	{
		super(pProperties, pType);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new MSHangingSignBlockEntity(pPos, pState);
	}
}
