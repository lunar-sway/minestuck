package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class FrogsLandType extends TitleLandType
{
	public static final String FROGS = "minestuck.frogs";
	
	public FrogsLandType()
	{
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{FROGS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock(StructureBlockRegistry.STRUCTURE_WOOL_2, Blocks.GREEN_WOOL);
		registry.setBlock(StructureBlockRegistry.CARPET, Blocks.LIME_CARPET);
	}
	
	@Override
	public void setSpawnInfo(MobSpawnSettings.Builder builder, LandBiomeType type)
	{
		if(type == LandBiomeType.NORMAL)
		{
			builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(MSEntityTypes.FROG.get(), 3, 0, 2));
		}
		
		if(type == LandBiomeType.ROUGH)
		{
			builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(MSEntityTypes.FROG.get(), 15, 1, 6));
		}
	}
	
	@Override
	public void addExtensions(HolderLookup.Provider provider, StructureBlockRegistry blocks)
	{
		HolderLookup.RegistryLookup<PlacedFeature> features = provider.lookupOrThrow(Registries.PLACED_FEATURE);
		
		addFeatureExtension(features, GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.WATERLILY_PATCH, LandBiomeType.OCEAN);
		
		addFeatureExtension(features, GenerationStep.Decoration.SURFACE_STRUCTURES, MSPlacedFeatures.FROG_RUINS, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
		addFeatureExtension(features, GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPACE_TREE, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandType otherType)
	{
		return !otherType.is(MSTags.TerrainLandTypes.IS_DANGEROUS);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_FROGS.get();
	}
}