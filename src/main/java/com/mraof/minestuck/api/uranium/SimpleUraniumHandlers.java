package com.mraof.minestuck.api.uranium;

import net.minecraft.nbt.CompoundTag;

public class SimpleUraniumHandlers implements IUraniumHandler
{
	private final int maxPower;
	private int power = 0;
	
	public SimpleUraniumHandlers(int max)
	{
		this.maxPower = max;
	}
	
	@Override
	public int receiveUranium(int toReceive, boolean simulate)
	{
		int toAdd = Math.min(toReceive, maxPower - power);
		
		if(!simulate) power += toAdd;
		
		return toAdd;
	}
	
	@Override
	public int extractUranium(int toExtract, boolean simulate)
	{
		int toRemove = Math.min(toExtract, power);
		
		if(!simulate) power -= toRemove;
		
		return toRemove;
	}
	
	@Override
	public int getUraniumStored()
	{
		return power;
	}
	
	public void setUraniumStored(int stored)
	{
		power = stored;
	}
	
	@Override
	public int getMaxUraniumStored()
	{
		return maxPower;
	}
	
	@Override
	public boolean canExtractUranium()
	{
		return true;
	}
	
	@Override
	public boolean canReceiveUranium()
	{
		return true;
	}
	
	public CompoundTag save()
	{
		var tag = new CompoundTag();
		tag.putInt("power", power);
		return tag;
	}
	
	public void load(CompoundTag tag)
	{
		power = tag.getInt("power");
	}
	
	/**
	 * Only allows extracting uranium power
	 */
	public static class Extract extends SimpleUraniumHandlers
	{
		public Extract(int max)
		{
			super(max);
		}
		
		@Override
		public boolean canReceiveUranium()
		{
			return false;
		}
	}
	
	/**
	 * Only allows inserting uranium power
	 */
	public static class Insert extends SimpleUraniumHandlers
	{
		public Insert(int max)
		{
			super(max);
		}
		
		@Override
		public boolean canExtractUranium()
		{
			return false;
		}
	}
}
