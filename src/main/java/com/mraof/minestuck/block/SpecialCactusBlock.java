package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class SpecialCactusBlock extends CactusBlock
{
	private ToolType toolType;
	
	public SpecialCactusBlock(Properties properties, ToolType effectiveTool)
	{
		super(properties);
		this.toolType = effectiveTool;
	}
	
	@Nullable
	@Override
	public ToolType getHarvestTool(BlockState state)
	{
		return toolType;
	}
	
	@Override
	public int getHarvestLevel(BlockState state)
	{
		return 0;
	}
}