package com.mraof.minestuck.item.crafting.alchemy;

import net.minecraft.network.PacketBuffer;

import java.util.Objects;

/**
 * Container for a GristType + integer combination that might be useful when iterating through a GristSet.
 */
public class GristAmount
{	//TODO Make immutable
	private GristType type;
	private long amount;
	
	public GristAmount(GristType type, long amount)
	{
		this.type = type;
		this.amount = amount;
	}
	
	public GristType getType()
	{
		return type;
	}

	public long getAmount()
	{
		return amount;
	}
	
	/**
	 * @return a value estimate for this grist amount
	 */
	public float getValue()
	{
		return type.getValue()*amount;
	}
	
	@Override
	public String toString()
	{
		return "gristAmount:[type="+type.getRegistryName()+",amount="+amount+"]";
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		GristAmount that = (GristAmount) o;
		return amount == that.amount &&
				type.equals(that.type);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(type, amount);
	}
	
	public void write(PacketBuffer buffer)
	{
		buffer.writeRegistryId(getType());
		buffer.writeLong(getAmount());
	}
	
	public static GristAmount read(PacketBuffer buffer)
	{
		GristType type = buffer.readRegistryIdSafe(GristType.class);
		long amount = buffer.readLong();
		return new GristAmount(type, amount);
	}
}
