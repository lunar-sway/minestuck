package com.mraof.minestuck.event;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class AlchemizeItemEvent extends Event
{
	private World     world;
	private ItemStack dowel;
	private ItemStack originalResultItem;
	private ItemStack resultItem;

	public AlchemizeItemEvent(World world, ItemStack dowel, ItemStack originalResultItem)
	{
		this.world = world;
		this.dowel = dowel;
		this.originalResultItem = originalResultItem;
		this.resultItem = originalResultItem;
	}

	public World getWorld()
	{
		return world;
	}

	public ItemStack getDowel()
	{
		return dowel;
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
