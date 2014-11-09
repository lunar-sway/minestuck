package com.mraof.minestuck.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.EnumSet;

import com.mraof.minestuck.inventory.ContainerEditmode;
import com.mraof.minestuck.util.Debug;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;

public class InventoryChangedPacket extends MinestuckPacket
{
	
	public int type;
	public int i;
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
			ArrayList<ItemStack> list = (ArrayList<ItemStack>) data[1];
			for(ItemStack stack : list)
				ByteBufUtils.writeItemStack(this.data, stack);
		} else this.data.writeInt((Integer) data[1]);
		
		return this;
	}

	@Override
	public MinestuckPacket consumePacket(ByteBuf data)
	{
		this.type = data.readByte();
		
		if(data.readableBytes() == 4)
			i = data.readInt();
		else
		{
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
			}
			else if(!player.worldObj.isRemote && player.openContainer instanceof ContainerEditmode)
				((ContainerEditmode)player.openContainer).scroll = this.i;
			break;
		}
		
	}

	@Override
	public EnumSet<Side> getSenderSide()
	{
		return EnumSet.allOf(Side.class);
	}

}
