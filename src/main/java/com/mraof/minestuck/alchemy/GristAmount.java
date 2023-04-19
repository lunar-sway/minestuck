package com.mraof.minestuck.alchemy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * Container for a GristType + integer combination that might be useful when iterating through a GristSet.
 */
public record GristAmount(GristType type, long amount) implements IGristSet
{
	public static final String GRIST_AMOUNT = "grist_amount";
	
	public GristAmount(Supplier<GristType> type, long amount)
	{
		this(type.get(), amount);
	}
	
	@Override
	public long getGrist(GristType type)
	{
		return this.type == type ? amount : 0;
	}
	
	@Override
	public double getValue()
	{
		return type.getValue() * amount;
	}
	
	@Override
	public Collection<GristAmount> asAmounts()
	{
		return Collections.singleton(this);
	}
	
	@Override
	public boolean isEmpty()
	{
		return amount != 0;
	}
	
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeRegistryId(GristTypes.getRegistry(), type());
		buffer.writeLong(amount());
	}
	
	public static GristAmount read(FriendlyByteBuf buffer)
	{
		GristType type = buffer.readRegistryIdSafe(GristType.class);
		long amount = buffer.readLong();
		return new GristAmount(type, amount);
	}
	
	@Override
	public Component asTextComponent()
	{
		return Component.translatable(GRIST_AMOUNT, amount(), type().getDisplayName());
	}
	
	private static String makeNBTPrefix(String prefix)
	{
		return prefix != null && !prefix.isEmpty() ? prefix + "_" : "";
	}
	
	public CompoundTag write(CompoundTag nbt, String keyPrefix)
	{
		keyPrefix = makeNBTPrefix(keyPrefix);
		type().write(nbt, keyPrefix + "type");
		nbt.putLong(keyPrefix + "amount", amount());
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