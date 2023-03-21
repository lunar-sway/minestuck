package com.mraof.minestuck.blockentity.machine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BooleanSupplier;

@ParametersAreNonnullByDefault
public final class ProgressTracker implements ContainerData
{
	private final RunType type;
	private final int maxProgress;
	int progress = 0;
	boolean ready = false;
	boolean overrideStop = false;
	
	public ProgressTracker(RunType type, int maxProgress)
	{
		this.type = type;
		this.maxProgress = maxProgress;
	}
	
	public void resetProgress()
	{
		this.progress = 0;
		this.ready = this.type == ProgressTracker.RunType.BUTTON_OVERRIDE && this.overrideStop;
	}
	
	private boolean shouldRun()
	{
		return this.ready || this.type == ProgressTracker.RunType.AUTOMATIC;
	}
	
	public void tick(BooleanSupplier isValid, Runnable onComplete)
	{
		if(!this.shouldRun() || !isValid.getAsBoolean())
		{
			this.resetProgress();
			return;
		}
		
		this.progress++;
		
		if(this.progress >= this.maxProgress)
		{
			this.resetProgress();
			onComplete.run();
		}
	}
	
	public void load(CompoundTag tag)
	{
		this.progress = tag.getInt("progress");
		if(this.type == ProgressTracker.RunType.BUTTON_OVERRIDE)
			this.overrideStop = tag.getBoolean("overrideStop");
	}
	
	public void save(CompoundTag tag)
	{
		tag.putInt("progress", this.progress);
		if(this.type == ProgressTracker.RunType.BUTTON_OVERRIDE)
			tag.putBoolean("overrideStop", this.overrideStop);
	}
	
	@Override
	public int get(int index)
	{
		if(index == 0)
			return this.progress;
		else if(index == 1)
			return this.overrideStop ? 1 : 0;
		else if(index == 2)
			return this.ready ? 1 : 0;
		return 0;
	}
	
	@Override
	public void set(int index, int value)
	{
		if(index == 0)
			this.progress = value;
		else if(index == 1)
			this.overrideStop = value != 0;
		else if(index == 2)
			this.ready = value != 0;
	}
	
	@Override
	public int getCount()
	{
		return 3;
	}
	
	public enum RunType
	{
		AUTOMATIC,
		BUTTON,
		BUTTON_OVERRIDE
	}
}
