package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.StrifeCardItem;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.StrifeSpecibus;
import com.mraof.minestuck.strife.StrifePortfolioHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Sent by the client when the player selects an abstrata type in {@link com.mraof.minestuck.client.gui.StrifeCardScreen}.
 * The server validates the selection, consumes the blank card, and adds the new specibus to the portfolio.
 */
public record SelectAbstrataForCardPacket(InteractionHand hand, String abstratusName) implements MSPacket.PlayToServer
{
	public static final Type<SelectAbstrataForCardPacket> ID = new Type<>(Minestuck.id("select_abstrata_for_card"));
	
	public static final StreamCodec<FriendlyByteBuf, SelectAbstrataForCardPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL.map(b -> b ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, h -> h == InteractionHand.MAIN_HAND), SelectAbstrataForCardPacket::hand, ByteBufCodecs.STRING_UTF8, SelectAbstrataForCardPacket::abstratusName, SelectAbstrataForCardPacket::new);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		if(KindAbstratusList.getTypeFromName(abstratusName()) == null) return;
		
		ItemStack card = player.getItemInHand(hand());
		if(!(card.getItem() instanceof StrifeCardItem)) return;
		if(StrifeCardItem.hasSpecibus(card)) return;
		
		StrifeSpecibus newSpecibus = new StrifeSpecibus(abstratusName());
		if(StrifePortfolioHandler.addSpecibus(player, newSpecibus)) card.shrink(1);
	}
}