package com.mraof.minestuck.item.crafting.alchemy;

public class GristGutter
{
	public int gristSet;
	public int gutterTotal;
	
	public GristGutter addGristSet(GristSet set)
	{
		gristSet.addGrist(set);
		
		//TODO: Add logic
		return this;
	}
}