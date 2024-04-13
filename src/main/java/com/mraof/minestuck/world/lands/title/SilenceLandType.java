package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeSetType;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandBiomeGenBuilder;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.phys.Vec3;

public class SilenceLandType extends TitleLandType
{
	public static final String SILENCE = "minestuck.silence";
	
	public SilenceLandType()
	{
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{SILENCE};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("structure_wool_2", Blocks.BLACK_WOOL);
		registry.setBlock("carpet", Blocks.BLUE_CARPET);
		
		if(registry.isUsingDefault("torch"))
			registry.setBlock("torch", Blocks.REDSTONE_TORCH);
		if(registry.isUsingDefault("wall_torch"))
			registry.setBlock("wall_torch", Blocks.REDSTONE_WALL_TORCH);
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		if(properties.biomes.hasPrecipitation() && properties.biomes.getTemperature() > 0)
			properties.forceRain = LandProperties.ForceType.OFF;
		properties.skylightBase = Math.min(1/2F, properties.skylightBase);
		properties.mergeFogColor(new Vec3(0, 0, 0.1), 0.5F);
	}
	
	@Override
	public void addBiomeGeneration(LandBiomeGenBuilder builder, StructureBlockRegistry blocks, LandBiomeSetType biomeSet)
	{
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.PUMPKIN, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MSPlacedFeatures.VOID_TREE, LandBiomeType.anyExcept(LandBiomeType.OCEAN));
		
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandType otherType)
	{
		LandProperties properties = LandProperties.createPartial(otherType);
		
		return properties.forceRain != LandProperties.ForceType.ON || !properties.biomes.hasPrecipitation() || properties.biomes.getTemperature() <= 0;
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_SILENCE.get();
	}
}