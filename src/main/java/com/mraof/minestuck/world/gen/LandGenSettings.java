package com.mraof.minestuck.world.gen;

import com.mraof.minestuck.world.biome.LandBiomeHolder;
import com.mraof.minestuck.world.gen.feature.structure.GateStructure;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.world.gen.GenerationSettings;

public class LandGenSettings extends GenerationSettings
{
	private LandTypePair landTypes;
	private StructureBlockRegistry blockRegistry;
	private LandBiomeHolder biomeHolder;
	private GateStructure.PieceFactory gatePiece;
	public float oceanChance = 1/3F, roughChance = 1/5F;
	
	public LandTypePair getLandTypes()
	{
		return landTypes;
	}
	
	public StructureBlockRegistry getBlockRegistry()
	{
		return blockRegistry;
	}
	
	public LandBiomeHolder getBiomeHolder()
	{
		return biomeHolder;
	}
	
	public void setLandTypes(LandTypePair landTypes)
	{
		this.landTypes = landTypes;
		
		landTypes.terrain.setGenSettings(this);
		landTypes.title.setGenSettings(this);
	}
	
	public void setStructureBlocks(StructureBlockRegistry blocks)
	{
		blockRegistry = blocks;
		setDefaultBlock(blockRegistry.getBlockState("ground"));
		setDefaultFluid(blockRegistry.getBlockState("ocean"));
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