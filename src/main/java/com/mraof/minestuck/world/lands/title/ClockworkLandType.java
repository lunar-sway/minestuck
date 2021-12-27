package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.block.Blocks;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class ClockworkLandType extends TitleLandType
{
	public static final String CLOCKWORK = "minestuck.clockwork";
	public static final String GEARS = "minestuck.gears";
	
	public ClockworkLandType()
	{
		super(EnumAspect.TIME);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{CLOCKWORK, GEARS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.LIGHT_GRAY_WOOL.defaultBlockState());
		registry.setBlockState("carpet", Blocks.RED_CARPET.defaultBlockState());
		registry.setBlockState("aspect_sapling", MSBlocks.TIME_ASPECT_SAPLING.getDefaultState());
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.mergeFogColor(new Vector3d(0.5, 0.5, 0.5), 0.5F);
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.COG.configured(IFeatureConfig.NONE).chance(2));
		} else
		{
			builder.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.COG.configured(IFeatureConfig.NONE).chance(10));
		}
		
		if(type == LandBiomeType.OCEAN)
		{
			builder.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.FLOOR_COG.configured(IFeatureConfig.NONE).chance(3));
		} else
		{
			builder.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, MSFeatures.FLOOR_COG.configured(IFeatureConfig.NONE).chance(20));
		}
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_CLOCKWORK;
	}
}