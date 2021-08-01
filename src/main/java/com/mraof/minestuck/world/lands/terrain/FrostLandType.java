package com.mraof.minestuck.world.lands.terrain;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

public class FrostLandType extends TerrainLandType
{
	public static final String FROST = "minestuck.frost";
	public static final String ICE = "minestuck.ice";
	public static final String SNOW = "minestuck.snow";
	
	private static final Vector3d fogColor = new Vector3d(0.5D, 0.6D, 0.98D);
	
	public FrostLandType()
	{
		super();
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("surface", Blocks.GRASS_BLOCK.defaultBlockState());
		registry.setBlockState("upper", Blocks.DIRT.defaultBlockState());
		registry.setBlockState("structure_primary", Blocks.PRISMARINE.defaultBlockState());
		registry.setBlockState("structure_primary_decorative", Blocks.PRISMARINE_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary", MSBlocks.FROST_BRICKS.defaultBlockState());
		registry.setBlockState("structure_secondary_stairs", MSBlocks.FROST_BRICK_STAIRS.defaultBlockState());
		registry.setBlockState("structure_secondary_decorative", MSBlocks.CHISELED_FROST_BRICKS.defaultBlockState());
		registry.setBlockState("structure_planks", Blocks.SPRUCE_PLANKS.defaultBlockState());
		registry.setBlockState("structure_planks_slab", Blocks.SPRUCE_SLAB.defaultBlockState());
		registry.setBlockState("river", Blocks.ICE.defaultBlockState());
		registry.setBlockState("light_block", Blocks.SEA_LANTERN.defaultBlockState());
		registry.setBlockState("bucket_1", Blocks.SNOW_BLOCK.defaultBlockState());
		registry.setBlockState("bush", Blocks.FERN.defaultBlockState());
		registry.setBlockState("structure_wool_1", Blocks.WHITE_WOOL.defaultBlockState());
		registry.setBlockState("structure_wool_3", Blocks.CYAN_WOOL.defaultBlockState());
	}
	
	@Override
	public String[] getNames() {
		return new String[] {FROST, ICE, SNOW};
	}
	
	@Override
	public LandBiomeSet getBiomeSet()
	{
		return MSBiomes.SNOW_LAND;
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.category = Biome.Category.ICY;
		properties.forceRain = LandProperties.ForceType.ON;
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = 1/4F;
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		//TODO
		//list.add(new SpruceTreeDecorator(MinestuckBlocks.log.getDefaultState().withProperty(BlockMinestuckLog1.VARIANT, BlockMinestuckLog1.BlockType.FROST), MinestuckBlocks.leaves1.getDefaultState().withProperty(BlockMinestuckLeaves1.VARIANT, BlockMinestuckLeaves1.BlockType.FROST).withProperty(BlockMinestuckLeaves1.CHECK_DECAY, Boolean.valueOf(false)), BiomeMinestuck.mediumNormal));
		//list.add(new SpruceTreeDecorator(MinestuckBlocks.log.getDefaultState().withProperty(BlockMinestuckLog1.VARIANT, BlockMinestuckLog1.BlockType.FROST), MinestuckBlocks.leaves1.getDefaultState().withProperty(BlockMinestuckLeaves1.VARIANT, BlockMinestuckLeaves1.BlockType.FROST).withProperty(BlockMinestuckLeaves1.CHECK_DECAY, Boolean.valueOf(false)), BiomeMinestuck.mediumRough));
		
		if(type != LandBiomeType.NORMAL)
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(Blocks.COARSE_DIRT.defaultBlockState(), FeatureSpread.of(2, 4), 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(10));
		
		if(type == LandBiomeType.NORMAL)
		{
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(Blocks.SNOW_BLOCK.defaultBlockState(), FeatureSpread.of(2, 2), 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(15));
		} else if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(Blocks.SNOW_BLOCK.defaultBlockState(), FeatureSpread.of(2, 1), 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(8));
			builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK
					.configured(new SphereReplaceConfig(Blocks.ICE.defaultBlockState(), FeatureSpread.of(2, 1), 1, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper"))))
					.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(8));
		}
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.PACKED_ICE.defaultBlockState(), 8))
				.range(64).squared().count(2));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.SNOW_BLOCK.defaultBlockState(), 16))
				.range(64).squared().count(3));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.DIRT.defaultBlockState(), 28))
				.range(64).squared().count(3));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.COAL_ORE.defaultBlockState(), 17))
				.range(64).squared().count(13));
		builder.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(blocks.getGroundType(), Blocks.DIAMOND_ORE.defaultBlockState(), 6))
				.range(24).squared().count(3));

		DefaultBiomeFeatures.addSurfaceFreezing(builder);
	}
	
	@Override
	public float getSkylightBase()
	{
		return 7/8F;
	}
	
	@Override
	public Vector3d getFogColor()
	{
		return fogColor;
	}
	
	@Override
	public Vector3d getSkyColor()
	{
		return new Vector3d(0.6D, 0.7D, 0.9D);
	}
	
	@Override
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return MSEntityTypes.IGUANA;
	}
	
	@Override
	public void addVillageCenters(CenterRegister register)
	{
		addIguanaVillageCenters(register);
	}
	
	@Override
	public void addVillagePieces(PieceRegister register, Random random)
	{
		addIguanaVillagePieces(register, random);
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_FROST;
	}
}