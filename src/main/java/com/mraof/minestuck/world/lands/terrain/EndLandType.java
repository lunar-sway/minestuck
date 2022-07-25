package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class EndLandType extends TerrainLandType
{
	public static final String END = "minestuck.end";
	public static final String DIMENSION = "minestuck.dimension";
	
	private static final Vec3 fogColor = new Vec3(0.0D, 0.4D, 0.2D);
	private static final Vec3 skyColor = new Vec3(0.3D, 0.1D, 0.5D);
	
	public EndLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setGroundState(MSBlocks.COARSE_END_STONE.get().defaultBlockState(), MSFillerBlockTypes.COARSE_END_STONE);
		registry.setBlockState("surface", Blocks.END_STONE.defaultBlockState());
		registry.setBlockState("surface_rough", MSBlocks.END_GRASS.get().defaultBlockState());
		registry.setBlockState("upper", Blocks.END_STONE.defaultBlockState());
		registry.setBlockState("ocean", MSBlocks.ENDER.get().defaultBlockState());
		registry.setBlockState("structure_primary", Blocks.END_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_primary_decorative", Blocks.PURPUR_PILLAR.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
		registry.setBlockState("structure_primary_stairs", Blocks.END_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary", Blocks.PURPUR_BLOCK.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", Blocks.PURPUR_STAIRS.defaultBlockState());
		registry.setBlockState("structure_planks", Blocks.BRICKS.defaultBlockState());
		registry.setBlockState("structure_planks_slab", Blocks.BRICK_SLAB.defaultBlockState());
		registry.setBlockState("village_path", MSBlocks.COARSE_END_STONE.get().defaultBlockState());
		registry.setBlockState("village_fence", Blocks.NETHER_BRICK_FENCE.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.GREEN_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.PURPLE_WOOL.defaultBlockState());
		registry.setBlockState("cruxite_ore", MSBlocks.END_STONE_CRUXITE_ORE.get().defaultBlockState());
		registry.setBlockState("uranium_ore", MSBlocks.END_STONE_URANIUM_ORE.get().defaultBlockState());
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{END, DIMENSION};
	}
	
	@Override
	public LandBiomeSet getBiomeSet()
	{
		return MSBiomes.NO_RAIN_LAND;
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.BiomeCategory.THEEND;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{/*
		if(type != LandBiomeType.OCEAN)
		{
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSCFeatures.get().END_TREE.decorated(Features.Placements.HEIGHTMAP_SQUARE)
					.decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(2, 0.1F, 1))));
		}
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MSFeatures.GRASSY_SURFACE_DISK
					.configured(new SphereReplaceConfig(MSBlocks.END_GRASS.defaultBlockState(), FeatureSpread.of(2, 2), 1, Lists.newArrayList(blocks.getBlockState("surface"), MSBlocks.END_GRASS.defaultBlockState())))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE));
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configured(MinestuckBiomeFeatures.TALL_END_GRASS_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(8));
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, MSFeatures.GRASSY_SURFACE_DISK.configured(new SphereReplaceConfig(Blocks.END_STONE.defaultBlockState(), FeatureSpread.of(2, 1), 1, Lists.newArrayList(blocks.getBlockState("surface_rough"), Blocks.END_STONE.defaultBlockState()))).decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE));
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.CHORUS_PLANT);
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configured(MinestuckBiomeFeatures.TALL_END_GRASS_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE.count(10)));
		}
		
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.END_STONE_IRON_ORE.defaultBlockState(), 9))
				.range(64).squared().count(20));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.END_STONE_REDSTONE_ORE.defaultBlockState(), 8))
				.range(32).squared().count(10));
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.END_STONE.defaultBlockState(), 36))
				.range(64).squared().count(40));*/
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
		return MSSoundEvents.MUSIC_END;
	}
}