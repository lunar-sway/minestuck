package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.playerStats.AtheneumScreen;
import com.mraof.minestuck.inventory.AtheneumMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AtheneumPacket implements PlayToBothPacket
{
	
	private final boolean b1, b2;
	private final List<ItemStack> inventory;
	
	private AtheneumPacket(boolean b1, boolean b2, List<ItemStack> inventory)
	{
		this.b1 = b1;
		this.b2 = b2;
		this.inventory = inventory;
	}
	
	public static AtheneumPacket update(List<ItemStack> inventory, boolean scrollLeft, boolean scrollRight)
	{
		return new AtheneumPacket(scrollLeft, scrollRight, inventory);
	}
	
	public static AtheneumPacket scroll(boolean isRight)
	{
		return new AtheneumPacket(isRight, false, null);
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
	
	public static AtheneumPacket decode(FriendlyByteBuf buffer)
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
		if(Minecraft.getInstance().screen instanceof AtheneumScreen)
		{
			AtheneumScreen gui = (AtheneumScreen) Minecraft.getInstance().screen;
			gui.less = b1;
			gui.more = b2;
			gui.getMenu().receiveUpdatePacket(this);
		}
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.containerMenu instanceof AtheneumMenu)
			((AtheneumMenu)player.containerMenu).updateScroll(b1);
	}
	
	public List<ItemStack> getInventory()
	{
		return inventory;
	}
}
