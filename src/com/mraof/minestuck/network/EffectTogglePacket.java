package com.mraof.minestuck.network;

import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EffectTogglePacket
{
	public void encode(PacketBuffer buffer)
	{
	}
	
	public static EffectTogglePacket decode(PacketBuffer buffer)
	{
		return new EffectTogglePacket();
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute(EntityPlayerMP player)
	{
		IdentifierHandler.PlayerIdentifier handler = IdentifierHandler.encode(player);
		MinestuckPlayerData.setEffectToggle(handler, !MinestuckPlayerData.getEffectToggle(handler));
		if(MinestuckPlayerData.getData(handler).effectToggle)
		{
			player.sendStatusMessage(new TextComponentTranslation("aspectEffects.on"), true);
		} else
		{
			player.sendStatusMessage(new TextComponentTranslation("aspectEffects.off"), true);
		}
	}
}