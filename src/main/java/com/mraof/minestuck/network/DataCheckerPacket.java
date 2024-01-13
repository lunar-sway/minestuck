package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.playerStats.DataCheckerScreen;
import com.mraof.minestuck.skaianet.DataCheckerManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DataCheckerPacket implements MSPacket.PlayToBoth
{
	
	private static int index = 0;
	
	/**
	 * Used to avoid confusion when the client sends several requests during a short period
	 */
	private final int packetIndex;
	private final CompoundTag nbtData;
	
	private DataCheckerPacket(int packetIndex, CompoundTag nbtData)
	{
		this.packetIndex = packetIndex;
		this.nbtData = nbtData;
	}
	
	public static DataCheckerPacket request()
	{
		index = (index + 1) % 100;
		return new DataCheckerPacket(index, null);
	}
	
	public static DataCheckerPacket data(int index, CompoundTag nbtData)
	{
		return new DataCheckerPacket(index, nbtData);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(packetIndex);
		if(nbtData != null)
		{
			try
			{
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				NbtIo.writeCompressed(nbtData, bytes);
				buffer.writeBytes(bytes.toByteArray());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static DataCheckerPacket decode(FriendlyByteBuf buffer)
	{
		int packetIndex = buffer.readInt();
		CompoundTag nbt = null;
		if(buffer.readableBytes() > 0)
		{
			byte[] bytes = new byte[buffer.readableBytes()];
			buffer.readBytes(bytes);
			try
			{
				nbt = NbtIo.readCompressed(new ByteArrayInputStream(bytes));
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return new DataCheckerPacket(packetIndex, nbt);
	}
	
	@Override
	public void execute()
	{
		if(packetIndex == index)
			DataCheckerScreen.activeComponent = new DataCheckerScreen.MainComponent(nbtData);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		DataCheckerManager.onDataRequest(player, packetIndex);
	}
}