package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;

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
	public String[] getNames() {
		return new String[] {WOOD, OAK, LUMBER};
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
	}
	/*
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.type == BiomeType.NORMAL)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.RED_MUSHROOM_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(2))));
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(2))));

			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(MSBlocks.VINE_WOOD.getDefaultState(), 7, 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper")))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(8))));
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(Blocks.NETHERRACK.getDefaultState(), 4, 1, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper")))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(6))));

		} else if(biome.type == BiomeType.ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.RED_MUSHROOM_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(4))));
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(4))));

			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(Blocks.OAK_LEAVES.getDefaultState(), 7, 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper")))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(15))));
		}

		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.GRAVEL.getDefaultState(), 33)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 256))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.DIRT.getDefaultState(), 17)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(18, 0, 0, 128))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.REDSTONE_ORE.getDefaultState(), 7)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 32))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.IRON_ORE.getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(24, 0, 0, 64))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.EMERALD_ORE.getDefaultState(), 3)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 24))));
	}
	*/
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
