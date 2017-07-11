package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.GristType;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMinestuckBeverage extends ItemFood
{
	public static String[] modelNames = new String[]{"tab"};
	
	private int[] healAmounts;
	private float[] saturationModifiers;
	private String[] unlocalizedNames;
	
	public ItemMinestuckBeverage()
	{
		super(0, 0, false);
		this.setHasSubtypes(true);
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("beverage");
		setMaxStackSize(16);
		
		int num_beverages = modelNames.length;
		healAmounts = new int[num_beverages];
		saturationModifiers = new float[num_beverages];
		unlocalizedNames = new String[num_beverages];
		
		for(int i = 0; i < num_beverages; i++)
		{
			healAmounts[i] = 1;
		}
		
		for(int i = 0; i < num_beverages; i++)
		{
			saturationModifiers[i] = 0.0F;
			String name = modelNames[i].substring(0, 1).toUpperCase() + modelNames[i].substring(1);
			unlocalizedNames[i] = "item.beverage"+name;
		}
		
		setAlwaysEdible();
	}
	
	@Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }
	
	@Override
	public int getHealAmount(ItemStack stack)
	{
		return healAmounts[stack.getItemDamage() % healAmounts.length];
	}
	
	@Override
	public float getSaturationModifier(ItemStack stack)
	{
		return saturationModifiers[stack.getItemDamage() % saturationModifiers.length];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return unlocalizedNames[stack.getItemDamage() % unlocalizedNames.length];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		int num_beverages = modelNames.length;
		for(int i = 0; i < num_beverages; i++)
			subItems.add(new ItemStack(itemIn, 1, i));
	}
}