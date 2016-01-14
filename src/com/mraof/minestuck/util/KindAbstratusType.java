package com.mraof.minestuck.util;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	@SideOnly(Side.CLIENT)
	public String getDisplayName() {
		return StatCollector.translateToLocal("strife."+unlocalizedName+".name");
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
		
		ItemClassType(Class<? extends Item> itemClass) {
			this.itemClass = itemClass;
		}
		
		boolean partOf(ItemStack item) {
			return this.itemClass.isInstance(item.getItem().getClass());
		}
		
	}
	
	private static class ItemIdType extends ItemType {
		final ResourceLocation itemId;
		
		ItemIdType(Item item) {
			itemId = Item.itemRegistry.getNameForObject(item);
		}
		
		boolean partOf(ItemStack item) {
			return this.itemId == Item.itemRegistry.getNameForObject(item.getItem());
		}
		
	}
	
}