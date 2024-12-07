package com.mraof.minestuck.item.components;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.alchemy.CardCaptchas;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record CaptchaCodeComponent(String code)
{
	public static final Codec<CaptchaCodeComponent> CODEC = Codec.STRING.xmap(CaptchaCodeComponent::new, CaptchaCodeComponent::code);
	public static final StreamCodec<ByteBuf, CaptchaCodeComponent> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CaptchaCodeComponent::code,
			CaptchaCodeComponent::new
	);
	
	public static final CaptchaCodeComponent ZERO = new CaptchaCodeComponent(CardCaptchas.EMPTY_CARD_CAPTCHA);
	
	public static CaptchaCodeComponent createFor(ItemStack itemStack, MinecraftServer mcServer)
	{
		return createFor(itemStack.getItem(), mcServer);
	}
	
	public static CaptchaCodeComponent createFor(Item item, MinecraftServer mcServer)
	{
		return new CaptchaCodeComponent(CardCaptchas.getCaptcha(item, mcServer));
	}
}
