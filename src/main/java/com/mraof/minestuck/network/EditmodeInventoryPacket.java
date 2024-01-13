package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.playerStats.InventoryEditmodeScreen;
import com.mraof.minestuck.inventory.EditmodeMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EditmodeInventoryPacket implements MSPacket.PlayToBoth
{
	
	private final boolean b1, b2;
	private final List<ItemStack> inventory;
	
	private EditmodeInventoryPacket(boolean b1, boolean b2, List<ItemStack> inventory)
	{
		this.b1 = b1;
		this.b2 = b2;
		this.inventory = inventory;
	}
	
	public static EditmodeInventoryPacket update(List<ItemStack> inventory, boolean scrollLeft, boolean scrollRight)
	{
		return new EditmodeInventoryPacket(scrollLeft, scrollRight, inventory);
	}
	
	public static EditmodeInventoryPacket scroll(boolean isRight)
	{
		return new EditmodeInventoryPacket(isRight, false, null);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(b1);
		if(inventory != null)
		{
			buffer.writeBoolean(b2);
			for(ItemStack stack : inventory)
				buffer.writeItem(stack);
		}
	}
	
	public static EditmodeInventoryPacket decode(FriendlyByteBuf buffer)
	{
		boolean b1 = buffer.readBoolean();
		if(buffer.readableBytes() > 0)
		{
			boolean b2 = buffer.readBoolean();
			List<ItemStack> inventory = new ArrayList<>();
			while(buffer.readableBytes() > 0)
			{
				inventory.add(buffer.readItem());
			}
			return update(inventory, b1, b2);
		}
		
		return scroll(b1);
	}
	
	@Override
	public void execute()
	{
		if(Minecraft.getInstance().screen instanceof InventoryEditmodeScreen)
		{
			InventoryEditmodeScreen gui = (InventoryEditmodeScreen) Minecraft.getInstance().screen;
			gui.less = b1;
			gui.more = b2;
			gui.getMenu().receiveUpdatePacket(this);
		}
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.containerMenu instanceof EditmodeMenu)
			((EditmodeMenu)player.containerMenu).updateScroll(b1);
	}
	
	public List<ItemStack> getInventory()
	{
		return inventory;
	}
}