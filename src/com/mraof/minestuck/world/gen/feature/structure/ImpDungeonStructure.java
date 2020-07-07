package com.mraof.minestuck.world.gen.feature.structure;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.Minestuck;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;

import java.util.function.Function;

public class ImpDungeonStructure extends ScatteredStructure<NoFeatureConfig>
{
	public ImpDungeonStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}
	
	@Override
	protected int getSeedModifier()
	{
		return 34527185;
	}
	
	@Override
	public IStartFactory getStartFactory()
	{
		return ImpDungeonStart::new;
	}
	
	@Override
	public String getStructureName()
	{
		return Minestuck.MOD_ID + ":imp_dungeon";
	}
	
	@Override
	public int getSize()
	{
		return 5;
	}
	
	@Override
	protected int getBiomeFeatureDistance(ChunkGenerator<?> chunkGenerator)
	{
		return 16;
	}
	
	@Override
	protected int getBiomeFeatureSeparation(ChunkGenerator<?> chunkGenerator)
	{
		return 4;
	}
}