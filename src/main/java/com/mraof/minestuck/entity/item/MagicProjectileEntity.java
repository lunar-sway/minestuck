package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.projectiles.ProjectileDamaging;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class MagicProjectileEntity extends DamagingProjectileEntity implements IRendersAsItem
{
	public int damage;
	
	/*public MagicProjectileEntity(EntityType<? extends MagicProjectileEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public MagicProjectileEntity(EntityType<? extends MagicProjectileEntity> type, double x, double y, double z, World worldIn)
	{
		super(type, x, y, z, worldIn);
	}
	
	public MagicProjectileEntity(EntityType<? extends MagicProjectileEntity> type, LivingEntity livingEntityIn, World worldIn, int damage)
	{
		super(type, livingEntityIn, worldIn);
		this.damage = damage;
	}*/
	
	public MagicProjectileEntity(EntityType<? extends MagicProjectileEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public MagicProjectileEntity(EntityType<? extends MagicProjectileEntity> type, double x, double y, double z, double accelX, double accelY, double accelZ, World worldIn)
	{
		super(type, x, y, z, accelX, accelY, accelZ, worldIn);
	}
	
	public MagicProjectileEntity(EntityType<? extends MagicProjectileEntity> type, LivingEntity livingEntityIn, World worldIn, double accelX, double accelY, double accelZ, int damage)
	{
		super(type, livingEntityIn, accelX, accelY, accelZ, worldIn);
		this.damage = damage;
	}
	
	public MagicProjectileEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ, int damage)
	{
		super(MSEntityTypes.MAGIC_PROJECTILE, shooter, accelX, accelY, accelZ, worldIn);
		this.damage = damage;
	}
	
	@Override
	protected void onImpact(RayTraceResult result)
	{
		//int damage = ProjectileDamaging.getDamageFromItem(getItemFromItemStack().getItem());
		
		if(result.getType() == RayTraceResult.Type.ENTITY)
		{
			Entity entity = ((EntityRayTraceResult) result).getEntity();
			if(entity instanceof UnderlingEntity)
				entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), damage * 1.5F);
			else
				entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), damage);
			if(entity != this.shootingEntity)
				removeEntity();
		}
		
		if(result.getType() == RayTraceResult.Type.BLOCK)
		{
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;
			Direction blockFace = blockResult.getFace();
			BlockPos blockPos = blockResult.getPos();
			if(Block.hasEnoughSolidSide(world, blockPos, blockFace))
			{
				removeEntity();
			}
		}
	}
	
	public void removeEntity()
	{
		for(int i = 0; i < this.rand.nextInt(3) + 2; ++i) {
			this.world.addParticle(ParticleTypes.POOF, this.getPosX(), this.getPosY(), this.getPosZ(), this.rand.nextGaussian() * 0.05D, 0.005D, this.rand.nextGaussian() * 0.05D);
		}
		this.remove();
	}
	
	public void tick()
	{
		Vec3d pos = getPositionVec();
		this.lastTickPosX = pos.x;
		this.lastTickPosY = pos.y;
		this.lastTickPosZ = pos.z;
		
		if(this.isInWater())
			this.setMotion(getMotion().scale(1.25));
		else
			this.setMotion(getMotion().scale(1.0055));
		
		if(this.ticksExisted >= 100)
		{
			removeEntity();
		}
		
		world.addParticle(ParticleTypes.END_ROD, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
		
		super.tick();
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public ItemStack getItem()
	{
		ItemStack itemStack = new ItemStack(Items.BRICK);
		return itemStack;
	}
	
	@Override
	protected boolean isFireballFiery()
	{
		return false;
	}
}
