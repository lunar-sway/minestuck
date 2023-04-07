package com.mraof.minestuck.world.lands.terrain;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;

public class NullTerrainLandType extends TerrainLandType
{
	
	public NullTerrainLandType()
	{
		super(new Builder(MSEntityTypes.SALAMANDER).names("null")
				.fogColor(1, 1, 1));
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry blocks)
	{
	
	}
}