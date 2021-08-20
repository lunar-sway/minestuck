package com.mraof.minestuck.event;

import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.tileentity.TileEntitySburbMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AlchemizeItemMinichemiterEvent extends AlchemizeItemEvent
{
	private TileEntitySburbMachine alchemiter;

	public AlchemizeItemMinichemiterEvent(World world, ItemStack dowel, ItemStack originalResultItem, TileEntitySburbMachine alchemiter)
	{
		super(world, dowel, originalResultItem);
		this.alchemiter = alchemiter;
	}

	public TileEntitySburbMachine getAlchemiter()
	{
		return alchemiter;
	}
}
