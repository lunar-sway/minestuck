package com.mraof.minestuck.network;

import com.mraof.minestuck.editmode.ClientEditHandler;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Supplier;

public class ServerEditPacket
{
	
	String target;
	int centerX, centerZ;
	boolean[] givenItems;
	NBTTagCompound deployTags;
	
	public ServerEditPacket exit()
	{
		return new ServerEditPacket();
	}
	
	public ServerEditPacket givenItems(boolean[] givenItems)
	{
		ServerEditPacket packet = new ServerEditPacket();
		packet.givenItems = givenItems;
		return packet;
	}
	
	public ServerEditPacket activate(String target, int centerX, int centerZ, boolean[] givenItems, NBTTagCompound deployTags)
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
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(this::execute);
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute()
	{
		ClientEditHandler.onClientPackage(target, centerX, centerZ, givenItems, deployTags);
	}
}