package com.mraof.minestuck.world.lands;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.timers.TimerQueue;

import javax.annotation.Nullable;
import java.util.UUID;

public class LandWorldInfo implements ServerLevelData
{
	private final ServerLevelData wrapped;
	private final LandProperties.ForceType forceRain, forceThunder;
	private final long dayTime;
	
	public LandWorldInfo(ServerLevelData wrapped,
						 LandProperties.ForceType forceRain, LandProperties.ForceType forceThunder, double skylight)
	{
		this.wrapped = wrapped;
		this.forceRain = forceRain;
		this.forceThunder = forceThunder;
		this.dayTime = dayTimeFromSkylight(skylight);
	}
	
	private static final int SUNSET_START = 11868, SUNSET_END = 13670;
	
	private static long dayTimeFromSkylight(double desiredLight) {
		final int STEPS = 20;
		for(int i = 0; i <= STEPS; i++)
		{
			int timeTicks = Math.round(Mth.lerp(((float) i)/STEPS, SUNSET_START, SUNSET_END));
			double timeFraction = Mth.frac(timeTicks / 24000D - 0.25D);
			timeFraction = (2D*timeFraction + (0.5D - Math.cos(timeFraction * Math.PI)/2D))/3D;
			double light = 0.5D + 2D*Math.cos(timeFraction * 2D*Math.PI);
			if(desiredLight >= light)
				return timeTicks;
		}
		return SUNSET_END;
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
	
	@Override
	public long getDayTime()
	{
		return dayTime;
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
	
	@Nullable
	@Override
	public UUID getWanderingTraderId()
	{
		return wrapped.getWanderingTraderId();
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
	public void setWorldBorder(WorldBorder.Settings border)
	{
		wrapped.setWorldBorder(border);
	}
	
	@Override
	public WorldBorder.Settings getWorldBorder()
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
	public TimerQueue<MinecraftServer> getScheduledEvents()
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