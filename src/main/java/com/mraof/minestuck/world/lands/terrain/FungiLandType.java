package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.structure.GateMushroomPiece;
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
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.setGatePiece(GateMushroomPiece::new);
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_BOOLEAN_SELECTOR
					.configured(new TwoFeatureChoiceConfig(() -> Features.HUGE_RED_MUSHROOM, () -> Features.HUGE_BROWN_MUSHROOM))
					.decorated(Features.Placements.HEIGHTMAP_SQUARE));
			
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(blocks.getBlockState("slime"), FeatureSpread.of(2, 4), 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE));
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_BOOLEAN_SELECTOR
					.configured(new TwoFeatureChoiceConfig(() -> Features.HUGE_RED_MUSHROOM, () -> Features.HUGE_BROWN_MUSHROOM))
					.decorated(Features.Placements.HEIGHTMAP_SQUARE).count(3));
			
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(blocks.getBlockState("slime"), FeatureSpread.of(2, 4), 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(2));
		}
		builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_BROWN_MUSHROOM.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(2));
		builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_RED_MUSHROOM.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(2));
		
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.GRAVEL.defaultBlockState(), 33))
				.range(256).squared().count(8));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.IRON_ORE.defaultBlockState(), 9))
				.range(64).squared().count(24));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.REDSTONE_ORE.defaultBlockState(), 8))
				.range(32).squared().count(12));
	}
	
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