package com.mraof.minestuck.event;

import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AlchemizeItemAlchemiterEvent extends AlchemizeItemEvent
{
	private TileEntityAlchemiter alchemiter;
	private BlockPos itemPos;

	public AlchemizeItemAlchemiterEvent(World world, ItemStack dowel, ItemStack originalResultItem, TileEntityAlchemiter alchemiter, BlockPos itemPos)
	{
		super(world, dowel, originalResultItem);
		this.alchemiter = alchemiter;
		this.itemPos = itemPos;
	}

	public TileEntityAlchemiter getAlchemiter()
	{
		return alchemiter;
	}

	public BlockPos getItemPos()
	{
		return itemPos;
	}
}
