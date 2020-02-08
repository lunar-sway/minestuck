package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.playerStats.EcheladderScreen;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;

public class EcheladderDataPacket implements PlayToClientPacket
{
	private final int rung;
	private final float progress;
	private final boolean sendMessage;
	private final boolean available;
	
	private EcheladderDataPacket(int rung, float progress, boolean sendMessage, boolean available)
	{
		this.rung = rung;
		this.progress = progress;
		this.sendMessage = sendMessage;
		this.available = available;
	}
	
	public static EcheladderDataPacket create(int rung, float progress, boolean sendMessage)
	{
		return new EcheladderDataPacket(rung, progress, sendMessage, true);
	}
	
	public static EcheladderDataPacket init(int rung, float progress, boolean available)
	{
		return new EcheladderDataPacket(rung, progress, false, available);
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(rung);
		buffer.writeFloat(progress);
		buffer.writeBoolean(sendMessage);
		buffer.writeBoolean(available);
	}
	
	public static EcheladderDataPacket decode(PacketBuffer buffer)
	{
		int rung = buffer.readInt();
		float progress = buffer.readFloat();
		boolean skipMessage = buffer.readBoolean();
		
		return create(rung, progress, skipMessage);
	}
	
	@Override
	public void execute()
	{
		int prev = ClientPlayerData.rung;
		ClientPlayerData.rung = rung;
		ClientPlayerData.rungProgress = progress;
		ClientPlayerData.echeladderAvailable = available;
		if(sendMessage)
			for(prev++; prev <= rung; prev++)
			{
				TranslationTextComponent rung = new TranslationTextComponent(Echeladder.translationKey(prev));
				Minecraft.getInstance().player.sendMessage(new TranslationTextComponent(Echeladder.NEW_RUNG, rung));
			}
		else EcheladderScreen.animatedRung = EcheladderScreen.lastRung = rung;
	}
}