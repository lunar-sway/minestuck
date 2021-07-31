package com.mraof.minestuck.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.GameType;

public class StopBuildInhibitEffectPacket implements PlayToClientPacket
{
	public GameType playerGameType;
	
	public StopBuildInhibitEffectPacket(GameType playerGameTypeIn)
	{
		this.playerGameType = playerGameTypeIn;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(playerGameType.getID());
	}
	
	public static StopBuildInhibitEffectPacket decode(PacketBuffer buffer)
	{
		GameType gameType = GameType.getByID(buffer.readInt());
		
		return new StopBuildInhibitEffectPacket(gameType);
	}
	
	@Override
	public void execute()
	{
		PlayerEntity playerEntity = Minecraft.getInstance().player;
		if(!playerEntity.isCreative())
		{
			//gameType.configurePlayerCapabilities(playerEntity.abilities);
			playerEntity.abilities.allowEdit = !playerGameType.hasLimitedInteractions();
		}
	}
}
