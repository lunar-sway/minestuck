package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.util.ColorCollector;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.util.EnumClass;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.Title;

public class PlayerDataPacket extends MinestuckPacket 
{
	public static final byte COLOR = 0, TITLE = 1, ECHELADDER = 2;
	
	public int type;
	public int i1;
	public int i2;
	public float f;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat) 
	{
		byte type = (Byte) dat[0];
		data.writeByte(type);
		if(type == COLOR)
		{
			if(dat.length > 1)
				data.writeInt((Integer) dat[1]);
		} else if(type == TITLE)
		{
			data.writeInt(EnumClass.getIntFromClass((EnumClass) dat[1]));
			data.writeInt(EnumAspect.getIntFromAspect((EnumAspect) dat[2]));
		} else if(type == ECHELADDER)
		{
			data.writeInt((Integer) dat[1]);
			data.writeFloat((Float) dat[2]);
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
		} else if(type == ECHELADDER)
		{
			i1 = data.readInt();
			f = data.readFloat();
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
				ColorCollector.displaySelectionGui = true;
			}
			else ColorCollector.playerColor = i1;
		} else if(type == TITLE)
		{
			MinestuckPlayerData.title = new Title(EnumClass.getClassFromInt(i1), EnumAspect.getAspectFromInt(i2));
		} else if(type == ECHELADDER)
		{
			int prev = MinestuckPlayerData.rung;
			MinestuckPlayerData.rung = i1;
			//MinestuckPlayerData.progress = f;
			if(prev != -1)
				for(prev++; prev <= i1; prev++)
				{
					String s = StatCollector.canTranslate("echeladder.rung"+prev) ? StatCollector.translateToLocal("echeladder.rung"+prev) : String.valueOf(prev+1);
					player.addChatMessage(new ChatComponentText("You reached rung "+s+'!'));
				}
		}
	}
	
	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.SERVER);
	}
}