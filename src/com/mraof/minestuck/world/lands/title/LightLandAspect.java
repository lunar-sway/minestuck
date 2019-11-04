package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.biome.LandWrapperBiome;
import com.mraof.minestuck.world.gen.feature.MSFeatures;
import com.mraof.minestuck.world.LandDimension;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.BushConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;

public class LightLandAspect extends TitleLandAspect
{
	public static final String LIGHT = "minestuck.light";
	public static final String BRIGHTNESS = "minestuck.brightness";
	
	public LightLandAspect()
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
		registry.setBlockState("structure_wool_2", Blocks.ORANGE_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.ORANGE_CARPET.getDefaultState());
		registry.setBlockState("torch", Blocks.TORCH.getDefaultState());
		registry.setBlockState("slime", MSBlocks.GLOWY_GOOP.getDefaultState());
	}
	
	@Override
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.skylightBase = 1.0F;
		worldProvider.mergeFogColor(new Vec3d(1, 1, 0.8), 0.5F);
	}
	
	@Override
	public void setBiomeGenSettings(LandWrapperBiome biome, StructureBlockRegistry blockRegistry)
	{
		BlockState lightBlock = blockRegistry.getBlockState("light_block");
		if(biome.staticBiome == MSBiomes.LAND_ROUGH)
		{
			biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(MSFeatures.LARGE_PILLAR, new BushConfig(lightBlock), Placement.COUNT_TOP_SOLID, new FrequencyConfig(3)));
		} else
		{
			biome.addFeature(GenerationStage.Decoration.LOCAL_MODIFICATIONS, Biome.createDecoratedFeature(MSFeatures.PILLAR, new BushConfig(lightBlock), Placement.CHANCE_TOP_SOLID_HEIGHTMAP, new ChanceConfig(2)));
		}
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		return aspect.getSkylightBase() >= 1/2F;	//TODO Add no thunder as condition
	}
	
}