/**
 * 
 */
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

/**
 * @author mraof
 *
 */
public class ItemClub extends Item 
{
	private int weaponDamage;
	private final EnumClubType clubType;
    public float efficiencyOnProperMaterial = 4.0F;
    
	public ItemClub(int id, EnumClubType clubType) 
	{
		super(id);
		this.clubType = clubType;
		this.maxStackSize = 1;
		this.setMaxDamage(clubType.getMaxUses());
		this.setCreativeTab(Minestuck.tabMinestuck);
		switch(clubType)
		{
		case DEUCE:
			this.setUnlocalizedName("deuceClub");
//			this.setIconIndex(19);
			break;
		}
		this.weaponDamage = 2 + clubType.getDamageVsEntity();
	}

    
	public int getDamageVsEntity(Entity par1Entity) {
		return weaponDamage;
	}
	
	public int getItemEnchantability()
	{
		return this.clubType.getEnchantability();
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
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon("Minestuck:DeuceClub");
	}
}
