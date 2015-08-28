package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;

import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.ContainerHandler;

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
			playerMP.openContainer = ContainerHandler.getPlayerStatsContainer(playerMP, i, ServerEditHandler.getData(playerMP.getCommandSenderName()) != null);
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
