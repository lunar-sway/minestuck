package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.TitleSelectionHook;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public final class TitleSelectPackets
{
	public record OpenScreen(Optional<Title> rejectedTitle) implements MSPacket.PlayToClient
	{
		public static final ResourceLocation ID = Minestuck.id("title_select/open_screen");
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeOptional(this.rejectedTitle, (buffer1, title) -> title.write(buffer));
		}
		
		public static OpenScreen read(FriendlyByteBuf buffer)
		{
			Optional<Title> rejectedTitle = buffer.readOptional(Title::read);
			return new OpenScreen(rejectedTitle);
		}
		
		@Override
		public void execute()
		{
			MSScreenFactories.displayTitleSelectScreen(this.rejectedTitle.orElse(null));
		}
	}
	
	public record PickTitle(Optional<Title> selectedTitle) implements MSPacket.PlayToServer
	{
		public static final ResourceLocation ID = Minestuck.id("title_select/pick");
		
		public static PickTitle random()
		{
			return new PickTitle(Optional.empty());
		}
		
		public static PickTitle pick(Title title)
		{
			return new PickTitle(Optional.of(title));
		}
		
		@Override
		public ResourceLocation id()
		{
			return ID;
		}
		
		@Override
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeOptional(this.selectedTitle, (buffer1, title) -> title.write(buffer));
		}
		
		public static PickTitle read(FriendlyByteBuf buffer)
		{
			Optional<Title> selectedTitle = buffer.readOptional(Title::read);
			return new PickTitle(selectedTitle);
		}
		
		@Override
		public void execute(ServerPlayer player)
		{
			TitleSelectionHook.handleTitleSelection(player, this.selectedTitle.orElse(null));
		}
	}
}
