package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class FloraLandType extends TerrainLandType
{
	public static final String FLORA = "minestuck.flora";
	public static final String FLOWERS = "minestuck.flowers";
	public static final String THORNS = "minestuck.thorns";
	
	private static final Vec3 fogColor = new Vec3(0.5D, 0.6D, 0.9D);
	private static final Vec3 skyColor = new Vec3(0.6D, 0.8D, 0.6D);
	
	public FloraLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", Blocks.GRASS_BLOCK.defaultBlockState());
		registry.setBlockState("surface_rough", Blocks.COARSE_DIRT.defaultBlockState());
		registry.setBlockState("upper", Blocks.DIRT.defaultBlockState());
		registry.setBlockState("ocean", MSBlocks.BLOOD.get().defaultBlockState());
		registry.setBlockState("structure_primary", MSBlocks.MOSSY_DECREPIT_STONE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_decorative", MSBlocks.FLOWERY_MOSSY_STONE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_mossy", MSBlocks.MOSSY_DECREPIT_STONE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_stairs", Blocks.STONE_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_secondary", Blocks.MOSSY_COBBLESTONE.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.FLOWERY_MOSSY_COBBLESTONE.get().defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", Blocks.DARK_OAK_STAIRS.defaultBlockState());
		registry.setBlockState("village_path", Blocks.DIRT_PATH.defaultBlockState());
		registry.setBlockState("bush", Blocks.FERN.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.CYAN_WOOL.defaultBlockState());
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{FLORA, FLOWERS, THORNS};
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.BiomeCategory.FOREST;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST);
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_FLOWER_FOREST);
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.DARK_OAK.getHolder().orElseThrow());
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.UNCOMMON_BROKEN_SWORD.getHolder().orElseThrow());
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.RARE_STRAWBERRY_PATCH.getHolder().orElseThrow());
		}
		
		if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.BLOOD_POOL.getHolder().orElseThrow());
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.SURFACE_FOSSIL.getHolder().orElseThrow());
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.BROKEN_SWORD.getHolder().orElseThrow());
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.STRAWBERRY_PATCH.getHolder().orElseThrow());
		}
		
		if(type == LandBiomeType.OCEAN)
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(Blocks.CLAY.defaultBlockState(), UniformInt.of(2, 3), 1, Lists.newArrayList(blocks.getBlockState("ocean_surface"), Blocks.CLAY.defaultBlockState())),
					InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
		}
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 33),
				CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(256)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33),
				CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(256)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 17),
				CountPlacement.of(13), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.EMERALD_ORE.defaultBlockState(), 3),
				CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.DIAMOND_ORE.defaultBlockState(), 3),
				CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.LAPIS_ORE.defaultBlockState(), 3),
				CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.STONE_QUARTZ_ORE.get().defaultBlockState(), 5),
				CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		
		builder.addCarver(GenerationStep.Carving.AIR, Carvers.CAVE);
		
	}
	
	@Override
	public Vec3 getFogColor()
	{
		return fogColor;
	}
	
	@Override
	public Vec3 getSkyColor()
	{
		return skyColor;
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
		return MSSoundEvents.MUSIC_FLORA;
	}
}