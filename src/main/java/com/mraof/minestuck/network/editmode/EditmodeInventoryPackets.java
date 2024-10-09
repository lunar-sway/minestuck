package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.playerStats.InventoryEditmodeScreen;
import com.mraof.minestuck.inventory.EditmodeMenu;
import com.mraof.minestuck.network.MSPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public final class EditmodeInventoryPackets
{
	public record Update(List<ItemStack> inventory, boolean canScrollLeft, boolean canScrollRight) implements MSPacket.PlayToClient
	{
		
		public static final Type<Update> ID = new Type<>(Minestuck.id("editmode_inventory/update"));
		public static final StreamCodec<? super RegistryFriendlyByteBuf, Update> STREAM_CODEC = StreamCodec.composite(
				ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
				Update::inventory,
				ByteBufCodecs.BOOL,
				Update::canScrollLeft,
				ByteBufCodecs.BOOL,
				Update::canScrollRight,
				Update::new
		);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
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
		public static final Type<Scroll> ID = new Type<>(Minestuck.id("editmode_inventory/scroll"));
		public static final StreamCodec<ByteBuf, Scroll> STREAM_CODEC = ByteBufCodecs.BOOL.map(Scroll::new, Scroll::isRight);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(player.containerMenu instanceof EditmodeMenu menu)
				menu.updateScroll(this.isRight);
		}
	}
}
