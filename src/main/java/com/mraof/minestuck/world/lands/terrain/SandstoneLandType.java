package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.BlockState;
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

public class SandstoneLandType extends TerrainLandType
{
	public static final String SANDSTONE = "minestuck.sandstone";
	public static final String STONY_DESERTS = "minestuck.stony_deserts";
	
	public static final ResourceLocation GROUP_NAME = new ResourceLocation(Minestuck.MOD_ID, "sandstone");
	private final Vector3d fogColor, skyColor;
	private final Variant type;
	
	public SandstoneLandType(Variant type)
	{
		super(GROUP_NAME);
		this.type = type;
		if(type == Variant.SANDSTONE)
		{
			fogColor = new Vector3d(0.9D, 0.7D, 0.05D);
			skyColor = new Vector3d(0.8D, 0.6D, 0.2D);
		} else
		{
			fogColor = new Vector3d(0.7D, 0.4D, 0.05D);
			skyColor = new Vector3d(0.8D, 0.5D, 0.1D);
			
		}
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		if(type == Variant.SANDSTONE)
		{
			registry.setBlockState("sand", Blocks.SAND.defaultBlockState());
			registry.setBlockState("ocean_surface", Blocks.SAND.defaultBlockState());
			registry.setBlockState("upper", Blocks.SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary", Blocks.SMOOTH_SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary_stairs", Blocks.SANDSTONE_STAIRS.defaultBlockState());
			registry.setBlockState("village_path", Blocks.RED_SAND.defaultBlockState());
		} else
		{
			registry.setBlockState("sand", Blocks.RED_SAND.defaultBlockState());
			registry.setBlockState("ocean_surface", Blocks.RED_SAND.defaultBlockState());
			registry.setBlockState("upper", Blocks.RED_SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary", Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary_decorative", Blocks.CHISELED_RED_SANDSTONE.defaultBlockState());
			registry.setBlockState("structure_primary_stairs", Blocks.RED_SANDSTONE_STAIRS.defaultBlockState());
			registry.setBlockState("village_path", Blocks.SAND.defaultBlockState());
		}
		registry.setBlockState("structure_secondary", Blocks.STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", Blocks.CHISELED_STONE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", Blocks.STONE_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_planks", Blocks.ACACIA_PLANKS.defaultBlockState());
		registry.setBlockState("structure_planks_slab", Blocks.ACACIA_SLAB.defaultBlockState());
		registry.setBlockState("torch", Blocks.REDSTONE_TORCH.defaultBlockState());
		registry.setBlockState("wall_torch", Blocks.REDSTONE_WALL_TORCH.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.WHITE_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.MAGENTA_WOOL.defaultBlockState());
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {SANDSTONE, STONY_DESERTS};
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.Category.MESA;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = 1/10F;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		BlockState sand = blocks.getBlockState("sand");
		BlockState sandstone = blocks.getBlockState("upper");
		
		if(type != LandBiomeType.OCEAN)
		{
			DefaultBiomeFeatures.addDesertVegetation(builder);
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(sand, FeatureSpread.of(2, 4), 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(4));
		}
		
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.BLOCK_BLOB
					.configured(new BlockStateFeatureConfig(sandstone)).decorated(Features.Placements.HEIGHTMAP_SQUARE).countRandom(3));
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.BLOCK_BLOB
					.configured(new BlockStateFeatureConfig(sandstone)).decorated(Features.Placements.HEIGHTMAP_SQUARE).countRandom(5));
		}
		
		builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.ORE
				.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, sandstone, 28))
				.range(256).squared().count(8));
		builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.ORE
				.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.IRON_ORE.defaultBlockState(), 9))
				.range(64).squared().count(24));
		builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.ORE
				.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, Blocks.REDSTONE_ORE.defaultBlockState(), 8))
				.range(32).squared().count(12));
	}
	
	@Override
	public float getSkylightBase()
	{
		return 3/4F;
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
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		addTurtleVillagePieces(register, random);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_SANDSTONE;
	}
	
	public enum Variant
	{
		SANDSTONE,
		RED_SANDSTONE;
		public String getName()
		{
			return this.toString().toLowerCase();
		}
	}
}