package com.mraof.minestuck.network.data;

import com.mraof.minestuck.network.PlayToClientPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ModusDataPacket implements PlayToClientPacket
{
	private final CompoundTag nbt;
	
	private ModusDataPacket(CompoundTag nbt)
	{
		this.nbt = nbt;
	}
	
	public static ModusDataPacket create(CompoundTag nbt)
	{
		return new ModusDataPacket(nbt);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		if(nbt != null)
		{
			try
			{
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				NbtIo.writeCompressed(nbt, bytes);
				buffer.writeBytes(bytes.toByteArray());
			} catch(IOException e)
			{
				throw new IllegalStateException(e);
			}
		}
	}
	
	public static ModusDataPacket decode(FriendlyByteBuf buffer)
	{
		if(buffer.readableBytes() > 0)
		{
			byte[] bytes = new byte[buffer.readableBytes()];
			buffer.readBytes(bytes);
			try
			{
				CompoundTag nbt = NbtIo.readCompressed(new ByteArrayInputStream(bytes));
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
	
	public CompoundTag getNBT()
	{
		return nbt;
	}
}