package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.client.renderer.entity.RendersAsItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ReturningProjectileEntity extends ConsumableProjectileEntity implements RendersAsItem
{
	private int bounce;
	public int maxTick = 0;
	private int inBlockTicks = 0;
	private BlockRayTraceResult blockResult;
	private Direction blockFace;
	private BlockPos blockPos;
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, double x, double y, double z, World worldIn)
	{
		super(type, x, y, z, worldIn);
	}
	
	public ReturningProjectileEntity(EntityType<? extends ReturningProjectileEntity> type, LivingEntity livingEntityIn, World worldIn, int damage, int maxTick)
	{
		super(type, livingEntityIn, worldIn, damage);
		this.maxTick = maxTick;
	}
	
	@Override
	protected void onImpact(RayTraceResult result)
	{
		PlayerEntity throwerPlayer = (PlayerEntity) this.getThrower();
		if(throwerPlayer != null && !world.isRemote)
		{
			int cooldownTicks = throwerPlayer.getCooldownTracker().hashCode();
			
			if(result.getType() == RayTraceResult.Type.ENTITY)
			{
				++bounce;
				this.setMotion(getMotion().scale(-1.05));
				Entity entity = ((EntityRayTraceResult) result).getEntity();
				if(entity != throwerPlayer)
					entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
				if(entity == throwerPlayer)
				{
					resetThrower();
				}
			} else if(result.getType() == RayTraceResult.Type.BLOCK && func_213882_k().getItem() != MSItems.UMBRAL_INFILTRATOR)
			{
				blockResult = (BlockRayTraceResult) result;
				blockFace = blockResult.getFace();
				blockPos = blockResult.getPos();
				if(Block.hasEnoughSolidSide(world, blockPos, blockFace))
				{
					++bounce;
					this.setMotion(getMotion().scale(-1.05));
					this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 0.6F, 4.0F);
				}
				
				if(Block.hasEnoughSolidSide(world, blockPos, blockFace) && blockResult.isInside())
				{
					++inBlockTicks;
				}
			}
			
			if(bounce > 15 || cooldownTicks <= 5)
			{
				resetThrower();
			}
		}
	}
	
	public void resetThrower()
	{
		PlayerEntity throwerPlayer = (PlayerEntity) this.getThrower();
		if(throwerPlayer != null)
		{
			throwerPlayer.getCooldownTracker().setCooldown(func_213882_k().getItem(), 5);
			this.remove();
		}
	}
	
	public void tick()
	{
		Vec3d pos = getPositionVec();
		this.lastTickPosX = pos.x;
		this.lastTickPosY = pos.y;
		this.lastTickPosZ = pos.z;
		
		if(this.isInWater())
			this.setMotion(getMotion().scale(1.2));
		else
			this.setMotion(getMotion().scale(1.005));
		
		if(this.ticksExisted >= maxTick || inBlockTicks >= 1)
		{
			resetThrower();
		}
		
		ProjectileHelper.rotateTowardsMovement(this, 0.2F);
		
		super.tick();
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
