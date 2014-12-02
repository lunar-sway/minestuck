package com.mraof.minestuck.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import com.mraof.minestuck.Minestuck;

public class OreCruxite extends Block 
{
	public static final PropertyInteger BLOCK_TYPE = PropertyInteger.create("blockType", 0, 3);
	
	private Random rand = new Random();
	public OreCruxite()
	{
		super(Material.rock);
		
		this.setUnlocalizedName("oreCruxite");
		setHardness(3.0F);
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
	
//	@Override
//	public void registerBlockIcons(IIconRegister iconRegister)
//	{
//		this.blockIcon = iconRegister.registerIcon("minestuck:CruxiteStone");
//		icons[0] = blockIcon;
//		icons[1] = iconRegister.registerIcon("minestuck:CruxiteNetherrack");
//		icons[2] = iconRegister.registerIcon("minestuck:CruxiteObsidian");
//		icons[3] = iconRegister.registerIcon("minestuck:CruxiteSandstoneSide");
//		
//		otherIcons[0] = iconRegister.registerIcon("minestuck:CruxiteSandstoneBottom");
//		otherIcons[1] = iconRegister.registerIcon("minestuck:CruxiteSandstoneTop");
//	}
	
//	@Override
//	public IIcon getIcon(int side, int meta)
//	{
//		if(meta == 3 && side < 2)
//			return otherIcons[side];
//		return icons[meta];
//	}
	
//	@Override
//	public Item getItemDropped(int p_149650_1_, Random random, int p_149650_3_) {
//		return Minestuck.rawCruxite;
//	}

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
//	@Override
//	public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
//		return MathHelper.getRandomIntegerInRange(rand, 2, 5);
//	}
	
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, BLOCK_TYPE);
	}
	
	public IBlockState getBlockState(IBlockState ground)
	{
		int meta = 0;
		if(ground.getBlock() == Blocks.stone)
			meta = 0;
		else if(ground.getBlock() == Blocks.netherrack)
			meta = 1;
		else if(ground.getBlock() == Blocks.obsidian)
			meta = 2;
		else if(ground.getBlock() == Blocks.sandstone)
			meta = 3;
		
		return getBlockState().getBaseState().withProperty(BLOCK_TYPE, meta);
	}
	
}
