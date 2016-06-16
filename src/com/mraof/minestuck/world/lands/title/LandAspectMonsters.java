package com.mraof.minestuck.world.lands.title;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;

import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;

public class LandAspectMonsters extends TitleLandAspect
{
	
	private final String name;
	private final List<TitleLandAspect> variations;
	private final List<SpawnListEntry> monsterList;
	
	public LandAspectMonsters()
	{
		this("monsters");
	}
	
	public LandAspectMonsters(String name)
	{
		this.variations = new ArrayList<TitleLandAspect>();
		this.name = name;
		this.monsterList = new ArrayList<SpawnListEntry>();
		if(this.name.equals("monsters"))
		{
			monsterList.add(new SpawnListEntry(EntityCreeper.class, 1, 1, 1));
			monsterList.add(new SpawnListEntry(EntitySpider.class, 1, 1, 2));
			monsterList.add(new SpawnListEntry(EntityZombie.class, 1, 1, 2));
			variations.add(this);
			variations.add(new LandAspectMonsters("monsters_dead"));
		}
		else if(this.name.equals("monsters_dead"))
		{
			monsterList.add(new SpawnListEntry(EntityZombie.class, 2, 1, 3));
			monsterList.add(new SpawnListEntry(EntitySkeleton.class, 1, 1, 2));
		}
	}
	
	@Override
	public String getPrimaryName()
	{
		return name;
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"monster"};
	}
	
	@Override
	protected void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
		chunkProvider.dayCycle = 2;
		chunkProvider.monsterList.addAll(this.monsterList);
		
		if(chunkProvider.decorators != null)
		{
			
		}
		
		chunkProvider.mergeFogColor(new Vec3d(0.1, 0, 0), 0.5F);
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return aspect.getDayCycleMode() != 1;
	}
	
	@Override
	public List<TitleLandAspect> getVariations()
	{
		return variations;
	}
	
	@Override
	public TitleLandAspect getPrimaryVariant()
	{
		return LandAspectRegistry.fromNameTitle("monsters");
	}
	
}