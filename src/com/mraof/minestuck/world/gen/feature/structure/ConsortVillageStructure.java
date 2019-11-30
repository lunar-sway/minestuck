package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.datafixers.Dynamic;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.Random;
import java.util.function.Function;

public class ConsortVillageStructure extends Structure<NoFeatureConfig>	//TODO Implement this
{
	public ConsortVillageStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}
	
	@Override
	public boolean hasStartAt(ChunkGenerator<?> chunkGenerator, Random random, int chunkX, int chunkZ)
	{
		return false;
	}
	
	@Override
	public IStartFactory getStartFactory()
	{
		return null;
	}
	
	@Override
	public String getStructureName()
	{
		return "consort_village";
	}
	
	@Override
	public int getSize()
	{
		return 0;
	}
}