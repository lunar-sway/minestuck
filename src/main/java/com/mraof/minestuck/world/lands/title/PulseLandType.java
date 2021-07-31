package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;

public class PulseLandType extends TitleLandType
{
	public static final String PULSE = "minestuck.pulse";
	public static final String BLOOD = "minestuck.blood";
	
	public PulseLandType()
	{
		super(EnumAspect.BLOOD);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{PULSE, BLOOD};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.RED_WOOL.defaultBlockState());
		registry.setBlockState("carpet", Blocks.BROWN_CARPET.defaultBlockState());
		
		registry.setBlockState("ocean", MSBlocks.BLOOD.defaultBlockState());
		registry.setBlockState("river", MSBlocks.BLOOD.defaultBlockState());
		registry.setBlockState("slime", MSBlocks.COAGULATED_BLOOD.defaultBlockState());
	}
	
	@Override
	public void setProperties(LandProperties properties)
	{
		properties.mergeFogColor(new Vector3d(0.8, 0, 0), 0.8F);
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = Math.max(settings.oceanChance, 0.2F);
	}
	/*
	@Override
	public void setBiomeSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.type == BiomeType.ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(MSBlocks.COAGULATED_BLOOD.getDefaultState(), 6, 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper")))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(25))));
		}
	}
	*/
	@Override
	public boolean isAspectCompatible(TerrainLandType aspect)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		aspect.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;	//Lava is likely a too important part of the terrain aspect to be replaced
	}
	
	@Override
	public SoundEvent getBackgroundMusic()
	{
		return MSSoundEvents.MUSIC_PULSE;
	}
}