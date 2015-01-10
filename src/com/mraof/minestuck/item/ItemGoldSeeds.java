package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MinestuckAchievementHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemGoldSeeds extends ItemSeeds
{

	public ItemGoldSeeds()
	{
		super(Minestuck.blockGoldSeeds, Blocks.farmland);
		setCreativeTab(Minestuck.tabMinestuck);
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return Minestuck.blockGoldSeeds.getUnlocalizedName();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getUnlocalizedName();
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		boolean b = super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
		if(b)
			playerIn.triggerAchievement(MinestuckAchievementHandler.goldSeeds);
		return b;
	}
	
}
