package com.mraof.minestuck.alchemy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Container for a GristType + integer combination that might be useful when iterating through a GristSet.
 */
public class GristAmount
{
	public static final String GRIST_AMOUNT = "grist_amount";
	
	private final GristType type;
	private final long amount;
	
	public GristAmount(Supplier<GristType> type, long amount)
	{
		this(type.get(), amount);
	}
	
	public GristAmount(GristType type, long amount)
	{
		this.type = Objects.requireNonNull(type);
		this.amount = amount;
	}
	
	@Nonnull
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
	public double getValue()
	{
		return type.getValue()*amount;
	}
	
	@Override
	public String toString()
	{
		return "gristAmount:[type="+type+",amount="+amount+"]";
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
	
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeRegistryId(GristTypes.getRegistry(), getType());
		buffer.writeLong(getAmount());
	}
	
	public static GristAmount read(FriendlyByteBuf buffer)
	{
		GristType type = buffer.readRegistryIdSafe(GristType.class);
		long amount = buffer.readLong();
		return new GristAmount(type, amount);
	}
	
	public Component asTextComponent()
	{
		return Component.translatable(GRIST_AMOUNT, getAmount(), getType().getDisplayName());
	}
	
	private static String makeNBTPrefix(String prefix)
	{
		return prefix != null && !prefix.isEmpty() ? prefix + "_" : "";
	}
	
	public CompoundTag write(CompoundTag nbt, String keyPrefix)
	{
		keyPrefix = makeNBTPrefix(keyPrefix);
		getType().write(nbt, keyPrefix + "type");
		nbt.putLong(keyPrefix + "amount", getAmount());
		return nbt;
	}
	
	public static GristAmount read(CompoundTag nbt, String keyPrefix)
	{
		keyPrefix = makeNBTPrefix(keyPrefix);
		GristType type = GristType.read(nbt, keyPrefix + "type");
		long amount = nbt.getLong(keyPrefix + "amount");
		return new GristAmount(type, amount);
	}
}