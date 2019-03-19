package com.mraof.minestuck.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BlockGoop extends BlockSlime
{

	public BlockGoop(Properties properties)
	{
		super(properties);
	}
	
	@Nullable
	@Override
	public ToolType getHarvestTool(IBlockState state)
	{
		return ToolType.SHOVEL;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.SOLID;
	}
	
	@Override
	public int getOpacity(IBlockState state, IBlockReader worldIn, BlockPos pos)
	{
		if (state.isOpaqueCube(worldIn, pos)) {
			return worldIn.getMaxLightLevel();
		} else {
			return state.propagatesSkylightDown(worldIn, pos) ? 0 : 1;
		}
	}
}