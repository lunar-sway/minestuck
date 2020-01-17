package com.mraof.minestuck.entity.carapacian;

import com.mraof.minestuck.entity.ai.AttackByDistanceGoal;
import com.mraof.minestuck.entity.ai.NearestAttackableTargetWithHeightGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class BishopEntity extends CarapacianEntity implements IRangedAttackMob, IMob
{
	int burnTime;

	public BishopEntity(EntityType<? extends BishopEntity> type, World par1World)
	{
		super(type, par1World);
		this.experienceValue = 3;
	}
	
	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(4, new AttackByDistanceGoal(this, 0.25F, 30, 64.0F));
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
	public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor)
	{
		
		double distanceX = target.posX - this.posX;
		double distanceY = target.getBoundingBox().minY + (double)(target.getHeight() / 2.0F) - (this.posY + (double)(this.getHeight() / 2.0F));
		double distanceZ = target.posZ - this.posZ;
		
		FireballEntity fireball = new FireballEntity(this.world, this, distanceX, distanceY, distanceZ);
		fireball.explosionPower = 1;
		double d8 = (double)this.getHeight();
		Vec3d vec3 = this.getLook(1.0F);
		fireball.posX = (this.getBoundingBox().minX + this.getBoundingBox().maxX) / 2.0F  + vec3.x * d8;
		fireball.posY = this.posY + (double)(this.getHeight() / 2.0F);
		fireball.posZ = (this.getBoundingBox().minZ + this.getBoundingBox().maxZ) / 2.0F + vec3.z * d8;
		this.world.addEntity(fireball);
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
		par1Entity.addVelocity(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F, 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)knockback * 0.5F));
		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
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
	NearestAttackableTargetWithHeightGoal entityAINearestAttackableTargetWithHeight()
	{
		NearestAttackableTargetWithHeightGoal ai = new NearestAttackableTargetWithHeightGoal(this, LivingEntity.class, 256.0F, 0, true, false, attackEntitySelector);
		ai.setTargetHeightDistance(64);
		return ai;
	}
}