package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.Title;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TitleSelectPacket
{
	private final Title title;
	
	public TitleSelectPacket()
	{
		title = null;
	}
	
	public TitleSelectPacket(Title title)
	{
		this.title = title;
	}
	
	public void encode(PacketBuffer buffer)
	{
		if(title != null)
		{
			title.write(buffer);
		}
	}
	
	public static TitleSelectPacket decode(PacketBuffer buffer)
	{
		if(buffer.readableBytes() > 0)
		{
			Title title = Title.read(buffer);
			return new TitleSelectPacket(title);
		} else return new TitleSelectPacket();
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		else if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(this::execute);
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute()
	{
		MSScreenFactories.displayTitleSelectScreen(title);
	}
	
	public void execute(ServerPlayerEntity player)
	{
		SburbHandler.titleSelected(player, title);
	}
}