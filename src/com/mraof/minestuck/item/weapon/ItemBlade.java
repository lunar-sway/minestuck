package com.mraof.minestuck.item.weapon;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mraof.minestuck.Minestuck;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlade extends ItemSword	//To allow enchantments such as sharpness
{
	private int weaponDamage;
	private final EnumBladeType bladeType;
	public float efficiencyOnProperMaterial = 4.0F;
	
	public ItemBlade(EnumBladeType bladeType)
	{
		super(ToolMaterial.IRON);
		
		setCreativeTab(Minestuck.tabMinestuck);
		this.bladeType = bladeType;
		this.setMaxDamage(bladeType.getMaxUses());
		this.setUnlocalizedName(bladeType.getName());
		this.weaponDamage = bladeType.getDamageVsEntity();
	}
	
	@Override
	public float getDamageVsEntity()
	{
		return bladeType.getDamageVsEntity();
	}
	
	@Override
	public int getItemEnchantability()
	{
		return this.bladeType.getEnchantability();
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
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
	
	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
	{
		Multimap multimap = HashMultimap.create();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", (double)this.weaponDamage, 0));
		return multimap;
	}
	
}
