package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageCenter;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillagePieces;
import net.minecraft.core.HolderGetter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.function.Consumer;

/**
 * Simple interface for all functions that are available for both types of land types.
 * A new land type should extend either {@link com.mraof.minestuck.world.lands.terrain.TerrainLandType}
 * or {@link com.mraof.minestuck.world.lands.title.TitleLandType}.
 */
public interface ILandType
{
	/**
	 * Returns a list of translation keys that can be used for the name of this land type.
	 * One is picked for each land dimension when the dimension is first created.
	 */
	String[] getNames();
	
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
	 * When you register a structure set normally, it will show up in all lands.
	 * Creating a structure set and giving it to the consumer lets the structure set be used by lands with this land type specifically.
	 */
	default void addStructureSets(Consumer<StructureSet> consumer, HolderGetter<Structure> structureLookup)
	{}
	
	/**
	 * Override this to add additional spawn info to a land biome.
	 * @param type the type of biome that the spawn info builder is for.
	 */
	default void setSpawnInfo(MobSpawnSettings.Builder builder, LandBiomeType type)
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
	default void addVillagePieces(PieceRegister register, RandomSource random)
	{}
	
	interface PieceRegister
	{
		void add(ConsortVillagePieces.PieceFactory factory, int weight, int limit);
	}
	
	default SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_DEFAULT.get();
	}
}
