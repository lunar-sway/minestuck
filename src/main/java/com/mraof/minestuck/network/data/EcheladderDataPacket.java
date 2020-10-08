package com.mraof.minestuck.network.data;

import com.mraof.minestuck.client.gui.playerStats.EcheladderScreen;
import com.mraof.minestuck.network.PlayToClientPacket;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;

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
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(rung);
		buffer.writeFloat(progress);
		buffer.writeBoolean(sendMessage);
	}
	
	public static EcheladderDataPacket decode(PacketBuffer buffer)
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
				TranslationTextComponent rung = new TranslationTextComponent(Echeladder.translationKey(prev));
				Minecraft.getInstance().player.sendMessage(new TranslationTextComponent(Echeladder.NEW_RUNG, rung), Util.DUMMY_UUID);
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