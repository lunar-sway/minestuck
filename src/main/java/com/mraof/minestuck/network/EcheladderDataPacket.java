package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.playerStats.EcheladderScreen;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.Echeladder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EcheladderDataPacket(int rung, float progress, boolean sendMessage) implements MSPacket.PlayToClient
{
	public static final Type<EcheladderDataPacket> ID = new Type<>(Minestuck.id("echeladder_data"));
	public static final StreamCodec<RegistryFriendlyByteBuf, EcheladderDataPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			EcheladderDataPacket::rung,
			ByteBufCodecs.FLOAT,
			EcheladderDataPacket::progress,
			ByteBufCodecs.BOOL,
			EcheladderDataPacket::sendMessage,
			EcheladderDataPacket::new
	);
	
	public static EcheladderDataPacket create(int rung, float progress, boolean sendMessage)
	{
		return new EcheladderDataPacket(rung, progress, sendMessage);
	}
	
	public static EcheladderDataPacket init(int rung, float progress)
	{
		return new EcheladderDataPacket(rung, progress, false);
	}
	
	@Override
	public void execute(IPayloadContext context)
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
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
}
