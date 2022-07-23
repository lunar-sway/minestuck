package com.mraof.minestuck.item.crafting.alchemy;

import java.util.Collection;

/*this is intended to tell the system "this is the max grist for each type(which is something equal and arbitrary)"
that adds up to our desired amount
 */
public class GristGutter extends GristSet
{
	public int gutterTotal;
	
	public static final int GUTTER_CAPACITY = 1000;
	
	public static long getGristLimitPerType()
	{
		Collection<GristType> values = GristTypes.values();
		int gutterCapacity = GUTTER_CAPACITY;
		
		long gutterTotal = gutterCapacity / values.size();
		
		return gutterTotal;
	}
}