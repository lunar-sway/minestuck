package com.mraof.minestuck.event;

import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class UnderlingSpawnListEvent extends Event
{
	private final int difficultyRating;
	private final List<MobSpawnInfo.Spawners> spawnList;
	
	public UnderlingSpawnListEvent(int difficultyRating, List<MobSpawnInfo.Spawners> spawnList)
	{
		this.difficultyRating = difficultyRating;
		this.spawnList = spawnList;
	}
	
	public int getDifficultyRating()
	{
		return difficultyRating;
	}
	
	public void addEntry(MobSpawnInfo.Spawners entry)
	{
		spawnList.add(entry);
	}
}