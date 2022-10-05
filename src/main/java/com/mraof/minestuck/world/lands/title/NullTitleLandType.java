package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;

public class NullTitleLandType extends TitleLandType
{
	public NullTitleLandType()
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