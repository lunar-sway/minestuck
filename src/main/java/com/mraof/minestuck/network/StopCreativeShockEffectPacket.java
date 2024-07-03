package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.ClientProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record StopCreativeShockEffectPacket(boolean mayBuild) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("stop_creative_shock_effect");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(mayBuild);
	}
	
	public static StopCreativeShockEffectPacket read(FriendlyByteBuf buffer)
	{
		boolean mayBuild = buffer.readBoolean();
		
		return new StopCreativeShockEffectPacket(mayBuild);
	}
	
	@Override
	public void execute()
	{
		Player playerEntity = ClientProxy.getClientPlayer();
		if(playerEntity != null)
		{
			playerEntity.getAbilities().mayBuild = !mayBuild;
		}
	}
}
