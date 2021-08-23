package com.mraof.minestuck.event;

import com.mraof.minestuck.alchemy.CombinationRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class AlchemyCombinationEvent extends Event
{
	private ItemStack itemA, itemB, originalResultItem, resultItem;
	private CombinationRegistry.Mode mode;

	public AlchemyCombinationEvent(ItemStack itemA, ItemStack itemB, CombinationRegistry.Mode mode, ItemStack originalResultItem)
	{
		this.itemA = itemA;
		this.itemB = itemB;
		this.mode = mode;
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

	public CombinationRegistry.Mode getMode()
	{
		return mode;
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
