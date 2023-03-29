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
	
	private final boolean scrollUp, scrollDown;
	private final List<ItemStack> inventory;
	
	private AtheneumPacket(boolean scrollUp, boolean scrollDown, List<ItemStack> inventory)
	{
		this.scrollUp = scrollUp;
		this.scrollDown = scrollDown;
		this.inventory = inventory;
	}
	
	public static AtheneumPacket update(List<ItemStack> inventory, boolean scrollUp, boolean scrollDown)
	{
		return new AtheneumPacket(scrollUp, scrollDown, inventory);
	}
	
	public static AtheneumPacket scroll(boolean isBottom)
	{
		return new AtheneumPacket(isBottom, false, null);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBoolean(scrollUp);
		if(inventory != null)
		{
			buffer.writeBoolean(scrollDown);
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
			gui.less = scrollUp;
			gui.more = scrollDown;
			gui.getMenu().receiveUpdatePacket(this);
		}
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.containerMenu instanceof AtheneumMenu atheneumMenu)
			atheneumMenu.updateScroll(scrollUp);
	}
	
	public List<ItemStack> getInventory()
	{
		return inventory;
	}
}
