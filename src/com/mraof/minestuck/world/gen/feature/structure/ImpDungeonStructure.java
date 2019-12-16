package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.Minestuck;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.Random;
import java.util.function.Function;

public class ImpDungeonStructure extends Structure<NoFeatureConfig>	//TODO Implement this
{
	public ImpDungeonStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
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
		return Minestuck.MOD_ID + ":imp_dungeon";
	}
	
	@Override
	public int getSize()
	{
		return 0;
	}
}