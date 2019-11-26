package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.gen.feature.structure.GateStructure;
import com.mraof.minestuck.world.lands.LandAspects;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.world.gen.GenerationSettings;

import java.util.Random;

public class LandGenSettings extends GenerationSettings
{
	private LandAspects landAspects;
	private StructureBlockRegistry blockRegistry;
	private LandBiomeHolder biomeHolder;
	private GateStructure.PieceFactory gatePiece;
	public float oceanChance = 1/3F, roughChance = 1/5F;
	
	public LandAspects getLandAspects()
	{
		return landAspects;
	}
	
	public StructureBlockRegistry getBlockRegistry()
	{
		return blockRegistry;
	}
	
	public LandBiomeHolder getBiomeHolder()
	{
		return biomeHolder;
	}
	
	public void setLandAspects(LandAspects landAspects)
	{
		this.landAspects = landAspects;
		
		blockRegistry = new StructureBlockRegistry();
		landAspects.terrain.registerBlocks(blockRegistry);
		landAspects.title.registerBlocks(blockRegistry);
		setDefaultBlock(blockRegistry.getBlockState("ground"));
		setDefaultFluid(blockRegistry.getBlockState("ocean"));
		
		landAspects.terrain.setGenSettings(this);
		landAspects.title.setGenSettings(this);
	}
	
	public void setBiomeHolder(LandBiomeHolder biomeHolder)
	{
		this.biomeHolder = biomeHolder;
	}
	
	public void setGatePiece(GateStructure.PieceFactory factory)
	{
		gatePiece = factory;
	}
	
	public GateStructure.PieceFactory getGatePiece()
	{
		return gatePiece;
	}
	
	@Override
	public int getBedrockFloorHeight()
	{
		return 0;
	}
}