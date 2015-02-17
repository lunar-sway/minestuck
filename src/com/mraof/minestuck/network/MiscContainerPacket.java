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
import net.minecraftforge.fml.relauncher.Side;

public class MiscContainerPacket extends MinestuckPacket
{
	
	int i;
	
	public MiscContainerPacket()
	{
		super(Type.CONTAINER);
	}
	
	@Override
	public MinestuckPacket generatePacket(Object... data)
	{
		this.data.writeInt((Integer) data[0]);
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		i = data.readInt();
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		if(player instanceof EntityPlayerMP)
		{
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			playerMP.openContainer = ContainerHandler.getPlayerStatsContainer(playerMP, i, ServerEditHandler.getData(playerMP.getName()) != null);
			playerMP.openContainer.windowId = ContainerHandler.windowIdStart + i;
			playerMP.addSelfToInternalCraftingInventory();	//Must be placed after setting the window id!!
		}
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.of(Side.CLIENT);
	}

}
