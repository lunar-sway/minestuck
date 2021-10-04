package com.mraof.minestuck.world.lands;

import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.IServerConfiguration;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraft.world.storage.ISpawnWorldInfo;

public class ForcedWeatherWorldInfo implements ISpawnWorldInfo
{
	private final ISpawnWorldInfo wrapped;
	private final LandProperties.ForceType forceRain, forceThunder;
	
	public ForcedWeatherWorldInfo(ISpawnWorldInfo wrapped,
								  LandProperties.ForceType forceRain, LandProperties.ForceType forceThunder)
	{
		this.wrapped = wrapped;
		this.forceRain = forceRain;
		this.forceThunder = forceThunder;
	}
	
	@Override
	public boolean isRaining()
	{
		if (forceRain == LandProperties.ForceType.ON)
			return true;
		else if (forceRain == LandProperties.ForceType.OFF)
			return false;
		else return wrapped.isRaining();
	}
	
	@Override
	public boolean isThundering()
	{
		if (forceThunder == LandProperties.ForceType.ON)
			return true;
		else if (forceThunder == LandProperties.ForceType.OFF)
			return false;
		else return wrapped.isThundering();
	}
	
	@Override
	public void setXSpawn(int x)
	{
		wrapped.setXSpawn(x);
	}
	
	@Override
	public void setYSpawn(int y)
	{
		wrapped.setYSpawn(y);
	}
	
	@Override
	public void setZSpawn(int z)
	{
		wrapped.setZSpawn(z);
	}
	
	@Override
	public void setSpawnAngle(float angle)
	{
		wrapped.setSpawnAngle(angle);
	}
	
	@Override
	public int getXSpawn()
	{
		return wrapped.getXSpawn();
	}
	
	@Override
	public int getYSpawn()
	{
		return wrapped.getYSpawn();
	}
	
	@Override
	public int getZSpawn()
	{
		return wrapped.getZSpawn();
	}
	
	@Override
	public float getSpawnAngle()
	{
		return wrapped.getSpawnAngle();
	}
	
	@Override
	public long getGameTime()
	{
		return wrapped.getGameTime();
	}
	
	@Override
	public long getDayTime()
	{
		return wrapped.getDayTime();
	}
	
	@Override
	public void setRaining(boolean isRaining)
	{
		wrapped.setRaining(isRaining);
	}
	
	@Override
	public boolean isHardcore()
	{
		return wrapped.isHardcore();
	}
	
	@Override
	public GameRules getGameRules()
	{
		return wrapped.getGameRules();
	}
	
	@Override
	public Difficulty getDifficulty()
	{
		return wrapped.getDifficulty();
	}
	
	@Override
	public boolean isDifficultyLocked()
	{
		return wrapped.isDifficultyLocked();
	}
}