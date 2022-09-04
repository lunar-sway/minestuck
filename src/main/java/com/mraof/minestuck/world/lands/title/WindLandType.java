package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.biome.LandBiomeType;
import com.mraof.minestuck.world.gen.feature.MSPlacedFeatures;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.phys.Vec3;

public class WindLandType extends TitleLandType
{
	public static final String WIND = "minestuck.wind";
	
	public WindLandType()
	{
		super(EnumAspect.BREATH);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {WIND};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlock("structure_wool_2", Blocks.LIGHT_BLUE_WOOL);
		registry.setBlock("carpet", Blocks.CYAN_CARPET);
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.mergeFogColor(new Vec3(0.1, 0.2, 0.8), 0.3F);
		if(properties.forceRain == LandProperties.ForceType.OFF)
			properties.forceRain = LandProperties.ForceType.DEFAULT;
		
	}
	
	@Override
	public void setBiomeGeneration(BiomeGenerationSettings.Builder builder, StructureBlockRegistry blocks, LandBiomeType type, Biome baseBiome)
	{
		
		if(type != LandBiomeType.OCEAN)
			builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MSPlacedFeatures.PARCEL_PYXIS.getHolder().orElseThrow());
		
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_WIND;
	}
}