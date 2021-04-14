package com.mraof.minestuck.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public abstract class OnCollisionTeleporterTileEntity<E extends Entity> extends TileEntity implements ITickableTileEntity
{
	private final Class<E> entityClass;
	private boolean hasCollision = false;
	
	public OnCollisionTeleporterTileEntity(TileEntityType<?> tileEntityTypeIn, Class<E> entityClass)
	{
		super(tileEntityTypeIn);
		this.entityClass = entityClass;
	}
	
	protected boolean shouldTeleport(E entity)
	{
		return!entity.isPassenger() && !entity.isBeingRidden();
	}
	
	protected abstract AxisAlignedBB getTeleportField();
	
	protected abstract void teleport(E entity);
	
	/**
	 * Should be called on entity collision.
	 */
	public void onCollision(E entity)
	{
		if(!entity.world.isRemote && canTeleport(entity) && shouldTeleport(entity))
		{
			hasCollision = true;
		}
	}
	
	@Override
	public void tick()
	{
		if(hasCollision && world instanceof ServerWorld)
		{
			AxisAlignedBB boundingBox = getTeleportField();
			
			List<E> entities = world.getEntitiesWithinAABB(entityClass, boundingBox, entity -> canTeleport(entity) && shouldTeleport(entity));
			for(E entity : entities)
			{
				if(entity.hasPortalCooldown())
					entity.setPortalCooldown();
				else teleport(entity);
			}
			hasCollision = false;	//TODO is this correct behavior if entity.timeUntilPortal != 0?
		}
	}
	
	private static boolean canTeleport(Entity entity)
	{
		return !entity.noClip && entity.canChangeDimension();
	}
}