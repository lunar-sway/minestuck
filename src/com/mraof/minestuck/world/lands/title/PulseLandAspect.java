package com.mraof.minestuck.world.lands.title;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.LandDimension;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.LandGenSettings;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class PulseLandAspect extends TitleLandAspect
{
	public PulseLandAspect()
	{
		super(EnumAspect.BLOOD);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"pulse", "blood"};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.RED_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.BROWN_CARPET.getDefaultState());
		
		registry.setBlockState("ocean", MSBlocks.BLOOD.getDefaultState());
		registry.setBlockState("river", MSBlocks.BLOOD.getDefaultState());
		registry.setBlockState("slime", MSBlocks.COAGULATED_BLOOD.getDefaultState());
	}
	
	@Override
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.mergeFogColor(new Vec3d(0.8, 0, 0), 0.8F);
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = Math.max(settings.oceanChance, 0.2F);
	}
	
	@Override
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blocks)
	{
		if(biome.staticBiome == MSBiomes.LAND_ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.DISK, new SphereReplaceConfig(MSBlocks.COAGULATED_BLOOD.getDefaultState(), 6, 2, Lists.newArrayList(blocks.getBlockState("surface"), blocks.getBlockState("upper<<<<<"))), Placement.COUNT_TOP_SOLID, new FrequencyConfig(25)));
		}
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		aspect.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;	//Lava is likely a too important part of the terrain aspect to be replaced
	}
}