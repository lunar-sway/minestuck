package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.playerStats.InventoryEditmodeScreen;
import com.mraof.minestuck.inventory.EditmodeMenu;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class EditmodeInventoryPackets
{
	public record Update(List<ItemStack> inventory, boolean canScrollLeft, boolean canScrollRight) implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("editmode_inventory/update");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeCollection(this.inventory, FriendlyByteBuf::writeItem);
			buffer.writeBoolean(this.canScrollLeft);
			buffer.writeBoolean(this.canScrollRight);
		}
		
		public static Update read(FriendlyByteBuf buffer)
		{
			List<ItemStack> inventory = buffer.readList(FriendlyByteBuf::readItem);
			boolean canScrollLeft = buffer.readBoolean();
			boolean canScrollRight = buffer.readBoolean();
			return new Update(inventory, canScrollLeft, canScrollRight);
		}
		
		@Override
		public void execute()
		{
			if(Minecraft.getInstance().screen instanceof InventoryEditmodeScreen gui)
			{
				gui.less = this.canScrollLeft;
				gui.more = this.canScrollRight;
				gui.getMenu().receiveUpdatePacket(this);
			}
		}
	}
	
	public record Scroll(boolean isRight) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("editmode_inventory/scroll");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeBoolean(this.isRight);
		}
		
		public static Scroll read(FriendlyByteBuf buffer)
		{
			boolean isRight = buffer.readBoolean();
			return new Scroll(isRight);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			if(player.containerMenu instanceof EditmodeMenu menu)
				menu.updateScroll(this.isRight);
		}
	}
}
