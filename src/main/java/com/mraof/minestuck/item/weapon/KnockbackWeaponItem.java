package com.mraof.minestuck.item.weapon;

import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;

public class KnockbackWeaponItem extends WeaponItem
{
	
	public KnockbackWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, MSToolType toolType, Properties builder, AttributeModifier modifierIn)
	{
		super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
		this.modifier = modifierIn;
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker)
	{
		if(!attacker.getEntityWorld().isRemote && attacker.getRNG().nextFloat() < .25)
		{
		
		}
		return super.hitEntity(itemStack, target, attacker);
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
	{
		modifier = stack.getAttributeModifiers(slot);
		return null;
	}
}
