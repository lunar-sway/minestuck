package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.MultipleRandomFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;

public class ForestLandType extends TerrainLandType
{
	public static final String FORESTS = "minestuck.forests";
	public static final String TREES = "minestuck.trees";
	public static final String BOREAL_FORESTS = "minestuck.boreal_forests";
	public static final String TAIGAS = "minestuck.taigas";
	public static final String COLD_FORESTS = "minestuck.cold_forests";
	
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "forest");
	private final Variant type;
	
	public ForestLandType(Variant variation)
	{
		super(GROUP_NAME);
		type = variation;
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", type == Variant.TAIGA ? Blocks.PODZOL.defaultBlockState() : Blocks.GRASS_BLOCK.defaultBlockState());
		registry.setBlockState("upper", Blocks.DIRT.defaultBlockState());
		if(type == Variant.TAIGA) {
			registry.setBlockState("structure_primary", Blocks.SPRUCE_WOOD.defaultBlockState());
			registry.setBlockState("structure_primary_decorative", MSBlocks.FROST_WOOD.defaultBlockState());
		} else {
			registry.setBlockState("structure_primary", MSBlocks.VINE_WOOD.defaultBlockState());
			registry.setBlockState("structure_primary_decorative", MSBlocks.FLOWERY_VINE_WOOD.defaultBlockState());
		}
		registry.setBlockState("structure_secondary", Blocks.STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", Blocks.CHISELED_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("village_path", Blocks.GRASS_PATH.defaultBlockState());
		registry.setBlockState("bush", Blocks.FERN.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.GREEN_WOOL.defaultBlockState());
		if(type == Variant.TAIGA) {
			registry.setBlockState("structure_wool_3", Blocks.LIGHT_BLUE_WOOL.defaultBlockState());
		} else {
			registry.setBlockState("structure_wool_3", Blocks.BROWN_WOOL.defaultBlockState());
		}
	}
	
	@Override
	public String[] getNames()
	{
		if(type == Variant.FOREST) {
			return new String[] {FORESTS, TREES};
		} else {
			return new String[] {TAIGAS, BOREAL_FORESTS, COLD_FORESTS};
		}
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = this.type == Variant.TAIGA ? Biome.Category.TAIGA : Biome.Category.FOREST;
		properties.forceRain = LandProperties.ForceType.DEFAULT;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_FOREST);
			
			switch(this.type)
			{
				case FOREST:
					builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR
							.configured(new MultipleRandomFeatureConfig(ImmutableList.of(Features.BIRCH.weighted(0.2F), Features.FANCY_OAK.weighted(0.1F)), Features.OAK))
							.decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(5, 0.1F, 1))));
					break;
				case TAIGA:
					builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR
							.configured(new MultipleRandomFeatureConfig(ImmutableList.of(Features.PINE.weighted(1/3F)), Features.SPRUCE))
							.decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(5, 0.1F, 1))));
					break;
			}
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
					.configured(Features.Configs.JUNGLE_GRASS_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(4));
			
			switch(this.type)
			{
				case FOREST:
					builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_SELECTOR
							.configured(new MultipleRandomFeatureConfig(ImmutableList.of(Features.BIRCH.weighted(0.2F), Features.FANCY_OAK.weighted(0.1F)), Features.OAK))
							.decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(10, 0.1F, 1))));
					break;
				case TAIGA:
					builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION,Feature.RANDOM_SELECTOR
							.configured(new MultipleRandomFeatureConfig(ImmutableList.of(Features.PINE.weighted(1/3F)), Features.SPRUCE))
							.decorated(Features.Placements.HEIGHTMAP_SQUARE).decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(10, 0.1F, 1))));
					break;
			}
		} else if(type == LandBiomeType.OCEAN)
		{
			DefaultBiomeFeatures.addSwampClayDisk(builder);
		}
		
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 33))
				.range(256).squared().count(10));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33))
				.range(256).squared().count(8));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 17))
				.range(128).squared().count(20));
		DefaultBiomeFeatures.addExtraEmeralds(builder);
		
	}
	@Override
	public Vector3d getFogColor()
	{
		return new Vector3d(0.0D, 1.0D, 0.6D);
	}
	
	@Override
	public Vector3d getSkyColor()
	{
		return new Vector3d(0.4D, 0.7D, 1.0D);
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return MSEntityTypes.IGUANA;
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		addIguanaVillageCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		addIguanaVillagePieces(register, random);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return type == Variant.TAIGA ? MSSoundEvents.MUSIC_TAIGA : MSSoundEvents.MUSIC_FOREST;
	}
	
	public enum Variant
	{
		FOREST,
		TAIGA
	}
}