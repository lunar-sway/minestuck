package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.playerStats.EcheladderScreen;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerDataPacket	//TODO Probably healthier if this is several different packets
{
	private static final byte COLOR = 0, TITLE = 1, ECHELADDER = 2, BOONDOLLAR = 3;
	private static final int NO_COLOR = -2;	//Can be removed if we remove -1 as a default color when colors are made to hexes
	
	public int type;
	public int i1;
	public int i2;
	public long l;
	public float f;
	public boolean b;
	
	public static PlayerDataPacket color()
	{
		return color(NO_COLOR);
	}
	
	public static PlayerDataPacket color(int color)
	{
		PlayerDataPacket packet = new PlayerDataPacket();
		packet.type = COLOR;
		packet.i1 = color;
		
		return packet;
	}
	
	public static PlayerDataPacket title(EnumClass enumClass, EnumAspect enumAspect)
	{
		PlayerDataPacket packet = new PlayerDataPacket();
		packet.type = TITLE;
		packet.i1 = EnumClass.getIntFromClass(enumClass);
		packet.i2 = EnumAspect.getIntFromAspect(enumAspect);
		
		return packet;
	}
	
	public static PlayerDataPacket echeladder(int rung, float progress, boolean skipMessage)
	{
		PlayerDataPacket packet = new PlayerDataPacket();
		packet.type = ECHELADDER;
		packet.i1 = rung;
		packet.f = progress;
		packet.b = skipMessage;
		
		return packet;
	}
	
	public static PlayerDataPacket boondollars(long count)
	{
		PlayerDataPacket packet = new PlayerDataPacket();
		packet.type = BOONDOLLAR;
		packet.l = count;
		
		return packet;
	}
	
	public void encode(PacketBuffer buffer)
	{
		buffer.writeByte(type);
		if(type == COLOR)
		{
			if(i1 != NO_COLOR)
				buffer.writeInt(i1);
		} else if(type == TITLE)
		{
			buffer.writeInt(i1);
			buffer.writeInt(i2);
		} else if(type == ECHELADDER)
		{
			buffer.writeInt(i1);
			buffer.writeFloat(f);
			buffer.writeBoolean(b);
		} else if(type == BOONDOLLAR)
		{
			buffer.writeLong(l);
		}
	}
	
	public static PlayerDataPacket decode(PacketBuffer buffer)
	{
		PlayerDataPacket packet = new PlayerDataPacket();
		packet.type = buffer.readByte();
		if(packet.type == COLOR)
		{
			if(buffer.readableBytes() > 0)
				packet.i1 = buffer.readInt();
			else packet.i1 = NO_COLOR;
		}
		else if(packet.type == TITLE)
		{
			packet.i1 = buffer.readInt();
			packet.i2 = buffer.readInt();
		} else if(packet.type == ECHELADDER)
		{
			packet.i1 = buffer.readInt();
			packet.f = buffer.readFloat();
			packet.b = buffer.readBoolean();
		} else if(packet.type == BOONDOLLAR)
		{
			packet.l = buffer.readLong();
		}
		
		return packet;
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(this::execute);
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute()
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
			PlayerSavedData.title = new Title(EnumClass.getClassFromInt(i1), EnumAspect.getAspectFromInt(i2));
		} else if(type == ECHELADDER)
		{
			int prev = PlayerSavedData.rung;
			PlayerSavedData.rung = i1;
			PlayerSavedData.rungProgress = f;
			if(!b)
				for(prev++; prev <= i1; prev++)
				{
					TranslationTextComponent rung = new TranslationTextComponent("echeladder.rung"+prev);
					Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("You reached rung %s!", rung));	//TODO Translation key
				}
			else EcheladderScreen.animatedRung = EcheladderScreen.lastRung = i1;
		} else if(type == BOONDOLLAR)
		{
			PlayerSavedData.boondollars = l;
		}
	}
}