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
public class ClientMovementPacket implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("client_movement");
	
	private final double moveX;
	private final double moveY;
	private final double moveZ;
	
	public static ClientMovementPacket createPacket(Vec3 moveVec)
	{
		return new ClientMovementPacket(moveVec.x, moveVec.y, moveVec.z);
	}
	
	private ClientMovementPacket(double moveX, double moveY, double moveZ)
	{
		this.moveX = moveX;
		this.moveY = moveY;
		this.moveZ = moveZ;
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeDouble(moveX);
		buffer.writeDouble(moveY);
		buffer.writeDouble(moveZ);
	}
	
	public static ClientMovementPacket read(FriendlyByteBuf buffer)
	{
		double moveX = buffer.readDouble();
		double moveY = buffer.readDouble();
		double moveZ = buffer.readDouble();
		
		return new ClientMovementPacket(moveX, moveY, moveZ);
	}
	
	@Override
	public void execute()
	{
		LocalPlayer player = Minecraft.getInstance().player;
		if(player != null)
		{
			player.push(moveX, moveY, moveZ);
		}
	}
}