package com.mraof.minestuck.network;

import com.mraof.minestuck.client.ClientProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class StopCreativeShockEffectPacket implements MSPacket.PlayToClient
{
	private final boolean mayBuild;
	
	public StopCreativeShockEffectPacket(boolean mayBuildIn)
	{
		this.mayBuild = mayBuildIn;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(mayBuild);
	}
	
	public static StopCreativeShockEffectPacket decode(FriendlyByteBuf buffer)
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