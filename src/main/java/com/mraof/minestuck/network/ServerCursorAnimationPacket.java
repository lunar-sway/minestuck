package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.ServerCursorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerCursorAnimationPacket(int entityID, ServerCursorEntity.AnimationType animation) implements MSPacket.PlayToClient
{
	public static final Type<ServerCursorAnimationPacket> ID = new Type<>(Minestuck.id("server_cursor_animation"));
	public static final StreamCodec<FriendlyByteBuf, ServerCursorAnimationPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			ServerCursorAnimationPacket::entityID,
			NeoForgeStreamCodecs.enumCodec(ServerCursorEntity.AnimationType.class),
			ServerCursorAnimationPacket::animation,
			ServerCursorAnimationPacket::new
	);
	
	public static ServerCursorAnimationPacket createPacket(ServerCursorEntity entity, ServerCursorEntity.AnimationType animation)
	{
		return new ServerCursorAnimationPacket(entity.getId(), animation);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		Entity entity = Minecraft.getInstance().level.getEntity(entityID);
		if(entity instanceof ServerCursorEntity cursorEntity)
		{
			cursorEntity.setAnimationFromPacket(animation);
		}
	}
}

