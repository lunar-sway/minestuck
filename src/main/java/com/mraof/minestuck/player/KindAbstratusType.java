package com.mraof.minestuck.player;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;

public class KindAbstratusType
{
	
	private String unlocalizedName;
	
	private ArrayList<ItemType> items = new ArrayList<ItemType>();
	
	public KindAbstratusType(String unlocName) {
		this.unlocalizedName = unlocName;
	}
	
	public KindAbstratusType addItemClass(Class<? extends Item> item) {
		items.add(new ItemClassType(item));
		return this;
	}
	
	public KindAbstratusType addItemId(Item item) {
		items.add(new ItemIdType(item));
		return this;
	}
	
	public ITextComponent getDisplayName() {
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
}