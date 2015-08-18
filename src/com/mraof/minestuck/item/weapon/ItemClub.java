/**
 * 
 */
package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author mraof
 *
 */
public class ItemClub extends ItemWeapon
{
	private final EnumClubType clubType;
	
	public ItemClub(EnumClubType clubType) 
	{
		super();
		this.clubType = clubType;
		this.setMaxDamage(clubType.getMaxUses());
		this.setUnlocalizedName(clubType.getName());
		this.weaponDamage = clubType.getDamageVsEntity();
	}
	
	@Override
	public int getAttackDamage() 
	{
		return weaponDamage;
	}
	
	@Override
	public int getItemEnchantability()
	{
		return this.clubType.getEnchantability();
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