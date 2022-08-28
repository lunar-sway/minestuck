package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageCenter;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.TrapezoidFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class RockLandType extends TerrainLandType
{
	public static final String ROCK = "minestuck.rock";
	public static final String STONE = "minestuck.stone";
	public static final String ORE = "minestuck.ore";
	public static final String PETRIFICATION = "minestuck.petrification";
	
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "rock");
	private final Variant type;
	
	public RockLandType(Variant variation)
	{
		super(new Builder(() -> MSEntityTypes.NAKAGATOR).group(GROUP_NAME).skylight(7/8F));
		type = variation;
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.PETRIFICATION)
		{
			registry.setBlockState("surface", Blocks.STONE.defaultBlockState());
		} else
		{
			registry.setBlockState("surface", Blocks.GRAVEL.defaultBlockState());
		}
		
		registry.setBlockState("upper", Blocks.COBBLESTONE.defaultBlockState());
		registry.setBlockState("structure_primary", MSBlocks.COARSE_STONE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_decorative", MSBlocks.CHISELED_COARSE_STONE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_stairs", MSBlocks.COARSE_STONE_BRICK_STAIRS.get().defaultBlockState());
		registry.setBlockState("structure_secondary", MSBlocks.COARSE_STONE.get().defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.CHISELED_COARSE_STONE.get().defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.COARSE_STONE_STAIRS.get().defaultBlockState());
		registry.setBlockState("structure_planks_slab", Blocks.BRICK_SLAB.defaultBlockState());
		registry.setBlockState("village_path", Blocks.MOSSY_COBBLESTONE.defaultBlockState());
		registry.setBlockState("village_fence", Blocks.COBBLESTONE_WALL.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.BROWN_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.GRAY_WOOL.defaultBlockState());
	}
	
	@Override
	public String[] getNames()
	{
		if(type == Variant.PETRIFICATION)
		{
			return new String[]{PETRIFICATION};
		} else
		{
			return new String[]{ROCK, STONE, ORE};
		}
	}
	
	@Override
	public Biome.BiomeCategory getBiomeCategory()
	{
		return Biome.BiomeCategory.EXTREME_HILLS;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanThreshold = -0.3F;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		int biomeMultiplier = type == LandBiomeType.ROUGH || type == LandBiomeType.OCEAN ? 2 : 1;
		
		if(type == LandBiomeType.OCEAN)
		{
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(Blocks.CLAY.defaultBlockState(), UniformInt.of(2, 5), 2, List.of(blocks.getBlockState("ocean_surface"), Blocks.CLAY.defaultBlockState())),
					CountPlacement.of(25), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID));
		}
		
		if(type == LandBiomeType.NORMAL)
		{
			if(this.type == Variant.ROCK)
			{
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.RANDOM_ROCK_BLOCK_BLOB.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_GRASS_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacementUtils.inlinePlaced(MSFeatures.GRASSY_SURFACE_DISK.get(),
						new DiskConfiguration(Blocks.COBBLESTONE.defaultBlockState(), UniformInt.of(2, 5), 1, List.of(blocks.getBlockState("surface"), Blocks.COBBLESTONE.defaultBlockState())),
						RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
				builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacementUtils.inlinePlaced(MSFeatures.GRASSY_SURFACE_DISK.get(),
						new DiskConfiguration(Blocks.STONE.defaultBlockState(), UniformInt.of(2, 4), 2, List.of(blocks.getBlockState("surface"), Blocks.STONE.defaultBlockState())),
						RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
			} else if(this.type == Variant.PETRIFICATION)
			{
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_TREE.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.COBBLESTONE_BLOCK_BLOB.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.PETRIFIED_GRASS_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.PETRIFIED_POPPY_PATCH.getHolder().orElseThrow());
			}
		}
		
		if(type == LandBiomeType.ROUGH)
		{
			if(this.type == Variant.ROCK)
			{
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_TREE.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.LARGE_RANDOM_ROCK_BLOCK_BLOB.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.PETRIFIED_GRASS_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, PlacementUtils.inlinePlaced(MSFeatures.STONE_MOUND.get(),
						new BlockStateConfiguration(blocks.getBlockState("ground")),
						InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
				builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacementUtils.inlinePlaced(MSFeatures.GRASSY_SURFACE_DISK.get(),
						new DiskConfiguration(Blocks.COBBLESTONE.defaultBlockState(), UniformInt.of(2, 5), 1, List.of(blocks.getBlockState("surface"), Blocks.COBBLESTONE.defaultBlockState())),
						RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
				builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, PlacementUtils.inlinePlaced(MSFeatures.GRASSY_SURFACE_DISK.get(),
						new DiskConfiguration(Blocks.STONE.defaultBlockState(), UniformInt.of(2, 4), 2, List.of(blocks.getBlockState("surface"), Blocks.STONE.defaultBlockState())),
						RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
			} else if(this.type == Variant.PETRIFICATION)
			{
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.PETRIFIED_TREE.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.COBBLESTONE_BLOCK_BLOB.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_GRASS_PATCH.getHolder().orElseThrow());
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.SPARSE_PETRIFIED_POPPY_PATCH.getHolder().orElseThrow());
			}
		}
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33),
				CountPlacement.of(10 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(256)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.INFESTED_STONE.defaultBlockState(), 9),
				CountPlacement.of(7 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 9),
				CountPlacement.of(15 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.IRON_ORE.defaultBlockState(), 5),
				CountPlacement.of(15 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.REDSTONE_ORE.defaultBlockState(), 5),
				CountPlacement.of(8 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.LAPIS_ORE.defaultBlockState(), 4),
				CountPlacement.of(3 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(24 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.STONE_QUARTZ_ORE.get().defaultBlockState(), 4),
				CountPlacement.of(3 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(24 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.GOLD_ORE.defaultBlockState(), 5),
				CountPlacement.of(3 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32 * biomeMultiplier)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.DIAMOND_ORE.defaultBlockState(), 4),
				CountPlacement.of(2 * biomeMultiplier), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(24 * biomeMultiplier)), BiomeFilter.biome()));
		
		builder.addCarver(GenerationStep.Carving.AIR, Holder.direct(WorldCarver.CANYON.configured(new CanyonCarverConfiguration(0.4F,
				BiasedToBottomHeight.of(VerticalAnchor.absolute(20), VerticalAnchor.absolute(67), 8),
				ConstantFloat.of(3), VerticalAnchor.absolute(10), CarverDebugSettings.DEFAULT, UniformFloat.of(-0.125F, 0.125F),
				new CanyonCarverConfiguration.CanyonShapeConfiguration(UniformFloat.of(0.75F, 1F), TrapezoidFloat.of(0, 6, 2), 3, UniformFloat.of(0.75F, 1F), 1F, 0F)))));
		
	}
	
	@Override
	public Vec3 getFogColor()
	{
		return new Vec3(0.5, 0.5, 0.55);
	}
	
	@Override
	public Vec3 getSkyColor()
	{
		return new Vec3(0.6D, 0.6D, 0.7D);
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		addNakagatorVillageCenters(register);
		
		register.add(ConsortVillageCenter.RockCenter::new, 5);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		addNakagatorVillagePieces(register, random);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return type == Variant.PETRIFICATION ? MSSoundEvents.MUSIC_PETRIFICATION : MSSoundEvents.MUSIC_ROCK;
	}
	
	public enum Variant
	{
		ROCK,
		PETRIFICATION;
		
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}