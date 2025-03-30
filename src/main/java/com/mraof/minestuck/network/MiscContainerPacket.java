package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.AtheneumMenu;
import com.mraof.minestuck.inventory.EditmodeMenu;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public record MiscContainerPacket(int index, boolean editmode) implements MSPacket.PlayToServer
{
	
		public static final Type<MiscContainerPacket> ID = new Type<>(Minestuck.id("misc_container"));
	public static final StreamCodec<FriendlyByteBuf, MiscContainerPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			MiscContainerPacket::index,
			ByteBufCodecs.BOOL,
			MiscContainerPacket::editmode,
			MiscContainerPacket::new
	);
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		boolean isInEditmode = ServerEditHandler.isInEditmode(player);
		
		if(editmode != isInEditmode)
		{
			if(isInEditmode)
				LOGGER.error("Sanity check failed: {} tried to open a minestuck gui while in editmode", player.getName().getString());
			else LOGGER.error("Sanity check failed: {} tried to open an editmode gui while outside editmode", player.getName().getString());
			
			ServerEditHandler.resendEditmodeStatus(player);
		} else
		{
			int id = PlayerStatsScreen.WINDOW_ID_START + index;
			AbstractContainerMenu menu;
			if(!isInEditmode)
				menu = new CaptchaDeckMenu(id, player.getInventory());
			else
				if(index == 0)
					menu = new EditmodeMenu(id, player.getInventory());
				else
					menu = new AtheneumMenu(id, player.getInventory());
				
			player.containerMenu = menu;
			player.initMenu(menu);
			NeoForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, menu));
		}
	}
}
