package com.mraof.minestuck.network;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.MutableGristSet;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.client.gui.toasts.GristToast;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;

public record GristToastPacket(GristSet gristValue, GristHelper.EnumSource source,
							   boolean isCacheOwner) implements PlayToClientPacket
{
	
	/**
	 * Sends a request to make a client-side Toast Notification for incoming/outgoing grist, if enabled in the config.
	 *
	 * @param server Used for getting the ServerPlayer from their PlayerIdentifier
	 * @param player The Player that the notification should appear for.
	 * @param set    The grist type and value pairs associated with the notifications. There can be multiple pairs in the set, but usually only one.
	 * @param source Indicates where the notification is coming from. See EnumSource.
	 */
	public static void notify(MinecraftServer server, PlayerIdentifier player, GristSet set, GristHelper.EnumSource source)
	{
		if(MinestuckConfig.SERVER.showGristChanges.get())
		{
			if(player.getPlayer(server) != null)
				MSPacketHandler.sendToPlayer(new GristToastPacket(set, source, true), player.getPlayer(server));
			
			if(source == GristHelper.EnumSource.SERVER)
			{
				SburbConnection sc = SkaianetHandler.get(server).getActiveConnection(player);
				if(sc == null)
					return;
				
				EditData ed = ServerEditHandler.getData(server, sc);
				if(ed == null)
					return;
				
				if(!player.appliesTo(ed.getEditor()))
					MSPacketHandler.sendToPlayer(new GristToastPacket(set, source, false), ed.getEditor());
				
			}
		}
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		MutableGristSet.write(gristValue, buffer);
		buffer.writeEnum(source);
		buffer.writeBoolean(isCacheOwner);
	}
	
	public static GristToastPacket decode(FriendlyByteBuf buffer)
	{
		GristSet gristValue = MutableGristSet.read(buffer);
		GristHelper.EnumSource source = buffer.readEnum(GristHelper.EnumSource.class);
		boolean isCacheOwner = buffer.readBoolean();
		return new GristToastPacket(gristValue, source, isCacheOwner);
	}
	
	@Override
	public void execute()
	{
		GristToast.handlePacket(this);
	}
}
