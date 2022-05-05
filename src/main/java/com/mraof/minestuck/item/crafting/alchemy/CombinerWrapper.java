package com.mraof.minestuck.item.crafting.alchemy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CombinerWrapper implements ItemCombiner
{
	private final ItemStack item1, item2;
	private final CombinationMode mode;
	
	public CombinerWrapper(ItemStack item1, ItemStack item2, CombinationMode mode)
	{
		this.item1 = item1;
		this.item2 = item2;
		this.mode = mode;
	}
	
	@Override
	public CombinationMode getMode()
	{
		return mode;
	}
	
	@Override
	public int getContainerSize()
	{
		return 2;
	}
	
	@Override
	public boolean isEmpty()
	{
		return item1.isEmpty() && item2.isEmpty();
	}
	
	@Override
	public ItemStack getItem(int index)
	{
		if(index == 0)
			return item1.copy();
		else if(index == 1)
			return item2.copy();
		else throw new IndexOutOfBoundsException(String.valueOf(index));
	}
	
	@Override
	public ItemStack removeItem(int index, int count)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public ItemStack removeItemNoUpdate(int index)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setItem(int index, ItemStack stack)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setChanged()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean stillValid(Player player)
	{
		return true;
	}
	
	@Override
	public void clearContent()
	{
		throw new UnsupportedOperationException();
	}
}