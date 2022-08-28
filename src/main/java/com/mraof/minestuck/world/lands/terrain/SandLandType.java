
package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageCenter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.UniformInt;
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

import java.util.List;
import java.util.Random;

public class SandLandType extends TerrainLandType
{
	public static final String SAND = "minestuck.sand";
	public static final String DUNES = "minestuck.dunes";
	public static final String DESERTS = "minestuck.deserts";
	public static final String LUSH_DESERTS = "minestuck.lush_deserts";
	
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "sand");
	private final Vec3 fogColor, skyColor;
	private final Variant type;
	
	public static TerrainLandType createSand()
	{
		return new SandLandType(Variant.SAND, new Builder(() -> MSEntityTypes.TURTLE).group(GROUP_NAME).biomeSet(MSBiomes.NO_RAIN_LAND));
	}
	
	public static TerrainLandType createLushDeserts()
	{
		return new SandLandType(Variant.LUSH_DESERTS, new Builder(() -> MSEntityTypes.TURTLE).group(GROUP_NAME).biomeSet(MSBiomes.NO_RAIN_LAND));
	}
	
	public static TerrainLandType createRedSand()
	{
		return new SandLandType(Variant.RED_SAND, new Builder(() -> MSEntityTypes.TURTLE).group(GROUP_NAME).biomeSet(MSBiomes.NO_RAIN_LAND));
	}
	
	private SandLandType(Variant variation, Builder builder)
	{
		super(builder);
		type = variation;
		
		if(type == Variant.SAND)
		{
			fogColor = new Vec3(0.99D, 0.8D, 0.05D);
			skyColor = new Vec3(0.8D, 0.8D, 0.1D);
		} else
		{
			fogColor = new Vec3(0.99D, 0.6D, 0.05D);
			skyColor = new Vec3(0.8D, 0.6D, 0.1D);
		}
		
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.SAND || type == Variant.LUSH_DESERTS)
		{
			registry.setGroundState(Blocks.SANDSTONE.defaultBlockState(), MSFillerBlockTypes.SANDSTONE);
			registry.setBlockState("upper", Blocks.SAND.defaultBlockState());
			registry.setBlockState("structure_primary", Blocks.SMOOTH_SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary_stairs", Blocks.SANDSTONE_STAIRS.defaultBlockState());
			registry.setBlockState("village_path", Blocks.RED_SAND.defaultBlockState());
			registry.setBlockState("cruxite_ore", MSBlocks.SANDSTONE_CRUXITE_ORE.get().defaultBlockState());
			registry.setBlockState("uranium_ore", MSBlocks.SANDSTONE_URANIUM_ORE.get().defaultBlockState());
		} else
		{
			registry.setGroundState(Blocks.RED_SANDSTONE.defaultBlockState(), MSFillerBlockTypes.RED_SANDSTONE);
			registry.setBlockState("upper", Blocks.RED_SAND.defaultBlockState());
			registry.setBlockState("structure_primary", Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_RED_SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary_stairs", Blocks.RED_SANDSTONE_STAIRS.defaultBlockState());
			registry.setBlockState("village_path", Blocks.SAND.defaultBlockState());
			registry.setBlockState("cruxite_ore", MSBlocks.RED_SANDSTONE_CRUXITE_ORE.get().defaultBlockState());
			registry.setBlockState("uranium_ore", MSBlocks.RED_SANDSTONE_URANIUM_ORE.get().defaultBlockState());
		}
		registry.setBlockState("structure_secondary", Blocks.STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", Blocks.CHISELED_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_planks", Blocks.ACACIA_PLANKS.defaultBlockState());
		registry.setBlockState("structure_planks_slab", Blocks.ACACIA_SLAB.defaultBlockState());
		registry.setBlockState("river", registry.getBlockState("upper"));
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.MAGENTA_WOOL.defaultBlockState());
	}
	
	@Override
	public String[] getNames()
	{
		if(type == Variant.LUSH_DESERTS) {
			return new String[] {LUSH_DESERTS};
		} else {
			return new String[] {SAND, DUNES, DESERTS};
		}
	}
	
	@Override
	public Biome.BiomeCategory getBiomeCategory()
	{
		return Biome.BiomeCategory.DESERT;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanThreshold = -1;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		if(this.type == Variant.LUSH_DESERTS)
		{
			
			if(type == LandBiomeType.NORMAL)
			{
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.OASIS.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.DESERT_BUSH_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DECORATED);
			}
			if(type == LandBiomeType.ROUGH)
			{
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_DESERT_BUSH_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.BLOOMING_CACTUS_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DECORATED);
			}
		} else
		{
			BiomeDefaultFeatures.addDesertVegetation(builder);
			if(type == LandBiomeType.NORMAL)
			{
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.OASIS.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DECORATED);
			}
			if(type == LandBiomeType.ROUGH)
			{
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.BLOOMING_CACTUS_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DECORATED);
			}
		}
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
				new DiskConfiguration(blocks.getBlockState("upper"), UniformInt.of(2, 6), 2, List.of(blocks.getBlockState("ground"))),
				CountPlacement.of(8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
		if(this.type != Variant.RED_SAND)
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
					new OreConfiguration(blocks.getGroundType(), MSBlocks.SANDSTONE_IRON_ORE.get().defaultBlockState(), 9),
					CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
					new OreConfiguration(blocks.getGroundType(), MSBlocks.SANDSTONE_GOLD_ORE.get().defaultBlockState(), 9),
					CountPlacement.of(6), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		} else
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
					new OreConfiguration(blocks.getGroundType(), MSBlocks.RED_SANDSTONE_IRON_ORE.get().defaultBlockState(), 9),
					CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
					new OreConfiguration(blocks.getGroundType(), MSBlocks.RED_SANDSTONE_GOLD_ORE.get().defaultBlockState(), 9),
					CountPlacement.of(6), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		}
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
	public void addVillageCenters(CenterRegister register)
	{
		addTurtleVillageCenters(register);
		
		register.add(ConsortVillageCenter.CactusPyramidCenter::new, 5);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		addTurtleVillagePieces(register, random);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return type == Variant.LUSH_DESERTS ? MSSoundEvents.MUSIC_LUSH_DESERTS : MSSoundEvents.MUSIC_SAND;
	}
	
	private enum Variant
	{
		SAND,
		LUSH_DESERTS,
		RED_SAND
	}
}
