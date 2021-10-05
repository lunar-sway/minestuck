package com.mraof.minestuck.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.GameType;

public class StopCreativeShockEffectPacket implements PlayToClientPacket
{
	public GameType playerGameType;
	
	public StopCreativeShockEffectPacket(GameType playerGameTypeIn)
	{
		this.playerGameType = playerGameTypeIn;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(playerGameType.getID());
	}
	
	public static StopCreativeShockEffectPacket decode(PacketBuffer buffer)
	{
		GameType gameType = GameType.getByID(buffer.readInt());
		
		return new StopCreativeShockEffectPacket(gameType);
	}
	
	@Override
	public void execute()
	{
		PlayerEntity playerEntity = Minecraft.getInstance().player;
		if(playerEntity != null)
		{
			playerEntity.abilities.allowEdit = !playerGameType.hasLimitedInteractions();
		}
	}
}
