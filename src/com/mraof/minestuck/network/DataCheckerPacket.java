package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import com.mraof.minestuck.client.gui.playerStats.GuiDataChecker;
import com.mraof.minestuck.network.skaianet.SessionHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public class DataCheckerPacket extends MinestuckPacket
{
	
	public static int index = 0;
	public static int[] sessionSize;
	
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
			
			for(int i = 1; i < dat.length; i++)
				data.writeByte((Integer) dat[i]);
			
			//TODO nbt is probably the best way of transferring the data
		}
		
		return this;
	}
	
	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		packetIndex = data.readByte();
		
		if(data.readableBytes() > 0)
		{
			sessionSize = new int[data.readableBytes()];
			for(int i = 0; i < sessionSize.length; i++)
				sessionSize[i] = data.readByte();
			
			//Starting with session size only while getting the gui properly working.
		}
		
		return this;
	}
	
	@Override
	public void execute(EntityPlayer player)
	{
		if(player.worldObj.isRemote)
		{
			if(packetIndex == index)
				GuiDataChecker.activeComponent = new GuiDataChecker.MainComponent(sessionSize);
		} else
		{
			Object[] data = SessionHandler.createDataObjects();
			data[0] = index;
			MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(Type.DATA_CHECKER, data), player);
		}
	}
	
	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.allOf(Side.class);
	}
	
}