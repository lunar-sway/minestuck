package com.mraof.minestuck.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.mraof.minestuck.Minestuck;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSickle extends ItemWeapon
{
	private int weaponDamage;
	private final EnumSickleType sickleType;
    public float efficiencyOnProperMaterial = 4.0F;
    
    public ItemSickle(EnumSickleType sickleType)
	{
		super();
		
		this.sickleType = sickleType;
		this.maxStackSize = 1;
		this.setMaxDamage(sickleType.getMaxUses());
		this.setCreativeTab(Minestuck.tabMinestuck);
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
		this.weaponDamage = 4 + sickleType.getDamageVsEntity();
	}
    
    @Override
	public int getAttackDamage() 
    {
		return weaponDamage;
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
	public boolean onBlockDestroyed(ItemStack itemStack, World world, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLiving)
	{
		if ((double)par3.getBlockHardness(world, par4, par5, par6) != 0.0D)
		{
			itemStack.damageItem(2, par7EntityLiving);
		}
		
		return true;
	}
    
    @Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) 
	{
		switch(sickleType)
		{
		case SICKLE:
			itemIcon = iconRegister.registerIcon("minestuck:Sickle");
			break;
		case HOMES:
			itemIcon = iconRegister.registerIcon("minestuck:HomesSmellYaLater");
			break;
		case REGISICKLE:
			itemIcon = iconRegister.registerIcon("minestuck:Regisickle");
			break;
		case CLAW:
			itemIcon = iconRegister.registerIcon("minestuck:ClawSickle");
			break;
		}
	}
}
