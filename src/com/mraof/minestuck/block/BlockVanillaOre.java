package com.mraof.minestuck.block;

import java.util.Random;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVanillaOre extends Block
{
	
	public static enum OreType
	{
		COAL,
		IRON,
		GOLD,
		LAPIS,
		DIAMOND,
		EMERALD,
		QUARTZ;
	}
	
	public final OreType oreType;
	
	public BlockVanillaOre(OreType type)	//For vanilla ores with a different background texture
	{
		super(Material.rock);
		oreType = type;
		setHardness(3.0F);
		setResistance(5.0F);	//Values normally used by ores
		this.setCreativeTab(Minestuck.tabMinestuck);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		switch(oreType)
		{
		case COAL: return Items.coal;
		case IRON: return Item.getItemFromBlock(this);
		case GOLD: return Item.getItemFromBlock(this);
		case LAPIS: return Items.dye;
		case DIAMOND: return Items.diamond;
		case EMERALD: return Items.emerald;
		case QUARTZ: return Items.quartz;
		default: return Item.getItemFromBlock(this);
		}
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return oreType == OreType.LAPIS ? 4 + random.nextInt(5) : 1;
	}
	
	@Override
	public int quantityDroppedWithBonus(int fortune, Random random)
	{
		if(fortune > 0 && oreType != OreType.IRON && oreType != OreType.GOLD)
		{
			int j = random.nextInt(fortune + 2) - 1;
			
			if(j < 0)
				j = 0;
			
			return this.quantityDropped(random) * (j + 1);
		}
		else return this.quantityDropped(random);
	}
	
	@Override
	public int getExpDrop(IBlockAccess world, BlockPos pos, int fortune)
	{
		IBlockState state = world.getBlockState(pos);
		Random rand = world instanceof World ? ((World)world).rand : new Random();
		if(oreType != OreType.IRON && oreType != OreType.GOLD)
		{
			int j = 0;
			
			if(oreType == OreType.COAL)
				j = MathHelper.getRandomIntegerInRange(rand, 0, 2);
			else if(oreType == OreType.DIAMOND)
				j = MathHelper.getRandomIntegerInRange(rand, 3, 7);
			else if(oreType == OreType.EMERALD)
				j = MathHelper.getRandomIntegerInRange(rand, 3, 7);
			else if(oreType == OreType.LAPIS)
				j = MathHelper.getRandomIntegerInRange(rand, 2, 5);
			else if(oreType == OreType.QUARTZ)
				j = MathHelper.getRandomIntegerInRange(rand, 2, 5);
			
			return j;
		}
		return 0;
	}
	
	@Override
	public int getDamageValue(World worldIn, BlockPos pos)
	{
		return 0;
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return oreType == OreType.LAPIS ? EnumDyeColor.BLUE.getDyeDamage() : 0;
	}
}