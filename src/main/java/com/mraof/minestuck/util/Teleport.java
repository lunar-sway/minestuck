package com.mraof.minestuck.util;

import net.minecraft.command.impl.TeleportCommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;

public class Teleport
{
	
	public static Entity teleportEntity(Entity entity, ServerWorld world)
	{
		return teleportEntity(entity, world, entity.posX, entity.posY, entity.posZ);
	}
	
	public static Entity teleportEntity(Entity entity, ServerWorld world, double x, double y, double z)
	{
		return teleportEntity(entity, world, x, y, z, entity.rotationYaw, entity.rotationPitch);
	}
	
	/**
	 * Made with the help of a private function in {@link TeleportCommand}.
	 */
	public static Entity teleportEntity(Entity entity, ServerWorld world, double x, double y, double z, float yaw, float pitch)
	{
		if(entity instanceof ServerPlayerEntity)
		{
			ServerPlayerEntity player = (ServerPlayerEntity) entity;
			
			ChunkPos chunkpos = new ChunkPos(new BlockPos(x, y, z));
			world.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, chunkpos, 1, player.getEntityId());
			player.stopRiding();
			if(player.isSleeping())
			{
				player.wakeUpPlayer(true, true, false);
			}
			
			boolean toNewDim = player.world != world;
			player.teleport(world, x, y, z, yaw, pitch);
			if(toNewDim && player.world != world)	//Was teleporting to a new dimension, but the teleportation did not go through
				return null;
			
			player.setRotationYawHead(yaw);
		} else
		{
			yaw = MathHelper.wrapDegrees(yaw);	//I think we can trust the function input enough to not need this, but better safe then sorry?
			pitch = MathHelper.wrapDegrees(pitch);
			pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
			if(world == entity.world)
			{
				entity.setLocationAndAngles(x, y, z, yaw, pitch);
				entity.setRotationYawHead(yaw);
			} else
			{
				entity.detach();
				entity.dimension = world.dimension.getType();
				Entity oldEntity = entity;
				entity = entity.getType().create(world);
				if (entity == null)
					return null;
				
				entity.copyDataFromOld(oldEntity);
				entity.setLocationAndAngles(x, y, z, yaw, pitch);
				entity.setRotationYawHead(yaw);
				world.func_217460_e(entity);
				oldEntity.remove(false);
			}
		}
		
		if(!(entity instanceof LivingEntity) || !((LivingEntity)entity).isElytraFlying())
		{
			entity.setMotion(entity.getMotion().mul(1.0D, 0.0D, 1.0D));
			entity.onGround = true;
		}
		
		return entity;
	}
}