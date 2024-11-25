package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.TitleSelectionHook;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

public final class TitleSelectPackets
{
	public record OpenScreen(Optional<Title> rejectedTitle) implements MSPacket.PlayToClient
	{
		
		public static final Type<OpenScreen> ID = new Type<>(Minestuck.id("title_select/open_screen"));
		public static final StreamCodec<FriendlyByteBuf, OpenScreen> STREAM_CODEC = ByteBufCodecs.optional(Title.STREAM_CODEC).map(OpenScreen::new, OpenScreen::rejectedTitle);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			MSScreenFactories.displayTitleSelectScreen(this.rejectedTitle.orElse(null));
		}
	}
	
	public record PickTitle(Optional<Title> selectedTitle) implements MSPacket.PlayToServer
	{
		
		public static final Type<PickTitle> ID = new Type<>(Minestuck.id("title_select/pick"));
		public static final StreamCodec<FriendlyByteBuf, PickTitle> STREAM_CODEC = ByteBufCodecs.optional(Title.STREAM_CODEC).map(PickTitle::new, PickTitle::selectedTitle);
		
		public static PickTitle random()
		{
			return new PickTitle(Optional.empty());
		}
		
		public static PickTitle pick(Title title)
		{
			return new PickTitle(Optional.of(title));
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			TitleSelectionHook.handleTitleSelection(player, this.selectedTitle.orElse(null));
		}
	}
}
