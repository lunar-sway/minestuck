package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumSet;

import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalouge.ContainerCaptchaDeck;
import com.mraof.minestuck.util.Debug;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class CaptchaDeckPacket extends MinestuckPacket
{
	
	public static final byte DATA = 0;
	public static final byte MODUS = 1;
	public static final byte CAPTCHALOUGE = 2;
	public static final byte GET = 3;
	public static final byte VALUE = 4;
	
	public byte type;
	
	public NBTTagCompound nbt;
	
	public int itemIndex;
	public boolean getCard;
	
	public byte valueType;
	public int value;
	
	@Override
	public MinestuckPacket generatePacket(Object... data)
	{
		byte type = (Byte) data[0];
		this.data.writeByte(type);	//Packet type
		if(data.length > 1)
		{
			if(type == DATA && data[1] != null)	//Server side data
			{
				try
				{
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					CompressedStreamTools.writeCompressed((NBTTagCompound)data[1], bytes);
					this.data.writeBytes(bytes.toByteArray());
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return null;
				}
			}
			else if(type == GET)
			{
				this.data.writeInt((Integer)data[1]);	//Client side index
				this.data.writeBoolean((Boolean)data[2]);	//Retrive card
			}
			else if(type == VALUE)
			{
				this.data.writeByte((Byte)data[1]);
				this.data.writeInt((Integer)data[2]);
			}
		}
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		this.type = data.readByte();
		
		if(data.readableBytes() > 0)
		{
			if(this.type == DATA)
			{
				byte[] bytes = new byte[data.readableBytes()];
				data.readBytes(bytes);
				try
				{
					this.nbt = CompressedStreamTools.readCompressed(new ByteArrayInputStream(bytes));
				}
				catch(IOException e)
				{
					e.printStackTrace();
					return null;
				}
			}
			else if(this.type == GET)
			{
				this.itemIndex = data.readInt();
				this.getCard = data.readBoolean();
			}
			else if(this.type == VALUE)
			{
				this.valueType = data.readByte();
				this.value = data.readInt();
			}
		}
		
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if(player != null && player.worldObj != null && !player.worldObj.isRemote)
		{
			if(ServerEditHandler.getData(player.getCommandSenderName()) != null)
				return;
			
			if(this.type == MODUS && player.openContainer instanceof ContainerCaptchaDeck)
				CaptchaDeckHandler.useItem((EntityPlayerMP) player);
			else if(this.type == CAPTCHALOUGE && player.getCurrentEquippedItem() != null)
				CaptchaDeckHandler.captchalougeItem((EntityPlayerMP) player);
			else if(this.type == GET)
				CaptchaDeckHandler.getItem((EntityPlayerMP) player, itemIndex, getCard);
//			else if(this.type == DATA)
//				MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(Type.CAPTCHA, DATA, CaptchaDeckHandler.writeToNBT(CaptchaDeckHandler.getModus(player))), player);
			else if(this.type == VALUE && CaptchaDeckHandler.getModus(player) != null)
				CaptchaDeckHandler.getModus(player).setValue(valueType, value);
		}
		else
		{
			if(this.type == DATA)
			{
//				if(player == null) Should not be needed anymore
//					MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(Type.CAPTCHA, DATA));
//				else
//				{
				CaptchaDeckHandler.clientSideModus = CaptchaDeckHandler.readFromNBT(nbt, true);
				if(CaptchaDeckHandler.clientSideModus != null)
					CaptchaDeckHandler.clientSideModus.getGuiHandler().updateContent();
				else Debug.print("Lost modus");
//				}
			}
		}
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.allOf(Side.class);
	}
	
}
