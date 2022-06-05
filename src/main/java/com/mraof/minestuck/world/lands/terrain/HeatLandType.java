package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

public class HeatLandType extends TerrainLandType
{
	public static final String HEAT = "minestuck.heat";
	public static final String FLAME = "minestuck.flame";
	public static final String FIRE = "minestuck.fire";
	
	private static final Vector3d skyColor = new Vector3d(0.4D, 0.0D, 0.0D);
	
	public HeatLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setGroundState(Blocks.NETHERRACK.defaultBlockState(), OreFeatureConfig.FillerBlockType.NETHER_ORE_REPLACEABLES);
		registry.setBlockState("upper", Blocks.NETHERRACK.defaultBlockState());
		registry.setBlockState("ocean", Blocks.LAVA.defaultBlockState());
		registry.setBlockState("structure_primary", MSBlocks.BLACK_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_primary_decorative", MSBlocks.CHISELED_BLACK_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_primary_cracked", MSBlocks.CRACKED_BLACK_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_primary_column", MSBlocks.BLACK_STONE_COLUMN.defaultBlockState());
		registry.setBlockState("structure_primary_stairs", MSBlocks.BLACK_STONE_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_secondary", MSBlocks.CAST_IRON.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.CHISELED_CAST_IRON.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.CAST_IRON_STAIRS.defaultBlockState());
		registry.setBlockState("fall_fluid", Blocks.WATER.defaultBlockState());
		registry.setBlockState("structure_planks", Blocks.BRICKS.defaultBlockState());
		registry.setBlockState("structure_planks_slab", Blocks.BRICK_SLAB.defaultBlockState());
		registry.setBlockState("village_path", Blocks.QUARTZ_BLOCK.defaultBlockState());
		registry.setBlockState("village_fence", Blocks.NETHER_BRICK_FENCE.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.PURPLE_WOOL.defaultBlockState());
		registry.setBlockState("cruxite_ore", MSBlocks.NETHERRACK_CRUXITE_ORE.defaultBlockState());
		registry.setBlockState("uranium_ore", MSBlocks.NETHERRACK_URANIUM_ORE.defaultBlockState());
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
		properties.category = Biome.Category.NETHER;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		if(type != LandBiomeType.OCEAN)
		{
			builder.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, MSFeatures.OCEAN_RUNDOWN
					.configured(IFeatureConfig.NONE).decorated(Features.Placements.HEIGHTMAP_SQUARE).count(3));
		}
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.FIRE_FIELD
					.configured(NoFeatureConfig.NONE).range(256).squared().count(7));
			
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(Blocks.GLOWSTONE.defaultBlockState(), FeatureSpread.of(2, 1), 1, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(5));
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(Blocks.SOUL_SAND.defaultBlockState(), FeatureSpread.of(2, 4), 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(8));
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.FIRE_FIELD.configured(IFeatureConfig.NONE)
					.range(256).squared().count(10));
			
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(Blocks.SOUL_SAND.defaultBlockState(), FeatureSpread.of(2, 4), 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(15));
		}
		
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.BLACK_STONE.defaultBlockState(), 33))
				.range(256).squared().count(8));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.NETHERRACK_COAL_ORE.defaultBlockState(), 17))
				.range(128).squared().count(26));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.NETHER_QUARTZ_ORE.defaultBlockState(), 8))
				.range(64).squared().count(13));
	}
	
	@Override
	public float getSkylightBase()
	{
		return 1/2F;
	}
	
	@Override
	public Vector3d getFogColor()
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