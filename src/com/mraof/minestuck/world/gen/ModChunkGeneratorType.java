package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.world.lands.gen.LandChunkGenerator;
import com.mraof.minestuck.world.lands.gen.LandGenSettings;
import net.minecraft.world.gen.ChunkGeneratorType;

public class ModChunkGeneratorType
{
	public static final ChunkGeneratorType<SkaiaGenSettings, SkaiaChunkGenerator> SKAIA = new ChunkGeneratorType<>(SkaiaChunkGenerator::new, false, SkaiaGenSettings::new);
	public static final ChunkGeneratorType<LandGenSettings, LandChunkGenerator> LANDS = new ChunkGeneratorType<>(LandChunkGenerator::new, false, LandGenSettings::new);
	//TODO these should probably be registered
}