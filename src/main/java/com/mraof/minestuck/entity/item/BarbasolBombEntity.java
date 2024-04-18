package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class BarbasolBombEntity extends ThrowableItemProjectile
{
	private boolean shouldDestroy = true;
	
	public BarbasolBombEntity(EntityType<? extends BarbasolBombEntity> type, Level level)
	{
		super(type, level);
	}
	
	public BarbasolBombEntity(EntityType<? extends BarbasolBombEntity> type, double x, double y, double z, Level level)
	{
		super(type, x, y, z, level);
	}
	
	public BarbasolBombEntity(EntityType<? extends BarbasolBombEntity> type, LivingEntity livingEntityIn, Level level, boolean shouldDestroy)
	{
		super(type, livingEntityIn, level);
		this.shouldDestroy = shouldDestroy;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putBoolean("shouldDestroy", shouldDestroy);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		shouldDestroy = compound.getBoolean("shouldDestroy");
	}
	
	@Override
	protected void onHit(HitResult result)
	{
		if(!this.level().isClientSide)
		{
			level().explode(null, result.getLocation().x, result.getLocation().y, result.getLocation().z, 3F,
					shouldDestroy ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE);
		}
		this.discard();
	}
	
	@Override
	protected Item getDefaultItem()
	{
		return MSItems.BARBASOL_BOMB.get();
	}
}
