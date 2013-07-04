package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCane extends Item 
{
	private int weaponDamage;
	private final EnumCaneType caneType;
    public float efficiencyOnProperMaterial = 4.0F;
    
	public ItemCane(int id, EnumCaneType caneType) 
	{
		super(id);
		this.caneType = caneType;
		this.maxStackSize = 1;
		this.setMaxDamage(caneType.getMaxUses());
		this.setCreativeTab(Minestuck.tabMinestuck);
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

    
	public int getDamageVsEntity(Entity par1Entity) {
		return weaponDamage;
	}
	
	public int getItemEnchantability()
	{
		return this.caneType.getEnchantability();
	}
	 
	public boolean hitEntity(ItemStack itemStack, EntityLiving target, EntityLiving player)
	{
		itemStack.damageItem(1, player);
		return true;
	}
	
	public boolean onBlockDestroyed(ItemStack itemStack, World world, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving)
	{
		if ((double)Block.blocksList[par3].getBlockHardness(world, par4, par5, par6) != 0.0D)
		{
			itemStack.damageItem(2, par7EntityLiving);
		}
		
		return true;
	}
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) 
	{
		switch(caneType)
		{
		case CANE:
			itemIcon = iconRegister.registerIcon("Minestuck:Cane");
			break;
		case SPEAR:
			itemIcon = iconRegister.registerIcon("Minestuck:SpearCane");
			break;
		case DRAGON:
			itemIcon = iconRegister.registerIcon("Minestuck:DragonCane");
			break;
		}
	}
}
