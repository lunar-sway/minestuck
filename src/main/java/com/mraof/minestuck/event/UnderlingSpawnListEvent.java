package com.mraof.minestuck.event;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class UnderlingSpawnListEvent extends Event
{
	private final int difficultyRating;
	private final List<Biome.SpawnListEntry> spawnList;
	
	public UnderlingSpawnListEvent(int difficultyRating, List<Biome.SpawnListEntry> spawnList)
	{
		this.difficultyRating = difficultyRating;
		this.spawnList = spawnList;
	}
	
	public int getDifficultyRating()
	{
		return difficultyRating;
	}
	
	public void addEntry(Biome.SpawnListEntry entry)
	{
		spawnList.add(entry);
	}
}