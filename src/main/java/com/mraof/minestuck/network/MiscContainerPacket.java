package com.mraof.minestuck.network;

import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.inventory.EditmodeContainer;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckContainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MiscContainerPacket implements PlayToServerPacket
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
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(index);
		buffer.writeBoolean(editmode);
	}
	
	public static MiscContainerPacket decode(PacketBuffer buffer)
	{
		int index = buffer.readInt();
		boolean editmode = buffer.readBoolean();
		
		return new MiscContainerPacket(index, editmode);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		boolean isInEditmode = ServerEditHandler.getData(player) != null;
		
		if(editmode != isInEditmode)
		{
			if(isInEditmode)
				LOGGER.error("Sanity check failed: {} tried to open a minestuck gui while in editmode", player.getName().getFormattedText());
			else LOGGER.error("Sanity check failed: {} tried to open an editmode gui while outside editmode", player.getName().getFormattedText());
			
			ServerEditHandler.resendEditmodeStatus(player);
		} else
		{
			if(!isInEditmode)
			{
				player.openContainer = new CaptchaDeckContainer(PlayerStatsScreen.WINDOW_ID_START + index, player.inventory);//ContainerHandler.windowIdStart + i;
			} else
			{
				player.openContainer = new EditmodeContainer(PlayerStatsScreen.WINDOW_ID_START + index, player.inventory);
			}
			
			player.addSelfToInternalCraftingInventory();
		}
	}
}