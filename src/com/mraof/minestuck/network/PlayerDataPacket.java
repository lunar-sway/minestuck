package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.util.ColorCollector;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.util.TitleHelper;

public class PlayerDataPacket extends MinestuckPacket 
{
	public static final byte COLOR = 0;
	public static final byte TITLE = 1;
	
	public int type;
	public int i1;
	public int i2;
	
	public PlayerDataPacket() 
	{
		super(Type.PLAYER_DATA);
	}

	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		byte type = (Byte) dat[0];
		data.writeByte(type);
		if(type == COLOR)
		{
			if(dat.length > 1)
				data.writeInt((Integer) dat[1]);
		}
		else if(type == TITLE)
		{
			data.writeInt(TitleHelper.getIntFromClass((EnumClass) dat[1]));
			data.writeInt(TitleHelper.getIntFromAspect((EnumAspect) dat[2]));
		}
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data) 
	{
		type = data.readByte();
		if(type == COLOR)
		{
			if(data.readableBytes() > 0)
				i1 = data.readInt();
			else i1 = -2;
		}
		else if(type == TITLE)
		{
			i1 = data.readInt();
			i2 = data.readInt();
		}
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if(type == COLOR)
		{
			if(i1 == -2)
			{
				ColorCollector.playerColor = -1;
				ColorCollector.selectionGui = true;
			}
			else ColorCollector.playerColor = i1;
		}
		else if(type == TITLE)
		{
			MinestuckPlayerData.title = new Title(TitleHelper.getClassFromInt(i1), TitleHelper.getAspectFromInt(i2));
		}
	}

	@Override
	public EnumSet<Side> getSenderSide() {
		return EnumSet.of(Side.SERVER);
	}
	
}
