package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.util.Debug;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.PacketBuffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ModusDataPacket implements PlayToClientPacket
{
	private final CompoundNBT nbt;
	
	private ModusDataPacket(CompoundNBT nbt)
	{
		this.nbt = Objects.requireNonNull(nbt);
	}
	
	public static ModusDataPacket create(CompoundNBT nbt)
	{
		return new ModusDataPacket(nbt);
	}
	
	@Override
	public void encode(PacketBuffer buffer)
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
	
	public static ModusDataPacket decode(PacketBuffer buffer)
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
	}
	
	@Override
	public void execute()
	{
		CaptchaDeckHandler.clientSideModus = CaptchaDeckHandler.readFromNBT(nbt, null);
		if(CaptchaDeckHandler.clientSideModus != null)
			MSScreenFactories.updateSylladexScreen();
		else Debug.debug("Player lost their modus after update packet");
	}
}