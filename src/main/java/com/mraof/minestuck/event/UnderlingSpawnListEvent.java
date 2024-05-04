package com.mraof.minestuck.event;

import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.bus.api.Event;
import java.util.List;

public class UnderlingSpawnListEvent extends Event
{
	private final int difficultyRating;
	private final List<MobSpawnSettings.SpawnerData> spawnList;
	
	public UnderlingSpawnListEvent(int difficultyRating, List<MobSpawnSettings.SpawnerData> spawnList)
	{
		this.difficultyRating = difficultyRating;
		this.spawnList = spawnList;
	}
	
	public int getDifficultyRating()
	{
		return difficultyRating;
	}
	
	public void addEntry(MobSpawnSettings.SpawnerData entry)
	{
		spawnList.add(entry);
	}
}