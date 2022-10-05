package com.mraof.minestuck.network.data;

import com.mraof.minestuck.client.gui.playerStats.EcheladderScreen;
import com.mraof.minestuck.network.PlayToClientPacket;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;

public class EcheladderDataPacket implements PlayToClientPacket
{
	private final int rung;
	private final float progress;
	private final boolean sendMessage;
	
	private EcheladderDataPacket(int rung, float progress, boolean sendMessage)
	{
		this.rung = rung;
		this.progress = progress;
		this.sendMessage = sendMessage;
	}
	
	public static EcheladderDataPacket create(int rung, float progress, boolean sendMessage)
	{
		return new EcheladderDataPacket(rung, progress, sendMessage);
	}
	
	public static EcheladderDataPacket init(int rung, float progress)
	{
		return new EcheladderDataPacket(rung, progress, false);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(rung);
		buffer.writeFloat(progress);
		buffer.writeBoolean(sendMessage);
	}
	
	public static EcheladderDataPacket decode(FriendlyByteBuf buffer)
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
				TranslatableComponent rung = new TranslatableComponent(Echeladder.translationKey(prev));
				Minecraft.getInstance().player.sendMessage(new TranslatableComponent(Echeladder.NEW_RUNG, rung), Util.NIL_UUID);
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