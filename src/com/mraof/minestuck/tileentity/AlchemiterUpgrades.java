package com.mraof.minestuck.tileentity;


import java.util.Arrays;

import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.Debug;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class AlchemiterUpgrades
{
	protected final int maxId = 64;
	protected int upgradeCount = 0;
	protected String[] upgradeName = new String[maxId];
	protected ItemStack[][] upgradeItem = new ItemStack[maxId][7];
	protected int[] upgradeType = new int[maxId];
	protected boolean[] isCombo = new boolean[maxId];
	
	public AlchemiterUpgrades()
	{
		registerUpgrade("captcha_card", new ItemStack(MinestuckItems.captchaCard), EnumType.SINGLE);
	}
	
	public boolean hasUpgrade(ItemStack[] list, ItemStack item)
	{
		if(Arrays.asList(upgradeItem[0]).contains(item))
		{
			Debug.warnf("%s is not a valid upgrade.", item);
			return false;
		}
		else return Arrays.asList(list).contains(item);
	}
	
	public boolean hasUpgradeCombo(ItemStack[] list, String name)
	{
		
		return false;
	}
	
	public int getIdFromName(String name)
	{
		return Arrays.asList(upgradeName).indexOf(name);
	}
	
	public void registerUpgrade(String name, ItemStack item, EnumType type)
	{
		upgradeName[upgradeCount] = name;
		upgradeItem[upgradeCount][0] = item;
		upgradeType[upgradeCount] = type.toInt();
		isCombo[upgradeCount] = false;
		
		upgradeCount++;
	}
	
	public void registerUpgradeCombo(String name, ItemStack[] items, EnumType type) throws upgradeComboOverflow
	{
		if(items.length > 7) throw new upgradeComboOverflow(upgradeCount);
		
		upgradeName[upgradeCount] = name;
		upgradeItem[upgradeCount] = items;
		upgradeType[upgradeCount] = type.toInt();
		isCombo[upgradeCount] = true;
		
		upgradeCount++;
	}
	
	public enum EnumType implements IStringSerializable
	{
		SINGLE,
		DOUBLE,
		TRIPLE,
		QUAD,
		TOTEM_PAD
		;
		
		public int toInt()
		{
			return this.ordinal();
		}
		
		@Override
		public String toString()
		{
			return getName();
		}
		
		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
		
	}
	
	public class upgradeComboOverflow extends Exception { 
	    public upgradeComboOverflow(int id) {
	        super("The JBE upgrade combination with id of " + id + "has exceeded the maximum amount of items.");
	    }}
}
