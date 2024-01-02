package com.mraof.minestuck.network.data;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.network.MSPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public record ModusDataPacket(@Nullable CompoundTag nbt) implements MSPacket.PlayToClient
{
	public static ModusDataPacket create(@Nullable Modus modus)
	{
		return new ModusDataPacket(CaptchaDeckHandler.writeToNBT(modus));
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
}