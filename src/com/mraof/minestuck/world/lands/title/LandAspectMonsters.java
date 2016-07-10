package com.mraof.minestuck.world.lands.title;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome.SpawnListEntry;

import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;

public class LandAspectMonsters extends TitleLandAspect
{
	
	private final Variant type;
	private final List<TitleLandAspect> variations;
	private final List<SpawnListEntry> monsterList;
	
	public LandAspectMonsters()
	{
		this(Variant.MONSTERS);
	}
	
	public LandAspectMonsters(Variant type)
	{
		this.variations = new ArrayList<TitleLandAspect>();
		this.type = type;
		this.monsterList = new ArrayList<SpawnListEntry>();
		if(this.type == Variant.MONSTERS)
		{
			monsterList.add(new SpawnListEntry(EntityCreeper.class, 1, 1, 1));
			monsterList.add(new SpawnListEntry(EntitySpider.class, 1, 1, 2));
			monsterList.add(new SpawnListEntry(EntityZombie.class, 1, 1, 2));
			variations.add(this);
			variations.add(new LandAspectMonsters(Variant.MONSTERS_DEAD));
		}
		else if(this.type == Variant.MONSTERS_DEAD)
		{
			monsterList.add(new SpawnListEntry(EntityZombie.class, 2, 1, 3));
			monsterList.add(new SpawnListEntry(EntitySkeleton.class, 1, 1, 2));
		}
	}
	
	@Override
	public String getPrimaryName()
	{
		return type.getName();
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
		
		chunkProvider.mergeFogColor(new Vec3d(0.1, 0, 0), 0.5F);
	}
	
	@Override
	protected void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		
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
	
	public static enum Variant
	{
		MONSTERS,
		MONSTERS_DEAD;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}