package com.mraof.minestuck.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class PositionTeleporter implements ITeleporter
{
	public double posX, posY, posZ;
	
	public PositionTeleporter(BlockPos pos)
	{
		this(pos.getX() + 0.5, pos.getY(), pos.getZ());
	}
	
	public PositionTeleporter(double posX, double posY, double posZ)
	{
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
	
	@Override
	public void placeEntity(World world, Entity entity, float yaw)
	{
		entity.setPosition(posX, posY, posZ);
	}
	
	public static void moveEntity(Entity entity, double posX, double posY, double posZ)
	{
		entity.stopRiding();
		if(entity instanceof EntityPlayerMP)
		{
			if (((EntityPlayerMP)entity).isPlayerSleeping())
			{
				((EntityPlayerMP)entity).wakeUpPlayer(true, true, false);
			}
			
			((EntityPlayerMP)entity).connection.setPlayerLocation(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
		} else
		{
			entity.setPosition(posX, posY, posZ);
		}
	}
}