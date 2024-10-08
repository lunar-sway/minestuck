package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

/**
 * Used for when the player needs to be moved but only server side access is available
 */
public record PushPlayerPacket(Vec3 moveVec) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("push_player");
	
	public static PushPlayerPacket createPacket(Vec3 moveVec)
	{
		return new PushPlayerPacket(moveVec);
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeVec3(moveVec);
	}
	
	public static PushPlayerPacket read(FriendlyByteBuf buffer)
	{
		Vec3 moveVec = buffer.readVec3();
		return new PushPlayerPacket(moveVec);
	}
	
	@Override
	public void execute()
	{
		LocalPlayer player = Minecraft.getInstance().player;
		if(player != null)
		{
			player.push(moveVec.x, moveVec.y, moveVec.z);
		}
	}
}
