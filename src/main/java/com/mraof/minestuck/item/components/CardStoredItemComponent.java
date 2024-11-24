package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.alchemy.CardCaptchas;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Optional;

public record CardStoredItemComponent(ItemStack storedStack, boolean isGhostItem, @Nullable String code)
{
	public static final Codec<CardStoredItemComponent> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					ItemStack.CODEC.fieldOf("item").forGetter(CardStoredItemComponent::storedStack),
					Codec.BOOL.optionalFieldOf("is_ghost_item", false).forGetter(CardStoredItemComponent::isGhostItem),
					Codec.STRING.optionalFieldOf("code").forGetter(o -> Optional.ofNullable(o.code))
			).apply(instance, (item, type, code) -> new CardStoredItemComponent(item, type, code.orElse(null)))
	);
	
	public static final StreamCodec<RegistryFriendlyByteBuf, CardStoredItemComponent> STREAM_CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC,
			CardStoredItemComponent::storedStack,
			ByteBufCodecs.BOOL,
			CardStoredItemComponent::isGhostItem,
			ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
			encodedItemComponent -> Optional.ofNullable(encodedItemComponent.code()),
			(stack, type, optionalCode) -> new CardStoredItemComponent(stack, type, optionalCode.orElse(null))
	);
	
	public static final CardStoredItemComponent EMPTY = new CardStoredItemComponent(ItemStack.EMPTY, false, "");
	
	public static CardStoredItemComponent create(ItemStack storedStack, boolean isGhostItem, MinecraftServer mcServer)
	{
		return new CardStoredItemComponent(storedStack, isGhostItem, storedStack.is(MSTags.Items.UNREADABLE) ? null : CardCaptchas.getCaptcha(storedStack.getItem(), mcServer));
	}
	
	public CardStoredItemComponent readable(MinecraftServer mcServer)
	{
		return new CardStoredItemComponent(this.storedStack, this.isGhostItem, CardCaptchas.getCaptcha(this.storedStack.getItem(), mcServer));
	}
	
	public boolean canReadCode()
	{
		return code() != null;
	}
}
