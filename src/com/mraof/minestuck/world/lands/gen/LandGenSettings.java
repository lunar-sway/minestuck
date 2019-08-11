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
	}
	
	@Override
	public int getBedrockFloorHeight()
	{
		return 0;
	}
}