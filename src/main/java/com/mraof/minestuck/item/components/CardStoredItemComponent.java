package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.alchemy.CardCaptchas;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import javax.annotation.Nullable;
import java.util.Optional;

public record CardStoredItemComponent(ItemStack storedStack, EncodeType type, @Nullable String code)
{
	public static final Codec<CardStoredItemComponent> FULL_CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					ItemStack.CODEC.fieldOf("item").forGetter(CardStoredItemComponent::storedStack),
					EncodeType.CODEC.fieldOf("type").forGetter(CardStoredItemComponent::type),
					Codec.STRING.optionalFieldOf("code").forGetter(o -> Optional.ofNullable(o.code))
			).apply(instance, (item, type, code) -> new CardStoredItemComponent(item, type, code.orElse(null)))
	);
	
	public static final Codec<CardStoredItemComponent> CODEC = Codec.withAlternative(FULL_CODEC, ItemStack.CODEC, stack -> new CardStoredItemComponent(stack, EncodeType.STORE, null));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, CardStoredItemComponent> STREAM_CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC,
			CardStoredItemComponent::storedStack,
			EncodeType.STREAM_CODEC,
			CardStoredItemComponent::type,
			ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
			encodedItemComponent -> Optional.ofNullable(encodedItemComponent.code()),
			(stack, type, optionalCode) -> new CardStoredItemComponent(stack, type, optionalCode.orElse(null))
	);
	
	public static final CardStoredItemComponent EMPTY = new CardStoredItemComponent(ItemStack.EMPTY, EncodeType.STORE, "");
	
	public static CardStoredItemComponent createStoredItem(ItemStack storedStack, MinecraftServer mcServer)
	{
		return create(storedStack, EncodeType.STORE, mcServer);
	}
	
	public static CardStoredItemComponent createGhostItem(ItemStack storedStack, MinecraftServer mcServer)
	{
		return create(storedStack, EncodeType.GHOST, mcServer);
	}
	
	private static CardStoredItemComponent create(ItemStack storedStack, EncodeType type, MinecraftServer mcServer)
	{
		return new CardStoredItemComponent(storedStack, type, storedStack.is(MSTags.Items.UNREADABLE) ? null : CardCaptchas.getCaptcha(storedStack.getItem(), mcServer));
	}
	
	public CardStoredItemComponent readable(MinecraftServer mcServer)
	{
		return new CardStoredItemComponent(this.storedStack, this.type, CardCaptchas.getCaptcha(this.storedStack.getItem(), mcServer));
	}
	
	public String code()
	{
		return code;
	}
	
	public boolean canReadCode()
	{
		return code() != null;
	}
	
	public boolean isGhostType()
	{
		return this.type == EncodeType.GHOST;
	}
	
	public enum EncodeType implements StringRepresentable
	{
		STORE("stored"),
		GHOST("ghost"),
		;
		public static final Codec<EncodeType> CODEC = StringRepresentable.fromEnum(EncodeType::values);
		public static final StreamCodec<RegistryFriendlyByteBuf, EncodeType> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(EncodeType.class);
		
		private final String name;
		
		EncodeType(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getSerializedName()
		{
			return name;
		}
	}
}
