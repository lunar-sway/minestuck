package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.playerStats.AtheneumScreen;
import com.mraof.minestuck.inventory.AtheneumMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class AtheneumPacket
{
	
	public record Scroll(boolean scrollUp) implements PlayToServerPacket
	{
		
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(scrollUp);
		}
		
		public static Scroll decode(FriendlyByteBuf buffer)
		{
			boolean scrollUp = buffer.readBoolean();
			return new Scroll(scrollUp);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(player.containerMenu instanceof AtheneumMenu atheneumMenu)
				atheneumMenu.updateScroll(scrollUp);
		}
		
	}
	
	public record Update(boolean less, boolean more, List<ItemStack> inventory) implements PlayToClientPacket
	{
		@Override
		public void encode(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(less);
			buffer.writeBoolean(more);
			for(ItemStack stack : inventory)
				buffer.writeItem(stack);
		}
		
		public static Update decode(FriendlyByteBuf buffer)
		{
			boolean less = buffer.readBoolean();
			boolean more = buffer.readBoolean();
			
			List<ItemStack> inventory = new ArrayList<>();
			while(buffer.readableBytes() > 0)
			{
				inventory.add(buffer.readItem());
			}
			
			return new Update(less, more, inventory);
		}
		
		@Override
		public void execute()
		{
			if(Minecraft.getInstance().screen instanceof AtheneumScreen gui)
			{
				gui.less = less;
				gui.more = more;
				gui.getMenu().receiveUpdatePacket(this);
			}
		}
		
		public List<ItemStack> getInventory()
		{
			return inventory;
		}
	}
}
