package com.mraof.minestuck.block;

import com.mraof.minestuck.MinestuckConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * To create vanilla ores with a different background texture
 */
public class BlockVanillaOre extends Block
{
	
	public enum OreType
	{
		COAL,
		IRON,
		GOLD,
		LAPIS,
		DIAMOND,
		EMERALD,
		QUARTZ,
		REDSTONE;
	}
	
	public final OreType oreType;
	
	public BlockVanillaOre(OreType type, Properties properties)
	{
		super(properties);
		oreType = type;
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
		switch(oreType)
		{
			case IRON: return 1;
			case GOLD: return 2;
			case LAPIS: return 1;
			case DIAMOND: return 2;
			case EMERALD: return 2;
			case REDSTONE: return 2;
			default: return 0;
		}
	}
	
	@Override
	public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune)
	{
		switch(oreType)
		{
			case COAL: return Items.COAL;
			case IRON: return MinestuckConfig.vanillaOreDrop ? Blocks.IRON_ORE : this;
			case GOLD: return MinestuckConfig.vanillaOreDrop ? Blocks.GOLD_ORE : this;
			case LAPIS: return Items.LAPIS_LAZULI;
			case DIAMOND: return Items.DIAMOND;
			case EMERALD: return Items.EMERALD;
			case QUARTZ: return Items.QUARTZ;
			case REDSTONE: return Items.REDSTONE;
			default: return this;
		}
	}
	
	@Override
	public int quantityDropped(IBlockState state, Random random)
	{
		return oreType == OreType.LAPIS ? 4 + random.nextInt(5) : oreType == OreType.REDSTONE ? 4 + random.nextInt(2) : 1;
	}
	
	@Override
	public int getItemsToDropCount(IBlockState state, int fortune, World worldIn, BlockPos pos, Random random)
	{
		if(oreType == OreType.REDSTONE)
		{
			return this.quantityDropped(state, random) + random.nextInt(fortune + 1);
		}
		else if(fortune > 0 && oreType != OreType.IRON && oreType != OreType.GOLD)
		{
			int j = random.nextInt(fortune + 2) - 1;
			
			if(j < 0)
				j = 0;
			
			return this.quantityDropped(state, random) * (j + 1);
		}
		else return this.quantityDropped(state, random);
	}
	
	@Override
	public int getExpDrop(IBlockState state, IWorldReader world, BlockPos pos, int fortune)
	{
		if(oreType != OreType.IRON && oreType != OreType.GOLD)
		{
			int j = 0;
			
			if(oreType == OreType.COAL)
				j = MathHelper.nextInt(RANDOM, 0, 2);
			else if(oreType == OreType.DIAMOND)
				j = MathHelper.nextInt(RANDOM, 3, 7);
			else if(oreType == OreType.EMERALD)
				j = MathHelper.nextInt(RANDOM, 3, 7);
			else if(oreType == OreType.LAPIS)
				j = MathHelper.nextInt(RANDOM, 2, 5);
			else if(oreType == OreType.QUARTZ)
				j = MathHelper.nextInt(RANDOM, 2, 5);
			else if(oreType == OreType.REDSTONE)
				j = MathHelper.nextInt(RANDOM, 1, 6);
			
			return j;
		}
		return 0;
	}
	
	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(this);
	}
	
	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		if(!MinestuckConfig.vanillaOreDrop)
			return super.getSilkTouchDrop(state);
		else switch(oreType)
		{
		case COAL: return new ItemStack(Blocks.COAL_ORE);
		case IRON: return new ItemStack(Blocks.IRON_ORE);
		case GOLD: return new ItemStack(Blocks.GOLD_ORE);
		case LAPIS: return new ItemStack(Blocks.LAPIS_ORE);
		case DIAMOND: return new ItemStack(Blocks.DIAMOND_ORE);
		case EMERALD: return new ItemStack(Blocks.EMERALD_ORE);
		case QUARTZ: return new ItemStack(Blocks.NETHER_QUARTZ_ORE);
		case REDSTONE: return new ItemStack(Blocks.REDSTONE_ORE);
		default: return new ItemStack(this);
		}
	}
}