package com.mraof.minestuck.world.lands;

import net.minecraft.command.TimerCallbackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.IServerWorldInfo;

import java.util.UUID;

public class LandWorldInfo implements IServerWorldInfo
{
	private final IServerWorldInfo wrapped;
	private final LandProperties.ForceType forceRain, forceThunder;
	
	public LandWorldInfo(IServerWorldInfo wrapped,
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
	public int getXSpawn()
	{
		return 0;
	}
	
	@Override
	public int getZSpawn()
	{
		return 0;
	}
	
	// Calls to wrapped info
	
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
	public int getYSpawn()
	{
		return wrapped.getYSpawn();
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
	
	@Override
	public String getLevelName()
	{
		return wrapped.getLevelName();
	}
	
	@Override
	public void setThundering(boolean isThundering)
	{
		wrapped.setThundering(isThundering);
	}
	
	@Override
	public int getRainTime()
	{
		return wrapped.getRainTime();
	}
	
	@Override
	public void setRainTime(int rainTime)
	{
		wrapped.setRainTime(rainTime);
	}
	
	@Override
	public void setThunderTime(int thunderTime)
	{
		wrapped.setThunderTime(thunderTime);
	}
	
	@Override
	public int getThunderTime()
	{
		return wrapped.getThunderTime();
	}
	
	@Override
	public int getClearWeatherTime()
	{
		return wrapped.getClearWeatherTime();
	}
	
	@Override
	public void setClearWeatherTime(int weatherTime)
	{
		wrapped.setClearWeatherTime(weatherTime);
	}
	
	@Override
	public int getWanderingTraderSpawnDelay()
	{
		return wrapped.getWanderingTraderSpawnDelay();
	}
	
	@Override
	public void setWanderingTraderSpawnDelay(int spawnDelay)
	{
		wrapped.setWanderingTraderSpawnDelay(spawnDelay);
	}
	
	@Override
	public int getWanderingTraderSpawnChance()
	{
		return wrapped.getWanderingTraderSpawnChance();
	}
	
	@Override
	public void setWanderingTraderSpawnChance(int spawnChance)
	{
		wrapped.setWanderingTraderSpawnChance(spawnChance);
	}
	
	@Override
	public void setWanderingTraderId(UUID id)
	{
		wrapped.setWanderingTraderId(id);
	}
	
	@Override
	public GameType getGameType()
	{
		return wrapped.getGameType();
	}
	
	@Override
	public void setWorldBorder(WorldBorder.Serializer border)
	{
		wrapped.setWorldBorder(border);
	}
	
	@Override
	public WorldBorder.Serializer getWorldBorder()
	{
		return wrapped.getWorldBorder();
	}
	
	@Override
	public boolean isInitialized()
	{
		return wrapped.isInitialized();
	}
	
	@Override
	public void setInitialized(boolean isInitialized)
	{
		wrapped.setInitialized(isInitialized);
	}
	
	@Override
	public boolean getAllowCommands()
	{
		return wrapped.getAllowCommands();
	}
	
	@Override
	public void setGameType(GameType gameType)
	{
		wrapped.setGameType(gameType);
	}
	
	@Override
	public TimerCallbackManager<MinecraftServer> getScheduledEvents()
	{
		return wrapped.getScheduledEvents();
	}
	
	@Override
	public void setGameTime(long gameTime)
	{
		wrapped.setGameTime(gameTime);
	}
	
	@Override
	public void setDayTime(long dayTime)
	{
		wrapped.setDayTime(dayTime);
	}
}