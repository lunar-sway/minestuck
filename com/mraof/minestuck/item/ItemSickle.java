package com.mraof.minestuck.item;

import com.mraof.minestuck.CommonProxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSickle extends Item
{
	private int weaponDamage;
	private final EnumSickleType sickleType;
    public float efficiencyOnProperMaterial = 4.0F;
    
    public ItemSickle(int id, EnumSickleType sickleType)
	{
		super(id);
		
		this.sickleType = sickleType;
		this.maxStackSize = 1;
		this.setMaxDamage(sickleType.getMaxUses());
		this.setCreativeTab(CreativeTabs.tabCombat);
		switch(sickleType)
		{
		case SICKLE:
			this.setUnlocalizedName("sickle");
//			this.setIconIndex(15);
			break;
		case HOMES:
			this.setUnlocalizedName("homesSmellYaLater");
//			this.setIconIndex(16);
			break;
		case REGISICKLE:
			this.setUnlocalizedName("regiSickle");
//			this.setIconIndex(17);
			break;
		case CLAW:
			this.setUnlocalizedName("clawSickle");
//			this.setIconIndex(18);
			break;
		}
		this.weaponDamage = 4 + sickleType.getDamageVsEntity();
	}
    

	public int getDamageVsEntity(Entity par1Entity) {
		return weaponDamage;
	}
	
	public int getItemEnchantability()
	{
		return this.sickleType.getEnchantability();
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
		switch(sickleType)
		{
		case SICKLE:
			itemIcon = iconRegister.registerIcon("Minestuck:sickle");
			break;
		case HOMES:
			itemIcon = iconRegister.registerIcon("Minestuck:homessmellyalater");
			break;
		case REGISICKLE:
			itemIcon = iconRegister.registerIcon("Minestuck:regisickle");
			break;
		case CLAW:
			itemIcon = iconRegister.registerIcon("Minestuck:clawsickle");
			break;
		}
	}
}
