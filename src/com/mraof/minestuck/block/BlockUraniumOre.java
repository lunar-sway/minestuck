package com.mraof.minestuck.block;

import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.MinestuckRandom;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockUraniumOre extends Block
{
	public BlockUraniumOre(Properties properties)
	{
		super(properties);
	}
	
	@Nullable
	@Override
	public ToolType getHarvestTool(IBlockState state)
	{
		return ToolType.PICKAXE;
	}
	
	@Override
	public int getHarvestLevel(IBlockState state)
	{
		return 1;
	}
	
	@Override
	public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune)
	{
		return MinestuckItems.RAW_URANIUM;
	}
	
	@Override
	public int quantityDropped(IBlockState state, Random random)
	{
		return 1;
	}
	
	@Override
	public int getItemsToDropCount(IBlockState state, int fortune, World worldIn, BlockPos pos, Random random)
	{
		if (fortune > 0)
		{
			int j = random.nextInt(fortune + 2) - 1;

			if (j < 0)
				j = 0;

			return this.quantityDropped(state, random) + (j + 1);
		}
		else return this.quantityDropped(state, random);
	}
	
	@Override
	public int getExpDrop(IBlockState state, IWorldReader world, BlockPos pos, int fortune)
	{
		return MathHelper.nextInt(RANDOM, 2, 5);
	}
	
	public static IBlockState getBlockState(IBlockState ground)
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