package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCane extends ItemWeapon
{
	private int weaponDamage;
	private final EnumCaneType caneType;
	
	public ItemCane(EnumCaneType caneType) 
	{
		super();
		this.caneType = caneType;
		this.setMaxDamage(caneType.getMaxUses());
		this.setUnlocalizedName(caneType.getName());
		this.weaponDamage = caneType.getDamageVsEntity();
	}
	
	@Override
	public int getAttackDamage() 
	{
		return weaponDamage;
	}
	
	@Override
	public int getItemEnchantability()
	{
		return this.caneType.getEnchantability();
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player)
	{
		itemStack.damageItem(1, player);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
}