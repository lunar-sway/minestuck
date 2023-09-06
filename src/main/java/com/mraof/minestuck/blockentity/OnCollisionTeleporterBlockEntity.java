package com.mraof.minestuck.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public abstract class OnCollisionTeleporterBlockEntity<E extends Entity> extends BlockEntity
{
	private final Class<E> entityClass;
	private boolean hasCollision = false;
	
	public OnCollisionTeleporterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, Class<E> entityClass)
	{
		super(type, pos, state);
		this.entityClass = entityClass;
	}
	
	protected boolean shouldTeleport(E entity)
	{
		return!entity.isPassenger() && !entity.isVehicle();
	}
	
	protected abstract AABB getTeleportField();
	
	protected abstract void teleport(E entity);
	
	/**
	 * Should be called on entity collision.
	 */
	public void onCollision(E entity)
	{
		if(!entity.level().isClientSide && canTeleport(entity) && shouldTeleport(entity))
		{
			hasCollision = true;
		}
	}
	
	public static <E extends Entity> void serverTick(Level level, BlockPos pos, BlockState state, OnCollisionTeleporterBlockEntity<E> blockEntity)
	{
		if(blockEntity.hasCollision && level instanceof ServerLevel)
		{
			AABB boundingBox = blockEntity.getTeleportField();
			
			List<E> entities = level.getEntitiesOfClass(blockEntity.entityClass, boundingBox, entity -> canTeleport(entity) && blockEntity.shouldTeleport(entity));
			for(E entity : entities)
			{
				if(entity.isOnPortalCooldown())
					entity.setPortalCooldown();
				else blockEntity.teleport(entity);
			}
			blockEntity.hasCollision = false;	//TODO is this correct behavior if entity.timeUntilPortal != 0?
		}
	}
	
	private static boolean canTeleport(Entity entity)
	{
		return !entity.noPhysics && entity.canChangeDimensions();
	}
}