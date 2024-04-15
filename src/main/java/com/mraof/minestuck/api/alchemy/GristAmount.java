package com.mraof.minestuck.api.alchemy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Container for a GristType + integer combination that might be useful when iterating through a GristSet.
 */
public record GristAmount(GristType type, long amount) implements ImmutableGristSet
{
	public static final Codec<GristAmount> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(GristTypes.REGISTRY.byNameCodec().fieldOf("type").forGetter(GristAmount::type),
							Codec.LONG.fieldOf("amount").forGetter(GristAmount::amount))
					.apply(instance, GristAmount::new));
	public static final Codec<List<GristAmount>> LIST_CODEC = CODEC.listOf();
	public static final Codec<GristAmount> NON_NEGATIVE_CODEC = CODEC.comapFlatMap(GristAmount::checkNonNegative, Function.identity());
	public static final Codec<List<GristAmount>> NON_NEGATIVE_LIST_CODEC = NON_NEGATIVE_CODEC.listOf();
	
	private static DataResult<GristAmount> checkNonNegative(GristAmount gristAmount)
	{
		if(gristAmount.amount >= 0)
			return DataResult.success(gristAmount);
		else
			return DataResult.error(() -> "Negative amount %s for grist type %s".formatted(gristAmount.amount, gristAmount.type));
	}
	
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
	public boolean isEmpty()
	{
		return amount == 0;
	}
	
	@Override
	public boolean hasType(GristType type)
	{
		return this.type == type;
	}
	
	@Override
	public double getValue()
	{
		return type.getValue() * amount;
	}
	
	@Override
	public Map<GristType, Long> asMap()
	{
		return Map.of(type, amount);
	}
	
	@Override
	public List<GristAmount> asAmounts()
	{
		return Collections.singletonList(this);
	}
	
	@Override
	public Component asTextComponent()
	{
		return Component.translatable(GRIST_AMOUNT, amount(), type().getDisplayName());
	}
	
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeId(GristTypes.REGISTRY, type());
		buffer.writeLong(amount());
	}
	
	public static GristAmount read(FriendlyByteBuf buffer)
	{
		GristType type = buffer.readById(GristTypes.REGISTRY);
		long amount = buffer.readLong();
		return new GristAmount(type, amount);
	}
}