package com.mraof.minestuck.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemMinestuckBeverage extends ItemFood
{
	public static final String[] NAMES = new String[]{"tab", "faygo", "faygo_candyapple", "faygo_cola",
			"faygo_cottoncandy", "faygo_creme", "faygo_grape", "faygo_moonmist", "faygo_peach", "faygo_redpop"};
	
	private int[] healAmounts;
	private float[] saturationModifiers;
	private String[] unlocalizedNames;
	
	public ItemMinestuckBeverage()
	{
		super(0, 0, false);
		this.setHasSubtypes(true);
		this.setCreativeTab(TabMinestuck.instance);
		this.setUnlocalizedName("beverage");
		setMaxStackSize(16);
		
		int num_beverages = NAMES.length;
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
			String name = NAMES[i].substring(0, 1).toUpperCase() + NAMES[i].substring(1);
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
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		int num_beverages = NAMES.length;
		if(this.isInCreativeTab(tab))
			for(int i = 0; i < num_beverages; i++)
				items.add(new ItemStack(this, 1, i));
	}
}