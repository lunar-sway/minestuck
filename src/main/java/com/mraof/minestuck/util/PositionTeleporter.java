package com.mraof.minestuck.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class PositionTeleporter
{
	public double posX, posY, posZ;
	
	public static void moveEntity(Entity entity, double posX, double posY, double posZ)
	{
		entity.stopRiding();
		if(entity instanceof ServerPlayerEntity)
		{
			if (((ServerPlayerEntity)entity).isSleeping())
			{
				((ServerPlayerEntity)entity).stopSleeping();
			}
			
			((ServerPlayerEntity)entity).connection.teleport(posX, posY, posZ, entity.yRot, entity.xRot);
		} else
		{
			entity.setPos(posX, posY, posZ);
		}
	}
}