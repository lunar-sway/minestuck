package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.block.TransportalizerItem;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MSItemComponents
{
	public static final DeferredRegister.DataComponents REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Minestuck.MOD_ID);
	
	public static final Supplier<DataComponentType<EncodedItemComponent>> ENCODED_ITEM = REGISTRY.registerComponentType("encoded_item",
			builder -> builder.persistent(EncodedItemComponent.CODEC).networkSynchronized(EncodedItemComponent.STREAM_CODEC));
	public static final Supplier<DataComponentType<CardStoredItemComponent>> CARD_STORED_ITEM = REGISTRY.registerComponentType("card_stored_item",
			builder -> builder.persistent(CardStoredItemComponent.CODEC).networkSynchronized(CardStoredItemComponent.STREAM_CODEC));
	public static final Supplier<DataComponentType<CaptchaCodeComponent>> CAPTCHA_CODE = REGISTRY.registerComponentType("captcha_code",
			builder -> builder.persistent(CaptchaCodeComponent.CODEC).networkSynchronized(CaptchaCodeComponent.STREAM_CODEC));
	
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<FrogTraitsComponent>> FROG_TRAITS = REGISTRY.register("frog_traits", () ->
			new DataComponentType.Builder<FrogTraitsComponent>()
					.persistent(FrogTraitsComponent.CODEC)
					.networkSynchronized(FrogTraitsComponent.STREAM_CODEC)
					.build());
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<StoneTabletTextComponent>> STONE_TABLET_TEXT = REGISTRY.register("stone_tablet_text", () ->
			new DataComponentType.Builder<StoneTabletTextComponent>()
					.persistent(StoneTabletTextComponent.CODEC)
					.networkSynchronized(StoneTabletTextComponent.STREAM_CODEC)
					.build());
	public static final Supplier<DataComponentType<HieroglyphCode>> HIEROGLYPH_CODE = REGISTRY.registerComponentType("hieroglyph_code",
			builder -> builder.persistent(HieroglyphCode.CODEC).networkSynchronized(HieroglyphCode.STREAM_CODEC));
	public static final Supplier<DataComponentType<Unit>> PARADOX_CODE = REGISTRY.registerComponentType("paradox_code",
			builder -> builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));
	
	public static final Supplier<DataComponentType<Long>> VALUE = REGISTRY.registerComponentType("value",
			builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG));
	public static final Supplier<DataComponentType<Integer>> COLOR = REGISTRY.registerComponentType("color",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final Supplier<DataComponentType<Integer>> POWER = REGISTRY.registerComponentType("power",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
	public static final Supplier<DataComponentType<GlobalPos>> TARGET_LOCATION = REGISTRY.registerComponentType("target_location",
			builder -> builder.persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC));
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<PosterComponent>> POSTER = REGISTRY.register("poster", () ->
			new DataComponentType.Builder<PosterComponent>()
					.persistent(PosterComponent.CODEC)
					.networkSynchronized(PosterComponent.STREAM_CODEC)
					.build());
	public static final Supplier<DataComponentType<TransportalizerItem.TransportalizerData>> TRANSPORTALIZER_DATA = REGISTRY.registerComponentType("transportalizer_data",
			builder -> builder.persistent(TransportalizerItem.TransportalizerData.CODEC).networkSynchronized(TransportalizerItem.TransportalizerData.STREAM_CODEC));
	
}
