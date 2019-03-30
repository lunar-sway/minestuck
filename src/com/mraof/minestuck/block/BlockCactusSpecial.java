package com.mraof.minestuck.block;

import net.minecraft.block.BlockCactus;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BlockCactusSpecial extends BlockCactus
{
	private ToolType toolType;
	
	public BlockCactusSpecial(Properties properties, ToolType effectiveTool)
	{
		super(properties);
		this.toolType = effectiveTool;
	}
	
	@Nullable
	@Override
	public ToolType getHarvestTool(IBlockState state)
	{
		return toolType;
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		return 0;
	}
}