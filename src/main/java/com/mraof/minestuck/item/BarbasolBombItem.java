package com.mraof.minestuck.item;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.item.BarbasolBombEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class BarbasolBombItem extends Item
{
	public BarbasolBombItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack item = playerIn.getItemInHand(handIn);
		
		if(!playerIn.isCreative())
		{
			item.shrink(1);
		}
		
		worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.TNT_PRIMED, SoundCategory.NEUTRAL, 1.0F, 1.0F);
		
		if(!worldIn.isClientSide)
		{
			
			BarbasolBombEntity bomb = new BarbasolBombEntity(MSEntityTypes.BARBASOL_BOMB, playerIn, worldIn, playerIn.abilities.mayBuild);
			bomb.setItem(item);
			bomb.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, -20.0F, 0.7F, 1.0F);
			worldIn.addFreshEntity(bomb);
		}
		
		playerIn.awardStat(Stats.ITEM_USED.get(this));
		return ActionResult.success(item);
	}
}
