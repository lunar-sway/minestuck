package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.playerStats.GuiInventoryEditmode;
import com.mraof.minestuck.inventory.ContainerEditmode;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EditmodeInventoryPacket
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
	
	public void consume(Supplier<NetworkEvent.Context> ctx)
	{
		if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			ctx.get().enqueueWork(() -> this.execute(ctx.get().getSender()));
		else if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(this::execute);
		
		ctx.get().setPacketHandled(true);
	}
	
	public void execute()
	{
		if(Minecraft.getInstance().currentScreen instanceof GuiInventoryEditmode)
		{
			GuiInventoryEditmode gui = (GuiInventoryEditmode) Minecraft.getInstance().currentScreen;
			gui.less = b1;
			gui.more = b2;
			for(int i = 0; i < inventory.size(); i++)
			{
				gui.inventoryEditmode.inventoryItemStacks.set(i, inventory.get(i).isEmpty() ? ItemStack.EMPTY : inventory.get(i).copy());
				gui.inventoryEditmode.inventory.setInventorySlotContents(i, inventory.get(i));
			}
		}
	}
	
	public void execute(EntityPlayerMP player)
	{
		if(player.openContainer instanceof ContainerEditmode)
			((ContainerEditmode)player.openContainer).updateScroll(b1);
	}
}