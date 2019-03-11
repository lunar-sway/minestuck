package com.mraof.minestuck.util;


import java.util.Arrays;

import com.mraof.minestuck.block.BlockAlchemiterUpgrades;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.block.BlockAlchemiterUpgrades.EnumParts;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.Debug;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class AlchemiterUpgrades_OLD
{
	protected String upgradeName;
	protected ItemStack[] upgradeItem = new ItemStack[7];
	protected EnumType upgradeType;
	protected IBlockState[] upgradeBlock = new IBlockState[15];
	protected boolean isCombo;
	
	public static AlchemiterUpgrades_OLD[] upgradeList = new AlchemiterUpgrades_OLD[64];
	public static int upgradeCount = 0;
	
	//Upgrade Combos
	//public static AlchemiterUpgrades itemWidget = new AlchemiterUpgrades(new ItemStack[] {new ItemStack(MinestuckBlocks.crockerMachine), new ItemStack(Blocks.HOPPER)}, EnumType.HORIZONTAL, EnumParts.BASE, EnumParts.BASE);
	public static AlchemiterUpgrades_OLD holoLathe = new AlchemiterUpgrades_OLD(new ItemStack[] {new ItemStack(MinestuckBlocks.totemlathe[0]), new ItemStack(MinestuckBlocks.holopad)}, EnumType.HORIZONTAL, EnumParts.BASE, EnumParts.BASE);
	public static AlchemiterUpgrades_OLD cruxiteBlender = new AlchemiterUpgrades_OLD(new ItemStack[] {new ItemStack(MinestuckBlocks.cruxtruder), new ItemStack(MinestuckBlocks.blender)}, EnumType.HORIZONTAL, EnumParts.BASE, EnumParts.BASE);
		
	//Solo Upgrades
	public static AlchemiterUpgrades_OLD captchaCard = new AlchemiterUpgrades_OLD(new ItemStack(MinestuckItems.captchaCard), EnumType.HORIZONTAL, EnumParts.CAPTCHA_CARD);
	public static AlchemiterUpgrades_OLD blender = new AlchemiterUpgrades_OLD(new ItemStack(MinestuckBlocks.blender), EnumType.TOTEM_PAD, EnumParts.NONE, EnumParts.BLENDER, EnumParts.BLANK, EnumParts.BLANK);
	public static AlchemiterUpgrades_OLD crafting = new AlchemiterUpgrades_OLD(new ItemStack(Blocks.CRAFTING_TABLE), EnumType.HORIZONTAL, EnumParts.CRAFTING);
	public static AlchemiterUpgrades_OLD hopper = new AlchemiterUpgrades_OLD(new ItemStack(Blocks.HOPPER), EnumType.HORIZONTAL, EnumParts.HOPPER);
	public static AlchemiterUpgrades_OLD chest = new AlchemiterUpgrades_OLD(new ItemStack(Blocks.CHEST), EnumType.HORIZONTAL, EnumParts.CHEST);
	public static AlchemiterUpgrades_OLD library = new AlchemiterUpgrades_OLD(new ItemStack(MinestuckItems.modusCard, 1, 5), EnumType.HORIZONTAL, EnumParts.LIBRARY);
	public static AlchemiterUpgrades_OLD gristWidget = new AlchemiterUpgrades_OLD(new ItemStack(MinestuckBlocks.crockerMachine), EnumType.HORIZONTAL, EnumParts.GRISTWIDGET);
	public static AlchemiterUpgrades_OLD dropper = new AlchemiterUpgrades_OLD(new ItemStack(Blocks.DROPPER), EnumType.HORIZONTAL, EnumParts.DROPPER);
	public static AlchemiterUpgrades_OLD redstone = new AlchemiterUpgrades_OLD(new ItemStack(Items.REDSTONE), EnumType.HORIZONTAL, EnumParts.REDSTONE);
	public static AlchemiterUpgrades_OLD gristToBoon = new AlchemiterUpgrades_OLD(new ItemStack(MinestuckItems.boondollars), EnumType.HORIZONTAL, EnumParts.BOONDOLLAR);
	public static AlchemiterUpgrades_OLD sbahjified = new AlchemiterUpgrades_OLD(new ItemStack(MinestuckItems.sbahjPoster), EnumType.NONE, EnumParts.BASE);
	public static AlchemiterUpgrades_OLD holopad = new AlchemiterUpgrades_OLD(new ItemStack(MinestuckBlocks.holopad), EnumType.TOTEM_PAD, EnumParts.BASE);
	public static AlchemiterUpgrades_OLD cruxtruder = new AlchemiterUpgrades_OLD(new ItemStack(MinestuckBlocks.cruxtruder), EnumType.TOTEM_PAD, EnumParts.BASE);
	public static AlchemiterUpgrades_OLD punchDesignix = new AlchemiterUpgrades_OLD(new ItemStack(MinestuckBlocks.punchDesignix), EnumType.HORIZONTAL, EnumParts.BASE, EnumParts.BASE);
	public static AlchemiterUpgrades_OLD compactJBE = new AlchemiterUpgrades_OLD(new ItemStack(MinestuckBlocks.jumperBlockExtension[0]), EnumType.HORIZONTAL, EnumParts.BASE, EnumParts.BASE);
	
	
	public AlchemiterUpgrades_OLD(ItemStack item, EnumType type, EnumParts...block)
	{
		this(new ItemStack[] {item}, type, block);
	}
	
	public AlchemiterUpgrades_OLD(ItemStack[] items, EnumType type, EnumParts...block)
	{
		setUpgradeItems(items);
		this.setUpgradeType(type);
		this.setUpgradeBlocks(block);
		
		upgradeList[upgradeCount] = this;
		upgradeCount++;
	}
	
	public static AlchemiterUpgrades_OLD[] getUpgradesFromList(ItemStack[] list)
	{
		AlchemiterUpgrades_OLD[] upgrades = new AlchemiterUpgrades_OLD[7];
		int upgCount = 0;
		
		for(AlchemiterUpgrades_OLD upg : upgradeList)
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
	
	public IBlockState[] getUpgradeBlocks()
	{
		return this.upgradeBlock;
	}
	
	public void setUpgradeBlocks(IBlockState[] block)
	{
		this.upgradeBlock = block;
	}
	
	public void setUpgradeBlocks(EnumParts[] block)
	{
		IBlockState[] states = new IBlockState[block.length];
		
		for(int i = 0; i < states.length; i++)
		{
			states[i] = BlockAlchemiterUpgrades.getBlockState(block[i]);
		}
		setUpgradeBlocks(states);
	}
	
	public IBlockState getUpgradeBlock(int id)
	{
		return this.upgradeBlock[id];
	}
	
	public boolean isUpgradeCombo()
	{
		return this.upgradeItem.length > 1;
	}
	
	public static boolean hasUpgrade(ItemStack[] list, AlchemiterUpgrades_OLD upgrade)
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
	
	public static boolean hasUpgrade(AlchemiterUpgrades_OLD[] list, AlchemiterUpgrades_OLD upgrade)
	{
		return Arrays.asList(list).contains(upgrade);
	}
	
	public static AlchemiterUpgrades_OLD getUpgradeFromBlock(EnumParts part)
	{
		if(part == EnumParts.BASE_CORNER_LEFT || part == EnumParts.BASE_CORNER_RIGHT || part == EnumParts.BASE_SIDE)
			return null;
		for(AlchemiterUpgrades_OLD i : upgradeList)
		{
			for(IBlockState j : i.getUpgradeBlocks())
			{
				if(j.getBlock() == null) 
					continue;
				if(j.getBlock() instanceof BlockAlchemiterUpgrades)
					if(BlockAlchemiterUpgrades.getPart(j) == part)
						return i;
			}
		}
		return null;
	}
	
	public enum EnumType implements IStringSerializable
	{
		NONE,
		HORIZONTAL,
		VERITCAL,
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

	public static boolean nullifiesAlchemiterFunc(AlchemiterUpgrades_OLD upg) 
	{
		return 
			upg == blender
		|| 	upg == sbahjified
				;
	}
}
