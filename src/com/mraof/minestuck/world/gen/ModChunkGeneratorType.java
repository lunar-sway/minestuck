package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.lands.gen.ChunkGeneratorLands;
import com.mraof.minestuck.world.lands.gen.LandGenSettings;
import net.minecraft.world.gen.ChunkGeneratorType;

public class ModChunkGeneratorType
{
	public static final ChunkGeneratorType<SkaiaGenSettings, ChunkGeneratorSkaia> SKAIA = ChunkGeneratorType.register(Minestuck.MOD_ID+":skaia", ChunkGeneratorSkaia::new, SkaiaGenSettings::new, false);
	public static final ChunkGeneratorType<LandGenSettings, ChunkGeneratorLands> LANDS = ChunkGeneratorType.register(Minestuck.MOD_ID+":lands", ChunkGeneratorLands::new, LandGenSettings::new, false);
	
}