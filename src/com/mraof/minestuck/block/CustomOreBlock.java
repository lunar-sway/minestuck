package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

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
			return MinestuckBlocks.STONE_CRUXITE_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.NETHERRACK)
			return MinestuckBlocks.NETHERRACK_CRUXITE_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.COBBLESTONE)
			return MinestuckBlocks.COBBLESTONE_CRUXITE_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.SANDSTONE)
			return MinestuckBlocks.SANDSTONE_CRUXITE_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.RED_SANDSTONE)
			return MinestuckBlocks.RED_SANDSTONE_CRUXITE_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.END_STONE)
			return MinestuckBlocks.END_STONE_CRUXITE_ORE.getDefaultState();
		else if(ground.getBlock() == MinestuckBlocks.PINK_STONE)
			return MinestuckBlocks.PINK_STONE_CRUXITE_ORE.getDefaultState();
		return MinestuckBlocks.STONE_CRUXITE_ORE.getDefaultState();
	}
	
	public static BlockState getUraniumState(BlockState ground)
	{
		if(ground.getBlock() == Blocks.STONE)
			return MinestuckBlocks.STONE_URANIUM_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.NETHERRACK)
			return MinestuckBlocks.NETHERRACK_URANIUM_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.COBBLESTONE)
			return MinestuckBlocks.COBBLESTONE_URANIUM_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.SANDSTONE)
			return MinestuckBlocks.SANDSTONE_URANIUM_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.RED_SANDSTONE)
			return MinestuckBlocks.RED_SANDSTONE_URANIUM_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.END_STONE)
			return MinestuckBlocks.END_STONE_URANIUM_ORE.getDefaultState();
		else if(ground.getBlock() == MinestuckBlocks.PINK_STONE)
			return MinestuckBlocks.PINK_STONE_URANIUM_ORE.getDefaultState();
		return MinestuckBlocks.STONE_URANIUM_ORE.getDefaultState();
	}
}