package com.mraof.minestuck.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RandomLocalTeleport
{
	public static LivingEntity teleportEntity(LivingEntity entity, World world)
	{
		return teleportEntity(entity, entity.getPosX(), entity.getPosY(), entity.getPosZ(), world);
	}
	
	public static LivingEntity teleportEntity(LivingEntity entity, double x, double y, double z, World worldIn)
	{
		for(int i = 0; i < 16; ++i)
		{
			double newPosX = x + (entity.getRNG().nextDouble() - 0.5D) * 16.0D;
			double newPosY = MathHelper.clamp(y + (double) (entity.getRNG().nextInt(16) - 8), 0.0D, worldIn.getActualHeight() - 1);
			double newPosZ = z + (entity.getRNG().nextDouble() - 0.5D) * 16.0D;
			if(entity.isPassenger())
				entity.stopRiding();
			if(entity.attemptTeleport(newPosX, newPosY, newPosZ, true))
			{
				entity.world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
				entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
				
				break;
			}
		}
		return entity;
	}
	
}
