package com.mraof.minestuck.event;

import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class AlchemizeItemEvent extends Event
{
	private World world;
	private BlockPos itemPos;
	private TileEntityAlchemiter alchemiter;
	private ItemStack dowel;
	private ItemStack originalResultItem;
	private ItemStack resultItem;

	public AlchemizeItemEvent(World world, BlockPos itemPos, TileEntityAlchemiter alchemiter, ItemStack dowel, ItemStack originalResultItem)
	{
		this.world = world;
		this.itemPos = itemPos;
		this.alchemiter = alchemiter;
		this.dowel = dowel;
		this.originalResultItem = originalResultItem;
		this.resultItem = originalResultItem;
	}

	public World getWorld()
	{
		return world;
	}

	public BlockPos getItemPos()
	{
		return itemPos;
	}

	public TileEntityAlchemiter getAlchemiter()
	{
		return alchemiter;
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
