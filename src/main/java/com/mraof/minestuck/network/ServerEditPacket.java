package com.mraof.minestuck.network;

import com.mraof.minestuck.editmode.ClientEditHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.PacketBuffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ServerEditPacket implements PlayToClientPacket
{
	
	String target;
	int centerX, centerZ;
	CompoundNBT deployTags;
	
	public static ServerEditPacket exit()
	{
		return new ServerEditPacket();
	}
	
	public static ServerEditPacket givenItems(CompoundNBT deployTags)
	{
		ServerEditPacket packet = new ServerEditPacket();
		packet.deployTags = deployTags;
		return packet;
	}
	
	public static ServerEditPacket activate(String target, int centerX, int centerZ, CompoundNBT deployTags)
	{
		ServerEditPacket packet = new ServerEditPacket();
		packet.target = target;
		packet.centerX = centerX;
		packet.centerZ = centerZ;
		packet.deployTags = deployTags;
		return packet;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		if(target != null)
		{
			buffer.writeBoolean(true);
			buffer.writeString(target, 16);
			buffer.writeInt(centerX);
			buffer.writeInt(centerZ);
		} else if(deployTags != null)
			buffer.writeBoolean(false);
		else return;
		
		if(deployTags != null)
		{
			try
			{
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				CompressedStreamTools.writeCompressed(deployTags, bytes);
				buffer.writeBytes(bytes.toByteArray());
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static ServerEditPacket decode(PacketBuffer buffer)
	{
		ServerEditPacket packet = new ServerEditPacket();
		if(buffer.readableBytes() > 0)
		{
			if(buffer.readBoolean())
			{
				packet.target = buffer.readString(16);
				packet.centerX = buffer.readInt();
				packet.centerZ = buffer.readInt();
			}
			
			if(buffer.readableBytes() > 0)
			{
				byte[] bytes = new byte[buffer.readableBytes()];
				buffer.readBytes(bytes);
				try
				{
					packet.deployTags = CompressedStreamTools.readCompressed(new ByteArrayInputStream(bytes));
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
		ClientEditHandler.onClientPackage(target, centerX, centerZ, deployTags);
	}
}