package com.mraof.minestuck.blockentity.machine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BooleanSupplier;

/**
 * An object for tracking progress for a machine by being called each tick.
 */
@ParametersAreNonnullByDefault
public final class ProgressTracker implements ContainerData
{
	public static final int PROGRESS_INDEX = 0, RUN_INDEX = 1, LOOPING_INDEX = 2;
	
	private final RunType type;
	private final int maxProgress;
	private final Runnable onChanged;
	private final BooleanSupplier isValid;
	private int progress = 0;
	private boolean shouldRun = false;
	private boolean isLooping = false;
	
	public ProgressTracker(RunType type, int maxProgress, Runnable onChanged, BooleanSupplier isValid)
	{
		this.type = type;
		this.maxProgress = maxProgress;
		this.onChanged = onChanged;
		this.isValid = isValid;
		
		updateRunAndLooping();
	}
	
	public void resetProgress()
	{
		if(this.shouldRun)
		{
			boolean changed = this.progress != 0;
			this.progress = 0;
			this.shouldRun = false;
			updateRunAndLooping();
			if(changed)
				this.onChanged.run();
		}
	}
	
	public void setShouldRun(boolean shouldRun)
	{
		if(shouldRun)
			this.shouldRun |= this.isValid.getAsBoolean();
		else
			resetProgress();
	}
	
	public void setIsLooping(boolean isLooping)
	{
		boolean wasLooping = this.isLooping;
		this.isLooping = isLooping;
		updateRunAndLooping();
		if(wasLooping != this.isLooping)
			this.onChanged.run();
	}
	
	private void updateRunAndLooping()
	{
		this.isLooping |= this.type == RunType.AUTOMATIC;
		this.isLooping &= this.type != RunType.ONCE;
		this.shouldRun |= this.isLooping;
	}
	
	public void tick(Runnable onComplete)
	{
		if(!this.shouldRun || !this.isValid.getAsBoolean())
		{
			this.resetProgress();
			return;
		}
		
		this.progress++;
		this.onChanged.run();
		
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
		this.shouldRun = this.progress != 0 || this.isLooping;
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
					default -> throw new UnsupportedOperationException("Unknown data index was used: " + index);
				};
	}
	
	@Override
	public void set(int index, int value)
	{
		switch(index)
		{
			case PROGRESS_INDEX -> throw new UnsupportedOperationException("Progress shouldn't be changed from outside");
			case RUN_INDEX -> this.setShouldRun(value != 0);
			case LOOPING_INDEX -> this.setIsLooping(value != 0);
			default -> throw new UnsupportedOperationException("Unknown data index was used: " + index);
		}
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
