package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlade extends ItemWeapon
{
	private int weaponDamage;
	private final EnumBladeType bladeType;
	public float efficiencyOnProperMaterial = 4.0F;
	
	public ItemBlade(EnumBladeType bladeType)
	{
		super();
		
		setCreativeTab(Minestuck.tabMinestuck);
		this.bladeType = bladeType;
		this.setMaxDamage(bladeType.getMaxUses());
		switch(bladeType)
		{
		case SORD:
			this.setUnlocalizedName("sord");
			break;
		case NINJA:
			this.setUnlocalizedName("ninjaSword");
			break;
		case KATANA:
			this.setUnlocalizedName("katana");
			break;
		case CALEDSCRATCH:
			this.setUnlocalizedName("caledscratch");
			break;
		case DERINGER:
			this.setUnlocalizedName("royalDeringer");
			break	;
		case REGISWORD:
			this.setUnlocalizedName("regisword");
			break;
		case SCARLET:
			this.setUnlocalizedName("scarletRibbitar");
			break;
		case DOGG:
			this.setUnlocalizedName("doggMachete");
		}
		this.weaponDamage = 4 + bladeType.getDamageVsEntity();
	}

	public int getAttackDamage() 
	{
		return weaponDamage;
	}
	
	@Override
	public int getItemEnchantability()
	{
		return this.bladeType.getEnchantability();
	}
	 
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker)
	{
		itemStack.damageItem(1, attacker);
		if (bladeType.equals(EnumBladeType.SORD) && Math.random() < .25)
		{
			EntityItem sord = new EntityItem(attacker.worldObj, attacker.posX, attacker.posY, attacker.posZ, itemStack);
			attacker.worldObj.spawnEntityInWorld(sord);
			itemStack.stackSize--;
		}
		
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack itemStack, World world, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLiving)
	{
		if (par3.getBlockHardness(world, par4, par5, par6) != 0.0D)
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
		switch(bladeType)
		{
		case SORD:
			itemIcon = iconRegister.registerIcon("minestuck:Sord");
			break;
		case NINJA:
		case KATANA:
			itemIcon = iconRegister.registerIcon("minestuck:Katana");
			break;
		case CALEDSCRATCH:
			itemIcon = iconRegister.registerIcon("minestuck:Caledscratch");
			break;
		case DERINGER:
			itemIcon = iconRegister.registerIcon("minestuck:RoyalDeringer");
			break	;
		case REGISWORD:
			itemIcon = iconRegister.registerIcon("minestuck:Regisword");
			break;
		case SCARLET:
			itemIcon = iconRegister.registerIcon("minestuck:ScarletRibbitar");
			break;
		case DOGG:
			itemIcon = iconRegister.registerIcon("minestuck:SnoopDoggMachete");
		}
	}
	
}
