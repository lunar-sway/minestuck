package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record EffectTogglePacket() implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("effect_toggle");
	
	public static final String ON = "minestuck.aspect_effects.on";
	public static final String OFF = "minestuck.aspect_effects.off";
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
	}
	
	public static EffectTogglePacket read(FriendlyByteBuf ignored)
	{
		return new EffectTogglePacket();
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		boolean newToggle = !player.getData(MSAttachments.EFFECT_TOGGLE);
		player.setData(MSAttachments.EFFECT_TOGGLE, newToggle);
		
		MutableComponent message = newToggle
				? Component.translatable(ON)
				: Component.translatable(OFF);
		player.displayClientMessage(message, true);
	}
}
