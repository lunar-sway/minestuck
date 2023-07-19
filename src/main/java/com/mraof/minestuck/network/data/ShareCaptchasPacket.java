package com.mraof.minestuck.network.data;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.alchemy.ClientCardCaptchas;
import com.mraof.minestuck.network.PlayToClientPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public record ShareCaptchasPacket(Map<Item, String> captchas) implements PlayToClientPacket
{
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		for(Map.Entry<Item, String> captchaEntry : captchas.entrySet())
		{
			buffer.writeRegistryId(ForgeRegistries.ITEMS, captchaEntry.getKey());
			buffer.writeUtf(captchaEntry.getValue(), 8);
		}
	}
	
	public static ShareCaptchasPacket decode(FriendlyByteBuf buffer)
	{
		ImmutableMap.Builder<Item, String> builder = new ImmutableMap.Builder<>();
		
		while(buffer.isReadable())
		{
			builder.put(buffer.readRegistryIdSafe(Item.class), buffer.readUtf(8));
		}
		
		return new ShareCaptchasPacket(builder.build());
	}
	
	@Override
	public void execute()
	{
		ClientCardCaptchas.receivePacket(this);
	}
}