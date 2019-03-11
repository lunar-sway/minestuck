package com.mraof.minestuck.util;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.block.BlockAlchemiterUpgrades;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.block.BlockAlchemiterUpgrades.EnumParts;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.AlchemiterUpgrade.EnumType;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class AlchemiterUpgrade 
{
	public static AlchemiterUpgrade captchaCard = new AlchemiterUpgrade("captchaCard",new ItemStack(MinestuckItems.captchaCard), EnumType.HORIZONTAL, EnumParts.CAPTCHA_CARD);
	public static AlchemiterUpgrade blender = new AlchemiterUpgrade("blender",new ItemStack(MinestuckBlocks.blender), EnumType.TOTEM_PAD, EnumParts.NONE, EnumParts.BLENDER, EnumParts.BLANK, EnumParts.BLANK);
	public static AlchemiterUpgrade crafting = new AlchemiterUpgrade("crafting",new ItemStack(Blocks.CRAFTING_TABLE), EnumType.HORIZONTAL, EnumParts.CRAFTING);
	public static AlchemiterUpgrade hopper = new AlchemiterUpgrade("hopper",new ItemStack(Blocks.HOPPER), EnumType.HORIZONTAL, EnumParts.HOPPER);
	public static AlchemiterUpgrade chest = new AlchemiterUpgrade("chest",new ItemStack(Blocks.CHEST), EnumType.HORIZONTAL, EnumParts.CHEST);
	public static AlchemiterUpgrade library = new AlchemiterUpgrade("library",new ItemStack(MinestuckItems.modusCard, 1, 5), EnumType.HORIZONTAL, EnumParts.LIBRARY);
	public static AlchemiterUpgrade gristWidget = new AlchemiterUpgrade("gristWidget",new ItemStack(MinestuckBlocks.crockerMachine), EnumType.HORIZONTAL, EnumParts.GRISTWIDGET);
	public static AlchemiterUpgrade dropper = new AlchemiterUpgrade("dropper",new ItemStack(Blocks.DROPPER), EnumType.HORIZONTAL, EnumParts.DROPPER);
	public static AlchemiterUpgrade redstone = new AlchemiterUpgrade("redstone",new ItemStack(Items.REDSTONE), EnumType.HORIZONTAL, EnumParts.REDSTONE);
	public static AlchemiterUpgrade gristToBoon = new AlchemiterUpgrade("gristToBoon",new ItemStack(MinestuckItems.boondollars), EnumType.HORIZONTAL, EnumParts.BOONDOLLAR);
	public static AlchemiterUpgrade sbahjified = new AlchemiterUpgrade("sbahj",new ItemStack(MinestuckItems.sbahjPoster), EnumType.NONE, EnumParts.BASE);
	public static AlchemiterUpgrade holopad = new AlchemiterUpgrade("holopad",new ItemStack(MinestuckBlocks.holopad), EnumType.TOTEM_PAD, EnumParts.BASE);
	public static AlchemiterUpgrade cruxtruder = new AlchemiterUpgrade("cruxtruder",new ItemStack(MinestuckBlocks.cruxtruder), EnumType.TOTEM_PAD, EnumParts.BASE);
	public static AlchemiterUpgrade punchDesignix = new AlchemiterUpgrade("punchDesignix",new ItemStack(MinestuckBlocks.punchDesignix), EnumType.HORIZONTAL, EnumParts.BASE, EnumParts.BASE);
	public static AlchemiterUpgrade compactJBE = new AlchemiterUpgrade("JBE",new ItemStack(MinestuckBlocks.jumperBlockExtension[0]), EnumType.HORIZONTAL, EnumParts.BASE, EnumParts.BASE);
	
	public static final AlchemiterUpgrade EMPTY = new AlchemiterUpgrade(null, null, null, (Object)null);
	
	//Vars
	public static List<AlchemiterUpgrade> list = new ArrayList<AlchemiterUpgrade>();
	
	protected String name;
	protected ItemStack item;
	protected EnumType placementType;
	protected List<IBlockState> block = new ArrayList<IBlockState>();
	
	//Constructors
	public AlchemiterUpgrade(String name, ItemStack items, EnumType placementType, Object... blocks)
	{
		for(Object i : blocks)
		{
			if(i instanceof Block) i = ((Block)i).getDefaultState();
			else if(i instanceof EnumParts) i = BlockAlchemiterUpgrades.getBlockState((EnumParts)i);
			
			if(i instanceof IBlockState) block.add((IBlockState) i);
		}
		
		setName(name);
		setItem(items);
		setPlacementType(placementType);
		list.add(this);
	}
	
	//Upgrade to Shunt
	public ItemStack getShunt() {return AlchemyRecipes.createShunt(item);}
	
	//Uprade getters from values
	public static AlchemiterUpgrade getUpgradeFromItem(ItemStack item)
	{
		for(AlchemiterUpgrade i : list)
			if(i.getItem().isItemEqual(item))
				return i;
		
		Debug.warnf("no upgrades where found with the itemstack %s", item);
		return null;
	}
	public static AlchemiterUpgrade getUpgradeFromName(String name)
	{
		for(AlchemiterUpgrade i : list)
			if(((String)i.getName()).equals(name))
				return i;
		
		Debug.warnf("no upgrades where found with the name %s", name);
		return null;
	}
	
	public boolean isEmpty()
	{
		return this == (EMPTY);
	}
	
	//Setters
	public void setName(String in) {name = in;}
	public void setItem(ItemStack in) {item = in;}
	public void setPlacementType(EnumType in) {placementType = in;}
	
	//Getters
	public String getName() {return name;}
	public ItemStack getItem() {return item;}
	public EnumType getPlacementType() {return placementType;}
	public List<IBlockState> getBlocks() {return block;}
	
	//Upgrades that disable alchemy
	public static boolean nullifiesAlchemiterFunc(AlchemiterUpgrade upg) 
	{
		return 
			upg == blender
		|| 	upg == sbahjified
				;
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
}
