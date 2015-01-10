package com.mraof.minestuck.item.weapon;

import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
			EntityItem sord = new EntityItem(attacker.worldObj, attacker.posX, attacker.posY, attacker.posZ, itemStack.copy());
			sord.getEntityItem().stackSize = 1;
			sord.setPickupDelay(40);
			attacker.worldObj.spawnEntityInWorld(sord);
			itemStack.stackSize--;
		}
		
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
	{
		if (blockIn.getBlockHardness(worldIn, pos) != 0.0D)
		{
			stack.damageItem(2, playerIn);
		}
		
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
}
