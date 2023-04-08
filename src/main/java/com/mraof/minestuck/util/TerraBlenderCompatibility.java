package com.mraof.minestuck.util;

import net.minecraft.world.level.biome.BiomeSource;
import terrablender.worldgen.IExtendedBiomeSource;

/**
 * A hack to fix the state of BiomeSource.featuresPerStep,
 * which TerraBlender may fail to leave in a correct state under certain circumstances (which we happen to have)
 * The problem appear to have been fixed in TerraBlender for 1.19, so this fix will not be needed in the future
 * (See https://github.com/Glitchfiend/TerraBlender/commit/a1250d486782d445e17147e0d0b32a59afff45ad)
 */
public class TerraBlenderCompatibility
{
	public static void fixBiomeSource(BiomeSource biomeSource)
	{
		((IExtendedBiomeSource) biomeSource).updateFeaturesPerStep();
	}
}
