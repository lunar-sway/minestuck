package com.mraof.minestuck.world.lands.title;

import com.google.common.collect.Lists;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.block.Blocks;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BlockWithContextConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;

public class SilenceLandType extends TitleLandType
{
	public static final String SILENCE = "minestuck.silence";
	
	public SilenceLandType()
	{
		super(EnumAspect.VOID);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{SILENCE};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.BLACK_WOOL.defaultBlockState());
		registry.setBlockState("carpet", Blocks.BLUE_CARPET.defaultBlockState());
		
		if(registry.getCustomBlock("torch") == null)
			registry.setBlockState("torch", Blocks.REDSTONE_TORCH.defaultBlockState());
		if(registry.getCustomBlock("wall_torch") == null)
			registry.setBlockState("wall_torch", Blocks.REDSTONE_WALL_TORCH.defaultBlockState());
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		if(properties.biomes.getPrecipitation() == Biome.RainType.RAIN)
			properties.forceRain = LandProperties.ForceType.OFF;
		properties.skylightBase = Math.min(1/2F, properties.skylightBase);
		properties.mergeFogColor(new Vector3d(0, 0, 0.1), 0.5F);
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		builder.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.SIMPLE_BLOCK
				.configured(new BlockWithContextConfig(Blocks.PUMPKIN.defaultBlockState(), Lists.newArrayList(blocks.getBlockState("surface")),
								Lists.newArrayList(Blocks.AIR.defaultBlockState()), Lists.newArrayList(Blocks.AIR.defaultBlockState())))
				.decorated(Features.Placements.HEIGHTMAP_SQUARE).chance(128));
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandType otherType)
	{
		LandProperties properties = LandProperties.createPartial(otherType);
		
		return properties.forceRain != LandProperties.ForceType.ON || properties.biomes.getPrecipitation() != Biome.RainType.RAIN;
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_SILENCE;
	}
}