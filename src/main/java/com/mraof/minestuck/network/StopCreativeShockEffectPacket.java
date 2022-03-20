package com.mraof.minestuck.network;

import com.mraof.minestuck.client.ClientProxy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

public class StopCreativeShockEffectPacket implements PlayToClientPacket
{
	private final boolean mayBuild;
	
	public StopCreativeShockEffectPacket(boolean mayBuildIn)
	{
		this.mayBuild = mayBuildIn;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBoolean(mayBuild);
	}
	
	public static StopCreativeShockEffectPacket decode(PacketBuffer buffer)
	{
		boolean mayBuild = buffer.readBoolean();
		
		return new StopCreativeShockEffectPacket(mayBuild);
	}
	
	@Override
	public void execute()
	{
		PlayerEntity playerEntity = ClientProxy.getClientPlayer();
		if(playerEntity != null)
		{
			playerEntity.abilities.mayBuild = !mayBuild;
		}
	}
}