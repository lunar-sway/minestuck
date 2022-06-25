package com.mraof.minestuck.block;

import com.mraof.minestuck.util.CustomVoxelShape;
import com.mraof.minestuck.util.Debug;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * An extension of CustomShapeBlock that makes players less likely to take damage while traveling down the stairs at a walking pace
 */
public class SteepStairsBlock extends CustomShapeBlock
{
	//TODO when falling down these, players take damage without triggering fallOn here. This includes when there is only an air block underneath them.
	// Remove this class before the end of the PR if no further ideas on how to fix it can be found
	
	public SteepStairsBlock(Properties properties, CustomVoxelShape shape)
	{
		super(properties, shape);
	}
	
	@Override
	public void fallOn(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		//Debug.debugf("%s", fallDistance);
		if(fallDistance > 6)
			super.fallOn(worldIn, pos, entityIn, fallDistance);
		else
			entityIn.causeFallDamage(0, 0.0F);
	}
}
