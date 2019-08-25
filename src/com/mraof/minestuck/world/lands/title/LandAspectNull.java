package com.mraof.minestuck.world.lands.title;

public class LandAspectNull extends TitleLandAspect
{
	public LandAspectNull()
	{
		super(null, null, false);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[] {"Null"};
	}
	
}