package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.AtheneumMenu;
import com.mraof.minestuck.inventory.EditmodeMenu;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MiscContainerPacket implements MSPacket.PlayToServer
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final int index;
	private final boolean editmode;
	
	public MiscContainerPacket(int index, boolean editmode)
	{
		this.index = index;
		this.editmode = editmode;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(index);
		buffer.writeBoolean(editmode);
	}
	
	public static MiscContainerPacket decode(FriendlyByteBuf buffer)
	{
		int index = buffer.readInt();
		boolean editmode = buffer.readBoolean();
		
		return new MiscContainerPacket(index, editmode);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		boolean isInEditmode = ServerEditHandler.getData(player) != null;
		
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
			MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, menu));
		}
	}
}