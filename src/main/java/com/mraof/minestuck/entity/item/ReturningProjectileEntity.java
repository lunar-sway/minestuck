package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.client.renderer.entity.RendersAsItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ReturningProjectileEntity extends ProjectileItemEntity implements RendersAsItem
{
	private int cooldownTicks;
	private int bounce;
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, double x, double y, double z, World worldIn)
	{
		super(type, x, y, z, worldIn);
	}
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, LivingEntity livingEntityIn, World worldIn)
	{
		super(type, livingEntityIn, worldIn);
	}
	
	@Override
	protected void onImpact(RayTraceResult result)
	{
		PlayerEntity throwerPlayer = (PlayerEntity) this.getThrower();
		if(throwerPlayer != null){
			if(this.world.getEntitiesWithinAABB(PlayerEntity.class, getBoundingBox().grow(5)).contains(throwerPlayer)){
				resetThrower();
			}
			++bounce;
			this.setMotion(this.getMotion().x*-1.1, this.getMotion().y*-1.1, this.getMotion().z*-1.1);
			if(!this.world.isRemote && result.getType() == RayTraceResult.Type.ENTITY)
			{
				Entity entity = ((EntityRayTraceResult) result).getEntity();
				if(entity != throwerPlayer)
					entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 5);
				if(entity == throwerPlayer){
					resetThrower();
				}
			} else if (!this.world.isRemote && result.getType() == RayTraceResult.Type.BLOCK){
				this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 0.6F, 4.0F);
			}
			
			if(throwerPlayer instanceof ServerPlayerEntity){
				cooldownTicks = throwerPlayer.getCooldownTracker().hashCode();
				if(cooldownTicks <= 5)
				{
					resetThrower();
				}
			}
			if(bounce > 15){
				resetThrower();
			}
		}
	}
	
	public void resetThrower(){
		PlayerEntity throwerPlayer = (PlayerEntity) this.getThrower();
		if(throwerPlayer != null){
			throwerPlayer.getCooldownTracker().setCooldown(MSItems.CHAKRAM, 5);
			this.remove();
		}
	}
	
	public void tick() {
		Vec3d pos = getPositionVec();
		this.lastTickPosX = pos.x;
		this.lastTickPosY = pos.y;
		this.lastTickPosZ = pos.z;
		super.tick();
		
		if (this.ticksExisted >= 75) {
			resetThrower();
		}
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	protected Item getDefaultItem()
	{
		return MSItems.CHAKRAM;
	}
}
