package com.mraof.minestuck.network;

import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import net.minecraft.nbt.*;
import net.minecraft.network.FriendlyByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ServerEditPacket implements PlayToClientPacket
{
	EditmodeLocations locations;
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
	
	public static ServerEditPacket activate(String target, CompoundTag deployTags, EditmodeLocations locations)
	{
		ServerEditPacket packet = new ServerEditPacket();
		packet.target = target;
		packet.deployTags = deployTags;
		packet.locations = locations;
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
		
		CompoundTag combinedTags = new CompoundTag();
		
		if(deployTags != null)
		{
			combinedTags.put("deploy_tags", deployTags);
		}
		
		if(locations != null)
		{
			ListTag listTag = EditmodeLocations.write(locations.getLocations());
			combinedTags.put("editmode_locations", listTag);
		}
		
		try
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			NbtIo.writeCompressed(combinedTags, bytes);
			buffer.writeBytes(bytes.toByteArray());
		} catch(IOException e)
		{
			e.printStackTrace();
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
					CompoundTag combinedTags = NbtIo.readCompressed(new ByteArrayInputStream(bytes));
					
					if(combinedTags.contains("deploy_tags"))
					{
						packet.deployTags = combinedTags.getCompound("deploy_tags");
					}
					
					if(combinedTags.contains("editmode_locations"))
					{
						packet.locations = EditmodeLocations.read(combinedTags.getList("editmode_locations", Tag.TAG_COMPOUND));
					}
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
		ClientEditHandler.onClientPackage(target, deployTags, locations);
	}
}