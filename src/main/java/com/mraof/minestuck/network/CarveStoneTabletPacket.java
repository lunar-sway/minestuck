package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.item.components.StoneTabletTextComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CarveStoneTabletPacket(String text, InteractionHand hand) implements MSPacket.PlayToServer
{
	
	public static final Type<CarveStoneTabletPacket> ID = new Type<>(Minestuck.id("carve_stone_tablet"));
	public static final StreamCodec<FriendlyByteBuf, CarveStoneTabletPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.STRING_UTF8,
			CarveStoneTabletPacket::text,
			NeoForgeStreamCodecs.enumCodec(InteractionHand.class),
			CarveStoneTabletPacket::hand,
			CarveStoneTabletPacket::new
	);
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		ItemStack tablet = player.getItemInHand(hand);
		ItemStack tool = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
		
		if(tablet.is(MSItems.STONE_TABLET) && tool.is(MSItems.CARVING_TOOL))
			StoneTabletTextComponent.setText(tablet, text);
	}
}
