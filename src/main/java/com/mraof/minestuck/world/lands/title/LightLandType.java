package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Features;

public class LightLandType extends TitleLandType
{
	public static final String LIGHT = "minestuck.light";
	public static final String BRIGHTNESS = "minestuck.brightness";
	
	public LightLandType()
	{
		super(EnumAspect.LIGHT);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {LIGHT, BRIGHTNESS};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.ORANGE_WOOL.defaultBlockState());
		registry.setBlockState("carpet", Blocks.ORANGE_CARPET.defaultBlockState());
		registry.setBlockState("torch", Blocks.TORCH.defaultBlockState());
		registry.setBlockState("slime", MSBlocks.GLOWY_GOOP.defaultBlockState());
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.skylightBase = 1.0F;
		properties.mergeFogColor(new Vector3d(1, 1, 0.8), 0.5F);
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		BlockState lightBlock = blocks.getBlockState("light_block");
		if(type == LandBiomeType.ROUGH)
		{
			builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.LARGE_PILLAR
					.configured(new BlockStateFeatureConfig(lightBlock)).decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(3));
		} else
		{
			builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, MSFeatures.PILLAR
					.configured(new BlockStateFeatureConfig(lightBlock)).decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).chance(2));
		}
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandType otherType)
	{
		LandProperties properties = LandProperties.createPartial(otherType);
		
		return otherType.getSkylightBase() >= 1/2F && properties.forceThunder == LandProperties.ForceType.OFF;
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_LIGHT;
	}
}