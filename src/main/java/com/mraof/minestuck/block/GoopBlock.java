package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class GoopBlock extends SlimeBlock
{

	public GoopBlock(Properties properties)
	{
		super(properties);
	}
	
	@Nullable
	@Override
	public ToolType getHarvestTool(BlockState state)
	{
		return ToolType.SHOVEL;
	}
	
	@Override
	public int getLightBlock(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		if (state.isSolidRender(worldIn, pos)) {
			return worldIn.getMaxLightLevel();
		} else {
			return state.propagatesSkylightDown(worldIn, pos) ? 0 : 1;
		}
	}
}