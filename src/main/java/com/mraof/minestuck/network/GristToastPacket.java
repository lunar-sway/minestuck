package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.ImmutableGristSet;
import com.mraof.minestuck.client.gui.toasts.GristToast;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.network.PacketDistributor;

public record GristToastPacket(GristSet gristValue, GristHelper.EnumSource source,
							   boolean isCacheOwner) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("grist_toast");
	
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
				PacketDistributor.PLAYER.with(player.getPlayer(server)).send(new GristToastPacket(set, source, true));
			
			if(source == GristHelper.EnumSource.SERVER)
			{
				EditData ed = ServerEditHandler.getData(server, player);
				if(ed == null)
					return;
				
				if(!player.appliesTo(ed.getEditor()))
					PacketDistributor.PLAYER.with(ed.getEditor()).send(new GristToastPacket(set, source, false));
				
			}
		}
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		GristSet.write(gristValue, buffer);
		buffer.writeEnum(source);
		buffer.writeBoolean(isCacheOwner);
	}
	
	public static GristToastPacket read(FriendlyByteBuf buffer)
	{
		ImmutableGristSet gristValue = GristSet.read(buffer);
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
