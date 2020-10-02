package com.mraof.minestuck.network;

import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckContainer;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class CaptchaDeckPacket implements PlayToServerPacket
{
	
	private static final byte MODUS = 0;
	private static final byte CAPTCHALOGUE = 1;
	private static final byte GET = 2;
	private static final byte MODUS_PARAM = 3;
	private static final byte CAPTCHALOGUE_INV = 4;
	
	public byte type;
	
	public CompoundNBT nbt;
	
	public int itemIndex;
	public boolean asCard;
	
	public byte valueType;
	public int value;
	
	public int slotIndex;
	
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
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeByte(type);	//Packet type
		if(type == GET)	//Take item from modus
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
			if(packet.type == GET)
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
	
	@Override
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
		{
			CaptchaDeckHandler.getModus(player).setValue(player, valueType, value);
			CaptchaDeckHandler.getModus(player).checkAndResend(player);
		}
	}
}