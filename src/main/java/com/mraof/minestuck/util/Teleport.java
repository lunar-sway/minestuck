package com.mraof.minestuck.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;

public class Teleport	//TODO there might still be things that vanilla does that we should do as well. Also, is it feasible to move over to the vanilla teleport method?
{
	
	public static Entity teleportEntity(Entity entity, ServerLevel level)
	{
		return teleportEntity(entity, level, entity.getX(), entity.getY(), entity.getZ());
	}
	
	public static Entity teleportEntity(Entity entity, ServerLevel level, double x, double y, double z)
	{
		return teleportEntity(entity, level, x, y, z, entity.getYRot(), entity.getXRot());
	}
	
	/**
	 * Made with the help of a private function in {@link net.minecraft.server.commands.TeleportCommand}.
	 */
	public static Entity teleportEntity(Entity entity, ServerLevel level, double x, double y, double z, float yaw, float pitch)
	{
		if(entity instanceof ServerPlayer player)
		{
			ChunkPos chunkpos = new ChunkPos(BlockPos.containing(x, y, z));
			level.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkpos, 1, player.getId());
			player.stopRiding();
			if(player.isSleeping())
			{
				player.stopSleeping();
			}
			
			boolean toNewDim = player.level() != level;
			player.teleportTo(level, x, y, z, yaw, pitch);
			if(toNewDim && player.level() != level)	//Was teleporting to a new dimension, but the teleportation did not go through
				return null;
			
			player.isChangingDimension = true;
			player.setYHeadRot(yaw);
			
			player.setExperienceLevels(player.experienceLevel);
			player.resetSentInfo();
			
		} else
		{
			yaw = Mth.wrapDegrees(yaw);	//I think we can trust the function input enough to not need this, but better safe then sorry?
			pitch = Mth.wrapDegrees(pitch);
			pitch = Mth.clamp(pitch, -90.0F, 90.0F);
			if(level == entity.level())
			{
				entity.moveTo(x, y, z, yaw, pitch);
				entity.setYHeadRot(yaw);
			} else
			{
				entity.unRide();
				Entity oldEntity = entity;
				entity = entity.getType().create(level);
				if (entity == null)
					return null;
				
				entity.restoreFrom(oldEntity);
				entity.moveTo(x, y, z, yaw, pitch);
				entity.setYHeadRot(yaw);
				oldEntity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
				level.addDuringTeleport(entity);
			}
		}
		
		if(!(entity instanceof LivingEntity living && living.isFallFlying()))
		{
			entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
			entity.setOnGround(true);
		}
		
		return entity;
	}
}