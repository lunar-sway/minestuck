package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class SbahjEEEEItem extends PogoWeaponItem
{
	
	public SbahjEEEEItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, double pogoMotion, MSToolType toolType, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, pogoMotion, toolType, builder);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity player)
	{
		player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), MSSoundEvents.ITEM_EEEEEEEEEEEE_HIT, SoundCategory.AMBIENT, 1.5F, 1.0F);
		return super.hitEntity(stack, target, player);
	}
}