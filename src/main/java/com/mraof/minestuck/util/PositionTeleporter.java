package com.mraof.minestuck.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class PositionTeleporter
{
	public double posX, posY, posZ;
	
	public static void moveEntity(Entity entity, double posX, double posY, double posZ)
	{
		entity.stopRiding();
		if(entity instanceof ServerPlayer player)
		{
			if (player.isSleeping())
			{
				player.stopSleeping();
			}
			
			player.connection.teleport(posX, posY, posZ, entity.getYRot(), entity.getXRot());
		} else
		{
			entity.setPos(posX, posY, posZ);
		}
	}
}