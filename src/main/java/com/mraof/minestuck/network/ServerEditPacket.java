package com.mraof.minestuck.network;

import com.mraof.minestuck.computer.editmode.ClientEditmodeData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ServerEditPacket implements PlayToClientPacket
{
	String target;
	CompoundTag deployTags;
	
	public static ServerEditPacket exit()
	{
		return new ServerEditPacket();
	}
	
	public static ServerEditPacket givenItems(CompoundTag deployTags)
	{
		ServerEditPacket packet = new ServerEditPacket();
		packet.deployTags = deployTags;
		return packet;
	}
	
	public static ServerEditPacket activate(String target, CompoundTag deployTags)
	{
		ServerEditPacket packet = new ServerEditPacket();
		packet.target = target;
		packet.deployTags = deployTags;
		return packet;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		if(target != null)
		{
			buffer.writeBoolean(true);
			buffer.writeUtf(target, 16);
		} else if(deployTags != null)
			buffer.writeBoolean(false);
		else return;
		
		if(deployTags != null)
		{
			try
			{
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				NbtIo.writeCompressed(deployTags, bytes);
				buffer.writeBytes(bytes.toByteArray());
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static ServerEditPacket decode(FriendlyByteBuf buffer)
	{
		ServerEditPacket packet = new ServerEditPacket();
		if(buffer.readableBytes() > 0)
		{
			if(buffer.readBoolean())
			{
				packet.target = buffer.readUtf(16);
			}
			
			if(buffer.readableBytes() > 0)
			{
				byte[] bytes = new byte[buffer.readableBytes()];
				buffer.readBytes(bytes);
				try
				{
					packet.deployTags = NbtIo.readCompressed(new ByteArrayInputStream(bytes));
				} catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return packet;
	}
	
	@Override
	public void execute()
	{
		ClientEditmodeData.onPacket(target, deployTags);
	}
}