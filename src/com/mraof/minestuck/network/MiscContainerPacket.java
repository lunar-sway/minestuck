package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.ContainerHandler;
import com.mraof.minestuck.util.Debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.relauncher.Side;

public class MiscContainerPacket extends MinestuckPacket
{
	
	int i;
	int i1 = -1;
	
	public MiscContainerPacket()
	{
		super(Type.CONTAINER);
	}
	
	@Override
	public MinestuckPacket generatePacket(Object... data)
	{
		this.data.writeInt((Integer) data[0]);
		if(data.length > 1)
			this.data.writeInt((Integer) data[1]);
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		i = data.readInt();
		if(data.readableBytes() > 0)
			i1 = data.readInt();
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if(player.worldObj.isRemote)
		{
			if(i1 != -1)
				if(ClientEditHandler.isActive())
					GuiPlayerStats.editmodeTab = GuiPlayerStats.EditmodeGuiType.values()[i1];
				else GuiPlayerStats.normalTab = GuiPlayerStats.NormalGuiType.values()[i1];
			GuiScreen gui = ClientEditHandler.isActive()? GuiPlayerStats.editmodeTab.createGuiInstance():GuiPlayerStats.normalTab.createGuiInstance();
			if(gui != null)
				Minecraft.getMinecraft().displayGuiScreen(gui);
			if(gui instanceof GuiContainer)
				Minecraft.getMinecraft().thePlayer.openContainer.windowId = i;
			if(gui == null)
				player.closeScreen();
			Debug.print("PacketPost:"+Minecraft.getMinecraft().thePlayer.inventory.mainInventory[14]);
		}
		else if(player instanceof EntityPlayerMP)
		{
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			playerMP.getNextWindowId();
			int windowId = ((EntityPlayerMP) player).currentWindowId;
			MinestuckPacket packet = MinestuckPacket.makePacket(Type.CONTAINER, windowId);
			MinestuckChannelHandler.sendToPlayer(packet, playerMP);
			player.openContainer = ContainerHandler.getPlayerStatsContainer(playerMP, i, ServerEditHandler.getData(playerMP.getCommandSenderName()) != null);
			player.openContainer.windowId = windowId;
		}
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.allOf(Side.class);
	}

}
