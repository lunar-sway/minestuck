package com.mraof.minestuck.item;

import com.mraof.minestuck.entity.item.EntityMetalBoat;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMetalBoat extends ItemCustomBoat
{
	
	public String[] names = {"iron", "gold"};
	
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
		return super.getUnlocalizedName(stack) + "." + names[stack.getItemDamage()];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		for(int i = 0; i < names.length; i++)
			subItems.add(new ItemStack(this, 1, i));
	}
	
}
