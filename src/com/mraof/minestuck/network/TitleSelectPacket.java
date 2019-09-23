package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.Title;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TitleSelectPacket
{
	private final EnumClass enumClass;
	private final EnumAspect enumAspect;
	
	public TitleSelectPacket()
	{
		enumClass = null;
		enumAspect = null;
	}
	
	public TitleSelectPacket(EnumClass enumClass, EnumAspect enumAspect)
	{
		this.enumClass = enumClass;
		this.enumAspect = enumAspect;
	}
	
	public void encode(PacketBuffer buffer)
	{
		if(enumClass != null && enumAspect != null)
		{
			buffer.writeInt(EnumClass.getIntFromClass(enumClass));
			buffer.writeInt(EnumAspect.getIntFromAspect(enumAspect));
		}
	}
	
	public static TitleSelectPacket decode(PacketBuffer buffer)
	{
		if(buffer.readableBytes() > 0)
		{
			int classInt = buffer.readInt();
			int aspectInt = buffer.readInt();
			return new TitleSelectPacket(EnumClass.getClassFromInt(classInt), EnumAspect.getAspectFromInt(aspectInt));
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
		Title title;
		if(enumClass != null && enumAspect != null)
			title = new Title(enumClass, enumAspect);
		else title = null;
		
		MSScreenFactories.displayTitleSelectScreen(title);
	}
	
	public void execute(ServerPlayerEntity player)
	{
		Title title;
		if(enumClass != null && enumAspect != null)
			title = new Title(enumClass, enumAspect);
		else title = null;
		
		SburbHandler.titleSelected(player, title);
	}
}