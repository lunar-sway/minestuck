package com.mraof.minestuck.world.lands.title;

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
	
}