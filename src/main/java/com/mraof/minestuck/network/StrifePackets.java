package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.item.StrifeCardItem;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.StrifePortfolioData;
import com.mraof.minestuck.player.StrifeSpecibus;
import com.mraof.minestuck.strife.StrifePortfolioHandler;
import com.mraof.minestuck.util.MSAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class StrifePackets
{
	public record AssignStrifePacket(InteractionHand hand) implements MSPacket.PlayToServer
	{
		public static final Type<AssignStrifePacket> ID = new Type<>(Minestuck.id("assign_strife"));
		
		public static final StreamCodec<FriendlyByteBuf, AssignStrifePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL.map(b -> b ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, h -> h == InteractionHand.MAIN_HAND), AssignStrifePacket::hand, AssignStrifePacket::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			StrifePortfolioHandler.assignStrife(player, hand());
		}
	}
	
	public record OpenStrifeCardGuiPacket(InteractionHand hand) implements MSPacket.PlayToClient
	{
		public static final Type<OpenStrifeCardGuiPacket> ID = new Type<>(Minestuck.id("open_strife_card_gui"));
		
		public static final StreamCodec<FriendlyByteBuf, OpenStrifeCardGuiPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL.map(b -> b ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, h -> h == InteractionHand.MAIN_HAND), OpenStrifeCardGuiPacket::hand, OpenStrifeCardGuiPacket::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			MSScreenFactories.displayStrifeCardScreen(hand());
		}
	}
	
	public record RetrieveStrifeCardPacket(int index) implements MSPacket.PlayToServer
	{
		public static final Type<RetrieveStrifeCardPacket> ID = new Type<>(Minestuck.id("retrieve_strife_card"));
		
		public static final StreamCodec<ByteBuf, RetrieveStrifeCardPacket> STREAM_CODEC = ByteBufCodecs.INT.map(RetrieveStrifeCardPacket::new, RetrieveStrifeCardPacket::index);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			StrifePortfolioHandler.retrieveCard(player, index());
		}
	}
	
	public record RetrieveWeaponPacket(int weaponIndex, InteractionHand hand) implements MSPacket.PlayToServer
	{
		public static final Type<RetrieveWeaponPacket> ID = new Type<>(Minestuck.id("retrieve_weapon"));
		
		public static final StreamCodec<FriendlyByteBuf, RetrieveWeaponPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, RetrieveWeaponPacket::weaponIndex, ByteBufCodecs.BOOL.map(b -> b ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, h -> h == InteractionHand.MAIN_HAND), RetrieveWeaponPacket::hand, RetrieveWeaponPacket::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			StrifePortfolioHandler.retrieveWeapon(player, weaponIndex(), hand());
		}
	}
	
	/**
	 * Sent by the client when the player selects an abstrata type in {@link com.mraof.minestuck.client.gui.StrifeCardScreen}.
	 * The server validates the selection, consumes the blank card, and adds the new specibus to the portfolio.
	 */
	public record SelectAbstrataForCardPacket(InteractionHand hand,
	                                          String abstratusName) implements MSPacket.PlayToServer
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
	
	public record SetActiveStrifePacket(int specibusIndex) implements MSPacket.PlayToServer
	{
		public static final Type<SetActiveStrifePacket> ID = new Type<>(Minestuck.id("set_active_strife"));
		
		public static final StreamCodec<ByteBuf, SetActiveStrifePacket> STREAM_CODEC = ByteBufCodecs.INT.map(SetActiveStrifePacket::new, SetActiveStrifePacket::specibusIndex);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			if(specibusIndex() < 0 || specibusIndex() >= StrifePortfolioHandler.getData(player).getPortfolio().length)
				return;
			StrifePortfolioHandler.setSelectedSpecibus(player, specibusIndex());
		}
	}
	
	public record SwapOffhandStrifePacket(int specibusIndex, int weaponIndex) implements MSPacket.PlayToServer
	{
		public static final Type<SwapOffhandStrifePacket> ID = new Type<>(Minestuck.id("swap_offhand_strife"));
		
		public static final StreamCodec<FriendlyByteBuf, SwapOffhandStrifePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, SwapOffhandStrifePacket::specibusIndex, ByteBufCodecs.INT, SwapOffhandStrifePacket::weaponIndex, SwapOffhandStrifePacket::new);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context, ServerPlayer player)
		{
			StrifePortfolioHandler.swapOffhandWeapon(player, specibusIndex(), weaponIndex());
		}
	}
	
	public record SyncPortfolioPacket(StrifePortfolioData portfolioData) implements MSPacket.PlayToClient
	{
		public static final Type<SyncPortfolioPacket> ID = new Type<>(Minestuck.id("sync_portfolio"));
		
		public static final StreamCodec<RegistryFriendlyByteBuf, SyncPortfolioPacket> STREAM_CODEC = StrifePortfolioData.STREAM_CODEC.map(SyncPortfolioPacket::new, SyncPortfolioPacket::portfolioData);
		
		@Override
		public Type<? extends CustomPacketPayload> type()
		{
			return ID;
		}
		
		@Override
		public void execute(IPayloadContext context)
		{
			Minecraft mc = Minecraft.getInstance();
			if(mc.player != null) mc.player.setData(MSAttachments.STRIFE_PORTFOLIO.get(), portfolioData());
		}
	}
	
}
