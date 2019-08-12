package com.mraof.minestuck.world.lands.gen;

import com.mraof.minestuck.world.biome.ModBiomes;
import com.mraof.minestuck.world.lands.LandAspects;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import net.minecraft.world.gen.GenerationSettings;

public class LandGenSettings extends GenerationSettings
{
	private LandAspects landAspects;
	private StructureBlockRegistry blockRegistry;
	public float normalBiomeDepth = ModBiomes.LAND_NORMAL.getDepth(), normalBiomeScale = ModBiomes.LAND_NORMAL.getScale();
	public float roughBiomeDepth = ModBiomes.LAND_ROUGH.getDepth(), roughBiomeScale = ModBiomes.LAND_ROUGH.getScale();
	public float oceanBiomeDepth = ModBiomes.LAND_OCEAN.getDepth(), oceanBiomeScale = ModBiomes.LAND_OCEAN.getScale();
	public float oceanChance = 1/3F, roughChance = 1/5F;
	
	public LandAspects getLandAspects()
	{
		return landAspects;
	}
	
	public StructureBlockRegistry getBlockRegistry()
	{
		return blockRegistry;
	}
	
	public void setLandAspects(LandAspects landAspects)
	{
		this.landAspects = landAspects;
		
		blockRegistry = new StructureBlockRegistry();
		landAspects.aspectTerrain.registerBlocks(blockRegistry);
		//TODO Also register from title landspect
		setDefaultBlock(blockRegistry.getBlockState("ground"));
		setDefaultFluid(blockRegistry.getBlockState("ocean"));
		
		landAspects.aspectTerrain.setSettings(this);
		landAspects.aspectTitle.setSettings(this);
	}
	
	@Override
	public int getBedrockFloorHeight()
	{
		return 0;
	}
}