package com.mraof.minestuck.blockentity.machine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BooleanSupplier;

@ParametersAreNonnullByDefault
public final class ProgressTracker implements ContainerData
{
	public static final int PROGRESS_INDEX = 0, RUN_INDEX = 1, LOOPING_INDEX = 2;
	
	private final RunType type;
	private final int maxProgress;
	private int progress;
	private boolean shouldRun;
	private boolean isLooping;
	
	public ProgressTracker(RunType type, int maxProgress)
	{
		this.type = type;
		this.maxProgress = maxProgress;
		resetProgress();
	}
	
	public void resetProgress()
	{
		this.progress = 0;
		this.shouldRun = false;
		updateRunAndLooping();
	}
	
	private void updateRunAndLooping()
	{
		this.isLooping |= this.type == RunType.AUTOMATIC;
		this.isLooping &= this.type != RunType.ONCE;
		this.shouldRun |= this.isLooping;
	}
	
	public void tick(BooleanSupplier isValid, Runnable onComplete)
	{
		if(!this.shouldRun || !isValid.getAsBoolean())
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
		if(this.type == ProgressTracker.RunType.ONCE_OR_LOOPING)
			this.isLooping = tag.getBoolean("isLooping");
	}
	
	public void save(CompoundTag tag)
	{
		tag.putInt("progress", this.progress);
		if(this.type == ProgressTracker.RunType.ONCE_OR_LOOPING)
			tag.putBoolean("isLooping", this.isLooping);
	}
	
	@Override
	public int get(int index)
	{
		return switch(index)
				{
					case PROGRESS_INDEX -> this.progress;
					case RUN_INDEX -> this.shouldRun ? 1 : 0;
					case LOOPING_INDEX -> this.isLooping ? 1 : 0;
					default -> 0;
				};
	}
	
	@Override
	public void set(int index, int value)
	{
		switch(index)
		{
			case PROGRESS_INDEX -> this.progress = value;
			case RUN_INDEX -> this.shouldRun = value != 0;
			case LOOPING_INDEX -> this.isLooping = value != 0;
		}
		updateRunAndLooping();
	}
	
	@Override
	public int getCount()
	{
		return 3;
	}
	
	public enum RunType
	{
		AUTOMATIC,
		ONCE,
		ONCE_OR_LOOPING
	}
}
