package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

/**
 * Can be right clicked on one of its horizontal faces in order to push it in that direction, it will only destroy replaceables in its path.
 * It can be affected by gravity and can be lifted upwards by an item magnet
 */
public class PortableBlock extends FallingBlock
{
	protected PortableBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(!player.isCrouching())
		{
			Direction direction = hit.getDirection().getOpposite();
			if((direction.getAxis() == Direction.Axis.X || direction.getAxis() == Direction.Axis.Z) && worldIn.getBlockState(pos.relative(direction)).isAir()/*.canBeReplaced(context)*/)
			{
				worldIn.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundCategory.BLOCKS, 0.5F, 1.6F);
				//worldIn.playSound(null, pos, SoundEvents.GILDED_BLACKSTONE_STEP, SoundCategory.BLOCKS, 0.5F, 0.2F);
				worldIn.removeBlock(pos, false);
				worldIn.setBlock(pos.relative(direction), state, 2);
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.PASS;
	}
}