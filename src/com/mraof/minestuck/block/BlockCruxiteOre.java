package com.mraof.minestuck.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MinestuckItems;

public class BlockCruxiteOre extends Block 
{
	public static final PropertyInteger BLOCK_TYPE = PropertyInteger.create("block_type", 0, 4);
	
	private Random rand = new Random();
	public BlockCruxiteOre()
	{
		super(Material.rock);
		
		this.setUnlocalizedName("oreCruxite");
		setHardness(3.0F);
		setResistance(5.0F);	//Values normally used by ores
		setHarvestLevel("pickaxe", 0);
		setDefaultState(getBlockState(Blocks.stone.getDefaultState()));
		this.setCreativeTab(Minestuck.tabMinestuck);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BLOCK_TYPE, meta % BLOCK_TYPE.getAllowedValues().size());
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (Integer) state.getValue(BLOCK_TYPE);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return MinestuckItems.rawCruxite;
	}

	public int quantityDropped(Random random)
	{
		return 2 + random.nextInt(4);
	}

	public int quantityDroppedWithBonus(int par1, Random par2Random)
	{
		if (par1 > 0)
		{
			int j = par2Random.nextInt(par1 + 2) - 1;

			if (j < 0)
			{
				j = 0;
			}

			return this.quantityDropped(par2Random) * (j + 1);
		}
		else
		{
			return this.quantityDropped(par2Random);
		}
	}
	
	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		return MathHelper.getRandomIntegerInRange(rand, 2, 5);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BLOCK_TYPE);
	}
	
	public IBlockState getBlockState(IBlockState ground)
	{
		int meta = 0;
		if(ground.getBlock() == Blocks.stone)
			meta = 0;
		else if(ground.getBlock() == Blocks.netherrack)
			meta = 1;
		else if(ground.getBlock() == Blocks.cobblestone)
			meta = 2;
		else if(ground.getBlock() == Blocks.sandstone)
			meta = 3;
		else if(ground.getBlock() == Blocks.red_sandstone)
			meta = 4;
		
		return getBlockState().getBaseState().withProperty(BLOCK_TYPE, meta);
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		for(int i = 0; i < 5; i++)
			list.add(new ItemStack(itemIn, 1, i));
	}
	
}
