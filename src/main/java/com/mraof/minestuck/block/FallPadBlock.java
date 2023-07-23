package com.mraof.minestuck.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Prevents fall damage for entities who land on the block as long as they do not crouch when they land
 */
public class FallPadBlock extends Block
{
	protected FallPadBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entityIn, float fallDistance)
	{
		if(entityIn.isSuppressingBounce())
		{
			super.fallOn(level, state, pos, entityIn, fallDistance);
		} else
		{
			entityIn.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall());
		}
	}
}