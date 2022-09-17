package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.TurtleVillagePieces;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;

import java.util.List;
import java.util.Random;

public class SandstoneLandType extends TerrainLandType
{
	public static final String SANDSTONE = "minestuck.sandstone";
	public static final String STONY_DESERTS = "minestuck.stony_deserts";
	
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "sandstone");
	private final Variant type;
	
	public static TerrainLandType createSandstone()
	{
		return new SandstoneLandType(Variant.SANDSTONE, new Builder(MSEntityTypes.TURTLE::get).group(GROUP_NAME).names(SANDSTONE, STONY_DESERTS)
				.skylight(3/4F).fogColor(0.9, 0.7, 0.05).skyColor(0.8, 0.6, 0.2)
				.category(Biome.BiomeCategory.MESA).music(() -> MSSoundEvents.MUSIC_SANDSTONE));
	}
	
	public static TerrainLandType createRedSandstone()
	{
		return new SandstoneLandType(Variant.RED_SANDSTONE, new Builder(MSEntityTypes.TURTLE::get).group(GROUP_NAME).names(SANDSTONE, STONY_DESERTS)
				.skylight(3/4F).fogColor(0.7, 0.4, 0.05).skyColor(0.8, 0.5, 0.1)
				.category(Biome.BiomeCategory.MESA).music(() -> MSSoundEvents.MUSIC_SANDSTONE));
	}
	
	private SandstoneLandType(Variant type, Builder builder)
	{
		super(builder);
		this.type = type;
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.SANDSTONE)
		{
			registry.setBlock("sand", Blocks.SAND);
			registry.setBlock("ocean_surface", Blocks.SAND);
			registry.setBlock("upper", Blocks.SANDSTONE);
			registry.setBlock("structure_primary", Blocks.SMOOTH_SANDSTONE);
			registry.setBlock("structure_primary_decorative", Blocks.CHISELED_SANDSTONE);
			registry.setBlock("structure_primary_stairs", Blocks.SANDSTONE_STAIRS);
			registry.setBlock("village_path", Blocks.RED_SAND);
		} else
		{
			registry.setBlock("sand", Blocks.RED_SAND);
			registry.setBlock("ocean_surface", Blocks.RED_SAND);
			registry.setBlock("upper", Blocks.RED_SANDSTONE);
			registry.setBlock("structure_primary", Blocks.SMOOTH_RED_SANDSTONE);
			registry.setBlock("structure_primary_decorative", Blocks.CHISELED_RED_SANDSTONE);
			registry.setBlock("structure_primary_stairs", Blocks.RED_SANDSTONE_STAIRS);
			registry.setBlock("village_path", Blocks.SAND);
		}
		registry.setBlock("structure_secondary", Blocks.STONE_BRICKS);
		registry.setBlock("structure_secondary_decorative", Blocks.CHISELED_STONE_BRICKS);
		registry.setBlock("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS);
		registry.setBlock("structure_planks", Blocks.ACACIA_PLANKS);
		registry.setBlock("structure_planks_slab", Blocks.ACACIA_SLAB);
		registry.setBlock("torch", Blocks.REDSTONE_TORCH);
		registry.setBlock("wall_torch", Blocks.REDSTONE_WALL_TORCH);
		registry.setBlock("structure_wool_1", Blocks.WHITE_WOOL);
		registry.setBlock("structure_wool_3", Blocks.MAGENTA_WOOL);
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanThreshold = -0.6F;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		BlockState sand = blocks.getBlockState("sand");
		BlockState sandstone = blocks.getBlockState("upper");
		
		if(type != LandBiomeType.OCEAN)
		{
			BiomeDefaultFeatures.addDesertVegetation(builder);
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(sand, UniformInt.of(2, 6), 2, List.of(blocks.getBlockState("surface"), blocks.getBlockState("upper"))),
					CountPlacement.of(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
		}
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, PlacementUtils.inlinePlaced(MSFeatures.BLOCK_BLOB.get(),
					new BlockStateConfiguration(sandstone),
					CountPlacement.of(UniformInt.of(0, 3)), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, PlacementUtils.inlinePlaced(MSFeatures.BLOCK_BLOB.get(),
					new BlockStateConfiguration(sandstone),
					CountPlacement.of(UniformInt.of(0, 5)), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
		}
		
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(OreFeatures.NATURAL_STONE, sandstone, 28),
				CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(256)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(OreFeatures.NATURAL_STONE, Blocks.IRON_ORE.defaultBlockState(), 9),
				CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(OreFeatures.NATURAL_STONE, Blocks.REDSTONE_ORE.defaultBlockState(), 8),
				CountPlacement.of(12), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		 
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		TurtleVillagePieces.addCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		TurtleVillagePieces.addPieces(register, random);
	}
	
	private enum Variant
	{
		SANDSTONE,
		RED_SANDSTONE
	}
}