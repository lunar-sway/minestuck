package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.util.MSCapabilities;
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
		PlayerData data = PlayerSavedData.getData(player);
		if(data == null)
			return;
		
		boolean newToggle = !data.getData(MSCapabilities.EFFECT_TOGGLE);
		data.setData(MSCapabilities.EFFECT_TOGGLE, newToggle);
		
		MutableComponent message = newToggle
				? Component.translatable(ON)
				: Component.translatable(OFF);
		player.displayClientMessage(message, true);
	}
}
