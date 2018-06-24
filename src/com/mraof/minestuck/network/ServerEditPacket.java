package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

import com.mraof.minestuck.editmode.ClientEditHandler;

public class ServerEditPacket extends MinestuckPacket
{
	
	String target;
	int posX, posZ;
	boolean[] givenItems;
	NBTTagCompound deployTags;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat)
	{
		if(dat.length == 0)
		{
			return this;
		}
		if(dat.length == 1 || dat.length == 2)
		{
			data.writeBoolean(true);
			boolean[] booleans = (boolean[]) dat[0];
			data.writeInt(booleans.length);
			for(boolean b : booleans)
				data.writeBoolean(b);
			if(dat.length == 2)
			{
				try
				{
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					CompressedStreamTools.writeCompressed((NBTTagCompound) dat[1], bytes);
					data.writeBytes(bytes.toByteArray());
				} catch(IOException e)
				{
					e.printStackTrace();
					return null;
				}
			}
			return this;
		}
		data.writeBoolean(false);
		writeString(data,dat[0].toString()+"\n");
		data.writeInt((Integer)dat[1]);
		data.writeInt((Integer)dat[2]);
		boolean[] booleans = (boolean[]) dat[3];
		data.writeInt(booleans.length);
		for(boolean b : booleans)
			data.writeBoolean(b);
		try
		{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			CompressedStreamTools.writeCompressed((NBTTagCompound) dat[4], bytes);
			data.writeBytes(bytes.toByteArray());
		} catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		if(data.readableBytes() == 0)
			return this;
		if(data.readBoolean())
		{
			givenItems = new boolean[data.readableBytes()];
			for(int i = 0; i < givenItems.length; i++)
			{
				givenItems[i] = data.readBoolean();
			}
			return this;
		}
		target = readLine(data);
		posX = data.readInt();
		posZ = data.readInt();
		givenItems = new boolean[data.readInt()];
		for(int i = 0; i < givenItems.length; i++)
		{
			givenItems[i] = data.readBoolean();
		}
		
		byte[] bytes = new byte[data.readableBytes()];
		data.readBytes(bytes);
		try
		{
			deployTags = CompressedStreamTools.readCompressed(new ByteArrayInputStream(bytes));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		ClientEditHandler.onClientPackage(target, posX, posZ, givenItems, deployTags);
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.SERVER);
	}

}
