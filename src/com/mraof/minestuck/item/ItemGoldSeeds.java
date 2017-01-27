package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.util.MinestuckAchievementHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemGoldSeeds extends ItemSeeds
{

	public ItemGoldSeeds()
	{
		super(MinestuckBlocks.blockGoldSeeds, Blocks.FARMLAND);
		setCreativeTab(Minestuck.tabMinestuck);
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return MinestuckBlocks.blockGoldSeeds.getUnlocalizedName();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getUnlocalizedName();
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		EnumActionResult result = super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		if(result == EnumActionResult.SUCCESS)
			player.addStat(MinestuckAchievementHandler.goldSeeds);
		return result;
	}
}