package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.ai.EntityAIAttackByDistance;
import com.mraof.minestuck.entity.ai.EntityAINearestAttackableTargetWithHeight;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class EntityBishop extends EntityCarapacian implements IRangedAttackMob, IMob
{
	private EntityAIAttackByDistance entityAIAttackByDistance = new EntityAIAttackByDistance(this, 0.25F, 30, 64.0F);
	int burnTime;

	public EntityBishop(EntityType<?> type, World par1World)
	{
		super(type, par1World);
		this.setSize(1.9F, 4.1F);
		this.experienceValue = 3;
	}
	
	@Override
	public float getWanderSpeed() 
	{
		return .2F;
	}

	@Override
	protected float getMaximumHealth() 
	{
		return 40;
	}
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase entityliving, float f) 
	{
		
		double distanceX = entityliving.posX - this.posX;
		double distanceY = entityliving.getBoundingBox().minY + (double)(entityliving.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
		double distanceZ = entityliving.posZ - this.posZ;
		
		EntityLargeFireball entitylargefireball = new EntityLargeFireball(this.world, this, distanceX, distanceY, distanceZ);
		entitylargefireball.explosionPower = 1;
		double d8 = (double)this.width;
		Vec3d vec3 = this.getLook(1.0F);
		entitylargefireball.posX = (this.getBoundingBox().minX + this.getBoundingBox().maxX) / 2.0F  + vec3.x * d8;
		entitylargefireball.posY = this.posY + (double)(this.height / 2.0F);
		entitylargefireball.posZ = (this.getBoundingBox().minZ + this.getBoundingBox().maxZ) / 2.0F + vec3.z * d8;
		this.world.spawnEntity(entitylargefireball);
	}
	public int getAttackStrength(Entity par1Entity)
	{
		ItemStack var2 = this.getHeldItemMainhand();
		int var3 = 0;
		
		/*if(!var2.isEmpty())
			var3 += var2.getItemDamage();*/
		
		return var3;
	}
	
//	/**
//	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
//	 */
//	protected void attackEntity(Entity par1Entity, float par2)
//	{
//		
//		if (this.attackTime <= 0 && par2 < 2.0F && par1Entity.getBoundingBox().maxY > this.getBoundingBox().minY && par1Entity.getBoundingBox().minY < this.getBoundingBox().maxY)
//		{
//			this.attackTime = 20;
//			this.attackEntityAsMob(par1Entity);
//		}
//	}
	
	@Override
	public boolean attackEntityAsMob(Entity par1Entity)
	{
		int damage = this.getAttackStrength(par1Entity);
		int knockback = 6;
		par1Entity.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F));
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
	}
	
	@Override
	protected void setCombatTask() 
	{
		if(entityAIAttackByDistance == null)
			entityAIAttackByDistance = new EntityAIAttackByDistance(this, 0.25F, 30, 64.0F);
		this.tasks.removeTask(this.entityAIAttackByDistance);
		this.tasks.addTask(4, this.entityAIAttackByDistance);
	}
	
	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack)
	{
		super.setItemStackToSlot(slotIn, stack);
		
		if (!this.world.isRemote && slotIn == EntityEquipmentSlot.MAINHAND)
		{
			this.setCombatTask();
		}
	}
	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) 
	{
		if(par1DamageSource.isFireDamage())
		{
			burnTime += par2;
			if(burnTime <= 3)
				return false;
		}
		return super.attackEntityFrom(par1DamageSource, par2);
	}
	@Override
	EntityAINearestAttackableTargetWithHeight entityAINearestAttackableTargetWithHeight() 
	{
		EntityAINearestAttackableTargetWithHeight ai = new EntityAINearestAttackableTargetWithHeight(this, EntityLivingBase.class, 256.0F, 0, true, false, attackEntitySelector);
		ai.setTargetHeightDistance(64);
		return ai;
	}
	
	@Override
	public void setSwingingArms(boolean swingingArms)
	{
		//TODO New method; do something?
	}
}
