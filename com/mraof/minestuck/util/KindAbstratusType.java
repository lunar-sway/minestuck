package com.mraof.minestuck.util;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class KindAbstratusType {
	
	private String unlocalizedName;
	
	private ArrayList<ItemType> items = new ArrayList();
	
	public KindAbstratusType(String unlocName) {
		this.unlocalizedName = unlocName;
	}
	
	public KindAbstratusType addItemClass(Class<? extends Item> item) {
		items.add(new ItemClassType(item));
		return this;
	}
	
	public KindAbstratusType addItemId(int id) {
		items.add(new ItemIdType(id));
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
		
		int itemId;
		
		ItemIdType(int id) {
			this.itemId = id;
		}
		
		@Override
		boolean partOf(ItemStack item) {
			return item.itemID == this.itemId;
		}
		
	}
	
}