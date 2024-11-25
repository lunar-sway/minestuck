package com.mraof.minestuck.network.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.playerStats.AtheneumScreen;
import com.mraof.minestuck.inventory.AtheneumMenu;
import com.mraof.minestuck.network.MSPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public final class AtheneumPackets
{
	
	public record Scroll(boolean scrollUp) implements MSPacket.PlayToServer
	{
		
		public static final Type<Scroll> ID = new Type<>(Minestuck.id("atheneum/scroll"));
		public static final StreamCodec<ByteBuf, Scroll> STREAM_CODEC = ByteBufCodecs.BOOL.map(Scroll::new, Scroll::scrollUp);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(player.containerMenu instanceof AtheneumMenu atheneumMenu)
				atheneumMenu.updateScroll(scrollUp);
		}
		
	}
	
	public record Update(boolean less, boolean more, List<ItemStack> inventory) implements MSPacket.PlayToClient
	{
		public static final Type<Update> ID = new Type<>(Minestuck.id("atheneum/update"));
		public static final StreamCodec<RegistryFriendlyByteBuf, Update> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.BOOL,
				Update::less,
				ByteBufCodecs.BOOL,
				Update::more,
				ItemStack.OPTIONAL_LIST_STREAM_CODEC,
				Update::inventory,
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
