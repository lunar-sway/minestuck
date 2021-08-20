package com.mraof.minestuck.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class AlchemyCombinationEvent extends Event
{
	private ItemStack itemA, itemB, originalResultItem, resultItem;

	public AlchemyCombinationEvent(ItemStack itemA, ItemStack itemB, ItemStack originalResultItem)
	{
		this.itemA = itemA;
		this.itemB = itemB;
		this.originalResultItem = originalResultItem;
		this.resultItem = originalResultItem;
	}

	public ItemStack getItemA()
	{
		return itemA;
	}

	public ItemStack getItemB()
	{
		return itemB;
	}

	public ItemStack getOriginalResultItem()
	{
		return originalResultItem;
	}

	public ItemStack getResultItem()
	{
		return resultItem;
	}

	public void setResultItem(ItemStack resultItem)
	{
		this.resultItem = resultItem;
	}
}
