package com.mraof.minestuck.util;


import java.util.Arrays;

import com.mraof.minestuck.block.BlockAlchemiterUpgrades;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.block.BlockAlchemiterUpgrades.EnumParts;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.Debug;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class AlchemiterUpgrades
{
	
	public static AlchemiterUpgrades captchaCard = new AlchemiterUpgrades(new ItemStack(MinestuckItems.captchaCard), EnumType.SINGLE, EnumParts.CAPTCHA_CARD);
	public static AlchemiterUpgrades blender = new AlchemiterUpgrades(new ItemStack(MinestuckBlocks.blender), EnumType.TOTEM_PAD, EnumParts.BLENDER);
	
	
	
	protected static String upgradeName;
	protected static ItemStack[] upgradeItem = new ItemStack[7];
	protected static EnumType upgradeType;
	protected static EnumParts[] upgradeBlock = new EnumParts[15];
	protected static boolean isCombo;
	
	public AlchemiterUpgrades(ItemStack item, EnumType type, EnumParts...block)
	{
		this(new ItemStack[] {item}, type, block);
	}
	
	public AlchemiterUpgrades(ItemStack[] items, EnumType type, EnumParts...block)
	{
		setUpgradeItems(items);
		setUpgradeType(type);
		setUpgradeBlocks(block);
	}
	
	protected void setUpgradeItems(ItemStack[] items)
	{
		this.upgradeItem = items;
		for(int i = 0; i < items.length; i++)
		{
		System.out.println(i + ") registered item upgrades: " + items[i]);
		System.out.println("thing is now: " + this.getUpgradeItems()[i]);
		}
	}
	
	public ItemStack[] getUpgradeItems()
	{
		return this.upgradeItem;
	}

	public EnumType getUpgradeType(AlchemiterUpgrades upgrade)
	{
		return this.upgradeType;
	}

	protected void setUpgradeType(EnumType type)
	{
		this.upgradeType = type;
	}
	
	public EnumParts[] getUpgradeBlocks()
	{
		return this.upgradeBlock;
	}
	
	public void setUpgradeBlocks(EnumParts[] block)
	{
		this.upgradeBlock = block;
	}
	
	public EnumParts getUpgradeBlock(int id)
	{
		return this.upgradeBlock[id];
	}
	
	public boolean isUpgradeCombo()
	{
		return this.upgradeItem.length > 1;
	}
	
	public static boolean hasUpgrade(ItemStack[] list, AlchemiterUpgrades upgrade)
	{
		System.out.println(upgrade);
		ItemStack[] stack = upgrade.getUpgradeItems();
		
		System.out.println(stack);
		for(int i = 0; i < stack.length; i++)
		{
			if(stack[i] == null) continue;
			System.out.println(stack[i]);
			System.out.println(!Arrays.asList(list).contains(stack[i]));
			
			boolean check = false;
			for(ItemStack item : list)
			{
				if(item == null) continue;
				if(item.isItemEqual(stack[i])) check = true;
			}
			if(!check) return false;
		}
		
		return true;
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
