package com.mraof.minestuck.util;


import java.util.Arrays;

import com.mraof.minestuck.block.BlockAlchemiterUpgrades;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.block.BlockAlchemiterUpgrades.EnumParts;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.Debug;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class AlchemiterUpgrades
{
	protected String upgradeName;
	protected ItemStack[] upgradeItem = new ItemStack[7];
	protected EnumType upgradeType;
	protected EnumParts[] upgradeBlock = new EnumParts[15];
	protected boolean isCombo;
	
	public static AlchemiterUpgrades[] upgradeList = new AlchemiterUpgrades[64];
	public static int upgradeCount = 0;
	
	//Upgrade Combos
	public static AlchemiterUpgrades itemWidget = new AlchemiterUpgrades(new ItemStack[] {new ItemStack(MinestuckBlocks.crockerMachine), new ItemStack(Blocks.HOPPER)}, EnumType.DOUBLE, EnumParts.PLACEHOLDER, EnumParts.PLACEHOLDER);
	public static AlchemiterUpgrades holoLathe = new AlchemiterUpgrades(new ItemStack[] {new ItemStack(MinestuckBlocks.totemlathe[0]), new ItemStack(MinestuckBlocks.holopad)}, EnumType.DOUBLE, EnumParts.PLACEHOLDER, EnumParts.PLACEHOLDER);
	public static AlchemiterUpgrades cruxiteBlender = new AlchemiterUpgrades(new ItemStack[] {new ItemStack(MinestuckBlocks.cruxtruder), new ItemStack(MinestuckBlocks.blender)}, EnumType.DOUBLE, EnumParts.PLACEHOLDER, EnumParts.PLACEHOLDER);
		
	//Solo Upgrades
	public static AlchemiterUpgrades captchaCard = new AlchemiterUpgrades(new ItemStack(MinestuckItems.captchaCard), EnumType.SINGLE, EnumParts.CAPTCHA_CARD);
	public static AlchemiterUpgrades blender = new AlchemiterUpgrades(new ItemStack(MinestuckBlocks.blender), EnumType.TOTEM_PAD, EnumParts.BLENDER);
	public static AlchemiterUpgrades crafting = new AlchemiterUpgrades(new ItemStack(Blocks.CRAFTING_TABLE), EnumType.SINGLE, EnumParts.CRAFTING);
	public static AlchemiterUpgrades hopper = new AlchemiterUpgrades(new ItemStack(Blocks.HOPPER), EnumType.SINGLE, EnumParts.HOPPER);
	public static AlchemiterUpgrades library = new AlchemiterUpgrades(new ItemStack(MinestuckItems.modusCard, 1, 3), EnumType.SINGLE, EnumParts.LIBRARY);
	public static AlchemiterUpgrades gristWidget = new AlchemiterUpgrades(new ItemStack(MinestuckBlocks.crockerMachine), EnumType.SINGLE, EnumParts.GRISTWIDGET);
	public static AlchemiterUpgrades dropper = new AlchemiterUpgrades(new ItemStack(Blocks.DROPPER), EnumType.SINGLE, EnumParts.DROPPER);
	public static AlchemiterUpgrades gristToBoon = new AlchemiterUpgrades(new ItemStack(MinestuckItems.boondollars), EnumType.SINGLE, EnumParts.BOONDOLLAR);
	public static AlchemiterUpgrades sbahjified = new AlchemiterUpgrades(new ItemStack(MinestuckItems.sbahjPoster), EnumType.NONE, EnumParts.PLACEHOLDER);
	public static AlchemiterUpgrades holopad = new AlchemiterUpgrades(new ItemStack(MinestuckBlocks.holopad), EnumType.TOTEM_PAD, EnumParts.PLACEHOLDER);
	public static AlchemiterUpgrades cruxtruder = new AlchemiterUpgrades(new ItemStack(MinestuckBlocks.cruxtruder), EnumType.TOTEM_PAD, EnumParts.PLACEHOLDER);
	public static AlchemiterUpgrades punchDesignix = new AlchemiterUpgrades(new ItemStack(MinestuckBlocks.punchDesignix), EnumType.DOUBLE, EnumParts.PLACEHOLDER, EnumParts.PLACEHOLDER);
	public static AlchemiterUpgrades compactJBE = new AlchemiterUpgrades(new ItemStack(MinestuckBlocks.jumperBlockExtension[0]), EnumType.DOUBLE, EnumParts.PLACEHOLDER, EnumParts.PLACEHOLDER);
	
	
	public AlchemiterUpgrades(ItemStack item, EnumType type, EnumParts...block)
	{
		this(new ItemStack[] {item}, type, block);
	}
	
	public AlchemiterUpgrades(ItemStack[] items, EnumType type, EnumParts...block)
	{
		setUpgradeItems(items);
		this.setUpgradeType(type);
		this.setUpgradeBlocks(block);
		
		upgradeList[upgradeCount] = this;
		upgradeCount++;
	}
	
	public static AlchemiterUpgrades[] getUpgradesFromList(ItemStack[] list)
	{
		AlchemiterUpgrades[] upgrades = new AlchemiterUpgrades[7];
		int upgCount = 0;
		
		for(AlchemiterUpgrades upg : upgradeList)
		{
			if(Arrays.asList(upgradeList).indexOf(upg) >= upgradeCount) break;
			
			if(hasUpgrade(list, upg)) 
			{
				upgrades[upgCount] = upg;
				upgCount++;
				
				for(ItemStack i : upg.getUpgradeItems())
				{
					for(int j = 0; j < list.length; j++) if(i.isItemEqual(list[j]))
					{
						list[j] = ItemStack.EMPTY;
						break;
					}
						
				}
					
			}
		}
		
		return upgrades;
	}
	
	protected void setUpgradeItems(ItemStack[] items)
	{
		this.upgradeItem = items;
	}
	
	public ItemStack[] getUpgradeItems()
	{
		return upgradeItem;
	}

	public EnumType getUpgradeType()
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
		ItemStack[] stack = upgrade.getUpgradeItems();
		
		for(int i = 0; i < stack.length; i++)
		{
			if(stack[i] == null) continue;
				
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
		NONE,
		SINGLE,
		DOUBLE,
		TRIPLE,
		QUAD,
		TOTEM_PAD,
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
