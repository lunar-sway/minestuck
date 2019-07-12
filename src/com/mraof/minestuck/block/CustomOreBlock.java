package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;

public class CustomOreBlock extends OreBlock
{
	private final int minExp, maxExp;
	
	public CustomOreBlock(Properties properties)
	{
		this(0, 0, properties);
	}
	
	public CustomOreBlock(int minExp, int maxExp, Properties properties)
	{
		super(properties);
		this.minExp = minExp;
		this.maxExp = maxExp;
	}
	
	@Override
	protected int func_220281_a(Random random)
	{
		return MathHelper.nextInt(random, minExp, maxExp);
	}
	
	public static BlockState getCruxiteState(BlockState ground)
	{
		if(ground.getBlock() == Blocks.STONE)
			return MinestuckBlocks.CRUXITE_ORE_STONE.getDefaultState();
		else if(ground.getBlock() == Blocks.NETHERRACK)
			return MinestuckBlocks.CRUXITE_ORE_NETHERRACK.getDefaultState();
		else if(ground.getBlock() == Blocks.COBBLESTONE)
			return MinestuckBlocks.CRUXITE_ORE_COBBLESTONE.getDefaultState();
		else if(ground.getBlock() == Blocks.SANDSTONE)
			return MinestuckBlocks.CRUXITE_ORE_SANDSTONE.getDefaultState();
		else if(ground.getBlock() == Blocks.RED_SANDSTONE)
			return MinestuckBlocks.CRUXITE_ORE_RED_SANDSTONE.getDefaultState();
		else if(ground.getBlock() == Blocks.END_STONE)
			return MinestuckBlocks.CRUXITE_ORE_END_STONE.getDefaultState();
		else if(ground.getBlock() == MinestuckBlocks.PINK_STONE)
			return MinestuckBlocks.CRUXITE_ORE_PINK_STONE.getDefaultState();
		return MinestuckBlocks.CRUXITE_ORE_STONE.getDefaultState();
	}
	
	public static BlockState getUraniumState(BlockState ground)
	{
		if(ground.getBlock() == Blocks.STONE)
			return MinestuckBlocks.URANIUM_ORE_STONE.getDefaultState();
		else if(ground.getBlock() == Blocks.NETHERRACK)
			return MinestuckBlocks.URANIUM_ORE_NETHERRACK.getDefaultState();
		else if(ground.getBlock() == Blocks.COBBLESTONE)
			return MinestuckBlocks.URANIUM_ORE_COBBLESTONE.getDefaultState();
		else if(ground.getBlock() == Blocks.SANDSTONE)
			return MinestuckBlocks.URANIUM_ORE_SANDSTONE.getDefaultState();
		else if(ground.getBlock() == Blocks.RED_SANDSTONE)
			return MinestuckBlocks.URANIUM_ORE_RED_SANDSTONE.getDefaultState();
		else if(ground.getBlock() == Blocks.END_STONE)
			return MinestuckBlocks.URANIUM_ORE_END_STONE.getDefaultState();
		else if(ground.getBlock() == MinestuckBlocks.PINK_STONE)
			return MinestuckBlocks.URANIUM_ORE_PINK_STONE.getDefaultState();
		return MinestuckBlocks.URANIUM_ORE_STONE.getDefaultState();
	}
}