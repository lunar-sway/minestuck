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

public class Teleport	//TODO there might still be things that vanilla does that we should do as well. Also, is it feasible to move over to the vanilla teleport method?
{
	
	public static Entity teleportEntity(Entity entity, ServerWorld world)
	{
		return teleportEntity(entity, world, entity.getX(), entity.getY(), entity.getZ());
	}
	
	public static Entity teleportEntity(Entity entity, ServerWorld world, double x, double y, double z)
	{
		return teleportEntity(entity, world, x, y, z, entity.yRot, entity.xRot);
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
			world.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkpos, 1, player.getId());
			player.stopRiding();
			if(player.isSleeping())
			{
				player.stopSleeping();
			}
			
			boolean toNewDim = player.level != world;
			player.teleportTo(world, x, y, z, yaw, pitch);
			if(toNewDim && player.level != world)	//Was teleporting to a new dimension, but the teleportation did not go through
				return null;
			
			player.isChangingDimension = true;
			player.setYHeadRot(yaw);
			
			player.setExperienceLevels(player.experienceLevel);
			player.resetSentInfo();
			
		} else
		{
			yaw = MathHelper.wrapDegrees(yaw);	//I think we can trust the function input enough to not need this, but better safe then sorry?
			pitch = MathHelper.wrapDegrees(pitch);
			pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);
			if(world == entity.level)
			{
				entity.moveTo(x, y, z, yaw, pitch);
				entity.setYHeadRot(yaw);
			} else
			{
				entity.unRide();
				Entity oldEntity = entity;
				entity = entity.getType().create(world);
				if (entity == null)
					return null;
				
				entity.restoreFrom(oldEntity);
				entity.moveTo(x, y, z, yaw, pitch);
				entity.setYHeadRot(yaw);
				world.addFromAnotherDimension(entity);
				oldEntity.remove(false);
			}
		}
		
		if(!(entity instanceof LivingEntity) || !((LivingEntity)entity).isFallFlying())
		{
			entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
			entity.setOnGround(true);
		}
		
		return entity;
	}
}