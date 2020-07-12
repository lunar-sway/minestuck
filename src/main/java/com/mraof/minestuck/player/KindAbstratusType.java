package com.mraof.minestuck.player;

import com.mraof.minestuck.util.Debug;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;

public class KindAbstratusType
{
	
	private final String unlocalizedName;
	private boolean selectable = true;
	
	private final ArrayList<ItemType> items = new ArrayList<>();
	
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
			else if(i instanceof ToolType) addToolClass((ToolType)i);
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
	
	public KindAbstratusType addToolClass(ToolType... in)
	{
		for(ToolType i : in)
			items.add(new ItemToolType(i));
		return this;
	}
	
	public KindAbstratusType includesFist()
	{
		items.add(new FistType());
		return this;
	}
	
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent("strife."+unlocalizedName);
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
			itemId = item.getRegistryName();
		}
		
		@Override
		boolean partOf(ItemStack item)
		{
			return this.itemId.equals(item.getItem().getRegistryName());
		}
	}
	
	private static class ItemToolType extends ItemType
	{
		final ToolType toolClass;
		
		ItemToolType(ToolType toolClass)
		{
			this.toolClass = toolClass;
		}
		
		@Override
		boolean partOf(ItemStack item)
		{
			
			return item.getItem().getToolTypes(item).contains(toolClass);
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