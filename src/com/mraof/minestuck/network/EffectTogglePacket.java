package com.mraof.minestuck.network;

import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EffectTogglePacket
{
	public static final String ON = "minestuck.aspect_effects.on";
	public static final String OFF = "minestuck.aspect_effects.off";
	
	public void encode(PacketBuffer buffer)
	{
	}
	
	public static EffectTogglePacket decode(PacketBuffer buffer)
	{
		return new EffectTogglePacket();
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute(ServerPlayerEntity player)
	{
		PlayerData data = PlayerSavedData.getData(player);
		data.effectToggle(!data.effectToggle());
		if(data.effectToggle())
		{
			player.sendStatusMessage(new TranslationTextComponent(ON), true);
		} else
		{
			player.sendStatusMessage(new TranslationTextComponent(OFF), true);
		}
	}
}