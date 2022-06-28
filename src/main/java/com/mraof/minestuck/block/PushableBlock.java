package com.mraof.minestuck.block;

import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * Can be right clicked on one of its horizontal faces in order to push it in that direction, it will only destroy replaceables in its path.
 * It can be affected by gravity and can be lifted upwards by an item magnet
 */
public class PushableBlock extends FallingBlock
{
	protected PushableBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		Direction direction = hit.getDirection().getOpposite();
		if((direction.getAxis() == Direction.Axis.X || direction.getAxis() == Direction.Axis.Z) && isReplaceable(worldIn.getBlockState(pos.relative(direction))))
		{
			if(!worldIn.isClientSide)
			{
				worldIn.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundCategory.BLOCKS, 0.6F, 1.3F);
				worldIn.removeBlock(pos, false);
				worldIn.destroyBlock(pos.relative(direction), true);
				worldIn.setBlock(pos.relative(direction), state, Constants.BlockFlags.DEFAULT);
			}
			
			return ActionResultType.sidedSuccess(worldIn.isClientSide);
		}
		
		return ActionResultType.PASS;
	}
	
	@Override
	protected int getDelayAfterPlace()
	{
		return 0;
	}
	
	@Override
	public void onLand(World worldIn, BlockPos pos, BlockState fallingBlockState, BlockState replacedState, FallingBlockEntity fallingBlockEntity)
	{
		worldIn.playSound(null, pos, SoundEvents.GILDED_BLACKSTONE_STEP, SoundCategory.BLOCKS, 0.5F, 0.3F);
	}
	
	public static boolean isReplaceable(BlockState state)
	{
		return isFree(state) || MSTags.Blocks.PUSHABLE_BLOCK_REPLACABLE.contains(state.getBlock());
	}
}