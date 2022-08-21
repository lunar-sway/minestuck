package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
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

import java.util.List;
import java.util.Random;

public class HeatLandType extends TerrainLandType
{
	public static final String HEAT = "minestuck.heat";
	public static final String FLAME = "minestuck.flame";
	public static final String FIRE = "minestuck.fire";
	
	private static final Vec3 skyColor = new Vec3(0.4D, 0.0D, 0.0D);
	
	public HeatLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setGroundState(Blocks.NETHERRACK.defaultBlockState(), OreFeatures.NETHER_ORE_REPLACEABLES);
		registry.setBlockState("upper", Blocks.NETHERRACK.defaultBlockState());
		registry.setBlockState("ocean", Blocks.LAVA.defaultBlockState());
		registry.setBlockState("structure_primary", MSBlocks.BLACK_STONE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_decorative", MSBlocks.CHISELED_BLACK_STONE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_cracked", MSBlocks.CRACKED_BLACK_STONE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_column", MSBlocks.BLACK_STONE_COLUMN.get().defaultBlockState());
		registry.setBlockState("structure_primary_stairs", MSBlocks.BLACK_STONE_BRICK_STAIRS.get().defaultBlockState());
		registry.setBlockState("structure_secondary", MSBlocks.CAST_IRON.get().defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.CHISELED_CAST_IRON.get().defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.CAST_IRON_STAIRS.get().defaultBlockState());
		registry.setBlockState("fall_fluid", Blocks.WATER.defaultBlockState());
		registry.setBlockState("structure_planks", Blocks.BRICKS.defaultBlockState());
		registry.setBlockState("structure_planks_slab", Blocks.BRICK_SLAB.defaultBlockState());
		registry.setBlockState("village_path", Blocks.QUARTZ_BLOCK.defaultBlockState());
		registry.setBlockState("village_fence", Blocks.NETHER_BRICK_FENCE.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.PURPLE_WOOL.defaultBlockState());
		registry.setBlockState("cruxite_ore", MSBlocks.NETHERRACK_CRUXITE_ORE.get().defaultBlockState());
		registry.setBlockState("uranium_ore", MSBlocks.NETHERRACK_URANIUM_ORE.get().defaultBlockState());
	}
	
	@Override
	public String[] getNames() {
		return new String[] {HEAT, FLAME, FIRE};
	}
	
	@Override
	public LandBiomeSet getBiomeSet()
	{
		return MSBiomes.NO_RAIN_LAND;
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.BiomeCategory.NETHER;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		if(type != LandBiomeType.OCEAN)
		{
			builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MSPlacedFeatures.OCEAN_RUNDOWN.getHolder().orElseThrow());
		}
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.FIRE_FIELD.getHolder().orElseThrow());
			
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(Blocks.GLOWSTONE.defaultBlockState(), UniformInt.of(2, 3), 1, List.of(blocks.getBlockState("surface"), blocks.getBlockState("upper"))),
					CountPlacement.of(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(Blocks.SOUL_SAND.defaultBlockState(), UniformInt.of(2, 6), 2, List.of(blocks.getBlockState("surface"), blocks.getBlockState("upper"))),
					CountPlacement.of(8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.EXTRA_FIRE_FIELD.getHolder().orElseThrow());
			
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(Blocks.SOUL_SAND.defaultBlockState(), UniformInt.of(2, 6), 2, List.of(blocks.getBlockState("surface"), blocks.getBlockState("upper"))),
					CountPlacement.of(15), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
		}
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.BLACK_STONE.get().defaultBlockState(), 33),
				CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(256)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), MSBlocks.NETHERRACK_COAL_ORE.get().defaultBlockState(), 17),
				CountPlacement.of(26), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(128)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.NETHER_QUARTZ_ORE.defaultBlockState(), 8),
				CountPlacement.of(13), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
		
	}
	
	@Override
	public float getSkylightBase()
	{
		return 1/2F;
	}
	
	@Override
	public Vec3 getFogColor()
	{
		return skyColor;
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return MSEntityTypes.NAKAGATOR;
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		addNakagatorVillageCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		addNakagatorVillagePieces(register, random);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_HEAT;
	}
}