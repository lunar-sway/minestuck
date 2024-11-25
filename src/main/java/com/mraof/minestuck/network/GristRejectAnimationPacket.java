package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.item.GristEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

public record GristRejectAnimationPacket(int entityID) implements MSPacket.PlayToClient
{
	
		public static final Type<GristRejectAnimationPacket> ID = new Type<>(Minestuck.id("grist_reject_animation"));
	public static final StreamCodec<ByteBuf, GristRejectAnimationPacket> STREAM_CODEC = ByteBufCodecs.INT.map(GristRejectAnimationPacket::new, GristRejectAnimationPacket::entityID);
	
	public static GristRejectAnimationPacket createPacket(GristEntity entity)
	{
		return new GristRejectAnimationPacket(entity.getId());
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		Entity entity = Objects.requireNonNull(Minecraft.getInstance().level).getEntity(entityID);
		if(entity instanceof GristEntity gristEntity)
		{
			gristEntity.setAnimationFromPacket();
		}
	}
}
