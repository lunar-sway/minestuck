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
	boolean[] givenItems;
	CompoundNBT deployTags;
	
	public static ServerEditPacket exit()
	{
		return new ServerEditPacket();
	}
	
	public static ServerEditPacket givenItems(boolean[] givenItems)
	{
		ServerEditPacket packet = new ServerEditPacket();
		packet.givenItems = givenItems;
		return packet;
	}
	
	public static ServerEditPacket activate(String target, int centerX, int centerZ, boolean[] givenItems, CompoundNBT deployTags)
	{
		ServerEditPacket packet = new ServerEditPacket();
		packet.target = target;
		packet.centerX = centerX;
		packet.centerZ = centerZ;
		packet.givenItems = givenItems;
		packet.deployTags = deployTags;
		return packet;
	}
	
	public void encode(PacketBuffer buffer)
	{
		if(target != null)
		{
			buffer.writeBoolean(true);
			buffer.writeString(target, 16);
			buffer.writeInt(centerX);
			buffer.writeInt(centerZ);
		} else if(givenItems != null)
			buffer.writeBoolean(false);
		else return;
		
		if(givenItems != null)
		{
			buffer.writeInt(givenItems.length);
			for(boolean b : givenItems)
				buffer.writeBoolean(b);
			
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
				packet.givenItems = new boolean[buffer.readInt()];
				for(int i = 0; i < packet.givenItems.length; i++)
				{
					packet.givenItems[i] = buffer.readBoolean();
				}
				
				if(buffer.readableBytes() > 0)
				{
					byte[] bytes = new byte[buffer.readableBytes()];
					buffer.readBytes(bytes);
					try
					{
						packet.deployTags = CompressedStreamTools.readCompressed(new ByteArrayInputStream(bytes));
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		
		return packet;
	}
	
	@Override
	public void execute()
	{
		ClientEditHandler.onClientPackage(target, centerX, centerZ, givenItems, deployTags);
	}
}