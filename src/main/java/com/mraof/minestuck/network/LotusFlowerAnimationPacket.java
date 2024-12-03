package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.LotusFlowerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LotusFlowerAnimationPacket(int entityID, LotusFlowerEntity.Animation animation) implements MSPacket.PlayToClient
{
	
		public static final Type<LotusFlowerAnimationPacket> ID = new Type<>(Minestuck.id("lotus_flower_animation"));
	public static final StreamCodec<FriendlyByteBuf, LotusFlowerAnimationPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			LotusFlowerAnimationPacket::entityID,
			NeoForgeStreamCodecs.enumCodec(LotusFlowerEntity.Animation.class),
			LotusFlowerAnimationPacket::animation,
			LotusFlowerAnimationPacket::new
	);
	
	public static LotusFlowerAnimationPacket createPacket(LotusFlowerEntity entity, LotusFlowerEntity.Animation animation)
	{
		return new LotusFlowerAnimationPacket(entity.getId(), animation);
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
		if(entity instanceof LotusFlowerEntity lotusFlower)
			lotusFlower.setAnimationFromPacket(animation);
	}
}
