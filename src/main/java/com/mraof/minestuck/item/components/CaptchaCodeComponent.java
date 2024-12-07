package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.alchemy.CardCaptchas;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record CaptchaCodeComponent(String code, boolean hasRefreshed)
{
	public static final Codec<CaptchaCodeComponent> CODEC = Codec.STRING.xmap(code -> new CaptchaCodeComponent(code, false), CaptchaCodeComponent::code);
	public static final StreamCodec<ByteBuf, CaptchaCodeComponent> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CaptchaCodeComponent::code,
			code -> new CaptchaCodeComponent(code, false)
	);
	
	public static final CaptchaCodeComponent ZERO = new CaptchaCodeComponent(CardCaptchas.EMPTY_CARD_CAPTCHA, false);
	
	public static CaptchaCodeComponent createFor(ItemStack itemStack, MinecraftServer mcServer)
	{
		return createFor(itemStack.getItem(), mcServer);
	}
	
	public static CaptchaCodeComponent createFor(Item item, MinecraftServer mcServer)
	{
		return new CaptchaCodeComponent(CardCaptchas.getCaptcha(item, mcServer), true);
	}
}
