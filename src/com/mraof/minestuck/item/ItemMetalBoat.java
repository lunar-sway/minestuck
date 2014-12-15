package com.mraof.minestuck.item;

import com.mraof.minestuck.entity.item.EntityMetalBoat;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMetalBoat extends ItemCustomBoat
{
	
	public String[] names = {"iron"};
	
	public ItemMetalBoat()
	{
		super();
		setUnlocalizedName("metalBoat");
	}
	
	@Override
	protected Entity createBoat(ItemStack stack, World world, double x, double y, double z)
	{
		return new EntityMetalBoat(world, x, y, z, stack.getItemDamage());
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "." + names[stack.getItemDamage()];
	}
	
}
