package com.mraof.minestuck.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCane extends ItemWeapon
{
	private int weaponDamage;
	private final EnumCaneType caneType;
    public float efficiencyOnProperMaterial = 4.0F;
    
	public ItemCane(EnumCaneType caneType) 
	{
		super();
		this.caneType = caneType;
		this.setMaxDamage(caneType.getMaxUses());
		switch(caneType)
		{
		case CANE:
			this.setUnlocalizedName("cane");
//			this.setIconIndex(20);
			break;
		case SPEAR:
			this.setUnlocalizedName("spearCane");
//			this.setIconIndex(21);
			break;
		case DRAGON:
			this.setUnlocalizedName("dragonCane");
//			this.setIconIndex(22);
			break;
		}
		this.weaponDamage = 2 + caneType.getDamageVsEntity();
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
		if(this.caneType == EnumCaneType.CANE)
		{
			System.out.println(target.getClass());
		}
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
		switch(caneType)
		{
		case CANE:
			itemIcon = iconRegister.registerIcon("minestuck:Cane");
			break;
		case SPEAR:
			itemIcon = iconRegister.registerIcon("minestuck:SpearCane");
			break;
		case DRAGON:
			itemIcon = iconRegister.registerIcon("minestuck:DragonCane");
			break;
		}
	}
}
