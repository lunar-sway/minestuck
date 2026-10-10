package com.mraof.minestuck.player;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public class KindAbstratusType
{
	
	private final String unlocalizedName;
	
	private final ArrayList<ItemType> items = new ArrayList<ItemType>();
	
	public KindAbstratusType(String unlocName) {
		this.unlocalizedName = unlocName;
	}
	
	public KindAbstratusType addItemId(Item item) {
		items.add(new ItemIdType(item));
		return this;
	}
	
	public Component getDisplayName() {
		return Component.translatable("strife."+unlocalizedName);
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
	
	private static class ItemIdType extends ItemType
	{
		final Item item;
		
		ItemIdType(Item item)
		{
			this.item = item;
		}
		
		@Override
		boolean partOf(ItemStack stack)
		{
			return stack.is(this.item);
		}
	}
	public KindAbstratusType addItemTag(TagKey<Item> tag){
		items.add(new ItemTagType(tag));
		return this;
	}
	private static class ItemTagType extends ItemType
	{
		final TagKey<Item> tag;
		
		ItemTagType(TagKey<Item> tag)
		{
			this.tag = tag;
		}
		
		@Override
		boolean partOf(ItemStack stack)
		{
			return stack.is(tag);
		}
	}
}