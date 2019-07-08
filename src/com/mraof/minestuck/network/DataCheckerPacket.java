package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.playerStats.GuiDataChecker;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Supplier;

public class DataCheckerPacket
{
	
	public static int index = 0;
	
	/**
	 * Used to avoid confusion when the client sends several requests during a short period
	 */
	public int packetIndex;
	public NBTTagCompound nbtData;
	
	public static DataCheckerPacket request()
	{
		DataCheckerPacket packet = new DataCheckerPacket();
		index = (index + 1) % 100;
		packet.packetIndex = index;
		
		return packet;
	}
	
	public static DataCheckerPacket data(int index, NBTTagCompound nbtData)
	{
		DataCheckerPacket packet = new DataCheckerPacket();
		packet.packetIndex = index;
		packet.nbtData = nbtData;
		
		return packet;
	}
	
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(packetIndex);
		if(nbtData != null)
		{
			try
			{
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				CompressedStreamTools.writeCompressed(nbtData, bytes);
				buffer.writeBytes(bytes.toByteArray());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static DataCheckerPacket decode(PacketBuffer buffer)
	{
		DataCheckerPacket packet = new DataCheckerPacket();
		packet.packetIndex = buffer.readInt();
		
		if(buffer.readableBytes() > 0)
		{
			byte[] bytes = new byte[buffer.readableBytes()];
			buffer.readBytes(bytes);
			try
			{
				packet.nbtData = CompressedStreamTools.readCompressed(new ByteArrayInputStream(bytes));
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return packet;
	}
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		else if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(this::execute);
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute()
	{
		if(packetIndex == index)
			GuiDataChecker.activeComponent = new GuiDataChecker.MainComponent(nbtData);
	}
	
	public void execute(EntityPlayerMP player)
	{
		if(MinestuckConfig.getDataCheckerPermissionFor(player))
		{
			NBTTagCompound data = SessionHandler.get(player.world).createDataTag();
			MinestuckPacketHandler.sendToPlayer(DataCheckerPacket.data(packetIndex, data), player);
		}
	}
}