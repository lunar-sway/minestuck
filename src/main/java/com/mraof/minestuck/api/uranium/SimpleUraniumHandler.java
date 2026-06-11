package com.mraof.minestuck.api.uranium;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SimpleUraniumHandler implements IUraniumHandler
{
	private final Supplier<Integer> maxGetter;
	private final Supplier<Integer> powerGetter;
	private final Consumer<Integer> powerSetter;
	
	public SimpleUraniumHandler(Supplier<Integer> max, Supplier<Integer> powerGetter, Consumer<Integer> powerSetter)
	{
		this.maxGetter = max;
		this.powerGetter = powerGetter;
		this.powerSetter = powerSetter;
	}
	
	@Override
	public int receiveUranium(int toReceive, boolean simulate)
	{
		int power = powerGetter.get();
		int toAdd = Math.min(toReceive, maxGetter.get() - power);
		
		if(!simulate) powerSetter.accept(power + toAdd);
		
		return toAdd;
	}
	
	@Override
	public int extractUranium(int toExtract, boolean simulate)
	{
		int power = powerGetter.get();
		int toRemove = Math.min(toExtract, power);
		
		if(!simulate) powerSetter.accept(power - toRemove);
		
		return toRemove;
	}
	
	@Override
	public int getUraniumStored()
	{
		return powerGetter.get();
	}
	
	@Override
	public int getMaxUraniumStored()
	{
		return maxGetter.get();
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
}
