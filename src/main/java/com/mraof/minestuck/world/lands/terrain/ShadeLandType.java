package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ShadeLandType extends TerrainLandType
{
	public static final String SHADE = "minestuck.shade";
	
	private static final Vec3 skyColor = new Vec3(0.16D, 0.38D, 0.54D);
	
	public ShadeLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setGroundState(MSBlocks.SHADE_STONE.get().defaultBlockState(), MSFillerBlockTypes.SHADE_STONE);
		registry.setBlockState("upper", MSBlocks.BLUE_DIRT.get().defaultBlockState());
		registry.setBlockState("ocean", MSBlocks.OIL.get().defaultBlockState());
		registry.setBlockState("structure_primary", MSBlocks.SHADE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_decorative", MSBlocks.CHISELED_SHADE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_cracked", MSBlocks.CRACKED_SHADE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_mossy", MSBlocks.MOSSY_SHADE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_primary_column", MSBlocks.SHADE_COLUMN.get().defaultBlockState());
		registry.setBlockState("structure_primary_stairs", MSBlocks.SHADE_STAIRS.get().defaultBlockState());
		registry.setBlockState("structure_secondary", MSBlocks.SMOOTH_SHADE_STONE.get().defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.TAR_SHADE_BRICKS.get().defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.SHADE_BRICK_STAIRS.get().defaultBlockState());
		registry.setBlockState("village_path", Blocks.GRAVEL.defaultBlockState());
		registry.setBlockState("light_block", MSBlocks.GLOWING_WOOD.get().defaultBlockState());
		registry.setBlockState("torch", Blocks.REDSTONE_TORCH.defaultBlockState());
		registry.setBlockState("wall_torch", Blocks.REDSTONE_WALL_TORCH.defaultBlockState());
		registry.setBlockState("mushroom_1", MSBlocks.GLOWING_MUSHROOM.get().defaultBlockState());
		registry.setBlockState("mushroom_2", MSBlocks.GLOWING_MUSHROOM.get().defaultBlockState());
		registry.setBlockState("bush", MSBlocks.GLOWING_MUSHROOM.get().defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.CYAN_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.GRAY_WOOL.defaultBlockState());
		registry.setBlockState("cruxite_ore", MSBlocks.SHADE_STONE_CRUXITE_ORE.get().defaultBlockState());
		registry.setBlockState("uranium_ore", MSBlocks.SHADE_STONE_URANIUM_ORE.get().defaultBlockState());
	}
	
	@Override
	public String[] getNames() {
		return new String[] {SHADE};
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.BiomeCategory.MUSHROOM;
		properties.forceRain = LandProperties.ForceType.DEFAULT;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		/*
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
					.configured(MinestuckBiomeFeatures.GLOWING_MUSHROOM_CONFIG)
					.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(2));
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE
					.configured(new BlockStateFeatureConfig(MSBlocks.GLOWING_LOG.defaultBlockState()))
					.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(2));
		}
		if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
					.configured(MinestuckBiomeFeatures.GLOWING_MUSHROOM_CONFIG)
					.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(4));
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MSFeatures.LEAFLESS_TREE
					.configured(new BlockStateFeatureConfig(MSBlocks.GLOWING_LOG.defaultBlockState()))
					.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(2));
		}
		
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.BLUE_DIRT.defaultBlockState(), 25))
				.range(256).squared().count(8));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SHADE_STONE_COAL_ORE.defaultBlockState(), 9))
				.range(64).squared().count(24));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SHADE_STONE_GOLD_ORE.defaultBlockState(), 7))
				.range(32).squared().count(6));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SHADE_STONE_CRUXITE_ORE.defaultBlockState(), 4))
				.range(64).squared().count(12));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
				.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SHADE_STONE_URANIUM_ORE.defaultBlockState(), 2))
				.range(32).squared().count(4));
		*/
	}
	
	@Override
	public float getSkylightBase()
	{
		return 0F;
	}
	
	@Override
	public Vec3 getFogColor()
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
