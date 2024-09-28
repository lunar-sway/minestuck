package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.playerStats.EcheladderScreen;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.Echeladder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record EcheladderDataPacket(int rung, float progress, boolean sendMessage) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("echeladder_data");
	
	public static EcheladderDataPacket create(int rung, float progress, boolean sendMessage)
	{
		return new EcheladderDataPacket(rung, progress, sendMessage);
	}
	
	public static EcheladderDataPacket init(int rung, float progress)
	{
		return new EcheladderDataPacket(rung, progress, false);
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(rung);
		buffer.writeFloat(progress);
		buffer.writeBoolean(sendMessage);
	}
	
	public static EcheladderDataPacket read(FriendlyByteBuf buffer)
	{
		int rung = buffer.readInt();
		float progress = buffer.readFloat();
		boolean sendMessage = buffer.readBoolean();
		
		return create(rung, progress, sendMessage);
	}
	
	@Override
	public void execute()
	{
		int prev = ClientPlayerData.getRung();
		ClientPlayerData.handleDataPacket(this);
		if(sendMessage)
			for(prev++; prev <= rung; prev++)
			{
				Component rung = Component.translatable(Echeladder.translationKey(prev));
				Minecraft.getInstance().player.sendSystemMessage(Component.translatable(Echeladder.NEW_RUNG, rung));
			}
		else EcheladderScreen.animatedRung = EcheladderScreen.lastRung = rung;
	}
	
	public int getRung()
	{
		return rung;
	}
	
	public float getProgress()
	{
		return progress;
	}
	
}
