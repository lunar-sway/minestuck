package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

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
		if(!worldIn.isClientSide && !player.isCrouching() && player.getItemInHand(hand).isEmpty())
		{
			Direction direction = hit.getDirection().getOpposite();
			if((direction.getAxis() == Direction.Axis.X || direction.getAxis() == Direction.Axis.Z) && isReplaceable(worldIn.getBlockState(pos.relative(direction))))
			{
				worldIn.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundCategory.BLOCKS, 0.6F, 1.3F);
				worldIn.removeBlock(pos, false);
				worldIn.destroyBlock(pos.relative(direction), true);
				worldIn.setBlock(pos.relative(direction), state, 2);
				return ActionResultType.SUCCESS;
			}
		}
		
		return ActionResultType.PASS;
	}
	
	@Override
	public void onLand(World worldIn, BlockPos pos, BlockState fallingBlockState, BlockState replacedState, FallingBlockEntity fallingBlockEntity)
	{
		if(worldIn.isClientSide)
			replacedState.addLandingEffects((ServerWorld) worldIn, pos, fallingBlockState, null, 20); //TODO does not work
		worldIn.playSound(null, pos, SoundEvents.GILDED_BLACKSTONE_STEP, SoundCategory.BLOCKS, 0.5F, 0.3F);
	}
	
	public static boolean isReplaceable(BlockState state)
	{
		Material material = state.getMaterial();
		return state.isAir() || state.is(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
	}
}