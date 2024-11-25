package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Used for when the player needs to be moved but only server side access is available
 */
public record PushPlayerPacket(Vec3 moveVec) implements MSPacket.PlayToClient
{
	public static final Type<PushPlayerPacket> ID = new Type<>(Minestuck.id("push_player"));
	public static final StreamCodec<FriendlyByteBuf, PushPlayerPacket> STREAM_CODEC = MSPayloads.VEC3_STREAM_CODEC.map(PushPlayerPacket::new, PushPlayerPacket::moveVec);
	
	public static PushPlayerPacket createPacket(Vec3 moveVec)
	{
		return new PushPlayerPacket(moveVec);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context)
	{
		LocalPlayer player = Minecraft.getInstance().player;
		if(player != null)
		{
			player.push(moveVec.x, moveVec.y, moveVec.z);
		}
	}
}
