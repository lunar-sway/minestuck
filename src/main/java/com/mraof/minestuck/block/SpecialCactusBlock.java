package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class SpecialCactusBlock extends CactusBlock
{
	public SpecialCactusBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		for(Direction direction : Direction.Plane.HORIZONTAL)
		{
			BlockState blockstate = worldIn.getBlockState(pos.offset(direction));
			Material material = blockstate.getMaterial();
			if(material.isSolid() || worldIn.getFluidState(pos.offset(direction)).isTagged(FluidTags.LAVA))
			{
				return false;
			}
		}
		
		BlockState soil = worldIn.getBlockState(pos.down());
		return isSustainableSoil(soil) && !worldIn.getBlockState(pos.up()).getMaterial().isLiquid();
	}
	
	protected boolean isSustainableSoil(BlockState soil)
	{
		return soil.getBlock() == this || BlockTags.SAND.contains(soil.getBlock());
	}
}