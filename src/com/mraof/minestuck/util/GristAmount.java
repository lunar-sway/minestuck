package com.mraof.minestuck.util;

/**
 * Container for a GristType + integer combination that might be useful when iterating through a GristSet.
 */
public class GristAmount
{
	private GristType type;
	private int amount;

	public GristAmount(GristType type, int amount)
	{
		this.type = type;
		this.amount = amount;
	}
	
	public GristType getType()
	{
		return type;
	}

	public int getAmount()
	{
		return amount;
	}
	
	@Override
	public String toString()
	{
		return "gristAmount:[type="+type.getName()+",amount="+amount+"]";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof GristAmount))
			return false;
		GristAmount grist = (GristAmount) obj;
		return this.type == grist.type && this.amount == grist.amount;
	}
	
	@Override
	public int hashCode()
	{
		return type.hashCode() + new Integer(amount).hashCode();
	}
	
}
