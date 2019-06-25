package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockDirt extends Block
{
	public BlockDirt(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing facing, IPlantable plantable)
	{
		if(plantable.getPlantType(world, pos.offset(facing)) == EnumPlantType.Plains)
			return true;
		return super.canSustainPlant(state, world, pos, facing, plantable);
	}
}
