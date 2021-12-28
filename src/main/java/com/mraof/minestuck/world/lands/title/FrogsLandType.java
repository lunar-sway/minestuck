package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;

public class FrogsLandType extends TitleLandType
{
	public static final String FROGS = "minestuck.frogs";
	
	public FrogsLandType()
	{
		super(EnumAspect.SPACE, false);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{FROGS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.GREEN_WOOL.defaultBlockState());
		registry.setBlockState("carpet", Blocks.LIME_CARPET.defaultBlockState());
		registry.setBlockState("aspect_sapling", MSBlocks.SPACE_ASPECT_SAPLING.defaultBlockState());
	}
	
	@Override
	public void setSpawnInfo(MobSpawnInfo.Builder builder, LandBiomeType type)
	{
		if(type == LandBiomeType.NORMAL)
		{
			builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(MSEntityTypes.FROG, 1, 0, 1));
		}
		
		if(type == LandBiomeType.ROUGH)
		{
			builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(MSEntityTypes.FROG, 10, 1, 6));
		}
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		if(type == LandBiomeType.OCEAN)
		{
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configured(
					new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.LILY_PAD.defaultBlockState()), SimpleBlockPlacer.INSTANCE).tries(10).build())
					.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE));
		}
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandType otherType)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		otherType.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_FROGS;
	}
}