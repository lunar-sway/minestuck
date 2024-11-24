package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.client.gui.toasts.GristToast;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GristToastPacket(GristSet.Immutable gristValue, GristHelper.EnumSource source,
							   boolean isCacheOwner) implements MSPacket.PlayToClient
{
	public static final Type<GristToastPacket> ID = new Type<>(Minestuck.id("grist_toast"));
	public static final StreamCodec<RegistryFriendlyByteBuf, GristToastPacket> STREAM_CODEC = StreamCodec.composite(
			GristSet.Codecs.STREAM_CODEC,
			GristToastPacket::gristValue,
			NeoForgeStreamCodecs.enumCodec(GristHelper.EnumSource.class),
			GristToastPacket::source,
			ByteBufCodecs.BOOL,
			GristToastPacket::isCacheOwner,
			GristToastPacket::new
	);
	
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
				PacketDistributor.sendToPlayer(player.getPlayer(server), new GristToastPacket(set.asImmutable(), source, true));
			
			if(source == GristHelper.EnumSource.SERVER)
			{
				EditData ed = ServerEditHandler.getData(server, player);
				if(ed == null)
					return;
				
				if(!player.appliesTo(ed.getEditor()))
					PacketDistributor.sendToPlayer(ed.getEditor(), new GristToastPacket(set.asImmutable(), source, false));
				
			}
		}
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		GristToast.handlePacket(this);
	}
}
