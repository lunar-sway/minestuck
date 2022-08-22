package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
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

public class WoodLandType extends TerrainLandType
{
	public static final String WOOD = "minestuck.wood";
	public static final String OAK = "minestuck.oak";
	public static final String LUMBER = "minestuck.lumber";
	
	private static final Vec3 fogColor = new Vec3(0.0D, 0.16D, 0.38D);
	
	public WoodLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("upper", Blocks.OAK_LOG.defaultBlockState());
		registry.setBlockState("surface", MSBlocks.TREATED_PLANKS.get().defaultBlockState());
		registry.setBlockState("structure_primary", Blocks.JUNGLE_WOOD.defaultBlockState());
		registry.setBlockState("structure_primary_decorative", Blocks.DARK_OAK_LOG.defaultBlockState());
		registry.setBlockState("structure_primary_stairs", Blocks.DARK_OAK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_secondary", Blocks.JUNGLE_PLANKS.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", Blocks.DARK_OAK_PLANKS.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", Blocks.JUNGLE_STAIRS.defaultBlockState());
		registry.setBlockState("light_block", MSBlocks.GLOWING_WOOD.get().defaultBlockState());
		registry.setBlockState("bush", Blocks.RED_MUSHROOM.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.PURPLE_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.GREEN_WOOL.defaultBlockState());
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{WOOD, OAK, LUMBER};
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.RED_MUSHROOM_PATCH.getHolder().orElseThrow());
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.BROWN_MUSHROOM_PATCH.getHolder().orElseThrow());
			
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(MSBlocks.VINE_WOOD.get().defaultBlockState(), UniformInt.of(2, 6), 2, List.of(blocks.getBlockState("surface"), blocks.getBlockState("upper"))),
					CountPlacement.of(8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(Blocks.NETHERRACK.defaultBlockState(), UniformInt.of(2, 3), 1, List.of(blocks.getBlockState("surface"), blocks.getBlockState("upper"))),
					CountPlacement.of(6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
			
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.RED_MUSHROOM_PATCH.getHolder().orElseThrow());
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.BROWN_MUSHROOM_PATCH.getHolder().orElseThrow());
			
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.DISK,
					new DiskConfiguration(Blocks.OAK_LEAVES.defaultBlockState(), UniformInt.of(2, 6), 2, List.of(blocks.getBlockState("surface"), blocks.getBlockState("upper"))),
					CountPlacement.of(15), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome()));
		}
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33),
				CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(256)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 17),
				CountPlacement.of(18), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(128)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.REDSTONE_ORE.defaultBlockState(), 7),
				CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.IRON_ORE.defaultBlockState(), 9),
				CountPlacement.of(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(64)), BiomeFilter.biome()));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PlacementUtils.inlinePlaced(Feature.ORE,
				new OreConfiguration(blocks.getGroundType(), Blocks.EMERALD_ORE.defaultBlockState(), 3),
				CountPlacement.of(8), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(24)), BiomeFilter.biome()));
		
	}
	
	@Override
	public float getSkylightBase()
	{
		return 1/2F;
	}
	
	@Override
	public Vec3 getFogColor()
	{
		return fogColor;
	}
	
	@Override
	public Vec3 getSkyColor()
	{
		return new Vec3(0.0D, 0.3D, 0.4D);
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return MSEntityTypes.SALAMANDER;
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		addSalamanderVillageCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		addSalamanderVillagePieces(register, random);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_WOOD;
	}
}
