package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;

public class NullTitleLandAspect extends TitleLandAspect
{
	public NullTitleLandAspect()
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