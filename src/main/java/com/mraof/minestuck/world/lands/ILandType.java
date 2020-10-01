package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.structure.village.ConsortVillageCenter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiConsumer;

public interface ILandType<A extends ILandType<?>> extends IForgeRegistryEntry<A>
{
	/**
	 * Returns a list of strings used in giving a land a random name.
	 */
	String[] getNames();
	
	boolean canBePickedAtRandom();
	
	ResourceLocation getGroup();
	
	void registerBlocks(StructureBlockRegistry blocks);
	
	default void setProperties(LandProperties properties)
	{}
	
	default void setGenSettings(LandGenSettings settings)
	{}
	
	default void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{}
	
	default void addVillageCenters(BiConsumer<ConsortVillageCenter.CenterFactory, Integer> factoryWeightConsumer)
	{}
}
