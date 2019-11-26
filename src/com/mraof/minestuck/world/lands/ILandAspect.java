package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface ILandAspect<A extends ILandAspect> extends IForgeRegistryEntry<A>
{
	/**
	 * Returns a list of strings used in giving a land a random name.
	 */
	String[] getNames();
	
	boolean canBePickedAtRandom();
	
	ResourceLocation getGroup();
	
	void registerBlocks(StructureBlockRegistry blocks);
	
	default void setBiomeSettings(LandBiomeHolder settings)
	{}
	
	default void setGenSettings(LandGenSettings settings)
	{}
	
	default void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{}
}
