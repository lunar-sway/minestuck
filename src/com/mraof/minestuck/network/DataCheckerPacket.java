package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumSet;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.playerStats.GuiDataChecker;
import com.mraof.minestuck.network.skaianet.SessionHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class DataCheckerPacket extends MinestuckPacket
{
	
	public static int index = 0;
	public static NBTTagCompound nbtData;
	
	/**
	 * Used to avoid confusion when the client sends several requests during a short period
	 */
	public int packetIndex;
	
	@Override
	public MinestuckPacket generatePacket(Object... dat)
	{
		if(dat.length == 0)	//Cient request to server
			data.writeByte(index = (index + 1) % 100);
		else
		{
			data.writeByte((Integer) dat[0]);
			
			try
			{
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				CompressedStreamTools.writeCompressed((NBTTagCompound)dat[1], bytes);
				this.data.writeBytes(bytes.toByteArray());
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return null;
			}
		}
		
		return this;
	}
	
	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		packetIndex = data.readByte();
		
		if(data.readableBytes() > 0)
		{
			byte[] bytes = new byte[data.readableBytes()];
			data.readBytes(bytes);
			try
			{
				this.nbtData = CompressedStreamTools.readCompressed(new ByteArrayInputStream(bytes));
			}
			catch(IOException e)
			{
				e.printStackTrace();
				return null;
			}
		}
		
		return this;
	}
	
	@Override
	public void execute(EntityPlayer player)
	{
		if(player.worldObj.isRemote)
		{
			if(packetIndex == index)
				GuiDataChecker.activeComponent = new GuiDataChecker.MainComponent(nbtData);
		} else if(player instanceof EntityPlayerMP && MinestuckConfig.getDataCheckerPermissionFor((EntityPlayerMP) player))
		{
			NBTTagCompound data = SessionHandler.createDataTag(player.getServer());
			MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(Type.DATA_CHECKER, packetIndex, data), player);
		}
	}
	
	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.allOf(Side.class);
	}
	
}