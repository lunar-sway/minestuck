package com.mraof.minestuck.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

import com.mraof.minestuck.Minestuck;

public class OreCruxite extends Block 
{
	private Random rand = new Random();
	public OreCruxite()
	{
		super(Material.rock);
		
		this.setBlockName("oreCruxite");
		setHardness(3.0F);
		this.setCreativeTab(Minestuck.tabMinestuck);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon("minestuck:CruxiteOre");
	}
	
	@Override
	public Item getItemDropped(int p_149650_1_, Random random, int p_149650_3_) {
		return Minestuck.rawCruxite;
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
	public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
		return MathHelper.getRandomIntegerInRange(rand, 2, 5);
	}

}
