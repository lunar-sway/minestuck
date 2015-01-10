package com.mraof.minestuck.entity.carapacian;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.mraof.minestuck.entity.ai.EntityAIAttackByDistance;
import com.mraof.minestuck.entity.ai.EntityAINearestAttackableTargetWithHeight;

public abstract class EntityBishop extends EntityCarapacian implements IRangedAttackMob, IMob
{
	private EntityAIAttackByDistance entityAIAttackByDistance = new EntityAIAttackByDistance(this, 0.25F, 30, 64.0F);
	int burnTime;

	public EntityBishop(World par1World) 
	{
		super(par1World);
		this.setSize(1.9F, 4.1F);
		this.experienceValue = 3;
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
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
        double distanceY = entityliving.getEntityBoundingBox().minY + (double)(entityliving.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
        double distanceZ = entityliving.posZ - this.posZ;
		
        EntityLargeFireball entitylargefireball = new EntityLargeFireball(this.worldObj, this, distanceX, distanceY, distanceZ);
		entitylargefireball.explosionPower = 1;
        double d8 = (double)this.width;
        Vec3 vec3 = this.getLook(1.0F);
        entitylargefireball.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0F  + vec3.xCoord * d8;
        entitylargefireball.posY = this.posY + (double)(this.height / 2.0F);
        entitylargefireball.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0F + vec3.zCoord * d8;
        this.worldObj.spawnEntityInWorld(entitylargefireball);
	}
	public int getAttackStrength(Entity par1Entity)
	{
		ItemStack var2 = this.getHeldItem();
		int var3 = 0;
		
		if (var2 != null)
			var3 += var2.getItemDamage();
		
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

	public boolean attackEntityAsMob(Entity par1Entity)
	{
		int damage = this.getAttackStrength(par1Entity);
		int knockback = 6;
		par1Entity.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F));
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
	}
	protected void setCombatTask() 
	{
		if(entityAIAttackByDistance == null)
			entityAIAttackByDistance = new EntityAIAttackByDistance(this, 0.25F, 30, 64.0F);
		this.tasks.removeTask(this.entityAIAttackByDistance);
		this.tasks.addTask(4, this.entityAIAttackByDistance);
	}
	public void setCurrentItemOrArmor(int slot, ItemStack par2ItemStack)
	{
		super.setCurrentItemOrArmor(slot, par2ItemStack);

		if (!this.worldObj.isRemote && slot == 0)
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
}
