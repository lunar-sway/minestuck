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
	
	default void setSpawnInfo(MobSpawnInfo.Builder builder, LandBiomeType type)
	{}
	
	default void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{}
	
	default void addVillageCenters(CenterRegister register)
	{}
	
	interface CenterRegister
	{
		void add(ConsortVillageCenter.CenterFactory factory, int weight);
	}
	
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
