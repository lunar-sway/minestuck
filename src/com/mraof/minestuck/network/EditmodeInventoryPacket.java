package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.playerStats.InventoryEditmodeScreen;
import com.mraof.minestuck.inventory.EditmodeContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.List;

public class EditmodeInventoryPacket implements PlayToBothPacket
{
	
	public boolean b1, b2;
	public List<ItemStack> inventory;
	
	public static EditmodeInventoryPacket update(List<ItemStack> inventory, boolean scrollLeft, boolean scrollRight)
	{
		EditmodeInventoryPacket packet = new EditmodeInventoryPacket();
		packet.b1 = scrollLeft;
		packet.b2 = scrollRight;
		packet.inventory = inventory;
		
		return packet;
	}
	
	public static EditmodeInventoryPacket scroll(boolean isRight)
	{
		EditmodeInventoryPacket packet = new EditmodeInventoryPacket();
		packet.b1 = isRight;
		
		return packet;
	}
	
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBoolean(b1);
		if(inventory != null)
		{
			buffer.writeBoolean(b2);
			for(ItemStack stack : inventory)
				buffer.writeItemStack(stack);
		}
	}
	
	public static EditmodeInventoryPacket decode(PacketBuffer buffer)
	{
		EditmodeInventoryPacket packet = new EditmodeInventoryPacket();
		packet.b1 = buffer.readBoolean();
		if(buffer.readableBytes() > 0)
		{
			packet.b2 = buffer.readBoolean();
			packet.inventory = new ArrayList<>();
			while(buffer.readableBytes() > 0)
			{
				packet.inventory.add(buffer.readItemStack());
			}
		}
		
		return packet;
	}
	
	@Override
	public void execute()
	{
		if(Minecraft.getInstance().currentScreen instanceof InventoryEditmodeScreen)
		{
			InventoryEditmodeScreen gui = (InventoryEditmodeScreen) Minecraft.getInstance().currentScreen;
			gui.less = b1;
			gui.more = b2;
			for(int i = 0; i < inventory.size(); i++)
			{
				((EditmodeContainer) gui.getContainer()).inventory.setInventorySlotContents(i, inventory.get(i));
			}
		}
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(player.openContainer instanceof EditmodeContainer)
			((EditmodeContainer)player.openContainer).updateScroll(b1);
	}
}