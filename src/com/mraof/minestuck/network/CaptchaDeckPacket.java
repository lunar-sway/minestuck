package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckContainer;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.util.Debug;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Supplier;

public class CaptchaDeckPacket
{
	
	private static final byte DATA = 0;
	private static final byte MODUS = 1;
	private static final byte CAPTCHALOGUE = 2;
	private static final byte GET = 3;
	private static final byte MODUS_PARAM = 4;
	private static final byte CAPTCHALOGUE_INV = 5;
	
	public byte type;
	
	public CompoundNBT nbt;
	
	public int itemIndex;
	public boolean asCard;
	
	public byte valueType;
	public int value;
	
	public int slotIndex;
	
	public static CaptchaDeckPacket data(CompoundNBT nbt)
	{
		CaptchaDeckPacket packet = new CaptchaDeckPacket();
		packet.type = DATA;
		packet.nbt = nbt;
		
		return packet;
	}
	
	public static CaptchaDeckPacket modus()
	{
		CaptchaDeckPacket packet = new CaptchaDeckPacket();
		packet.type = MODUS;
		
		return packet;
	}
	
	public static CaptchaDeckPacket captchalogue()
	{
		CaptchaDeckPacket packet = new CaptchaDeckPacket();
		packet.type = CAPTCHALOGUE;
		
		return packet;
	}
	
	public static CaptchaDeckPacket get(int index, boolean asCard)
	{
		CaptchaDeckPacket packet = new CaptchaDeckPacket();
		packet.type = GET;
		packet.itemIndex = index;
		packet.asCard = asCard;
		
		return packet;
	}
	
	public static CaptchaDeckPacket modusParam(byte valueType, int value)
	{
		CaptchaDeckPacket packet = new CaptchaDeckPacket();
		packet.type = MODUS_PARAM;
		packet.valueType = valueType;
		packet.value = value;
		
		return packet;
	}
	
	public static CaptchaDeckPacket captchalogueInv(int slotIndex)
	{
		CaptchaDeckPacket packet = new CaptchaDeckPacket();
		packet.type = CAPTCHALOGUE_INV;
		packet.slotIndex = slotIndex;
		
		return packet;
	}
	
	public void encode(PacketBuffer buffer)
	{
		buffer.writeByte(type);	//Packet type
		if(type == DATA)	//Generic data from server side
		{
			try
			{
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				CompressedStreamTools.writeCompressed(nbt, bytes);
				buffer.writeBytes(bytes.toByteArray());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		} else if(type == GET)	//Take item from modus
		{
			buffer.writeInt(itemIndex);
			buffer.writeBoolean(asCard);
		} else if(type == MODUS_PARAM)
		{
			buffer.writeByte(valueType);
			buffer.writeInt(value);
		} else if(type == CAPTCHALOGUE_INV)
		{
			buffer.writeInt(slotIndex);
		}
	}
	
	public static CaptchaDeckPacket decode(PacketBuffer buffer)
	{
		CaptchaDeckPacket packet = new CaptchaDeckPacket();
		packet.type = buffer.readByte();
		
		if(buffer.readableBytes() > 0)
		{
			if(packet.type == DATA)
			{
				byte[] bytes = new byte[buffer.readableBytes()];
				buffer.readBytes(bytes);
				try
				{
					packet.nbt = CompressedStreamTools.readCompressed(new ByteArrayInputStream(bytes));
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			else if(packet.type == GET)
			{
				packet.itemIndex = buffer.readInt();
				packet.asCard = buffer.readBoolean();
			}
			else if(packet.type == MODUS_PARAM)
			{
				packet.valueType = buffer.readByte();
				packet.value = buffer.readInt();
			}
			else if(packet.type == CAPTCHALOGUE_INV)
			{
				packet.slotIndex = buffer.readInt();
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
		if(this.type == DATA)
		{
			CaptchaDeckHandler.clientSideModus = CaptchaDeckHandler.readFromNBT(nbt, null);
			if(CaptchaDeckHandler.clientSideModus != null)
				MSScreenFactories.updateSylladexScreen();
			else Debug.debug("Player lost their modus after update packet");
		}
	}
	
	public void execute(ServerPlayerEntity player)
	{
		if(ServerEditHandler.getData(player) != null)
			return;
		
		if(this.type == MODUS && player.openContainer instanceof CaptchaDeckContainer)
			CaptchaDeckHandler.useItem(player);
		else if(this.type == CAPTCHALOGUE && !player.getItemStackFromSlot(EquipmentSlotType.MAINHAND).isEmpty())
			CaptchaDeckHandler.captchalogueItem(player);
		else if(this.type == CAPTCHALOGUE_INV)
		{
			CaptchaDeckHandler.captchalogueInventoryItem(player, slotIndex);
		} else if(this.type == GET)
			CaptchaDeckHandler.getItem(player, itemIndex, asCard);
		else if(this.type == MODUS_PARAM && CaptchaDeckHandler.getModus(player) != null)
			CaptchaDeckHandler.getModus(player).setValue(player, valueType, value);
	}
}