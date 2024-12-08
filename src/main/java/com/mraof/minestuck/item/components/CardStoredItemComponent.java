package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record CardStoredItemComponent(ItemStack storedStack, boolean isGhostItem)
{
	public static final Codec<CardStoredItemComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					ItemStack.CODEC.fieldOf("item").forGetter(CardStoredItemComponent::storedStack),
					Codec.BOOL.optionalFieldOf("is_ghost_item", false).forGetter(CardStoredItemComponent::isGhostItem)
			).apply(instance, CardStoredItemComponent::new)
	);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, CardStoredItemComponent> STREAM_CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC,
			CardStoredItemComponent::storedStack,
			ByteBufCodecs.BOOL,
			CardStoredItemComponent::isGhostItem,
			CardStoredItemComponent::new
	);
	
	public static final CardStoredItemComponent EMPTY = new CardStoredItemComponent(ItemStack.EMPTY, false);
	
	/**
	 * Returns a non-ghost item if contained in the card, or an empty stack otherwise.
	 */
	public static ItemStack getContainedRealItem(ItemStack card)
	{
		return Optional.ofNullable(card.get(MSItemComponents.CARD_STORED_ITEM))
				.filter(component -> !component.isGhostItem())
				.map(CardStoredItemComponent::storedStack).orElse(ItemStack.EMPTY);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof CardStoredItemComponent(ItemStack stack, boolean ghostItem))) return false;
		return this.isGhostItem == ghostItem && ItemStack.matches(this.storedStack, stack);
	}
	
	@Override
	public int hashCode()
	{
		return 31 * ItemStack.hashItemAndComponents(this.storedStack) + Boolean.hashCode(this.isGhostItem);
	}
}
