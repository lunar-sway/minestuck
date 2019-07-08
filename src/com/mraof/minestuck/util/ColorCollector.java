package com.mraof.minestuck.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the array with colors that the player picks from.
 * Also has two variables related to color selection for clientside.
 */
public class ColorCollector
{
	private static List<Integer> colors;
//	protected static boolean customColor;
	
	@OnlyIn(Dist.CLIENT)
	public static int playerColor;
	@OnlyIn(Dist.CLIENT)
	public static boolean displaySelectionGui;
	
	static
	{
		colors = new ArrayList<>();
		
		colors.add(0x0715cd);
		colors.add(0xb536da);
		colors.add(0xe00707);
		colors.add(0x4ac925);
		
		colors.add(0x00d5f2);
		colors.add(0xff6ff2);
		colors.add(0xf2a400);
		colors.add(0x1f9400);
		
		colors.add(0xa10000);
		colors.add(0xa15000);
		colors.add(0xa1a100);
		colors.add(0x626262);
		colors.add(0x416600);
		colors.add(0x008141);
		colors.add(0x008282);
		colors.add(0x005682);
		colors.add(0x000056);
		colors.add(0x2b0057);
		colors.add(0x6a006a);
		colors.add(0x77003c);
	}
	
	public static int getColor(int index)
	{
		if(index < 0 || index >= colors.size())
			index = 0;
		return colors.get(index);
	}
	
	public static int getColorSize()
	{
		return colors.size();
	}
	
	public static ItemStack setColor(ItemStack stack, int color)
	{
		stack.getOrCreateTag().putInt("color", color);
		return stack;
	}
	
	public static int getColorFromStack(ItemStack stack, int defaultColor)
	{
		if(stack.hasTag() && stack.getTag().contains("color", 99))
			return stack.getTag().getInt("color");
		else return defaultColor;
	}
}