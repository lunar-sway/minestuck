package com.mraof.minestuck.network;

import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

public class EffectTogglePacket implements PlayToServerPacket
{
	public static final String ON = "minestuck.aspect_effects.on";
	public static final String OFF = "minestuck.aspect_effects.off";
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
	}
	
	public static EffectTogglePacket decode(FriendlyByteBuf buffer)
	{
		return new EffectTogglePacket();
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		PlayerData data = PlayerSavedData.getData(player);
		data.effectToggle(!data.effectToggle());
		if(data.effectToggle())
		{
			player.displayClientMessage(new TranslatableComponent(ON), true);
		} else
		{
			player.displayClientMessage(new TranslatableComponent(OFF), true);
		}
	}
}