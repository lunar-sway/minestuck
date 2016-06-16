package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;

public class ItemSickle extends ItemWeapon
{
	private final EnumSickleType sickleType;
	
	public ItemSickle(EnumSickleType sickleType)
	{
		super();
		
		this.sickleType = sickleType;
		this.maxStackSize = 1;
		this.setMaxDamage(sickleType.getMaxUses());
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName(sickleType.getName());
		switch(sickleType)
		{
		case SICKLE:
			this.setUnlocalizedName("sickle");
			break;
		case HOMES:
			this.setUnlocalizedName("homesSmellYaLater");
			break;
		case REGISICKLE:
			this.setUnlocalizedName("regiSickle");
			break;
		case CLAW:
			this.setUnlocalizedName("clawSickle");
			break;
		}
		this.weaponDamage = sickleType.getDamageVsEntity();
		this.weaponSpeed = sickleType.getAttackSpeed();
	}
	
	@Override
	public int getItemEnchantability()
	{
		return this.sickleType.getEnchantability();
	}
	
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker)
	{
		itemStack.damageItem(1, attacker);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
}