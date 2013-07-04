package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlade extends Item{
	private int weaponDamage;
	private final EnumBladeType bladeType;
    public float efficiencyOnProperMaterial = 4.0F;
    
    public ItemBlade(int id, EnumBladeType bladeType)
	{
		super(id);
		
		this.bladeType = bladeType;
		this.maxStackSize = 1;
		this.setMaxDamage(bladeType.getMaxUses());
		this.setCreativeTab(Minestuck.tabMinestuck);
		switch(bladeType)
		{
		case SORD:
			this.setUnlocalizedName("sord");
//			this.setIconIndex(8);
			break;
		case NINJA:
			this.setUnlocalizedName("ninjaSword");
//			this.setIconIndex(9);
			break;
		case KATANA:
			this.setUnlocalizedName("katana");
//			this.setIconIndex(10);
			break;
		case CALEDSCRATCH:
			this.setUnlocalizedName("caledscratch");
//			this.setIconIndex(10);
			break;
		case DERINGER:
			this.setUnlocalizedName("royalDeringer");
//			this.setIconIndex(11);
			break	;
		case REGISWORD:
			this.setUnlocalizedName("regisword");
//			this.setIconIndex(12);
			break;
		case SCARLET:
			this.setUnlocalizedName("scarletRibbitar");
//			this.setIconIndex(13);
			break;
		case DOGG:
			this.setUnlocalizedName("doggMachete");
//			this.setIconIndex(14);
		}
		this.weaponDamage = 4 + bladeType.getDamageVsEntity();
	}

    
	public int getDamageVsEntity(Entity par1Entity) {
		return weaponDamage;
	}
	
	public int getItemEnchantability()
	{
		return this.bladeType.getEnchantability();
	}
	 
	public boolean hitEntity(ItemStack itemStack, EntityLiving target, EntityLiving attacker)
	{
		itemStack.damageItem(1, attacker);
		if(bladeType.equals(bladeType.SORD) && Math.random() < .25)
		{
			EntityItem sord = new EntityItem(attacker.worldObj, attacker.posX, attacker.posY, attacker.posZ, itemStack);
			attacker.worldObj.spawnEntityInWorld(sord);
			itemStack.stackSize--;
		}
		
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
		switch(bladeType)
		{
		case SORD:
			itemIcon = iconRegister.registerIcon("Minestuck:Sord");
			break;
		case NINJA:
		case KATANA:
			itemIcon = iconRegister.registerIcon("Minestuck:Katana");
			break;
		case CALEDSCRATCH:
			itemIcon = iconRegister.registerIcon("Minestuck:Caledscratch");
			break;
		case DERINGER:
			itemIcon = iconRegister.registerIcon("Minestuck:RoyalDeringer");
			break	;
		case REGISWORD:
			itemIcon = iconRegister.registerIcon("Minestuck:Regisword");
			break;
		case SCARLET:
			itemIcon = iconRegister.registerIcon("Minestuck:ScarletRibbitar");
			break;
		case DOGG:
			itemIcon = iconRegister.registerIcon("Minestuck:SnoopDoggMachete");
		}
	}
	
}
