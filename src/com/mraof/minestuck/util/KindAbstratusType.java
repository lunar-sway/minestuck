package com.mraof.minestuck.util;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KindAbstratusType
{
	
	private String unlocalizedName;
	private boolean selectable = true;
	
	private ArrayList<ItemType> items = new ArrayList<ItemType>();
	
	public KindAbstratusType(String unlocName) 
	{
		this.unlocalizedName = unlocName;
	}
	
	public KindAbstratusType(String unlocName, Object... items)
	{
		this(unlocName);
		addItemChecks(items);
	}
	public void addItemChecks(Object... items)
	{
		for(Object i : items)
		{
			if(i instanceof Item) addItemId((Item)i);
			else if(i instanceof String) addToolClass((String)i);
			else if(i.getClass().isAssignableFrom(Item.class)) addItemClass((Class<? extends Item>)i);
			else Debug.warnf("%s is not a valid item check.", i);
		}
	}
	
	public KindAbstratusType setSelectable(boolean in){selectable = in; return this;}
	public boolean 			 getSelectable() 		  {return selectable;}
	
	public KindAbstratusType addItemClass(Class<? extends Item>... items) 
	{
		for(Class<? extends Item> item : items)
			this.items.add(new ItemClassType(item));
		return this;
	}
	
	public KindAbstratusType addItemId(Item... items) 
	{
		for(Item item : items)
			this.items.add(new ItemIdType(item));
		return this;
	}
	
	public KindAbstratusType addToolClass(String... in) 
	{
		for(String i : in)
			items.add(new ItemToolType(i));
		return this;
	}
	
	public KindAbstratusType includesFist() 
	{
		items.add(new FistType());
		return this;
	}
	@SideOnly(Side.CLIENT)
	public String getDisplayName() {
		return I18n.translateToLocal("strife."+unlocalizedName+".name");
	}
	
	public String getUnlocalizedName() {
		return unlocalizedName;
	}
	
	public boolean partOf(ItemStack item) {
		for(ItemType type : items)
			if(type.partOf(item))
				return true;
		return false;
	}
	
	private static abstract class ItemType {
		
		abstract boolean partOf(ItemStack item);
		
	}
	
	private static class ItemClassType extends ItemType {
		
		final Class<? extends Item> itemClass;
		
		ItemClassType(Class<? extends Item> itemClass)
		{
			this.itemClass = itemClass;
		}
		
		@Override
		boolean partOf(ItemStack item)
		{
			return this.itemClass.isInstance(item.getItem().getClass());
		}
		
	}
	
	private static class ItemIdType extends ItemType
	{
		final ResourceLocation itemId;
		
		ItemIdType(Item item)
		{
			itemId = Item.REGISTRY.getNameForObject(item);
		}
		
		@Override
		boolean partOf(ItemStack item)
		{
			return this.itemId.equals(Item.REGISTRY.getNameForObject(item.getItem()));
		}
	}
	
	private static class ItemToolType extends ItemType
	{
		final String toolClass;
		
		ItemToolType(String toolClass)
		{
			this.toolClass = toolClass;
		}
		
		@Override
		boolean partOf(ItemStack item) 
		{
			
			return item.getItem().getToolClasses(item).contains(toolClass);
		}
		
	}
	
	private static class FistType extends ItemType
	{
		@Override
		boolean partOf(ItemStack item) 
		{
			return item.isEmpty();
		}
		
	}
	
}