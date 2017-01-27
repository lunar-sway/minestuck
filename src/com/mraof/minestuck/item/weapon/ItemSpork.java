package com.mraof.minestuck.item.weapon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

//I called it a spork because it includes both
public class ItemSpork extends ItemWeapon
{
	private String prefix;

	public ItemSpork(int maxUses, double damageVsEntity, double weaponSpeed, int enchantability, String name)
	{
		super(maxUses, damageVsEntity, weaponSpeed, enchantability, name + "Spork");
		prefix = name;
	}

	@Override
	public double getAttackDamage(ItemStack stack)
	{
	    double damage = super.getAttackDamage(stack);
		if(!isSpoon(stack))
		{
		    damage *= 1.5;
		}
		return damage;
	}
	
	@Override
	protected double getAttackSpeed(ItemStack stack)
	{
		if(!isSpoon(stack))
		{
			return super.getAttackSpeed(stack) * 1.2;
		}
		return super.getAttackSpeed(stack);
	}

	public boolean isSpoon(ItemStack itemStack)
	{
		return checkTagCompound(itemStack).getBoolean("isSpoon");
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemStack)
	{
		return 72000;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (playerIn.isSneaking())
		{
				if(!worldIn.isRemote)
				{
					NBTTagCompound tagCompound = checkTagCompound(stack);
					tagCompound.setBoolean("isSpoon", !tagCompound.getBoolean("isSpoon"));
				}
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
	}
	
	private NBTTagCompound checkTagCompound(ItemStack stack)
	{
	    NBTTagCompound tagCompound = stack.getTagCompound();
	    if(tagCompound == null)
		{
			tagCompound = new NBTTagCompound();
			stack.setTagCompound(tagCompound);
		}
		if(!tagCompound.hasKey("isSpoon"))
		{
			tagCompound.setBoolean("isSpoon", true);
		}
	    return tagCompound;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item." + prefix + (isSpoon(stack) ? "Spoon" : "Fork");
	}
	
}