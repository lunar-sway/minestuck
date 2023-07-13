package com.mraof.minestuck.network.data;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.alchemy.ClientCardCaptchas;
import com.mraof.minestuck.network.PlayToClientPacket;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Map;

public class ShareCaptchasPacket implements PlayToClientPacket
{
	private final Map<String, String> captchas;
	
	public ShareCaptchasPacket(Map<String, String> captchas)
	{
		this.captchas = captchas;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		for(Map.Entry<String, String> captchaEntry : captchas.entrySet())
		{
			buffer.writeUtf(captchaEntry.getKey(), 256); //TODO confirm this number is accurate
			buffer.writeUtf(captchaEntry.getValue(), 8);
		}
	}
	
	public static ShareCaptchasPacket decode(FriendlyByteBuf buffer)
	{
		ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<>();
		
		while(buffer.isReadable())
		{
			builder.put(buffer.readUtf(256), buffer.readUtf(8));
		}
		
		return new ShareCaptchasPacket(builder.build());
	}
	
	@Override
	public void execute()
	{
		ClientCardCaptchas.receivePacket(this);
	}
	
	public Map<String, String> getCaptchas()
	{
		return captchas;
	}
}