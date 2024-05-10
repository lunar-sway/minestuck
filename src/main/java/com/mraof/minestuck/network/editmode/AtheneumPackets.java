package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.playerStats.AtheneumScreen;
import com.mraof.minestuck.inventory.AtheneumMenu;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class AtheneumPackets
{
	
	public record Scroll(boolean scrollUp) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("atheneum/scroll");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(scrollUp);
		}
		
		public static Scroll read(FriendlyByteBuf buffer)
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
	
	public record Update(boolean less, boolean more, List<ItemStack> inventory) implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("atheneum/update");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(less);
			buffer.writeBoolean(more);
			for(ItemStack stack : inventory)
				buffer.writeItem(stack);
		}
		
		public static Update read(FriendlyByteBuf buffer)
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
