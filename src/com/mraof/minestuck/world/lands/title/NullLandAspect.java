package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;

public class NullLandAspect extends TitleLandAspect
{
	public NullLandAspect()
	{
		super(null, false);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"Null"};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
	
	}
}