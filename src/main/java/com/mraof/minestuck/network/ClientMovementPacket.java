package com.mraof.minestuck.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * Used for when the player needs to be moved but only server side access is available
 */
public class ClientMovementPacket implements PlayToClientPacket
{
	private final int playerID;
	private final double moveX;
	private final double moveY;
	private final double moveZ;
	
	public static ClientMovementPacket createPacket(Player player, Vec3 moveVec)
	{
		return new ClientMovementPacket(player.getId(), moveVec.x, moveVec.y, moveVec.z);
	}
	
	private ClientMovementPacket(int playerID, double moveX, double moveY, double moveZ)
	{
		this.playerID = playerID;
		this.moveX = moveX;
		this.moveY = moveY;
		this.moveZ = moveZ;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(playerID);
		buffer.writeDouble(moveX);
		buffer.writeDouble(moveY);
		buffer.writeDouble(moveZ);
	}
	
	public static ClientMovementPacket decode(FriendlyByteBuf buffer)
	{
		int playerID = buffer.readInt();
		double moveX = buffer.readDouble();
		double moveY = buffer.readDouble();
		double moveZ = buffer.readDouble();
		
		return new ClientMovementPacket(playerID, moveX, moveY, moveZ);
	}
	
	@Override
	public void execute()
	{
		Entity entity = Minecraft.getInstance().level.getEntity(playerID);
		if(entity instanceof Player)
		{
			entity.push(moveX, moveY, moveZ);
		}
	}
}