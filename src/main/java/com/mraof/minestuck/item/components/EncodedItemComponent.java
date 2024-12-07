package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public record EncodedItemComponent(Item item)
{
	public static final Codec<EncodedItemComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(EncodedItemComponent::item)
			).apply(instance, EncodedItemComponent::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, EncodedItemComponent> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.registry(Registries.ITEM),
			EncodedItemComponent::item,
			EncodedItemComponent::new
	);
	
	public ItemStack asItemStack()
	{
		return this.item().getDefaultInstance();
	}
	
	public static ItemStack createEncoded(ItemLike containerItem, Item encodedItem)
	{
		ItemStack stack = new ItemStack(containerItem);
		stack.set(MSItemComponents.ENCODED_ITEM, new EncodedItemComponent(encodedItem));
		return stack;
	}
	
	public static ItemStack setEncodedUnlessBlank(ItemStack stack, Item encodedItem)
	{
		if (encodedItem != MSItems.GENERIC_OBJECT.get())
			stack.set(MSItemComponents.ENCODED_ITEM, new EncodedItemComponent(encodedItem));
		return stack;
	}
	
	public static ItemStack getEncodedOrBlank(ItemStack stack)
	{
		EncodedItemComponent encodedItemComponent = stack.get(MSItemComponents.ENCODED_ITEM);
		return encodedItemComponent != null ? encodedItemComponent.asItemStack() : new ItemStack(MSItems.GENERIC_OBJECT.get());
	}
}
