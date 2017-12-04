package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.EnumHelper;

public class ItemUraniumWeapon extends ItemPotionWeapon
{
	public static Item.ToolMaterial toolUranium = EnumHelper.addToolMaterial("URANIUM", 3, 1220, 12.0F, 6.0F, 15).setRepairItem(new ItemStack(MinestuckItems.rawUranium));
	
	public ItemUraniumWeapon(int maxUses, int damageVsEntity, double weaponSpeed, int enchantability, String name)
	{
		super(toolUranium, maxUses, damageVsEntity, weaponSpeed, enchantability, name, new PotionEffect(MobEffects.WITHER, 100, 1));
	}
	
	public ItemUraniumWeapon(int maxUses, int damageVsEntity, double weaponSpeed, int enchantability, String name, int radius, int terminus)
	{
		super(toolUranium, maxUses, damageVsEntity, weaponSpeed, enchantability, name, new PotionEffect(MobEffects.WITHER, 100, 1));
		setTerminus(radius, terminus);
	}
}