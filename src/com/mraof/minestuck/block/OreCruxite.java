package com.mraof.minestuck.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.lands.BlockWithMetadata;

public class OreCruxite extends Block 
{
//	public IIcon[] icons = new IIcon[4];
//	public IIcon[] otherIcons = new IIcon[2];
	
	private Random rand = new Random();
	public OreCruxite()
	{
		super(Material.rock);
		
		this.setUnlocalizedName("oreCruxite");
		setHardness(3.0F);
		this.setCreativeTab(Minestuck.tabMinestuck);
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
	
	public static int getMetadata(BlockWithMetadata ground)
	{
		if(ground.block == Blocks.stone)
			return 0;
		if(ground.block == Blocks.netherrack)
			return 1;
		if(ground.block == Blocks.obsidian)
			return 2;
		if(ground.block == Blocks.sandstone)
			return 3;
		
		return 0;
	}
	
}
