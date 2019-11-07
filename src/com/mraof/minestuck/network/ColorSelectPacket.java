package com.mraof.minestuck.network;

import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.ColorCollector;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ColorSelectPacket
{
	public int colorIndex;
	
	public ColorSelectPacket(int colorIndex)
	{
		this.colorIndex = colorIndex;
	}
	
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(colorIndex);
	}
	
	public static ColorSelectPacket decode(PacketBuffer buffer)
	{
		int color = buffer.readInt();
		
		return new ColorSelectPacket(color);
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute(ServerPlayerEntity player)
	{
		if(SburbHandler.canSelectColor(player))
			PlayerSavedData.getData(player).color = ColorCollector.getColor(colorIndex);
	}
}