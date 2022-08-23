package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.phys.Vec3;

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
		if(properties.biomes.getPrecipitation() == Biome.Precipitation.RAIN)
			properties.forceRain = LandProperties.ForceType.OFF;
		properties.skylightBase = Math.min(1/2F, properties.skylightBase);
		properties.mergeFogColor(new Vec3(0, 0, 0.1), 0.5F);
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.PUMPKIN.getHolder().orElseThrow());
		
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandType otherType)
	{
		LandProperties properties = LandProperties.createPartial(otherType);
		
		return properties.forceRain != LandProperties.ForceType.ON || properties.biomes.getPrecipitation() != Biome.Precipitation.RAIN;
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_SILENCE;
	}
}