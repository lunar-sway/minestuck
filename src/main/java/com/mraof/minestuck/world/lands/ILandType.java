package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.structure.village.ConsortVillageCenter;
import com.mraof.minestuck.world.gen.feature.structure.village.ConsortVillagePieces;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Random;

/**
 * Simple interface for all functions that are available for both types of land types.
 * A new land type should extend either {@link com.mraof.minestuck.world.lands.terrain.TerrainLandType}
 * or {@link com.mraof.minestuck.world.lands.title.TitleLandType}.
 */
public interface ILandType<A extends ILandType<?>> extends IForgeRegistryEntry<A>
{
	/**
	 * Returns a list of translation keys that can be used for the name of this land type.
	 * One is picked for each land dimension when the dimension is first created.
	 */
	String[] getNames();
	
	/**
	 * Returns true if the land type should be available when a land type is picked randomly.
	 */
	boolean canBePickedAtRandom();
	
	/**
	 * Returns a group id. All land types with the same group are considered equal when picking a random land type.
	 * Consort dialogue and loot conditions may also match against a group instead of specific land types.
	 */
	ResourceLocation getGroup();
	
	/**
	 * Override this function to register blocks to the block palette of a land.
	 */
	void registerBlocks(StructureBlockRegistry blocks);
	
	/**
	 * Override this to set a collection of different land properties, such as sky color and weather info.
	 */
	default void setProperties(LandProperties properties)
	{}
	
	/**
	 * Override this to set various world-generation settings on lands, such as the land gate structure and biome selection settings.
	 */
	default void setGenSettings(LandGenSettings settings)
	{}
	
	/**
	 * Override this to add additional spawn info to a land biome.
	 * @param type the type of biome that the spawn info builder is for.
	 */
	default void setSpawnInfo(MobSpawnInfo.Builder builder, LandBiomeType type)
	{}
	
	/**
	 * Override this to set biome generation settings.
	 */
	default void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{}
	
	/**
	 * Override this to register land type-specific village centers.
	 */
	default void addVillageCenters(CenterRegister register)
	{}
	
	interface CenterRegister
	{
		void add(ConsortVillageCenter.CenterFactory factory, int weight);
	}
	
	/**
	 * Override this to register land type-specific village buildings.
	 */
	default void addVillagePieces(PieceRegister register, Random random)
	{}
	
	interface PieceRegister
	{
		void add(ConsortVillagePieces.PieceFactory factory, int weight, int limit);
	}
	
	default SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_DEFAULT;
	}
}
