package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.block.Blocks;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.biome.Biome;

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
		registry.setBlockState("structure_wool_2", Blocks.BLACK_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.BLUE_CARPET.getDefaultState());
		
		if(registry.getCustomBlock("torch") == null)
			registry.setBlockState("torch", Blocks.REDSTONE_TORCH.getDefaultState());
		if(registry.getCustomBlock("wall_torch") == null)
			registry.setBlockState("wall_torch", Blocks.REDSTONE_WALL_TORCH.getDefaultState());
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		if(properties.biomes.NORMAL.get().getPrecipitation() == Biome.RainType.RAIN)
			properties.forceRain = LandProperties.ForceType.OFF;
		properties.skylightBase = Math.min(1/2F, properties.skylightBase);
		properties.mergeFogColor(new Vector3d(0, 0, 0.1), 0.5F);
	}
	/*
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Feature.SIMPLE_BLOCK.withConfiguration(new BlockWithContextConfig(Blocks.PUMPKIN.getDefaultState(), Lists.newArrayList(blocks.getBlockState("surface")), Lists.newArrayList(Blocks.AIR.getDefaultState()), Lists.newArrayList(Blocks.AIR.getDefaultState()))).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(128))));
	}
	*/
	@Override
	public boolean isAspectCompatible(TerrainLandType aspect)
	{
		LandProperties properties = new LandProperties(aspect);
		aspect.setProperties(properties);
		return properties.forceRain != LandProperties.ForceType.ON || properties.biomes.NORMAL.get().getPrecipitation() != Biome.RainType.RAIN;
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_SILENCE;
	}
}