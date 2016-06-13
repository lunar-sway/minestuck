package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.Minestuck;

public abstract class ItemWeapon extends Item
{
	protected double weaponDamage, weaponSpeed;
	
	public ItemWeapon() 
	{
		this.maxStackSize = 1;
		this.setCreativeTab(Minestuck.tabMinestuck);
	}
	
	protected double getAttackDamage(ItemStack stack)
	{
		return weaponDamage;
	}
	
	protected double getAttackSpeed(ItemStack stack)
	{
		return weaponSpeed;
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap multimap = HashMultimap.create();
		if(slot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", getAttackDamage(stack), 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", getAttackSpeed(stack), 0));
		}
		return multimap;
	}
	
}