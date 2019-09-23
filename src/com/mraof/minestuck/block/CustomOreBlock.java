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
			return MSBlocks.STONE_CRUXITE_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.NETHERRACK)
			return MSBlocks.NETHERRACK_CRUXITE_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.COBBLESTONE)
			return MSBlocks.COBBLESTONE_CRUXITE_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.SANDSTONE)
			return MSBlocks.SANDSTONE_CRUXITE_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.RED_SANDSTONE)
			return MSBlocks.RED_SANDSTONE_CRUXITE_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.END_STONE)
			return MSBlocks.END_STONE_CRUXITE_ORE.getDefaultState();
		else if(ground.getBlock() == MSBlocks.PINK_STONE)
			return MSBlocks.PINK_STONE_CRUXITE_ORE.getDefaultState();
		return MSBlocks.STONE_CRUXITE_ORE.getDefaultState();
	}
	
	public static BlockState getUraniumState(BlockState ground)
	{
		if(ground.getBlock() == Blocks.STONE)
			return MSBlocks.STONE_URANIUM_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.NETHERRACK)
			return MSBlocks.NETHERRACK_URANIUM_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.COBBLESTONE)
			return MSBlocks.COBBLESTONE_URANIUM_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.SANDSTONE)
			return MSBlocks.SANDSTONE_URANIUM_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.RED_SANDSTONE)
			return MSBlocks.RED_SANDSTONE_URANIUM_ORE.getDefaultState();
		else if(ground.getBlock() == Blocks.END_STONE)
			return MSBlocks.END_STONE_URANIUM_ORE.getDefaultState();
		else if(ground.getBlock() == MSBlocks.PINK_STONE)
			return MSBlocks.PINK_STONE_URANIUM_ORE.getDefaultState();
		return MSBlocks.STONE_URANIUM_ORE.getDefaultState();
	}
}