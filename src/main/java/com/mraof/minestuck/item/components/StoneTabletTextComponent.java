package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record StoneTabletTextComponent(String text)
{
	private static final StoneTabletTextComponent DEFAULT = new StoneTabletTextComponent("");
	
	public static final Codec<StoneTabletTextComponent> CODEC = Codec.STRING.xmap(StoneTabletTextComponent::new, StoneTabletTextComponent::text);
	
	public static final StreamCodec<ByteBuf, StoneTabletTextComponent> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(StoneTabletTextComponent::new, StoneTabletTextComponent::text);
	
	public static boolean hasText(ItemStack tablet)
	{
		return tablet.has(MSItemComponents.STONE_TABLET_TEXT);
	}
	
	public static String getText(ItemStack tablet)
	{
		return tablet.getOrDefault(MSItemComponents.STONE_TABLET_TEXT, DEFAULT).text;
	}
	
	public static void setText(ItemStack tablet, String text)
	{
		tablet.set(MSItemComponents.STONE_TABLET_TEXT, new StoneTabletTextComponent(text));
	}
}
