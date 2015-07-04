package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.UsernameHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public class SelectionPacket extends MinestuckPacket
{
	public static final byte COLOR = 0;
	
	
	public byte type;
	public int color;
	
	@Override
	public MinestuckPacket generatePacket(Object... data)
	{
		byte type = (Byte) data[0];
		this.data.writeByte(type);
		if(type == COLOR)
			this.data.writeInt((Integer) data[1]);
		
		return this;
	}
	
	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		this.type = data.readByte();
		if(this.type == COLOR)
			this.color = data.readInt();
		
		return this;
	}
	
	@Override
	public void execute(EntityPlayer player)
	{
		if(this.type == COLOR)
		{
			if(SkaianetHandler.getClientConnection(UsernameHandler.encode(player.getName())) == null);
		}
	}
	
	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}
	
}