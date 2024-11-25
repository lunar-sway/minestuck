package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.machine.AnthvilBlockEntity;
import com.mraof.minestuck.inventory.AnthvilMenu;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record TriggerAnthvilPacket() implements MSPacket.PlayToServer
{
	public static final Type<TriggerAnthvilPacket> ID = new Type<>(Minestuck.id("trigger_anthvil"));
	public static final StreamCodec<FriendlyByteBuf, TriggerAnthvilPacket> STREAM_CODEC = StreamCodec.unit(new TriggerAnthvilPacket());
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		AbstractContainerMenu playerContainer = player.containerMenu;
		if(!(playerContainer instanceof AnthvilMenu anthvilMenu))
			return;
		
		anthvilMenu.getPosition().execute((level, machinePos) -> {
			if(level.getBlockEntity(machinePos) instanceof AnthvilBlockEntity anthvilBlockEntity)
			{
				AnthvilBlockEntity.attemptMendAndRefuel(anthvilBlockEntity, player);
			}
		});
	}
}
