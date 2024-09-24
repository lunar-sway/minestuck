package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.alchemy.CardCaptchas;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.Optional;

public record EncodedItemComponent(ItemStack storedStack, EncodeType type, @Nullable String code)
{
	public static final Codec<EncodedItemComponent> FULL_CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					ItemStack.CODEC.fieldOf("item").forGetter(EncodedItemComponent::storedStack),
					EncodeType.CODEC.fieldOf("type").forGetter(EncodedItemComponent::type),
					Codec.BOOL.optionalFieldOf("discovered").forGetter(o -> Optional.of(o.code != null))
			).apply(instance, (item, type, discovered) -> create(item, type, discovered.isPresent() && discovered.get()))
	);
	
	public static final Codec<EncodedItemComponent> CODEC = Codec.withAlternative(FULL_CODEC, ItemStack.CODEC, stack -> EncodedItemComponent.create(stack, EncodeType.STORE, false));
	
	public static final StreamCodec<RegistryFriendlyByteBuf, EncodedItemComponent> STREAM_CODEC = StreamCodec.composite(
			ItemStack.STREAM_CODEC,
			EncodedItemComponent::storedStack,
			EncodeType.STREAM_CODEC,
			EncodedItemComponent::type,
			ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8),
			encodedItemComponent -> Optional.of(encodedItemComponent.code()),
			(stack, type, optionalCode) -> new EncodedItemComponent(stack, type, optionalCode.orElse(null))
	);
	
	public static final EncodedItemComponent EMPTY = new EncodedItemComponent(ItemStack.EMPTY, EncodeType.STORE, "");
	
	public static EncodedItemComponent create(ItemStack storedStack, EncodeType type, boolean codeDiscovered)
	{
		return new EncodedItemComponent(storedStack, type, !codeDiscovered && storedStack.is(MSTags.Items.UNREADABLE) ? null : CardCaptchas.getCaptcha(storedStack.getItem(), ServerLifecycleHooks.getCurrentServer()));
	}
	
	public String code()
	{
		return code;
	}
	
	public boolean canReadCode()
	{
		return code() != null;
	}
	
	public enum EncodeType implements StringRepresentable
	{
		STORE("stored"),
		PUNCHED("punched"),
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
