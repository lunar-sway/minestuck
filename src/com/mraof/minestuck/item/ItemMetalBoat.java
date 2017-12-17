package com.mraof.minestuck.item;

import com.mraof.minestuck.entity.item.EntityMetalBoat;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemMetalBoat extends ItemCustomBoat
{
	
	public static final String[] NAMES = {"iron", "gold"};
	
	public ItemMetalBoat()
	{
		super();
		setUnlocalizedName("metalBoat");
		setHasSubtypes(true);
	}
	
	@Override
	protected Entity createBoat(ItemStack stack, World world, double x, double y, double z)
	{
		return new EntityMetalBoat(world, x, y, z, stack.getItemDamage());
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "." + NAMES[stack.getItemDamage()];
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(this.isInCreativeTab(tab))
			for(int i = 0; i < NAMES.length; i++)
				items.add(new ItemStack(this, 1, i));
	}
	
}
