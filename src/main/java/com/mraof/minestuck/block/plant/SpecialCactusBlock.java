package com.mraof.minestuck.block.plant;

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
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		for(Direction direction : Direction.Plane.HORIZONTAL)
		{
			BlockState blockstate = worldIn.getBlockState(pos.relative(direction));
			Material material = blockstate.getMaterial();
			if(material.isSolid() || worldIn.getFluidState(pos.relative(direction)).is(FluidTags.LAVA))
			{
				return false;
			}
		}
		
		BlockState soil = worldIn.getBlockState(pos.below());
		return isSustainableSoil(soil) && !worldIn.getBlockState(pos.above()).getMaterial().isLiquid();
	}
	
	protected boolean isSustainableSoil(BlockState soil)
	{
		return soil.getBlock() == this || BlockTags.SAND.contains(soil.getBlock());
	}
}