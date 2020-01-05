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
				((ServerPlayerEntity)entity).wakeUpPlayer(true, true, false);
			}
			
			((ServerPlayerEntity)entity).connection.setPlayerLocation(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
		} else
		{
			entity.setPosition(posX, posY, posZ);
		}
	}
}