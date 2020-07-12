package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created by mraof on 2017 January 18 at 7:15 PM.
 */
public class SordItem extends WeaponItem
{
	public static final String DROP_MESSAGE = "drop_message";
	
	public SordItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, MSToolType toolType, Properties builder)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker)
	{
		if(!attacker.getEntityWorld().isRemote && attacker.getRNG().nextFloat() < .25)
		{
			ItemEntity sord = new ItemEntity(attacker.world, attacker.posX, attacker.posY, attacker.posZ, itemStack.copy());
			sord.getItem().setCount(1);
			sord.setPickupDelay(40);
			attacker.world.addEntity(sord);
			itemStack.shrink(1);
			attacker.sendMessage(new TranslationTextComponent(getTranslationKey() + "." + DROP_MESSAGE));
		}
		return super.hitEntity(itemStack, target, attacker);
	}
}
