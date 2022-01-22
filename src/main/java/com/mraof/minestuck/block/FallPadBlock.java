package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	public void fallOn(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		if(entityIn.isSuppressingBounce())
		{
			super.fallOn(worldIn, pos, entityIn, fallDistance);
		} else
		{
			entityIn.causeFallDamage(fallDistance, 0.0F);
		}
	}
}