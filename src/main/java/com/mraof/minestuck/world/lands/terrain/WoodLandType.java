package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
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

public class WoodLandType extends TerrainLandType
{
	public static final String WOOD = "minestuck.wood";
	public static final String OAK = "minestuck.oak";
	public static final String LUMBER = "minestuck.lumber";
	
	private static final Vector3d fogColor = new Vector3d(0.0D, 0.16D, 0.38D);
	
	public WoodLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("upper", Blocks.OAK_LOG.defaultBlockState());
		registry.setBlockState("surface", MSBlocks.TREATED_PLANKS.defaultBlockState());
		registry.setBlockState("structure_primary", Blocks.JUNGLE_WOOD.defaultBlockState());
		registry.setBlockState("structure_primary_decorative", Blocks.DARK_OAK_LOG.defaultBlockState());
		registry.setBlockState("structure_primary_stairs", Blocks.DARK_OAK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_secondary", Blocks.JUNGLE_PLANKS.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", Blocks.DARK_OAK_PLANKS.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", Blocks.JUNGLE_STAIRS.defaultBlockState());
		registry.setBlockState("light_block", MSBlocks.GLOWING_WOOD.defaultBlockState());
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
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_RED_MUSHROOM
					.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(2));
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_BROWN_MUSHROOM
					.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(2));
			
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(MSBlocks.VINE_WOOD.defaultBlockState(), FeatureSpread.of(2, 4), 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(8));
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(Blocks.NETHERRACK.defaultBlockState(), FeatureSpread.of(2, 1), 1, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(6));
			
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_RED_MUSHROOM
					.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(4));
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_BROWN_MUSHROOM
					.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(4));
			
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(Blocks.OAK_LEAVES.defaultBlockState(), FeatureSpread.of(2, 4), 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(15));
		}
		
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33))
				.range(256).squared().count(8));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 17))
				.range(128).squared().count(18));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.REDSTONE_ORE.defaultBlockState(), 7))
				.range(32).squared().count(8));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.IRON_ORE.defaultBlockState(), 9))
				.range(64).squared().count(24));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.EMERALD_ORE.defaultBlockState(), 3))
				.range(24).squared().count(8));
	}
	
	@Override
	public float getSkylightBase()
	{
		return 1/2F;
	}
	
	@Override
	public Vector3d getFogColor()
	{
		return fogColor;
	}
	
	@Override
	public Vector3d getSkyColor()
	{
		return new Vector3d(0.0D, 0.3D, 0.4D);
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
