package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class ShadeLandType extends TerrainLandType
{
	public static final String SHADE = "minestuck.shade";
	
	private static final Vector3d skyColor = new Vector3d(0.16D, 0.38D, 0.54D);
	
	public ShadeLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setGroundState(MSBlocks.SHADE_STONE.defaultBlockState(), MSFillerBlockTypes.SHADE_STONE);
		registry.setBlockState("upper", MSBlocks.BLUE_DIRT.defaultBlockState());
		registry.setBlockState("ocean", MSBlocks.OIL.defaultBlockState());
		registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_primary_stairs", Blocks.STONE_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_secondary", MSBlocks.SHADE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.SMOOTH_SHADE_STONE.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.SHADE_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("village_path", Blocks.GRAVEL.defaultBlockState());
		registry.setBlockState("light_block", MSBlocks.GLOWING_WOOD.defaultBlockState());
		registry.setBlockState("torch", Blocks.REDSTONE_TORCH.defaultBlockState());
		registry.setBlockState("wall_torch", Blocks.REDSTONE_WALL_TORCH.defaultBlockState());
		registry.setBlockState("mushroom_1", MSBlocks.GLOWING_MUSHROOM.defaultBlockState());
		registry.setBlockState("mushroom_2", MSBlocks.GLOWING_MUSHROOM.defaultBlockState());
		registry.setBlockState("bush", MSBlocks.GLOWING_MUSHROOM.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.CYAN_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.GRAY_WOOL.defaultBlockState());
		registry.setBlockState("cruxite_ore", MSBlocks.SHADE_STONE_CRUXITE_ORE.defaultBlockState());
		registry.setBlockState("uranium_ore", MSBlocks.SHADE_STONE_URANIUM_ORE.defaultBlockState());
	}
	
	@Override
	public String[] getNames() {
		return new String[] {SHADE};
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.Category.MUSHROOM;
		properties.forceRain = LandProperties.ForceType.DEFAULT;
	}
	/*
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.type == BiomeType.NORMAL)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(MinestuckBiomeFeatures.GLOWING_MUSHROOM_CONFIG).withPlacement(Placement.COUNT_CHANCE_HEIGHTMAP.configure(new HeightWithChanceConfig(1, 1/2F))));
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE.withConfiguration(new BlockStateFeatureConfig(MSBlocks.GLOWING_LOG.getDefaultState())).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(2))));
		}
		if(biome.type == BiomeType.ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(MinestuckBiomeFeatures.GLOWING_MUSHROOM_CONFIG).withPlacement(Placement.COUNT_CHANCE_HEIGHTMAP.configure(new HeightWithChanceConfig(1, 1/4F))));
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE.withConfiguration(new BlockStateFeatureConfig(MSBlocks.GLOWING_LOG.getDefaultState())).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(2))));
		}

		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.BLUE_DIRT.getDefaultState(), 25)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(8, 0, 0, 256))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SHADE_STONE_COAL_ORE.getDefaultState(), 9)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(24, 0, 0, 64))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SHADE_STONE_GOLD_ORE.getDefaultState(), 7)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(6, 0, 0, 32))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SHADE_STONE_CRUXITE_ORE.getDefaultState(), 4)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(12, 0, 0, 64))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SHADE_STONE_URANIUM_ORE.getDefaultState(), 2)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(4, 0, 0, 32))));
	}
	*/
	@Override
	public float getSkylightBase()
	{
		return 0F;
	}
	
	@Override
	public Vector3d getFogColor()
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
		return MSSoundEvents.MUSIC_SHADE;
	}
}
