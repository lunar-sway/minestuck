package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

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
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.mergeFogColor(new Vec3(0.5, 0.5, 0.5), 0.5F);
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		/*
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
		*/
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_CLOCKWORK;
	}
}