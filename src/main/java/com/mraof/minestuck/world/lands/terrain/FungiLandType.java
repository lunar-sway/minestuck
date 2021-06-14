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
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class FungiLandType extends TerrainLandType
{
	public static final String FUNGI = "minestuck.fungi";
	public static final String DANK = "minestuck.dank";
	public static final String MUST = "minestuck.must";
	public static final String MYCELIUM = "minestuck.mycelium";
	public static final String MOLD = "minestuck.mold";
	public static final String MILDEW = "minestuck.mildew";
	
	private static final Vector3d fogColor = new Vector3d(0.69D, 0.76D, 0.61D);
	private static final Vector3d skyColor = new Vector3d(0.69D, 0.76D, 0.61D);
	
	public FungiLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", Blocks.MYCELIUM.defaultBlockState());
		registry.setBlockState("upper", Blocks.DIRT.defaultBlockState());
		registry.setBlockState("ocean", Blocks.WATER.defaultBlockState());
		registry.setBlockState("structure_primary_decorative", Blocks.MOSSY_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_primary_stairs", Blocks.STONE_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_secondary", MSBlocks.MYCELIUM_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", Blocks.CHISELED_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.MYCELIUM_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("village_path", Blocks.GRASS_PATH.defaultBlockState());
		registry.setBlockState("light_block", MSBlocks.GLOWY_GOOP.defaultBlockState());
		registry.setBlockState("torch", Blocks.REDSTONE_TORCH.defaultBlockState());
		registry.setBlockState("wall_torch", Blocks.REDSTONE_WALL_TORCH.defaultBlockState());
		registry.setBlockState("mushroom_1", Blocks.RED_MUSHROOM.defaultBlockState());
		registry.setBlockState("mushroom_2", Blocks.BROWN_MUSHROOM.defaultBlockState());
		registry.setBlockState("bush", Blocks.BROWN_MUSHROOM.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.LIME_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.GRAY_WOOL.defaultBlockState());
	}
	
	@Override
	public String[] getNames() {
		return new String[] {FUNGI, DANK, MUST, MOLD, MILDEW, MYCELIUM};
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.Category.MUSHROOM;
		properties.forceRain = LandProperties.ForceType.ON;
	}
	/*
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.setGatePiece(GateMushroomPiece::new);
	}
	
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.type == BiomeType.NORMAL)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_BOOLEAN_SELECTOR.withConfiguration(new TwoFeatureChoiceConfig(Feature.HUGE_RED_MUSHROOM.withConfiguration(DefaultBiomeFeatures.BIG_RED_MUSHROOM), Feature.HUGE_BROWN_MUSHROOM.withConfiguration(DefaultBiomeFeatures.BIG_BROWN_MUSHROOM))).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(1))));

			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(blocks.getBlockState("slime"), 7, 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper")))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(30))));
		} else if(biome.type == BiomeType.ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_BOOLEAN_SELECTOR.withConfiguration(new TwoFeatureChoiceConfig(Feature.HUGE_RED_MUSHROOM.withConfiguration(DefaultBiomeFeatures.BIG_RED_MUSHROOM), Feature.HUGE_BROWN_MUSHROOM.withConfiguration(DefaultBiomeFeatures.BIG_BROWN_MUSHROOM))).withPlacement(Placement.COUNT_HEIGHTMAP.configure(new FrequencyConfig(3))));

			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(blocks.getBlockState("slime"), 7, 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper")))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(60))));
		}
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.BROWN_MUSHROOM_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(2))));
		biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(DefaultBiomeFeatures.RED_MUSHROOM_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceConfig(2))));

		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.GRAVEL.getDefaultState(), 33)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 256))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.IRON_ORE.getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(24, 0, 0, 64))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.REDSTONE_ORE.getDefaultState(), 8)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(12, 0, 0, 32))));
	}
	*/
	@Override
	public Vector3d getFogColor()
	{
		return fogColor;
	}
	
	@Override
	public Vector3d getSkyColor()
	{
		return skyColor;
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
		return MSSoundEvents.MUSIC_FUNGI;
	}
}