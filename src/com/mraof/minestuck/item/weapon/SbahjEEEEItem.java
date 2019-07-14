package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class SbahjEEEEItem extends PogoWeaponItem
{
	
	public SbahjEEEEItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, double pogoMotion, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, pogoMotion, builder);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		player.world.playSound(null, player.posX, player.posY, player.posZ, MinestuckSoundHandler.soundScreech, SoundCategory.AMBIENT, 1.5F, 1.0F);
		return super.hitEntity(stack, target, player);
	}
}