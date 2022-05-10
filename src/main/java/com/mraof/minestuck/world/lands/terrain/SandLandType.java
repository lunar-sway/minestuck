
package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.biome.MinestuckBiomeFeatures;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.structure.village.ConsortVillageCenter;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

public class SandLandType extends TerrainLandType
{
	public static final String SAND = "minestuck.sand";
	public static final String DUNES = "minestuck.dunes";
	public static final String DESERTS = "minestuck.deserts";
	public static final String LUSH_DESERTS = "minestuck.lush_deserts";
	
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "sand");
	private final Vector3d fogColor, skyColor;
	private final Variant type;
	
	public SandLandType(Variant variation)
	{
		super(GROUP_NAME);
		type = variation;
		
		if(type == Variant.SAND)
		{
			fogColor = new Vector3d(0.99D, 0.8D, 0.05D);
			skyColor = new Vector3d(0.8D, 0.8D, 0.1D);
		} else
		{
			fogColor = new Vector3d(0.99D, 0.6D, 0.05D);
			skyColor = new Vector3d(0.8D, 0.6D, 0.1D);
		}
		
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.SAND || type == Variant.LUSH_DESERTS)
		{
			registry.setGroundState(Blocks.SANDSTONE.defaultBlockState(), MSFillerBlockTypes.SANDSTONE);
			registry.setBlockState("upper", Blocks.SAND.defaultBlockState());
			registry.setBlockState("structure_primary", Blocks.SMOOTH_SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary_stairs", Blocks.SANDSTONE_STAIRS.defaultBlockState());
			registry.setBlockState("village_path", Blocks.RED_SAND.defaultBlockState());
			registry.setBlockState("cruxite_ore", MSBlocks.SANDSTONE_CRUXITE_ORE.defaultBlockState());
			registry.setBlockState("uranium_ore", MSBlocks.SANDSTONE_URANIUM_ORE.defaultBlockState());
		} else
		{
			registry.setGroundState(Blocks.RED_SANDSTONE.defaultBlockState(), MSFillerBlockTypes.RED_SANDSTONE);
			registry.setBlockState("upper", Blocks.RED_SAND.defaultBlockState());
			registry.setBlockState("structure_primary", Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_RED_SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary_stairs", Blocks.RED_SANDSTONE_STAIRS.defaultBlockState());
			registry.setBlockState("village_path", Blocks.SAND.defaultBlockState());
			registry.setBlockState("cruxite_ore", MSBlocks.RED_SANDSTONE_CRUXITE_ORE.defaultBlockState());
			registry.setBlockState("uranium_ore", MSBlocks.RED_SANDSTONE_URANIUM_ORE.defaultBlockState());
		}
		registry.setBlockState("structure_secondary", Blocks.STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", Blocks.CHISELED_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_planks", Blocks.ACACIA_PLANKS.defaultBlockState());
		registry.setBlockState("structure_planks_slab", Blocks.ACACIA_SLAB.defaultBlockState());
		registry.setBlockState("river", registry.getBlockState("upper"));
		registry.setBlockState("structure_wool_1", Blocks.YELLOW_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.MAGENTA_WOOL.defaultBlockState());
	}
	
	@Override
	public String[] getNames()
	{
		if(type == Variant.LUSH_DESERTS) {
			return new String[] {LUSH_DESERTS};
		} else {
			return new String[] {SAND, DUNES, DESERTS};
		}
	}
	
	@Override
	public LandBiomeSet getBiomeSet()
	{
		return MSBiomes.NO_RAIN_LAND;
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.Category.DESERT;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = 0.0F;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		if(this.type == Variant.LUSH_DESERTS)
		{
			
			if(type == LandBiomeType.NORMAL)
			{
				builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.OASIS.configured(IFeatureConfig.NONE)
						.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(128));
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
						.configured(MinestuckBiomeFeatures.DESERT_BUSH_CONFIG)
						.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(2));
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_CACTUS
						.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(5));
			}
			if(type == LandBiomeType.ROUGH)
			{
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
						.configured(MinestuckBiomeFeatures.DESERT_BUSH_CONFIG)
						.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(2));
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
						.configured(MinestuckBiomeFeatures.BLOOMING_CACTUS_CONFIG)
						.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(2));
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_CACTUS
						.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(3));
			}
		} else
		{
			DefaultBiomeFeatures.addDesertVegetation(builder);
			if(type == LandBiomeType.NORMAL)
			{
				builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.OASIS.configured(IFeatureConfig.NONE)
						.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(128));
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_CACTUS
						.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(5));
			}
			if(type == LandBiomeType.ROUGH)
			{
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH
						.configured(MinestuckBiomeFeatures.BLOOMING_CACTUS_CONFIG)
						.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(2));
				builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_CACTUS
						.decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(2));
			}
		}
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
				.configured(new SphereReplaceConfig(blocks.getBlockState("upper"), FeatureSpread.of(2, 4), 2, Lists.newArrayList(blocks.getBlockState("ground"))))
				.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(8));
		if(this.type != Variant.RED_SAND)
		{
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
					.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SANDSTONE_IRON_ORE.defaultBlockState(), 9))
					.range(64).squared().count(24));
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
					.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.SANDSTONE_GOLD_ORE.defaultBlockState(), 9))
					.range(32).squared().count(6));
		} else
		{
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
					.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.RED_SANDSTONE_IRON_ORE.defaultBlockState(), 9))
					.range(64).squared().count(24));
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE
					.configured(new OreFeatureConfig(blocks.getGroundType(), MSBlocks.RED_SANDSTONE_GOLD_ORE.defaultBlockState(), 9))
					.range(32).squared().count(6));
		}
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
		return MSEntityTypes.TURTLE;
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		addTurtleVillageCenters(register);
		
		register.add(ConsortVillageCenter.CactusPyramidCenter::new, 5);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		addTurtleVillagePieces(register, random);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return type == Variant.LUSH_DESERTS ? MSSoundEvents.MUSIC_LUSH_DESERTS : MSSoundEvents.MUSIC_SAND;
	}
	
	public enum Variant
	{
		SAND,
		LUSH_DESERTS,
		RED_SAND;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}
