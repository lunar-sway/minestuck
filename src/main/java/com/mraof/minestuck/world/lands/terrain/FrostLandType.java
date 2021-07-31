package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSet;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;

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
	/*
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		//list.add(new SpruceTreeDecorator(MinestuckBlocks.log.getDefaultState().withProperty(BlockMinestuckLog1.VARIANT, BlockMinestuckLog1.BlockType.FROST), MinestuckBlocks.leaves1.getDefaultState().withProperty(BlockMinestuckLeaves1.VARIANT, BlockMinestuckLeaves1.BlockType.FROST).withProperty(BlockMinestuckLeaves1.CHECK_DECAY, Boolean.valueOf(false)), BiomeMinestuck.mediumNormal));
		//list.add(new SpruceTreeDecorator(MinestuckBlocks.log.getDefaultState().withProperty(BlockMinestuckLog1.VARIANT, BlockMinestuckLog1.BlockType.FROST), MinestuckBlocks.leaves1.getDefaultState().withProperty(BlockMinestuckLeaves1.VARIANT, BlockMinestuckLeaves1.BlockType.FROST).withProperty(BlockMinestuckLeaves1.CHECK_DECAY, Boolean.valueOf(false)), BiomeMinestuck.mediumRough));
		
		if(biome.type != BiomeType.NORMAL)
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(Blocks.COARSE_DIRT.getDefaultState(), 7, 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper")))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(10))));
		
		if(biome.type == BiomeType.NORMAL)
		{
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(Blocks.SNOW_BLOCK.getDefaultState(), 5, 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper")))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(15))));
		} else if(biome.type == BiomeType.ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(Blocks.SNOW_BLOCK.getDefaultState(), 4, 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper")))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(8))));
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(Blocks.ICE.getDefaultState(), 4, 1, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper")))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(8))));
		}
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.PACKED_ICE.getDefaultState(), 8)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(2, 0, 0, 64))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.SNOW_BLOCK.getDefaultState(), 16)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 64))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.DIRT.getDefaultState(), 28)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 64))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.COAL_ORE.getDefaultState(), 17)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(13, 0, 0, 64))));
		biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(blocks.getGroundType(), Blocks.DIAMOND_ORE.getDefaultState(), 6)).withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(3, 0, 0, 24))));

		DefaultBiomeFeatures.addFreezeTopLayer(biome);
	}
	*/
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