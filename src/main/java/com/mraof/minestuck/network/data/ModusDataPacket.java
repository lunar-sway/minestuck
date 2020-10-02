package com.mraof.minestuck.network.data;

import com.mraof.minestuck.network.PlayToClientPacket;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.PacketBuffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ModusDataPacket implements PlayToClientPacket
{
	private final CompoundNBT nbt;
	
	private ModusDataPacket(CompoundNBT nbt)
	{
		this.nbt = nbt;
	}
	
	public static ModusDataPacket create(CompoundNBT nbt)
	{
		return new ModusDataPacket(nbt);
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		if(nbt != null)
		{
			try
			{
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				CompressedStreamTools.writeCompressed(nbt, bytes);
				buffer.writeBytes(bytes.toByteArray());
			} catch(IOException e)
			{
				throw new IllegalStateException(e);
			}
		}
	}
	
	public static ModusDataPacket decode(PacketBuffer buffer)
	{
		if(buffer.readableBytes() > 0)
		{
			byte[] bytes = new byte[buffer.readableBytes()];
			buffer.readBytes(bytes);
			try
			{
				CompoundNBT nbt = CompressedStreamTools.readCompressed(new ByteArrayInputStream(bytes));
				return new ModusDataPacket(nbt);
			} catch(IOException e)
			{
				throw new IllegalStateException(e);
			}
		} else return new ModusDataPacket(null);
	}
	
	@Override
	public void execute()
	{
		ClientPlayerData.handleDataPacket(this);
	}
	
	public CompoundNBT getNBT()
	{
		return nbt;
	}
}