package com.mraof.minestuck.block;

import com.mraof.minestuck.blockentity.MSSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * Used to create modded sign block entities with custom textures.
 *
 * MSStandingSignBlock represents modded standing signs, placed on the top of a block.
 */

public class MSStandingSignBlock extends StandingSignBlock
{
	public MSStandingSignBlock(Properties pProperties, WoodType pType)
	{
		super(pProperties, pType);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new MSSignBlockEntity(pPos, pState);
	}
}
