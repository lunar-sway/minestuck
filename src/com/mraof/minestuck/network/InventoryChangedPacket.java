package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.EnumSet;

import com.mraof.minestuck.client.gui.playerStats.GuiInventoryEditmode;
import com.mraof.minestuck.inventory.ContainerEditmode;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

public class InventoryChangedPacket extends MinestuckPacket
{
	
	public int type;
	public boolean b1, b2;
	public ArrayList<ItemStack> inventory;
	
	public InventoryChangedPacket()
	{
		super(Type.INVENTORY);
	}
	
	@Override
	public MinestuckPacket generatePacket(Object... data)
	{
		this.data.writeByte((Integer) data[0]);
		if(data[1] instanceof ArrayList)
		{
			this.data.writeBoolean((Boolean) data[2]);
			this.data.writeBoolean((Boolean) data[3]);
			ArrayList<ItemStack> list = (ArrayList<ItemStack>) data[1];
			for(ItemStack stack : list)
				ByteBufUtils.writeItemStack(this.data, stack);
		} else this.data.writeBoolean((Boolean) data[1]);
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		this.type = data.readByte();
		
		if(data.readableBytes() == 1)
			b1 = data.readBoolean();
		else
		{
			b1 = data.readBoolean();
			b2 = data.readBoolean();
			inventory = new ArrayList<ItemStack>();
			while(data.readableBytes() > 0)
			{
				inventory.add(ByteBufUtils.readItemStack(data));
			}
		}
		return this;
	}

	@Override
	public void execute(EntityPlayer player)
	{
		switch(type)
		{
		case 0:
			if(player.worldObj.isRemote && this.inventory != null && player.openContainer instanceof ContainerEditmode)
			{
				for(int i = 0; i < inventory.size(); i++)
				{
					((ContainerEditmode)player.openContainer).inventoryItemStacks.set(i, inventory.get(i) == null? null:inventory.get(i).copy());
					((ContainerEditmode)player.openContainer).inventory.setInventorySlotContents(i, inventory.get(i));
				}
				if(FMLClientHandler.instance().getClient().currentScreen instanceof GuiInventoryEditmode)
				{
					((GuiInventoryEditmode)FMLClientHandler.instance().getClient().currentScreen).less = b1;
					((GuiInventoryEditmode)FMLClientHandler.instance().getClient().currentScreen).more = b2;
				}
			}
			else if(!player.worldObj.isRemote && player.openContainer instanceof ContainerEditmode)
				((ContainerEditmode)player.openContainer).scroll += b1 ? 1 : -1;
			break;
		}
		
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.allOf(Side.class);
	}

}
