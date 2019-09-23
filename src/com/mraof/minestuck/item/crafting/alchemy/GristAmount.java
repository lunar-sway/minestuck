package com.mraof.minestuck.item.crafting.alchemy;

import net.minecraft.network.PacketBuffer;

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
	
	public void write(PacketBuffer buffer)
	{
		buffer.writeRegistryId(getType());
		buffer.writeInt(getAmount());
	}
	
	public static GristAmount read(PacketBuffer buffer)
	{
		GristType type = buffer.readRegistryIdSafe(GristType.class);
		int amount = buffer.readInt();
		return new GristAmount(type, amount);
	}
}
