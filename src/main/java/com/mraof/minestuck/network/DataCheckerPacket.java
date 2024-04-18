package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.playerStats.DataCheckerScreen;
import com.mraof.minestuck.skaianet.DataCheckerManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class DataCheckerPacket implements MSPacket.PlayToBoth
{
	public static final ResourceLocation ID = Minestuck.id("data_checker");
	
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
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(packetIndex);
		if(nbtData != null)
		{
			buffer.writeNbt(nbtData);
		}
	}
	
	public static DataCheckerPacket read(FriendlyByteBuf buffer)
	{
		int packetIndex = buffer.readInt();
		CompoundTag nbt = null;
		if(buffer.readableBytes() > 0)
		{
			nbt = buffer.readNbt();
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